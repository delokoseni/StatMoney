package com.example.StatMoney.controller;

import com.example.StatMoney.config.MyUserDetails;
import com.example.StatMoney.AggregatedAsset;
import com.example.StatMoney.entity.*;
import com.example.StatMoney.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class PortfolioController {
    private final AssetService assetService;
    private final PortfolioService portfolioService;
    private final MoexService moexService;
    private final CryptoCompareService cryptoCompareService;
    private final CbrService cbrService;

    @Autowired
    public PortfolioController(AssetService assetService, PortfolioService portfolioService, MoexService moexService, CryptoCompareService cryptoCompareService, CbrService cbrService) {
        this.assetService = assetService;
        this.portfolioService = portfolioService;
        this.moexService = moexService;
        this.cryptoCompareService = cryptoCompareService;
        this.cbrService = cbrService;
    }

    @GetMapping("/portfolio")
    public String getAssetsPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails = (MyUserDetails) auth.getPrincipal();
        User user = myUserDetails.getUser();

        Optional<Portfolio> portfolioOpt = portfolioService.findByUser(user);
        if (portfolioOpt.isPresent()) {
            Portfolio portfolio = portfolioOpt.get();
            List<Asset> assets = assetService.findByPortfolioId(portfolio.getId());
            float usdToRubRate = cbrService.getCurrentCurrencyRate("USD");

            Map<String, AggregatedAsset> aggregatedAssetsMap = new HashMap<>();

            for (Asset asset : assets) {
                String ticker = getTickerOfAsset(asset);
                AggregatedAsset aggregatedAsset = aggregatedAssetsMap.getOrDefault(ticker, new AggregatedAsset());
                aggregatedAsset.setTicker(ticker);
                aggregatedAsset.setName(asset.getName());
                aggregatedAsset.setCategory(getRussianCategory(asset.getCategory()));
                aggregatedAsset.setTotalQuantity(aggregatedAsset.getTotalQuantity() + asset.getQuantity());
                aggregatedAsset.setAveragePurchasePriceRub(
                        (float) ((aggregatedAsset.getAveragePurchasePriceRub() * (aggregatedAsset.getTotalQuantity() - asset.getQuantity())
                                + asset.getPurchasePriceRub() * asset.getQuantity()) / aggregatedAsset.getTotalQuantity()));
                aggregatedAsset.setAveragePurchasePriceUsd(
                        (float) ((aggregatedAsset.getAveragePurchasePriceUsd() * (aggregatedAsset.getTotalQuantity() - asset.getQuantity())
                                + asset.getPurchasePriceUsd() * asset.getQuantity()) / aggregatedAsset.getTotalQuantity()));

                float currentPriceRub = getCurrentAssetPrice(asset);
                aggregatedAsset.setCurrentPriceRub(currentPriceRub);
                aggregatedAsset.setCurrentPriceUsd(currentPriceRub / usdToRubRate);

                aggregatedAssetsMap.put(ticker, aggregatedAsset);
            }

            Map<String, Float> profitRub = new HashMap<>();
            Map<String, Float> profitUsd = new HashMap<>();
            float totalValueRub = 0;

            for (AggregatedAsset asset : aggregatedAssetsMap.values()) {
                float profitRubPercent = ((asset.getCurrentPriceRub() - asset.getAveragePurchasePriceRub()) / asset.getAveragePurchasePriceRub()) * 100;
                float profitUsdPercent = ((asset.getCurrentPriceUsd() - asset.getAveragePurchasePriceUsd()) / asset.getAveragePurchasePriceUsd()) * 100;

                profitRub.put(asset.getTicker(), profitRubPercent);
                profitUsd.put(asset.getTicker(), profitUsdPercent);

                totalValueRub += asset.getCurrentPriceRub() * asset.getTotalQuantity();
            }

            model.addAttribute("assets", aggregatedAssetsMap.values());
            model.addAttribute("profitRub", profitRub);
            model.addAttribute("profitUsd", profitUsd);
            model.addAttribute("totalValueRub", totalValueRub);
            model.addAttribute("totalValueUsd", totalValueRub / usdToRubRate);
        } else {
            model.addAttribute("message", "No portfolio found for user");
        }

        return "portfolio";
    }

    @GetMapping("/portfolio/add")
    public String showAddAssetForm(Model model) {
        model.addAttribute("asset", new Asset());
        return "add-asset";
    }

    @PostMapping("/portfolio/add")
    public String addAsset(@ModelAttribute Asset asset) {
        assetService.addAsset(asset);
        return "redirect:/portfolio";
    }

    @PostMapping("/portfolio/delete/{id}")
    public String deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return "redirect:/portfolio";
    }

    private String getTickerOfAsset(Asset asset) {
        if (asset instanceof Bond) {
            return ((Bond) asset).getTicker();
        } else if (asset instanceof Cryptocurrency) {
            return ((Cryptocurrency) asset).getTicker();
        } else if (asset instanceof Share) {
            return ((Share) asset).getTicker();
        } else {
            return "N/A";
        }
    }

    private String getRussianCategory(String category) {
        switch (category) {
            case "Bond":
                return "Облигация";
            case "Cryptocurrency":
                return "Криптовалюта";
            case "Share":
                return "Акция";
            default:
                return category;
        }
    }

    private float getCurrentAssetPrice(Asset asset) {
        if (asset instanceof Bond) {
            return (float) moexService.getCurrentBondPrice(((Bond) asset).getTicker());
        } else if (asset instanceof Cryptocurrency) {
            return (float) cryptoCompareService.getCryptoPrice(((Cryptocurrency) asset).getTicker());
        } else if (asset instanceof Share) {
            return (float) moexService.getCurrentPrice(((Share) asset).getTicker());
        } else {
            return 0;
        }
    }
}
