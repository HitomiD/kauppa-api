package com.grupo_b.kauppa_api.web_report;

import com.grupo_b.kauppa_api.product.Product;
import com.grupo_b.kauppa_api.sale.SaleRequestDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@CrossOrigin(origins ="/**")
@RequestMapping(path="/web-report")
public class WebReportController {

    private final WebReportService webReportService;

    @Autowired
    public WebReportController(WebReportService webReportService){
        this.webReportService = webReportService;
    }

    //produces = "application/json"
    @PostMapping(value = "/monthly", consumes = "application/json")
    public String monthlyReport(@RequestBody ArrayList<SaleRequestDTO> requestSales, Model model){
        List<Product> topProducts = webReportService.getTopMonthlySales(requestSales);
        model.addAttribute("products", topProducts);
        return "monthly-report";
    }

    @PostMapping(value = "/annual", consumes = "application/json")
    public String annualReport(@RequestBody ArrayList<SaleRequestDTO> requestSales, Model model){
        List<Product> topProducts = webReportService.getTopMonthlySales(requestSales);
        model.addAttribute("products", topProducts);
        return "annual-report";
    }
}
