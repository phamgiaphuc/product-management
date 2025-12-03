package com.example.product_management.controller;

import com.example.product_management.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final ProductService productService;

    public DashboardController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String showDashboard(Model model) {
        model.addAttribute("totalCount", productService.getTotalCount());
        model.addAttribute("totalValue", productService.getTotalInventoryValue());
        model.addAttribute("avgPrice", productService.getAveragePrice());

        List<String> categories = productService.getAllCategories();
        Map<String, Long> categoryStats = new HashMap<>();
        for (String cat : categories) {
            categoryStats.put(cat, productService.getCountByCategory(cat));
        }
        model.addAttribute("categoryStats", categoryStats);

        model.addAttribute("lowStockProducts", productService.getLowStockProducts(10));
        model.addAttribute("recentProducts", productService.getRecentProducts());

        return "dashboard";
    }
}