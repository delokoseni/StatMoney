package com.example.StatMoney.controller;

import com.example.StatMoney.entity.Asset;
import com.example.StatMoney.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PortfolioController {
    private final AssetService assetService;

    @Autowired
    public PortfolioController(AssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping("/portfolio")
    public String getAssetsPage(Model model) {
        model.addAttribute("assets", assetService.getAllAssets());
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
}
