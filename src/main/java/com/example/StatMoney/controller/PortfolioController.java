package com.example.StatMoney.controller;

import com.example.StatMoney.config.MyUserDetails;
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

            Map<Long, Float> currentPricesRub = new HashMap<>();
            Map<Long, Float> currentPricesUsd = new HashMap<>();
            Map<Long, String> assetTickers = new HashMap<>();
            Map<Long, String> assetCategoriesRus = new HashMap<>();
            Map<Long, Float> profitRub = new HashMap<>();
            Map<Long, Float> profitUsd = new HashMap<>();

            float totalValueRub = 0;

            for (Asset asset : assets) {
                String ticker = getTickerOfAsset(asset);
                float currentPriceRub = getCurrentAssetPrice(asset);
                float currentPriceUsd = currentPriceRub / usdToRubRate;
                float purchasePriceRub = (float) asset.getPurchasePriceRub() * asset.getQuantity();
                float profitRubPercent = ((currentPriceRub - (float) asset.getPurchasePriceRub()) / (float) asset.getPurchasePriceRub()) * 100;
                float purchasePriceUsd = (float) asset.getPurchasePriceUsd() * asset.getQuantity();
                float profitUsdPercent = ((currentPriceUsd - (float) asset.getPurchasePriceUsd()) / (float) asset.getPurchasePriceUsd()) * 100;

                assetTickers.put(asset.getId(), ticker);
                currentPricesRub.put(asset.getId(), currentPriceRub);
                currentPricesUsd.put(asset.getId(), currentPriceUsd);
                assetCategoriesRus.put(asset.getId(), getRussianCategory(asset.getCategory()));
                profitRub.put(asset.getId(), profitRubPercent);
                profitUsd.put(asset.getId(), profitUsdPercent);

                totalValueRub += currentPriceRub * asset.getQuantity();
            }

            model.addAttribute("assets", assets);
            model.addAttribute("currentPricesRub", currentPricesRub);
            model.addAttribute("currentPricesUsd", currentPricesUsd);
            model.addAttribute("assetTickers", assetTickers);
            model.addAttribute("assetCategoriesRus", assetCategoriesRus);
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
