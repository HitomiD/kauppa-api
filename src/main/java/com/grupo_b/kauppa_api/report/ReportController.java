package com.grupo_b.kauppa_api.report;

import com.grupo_b.kauppa_api.sale.SaleWithProducts;
import com.grupo_b.kauppa_api.sale.SaleWithProfitDTO;
import com.grupo_b.kauppa_api.sale.TopSalesDTO;
import com.grupo_b.kauppa_api.sale.SaleWithProfit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins ="*")
@RequestMapping(path="/report")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService){
        this.reportService = reportService;
    }


    @PostMapping(value = "/top-sales/monthly", consumes = "application/json", produces = "application/json")
    public List<TopSalesDTO> topMonthlySales(@RequestBody List<SaleWithProducts> sales){
        return reportService.getTopMonthlySale(sales);

    };

    @PostMapping(value = "/top-profitable-products/monthly", consumes = "application/json", produces = "application/json")
    public List<SaleWithProfitDTO> topMonthlyProductProfit(@RequestBody List<SaleWithProfit> sales){
        return reportService.getTopProfitableProducts(sales);
    }

    @GetMapping("/test")
    public String test(){return "hola";};
}
