package com.grupo_b.kauppa_api.report;

import com.grupo_b.kauppa_api.sale.SaleWithProfit;
import com.grupo_b.kauppa_api.sale.SaleWithProducts;
import com.grupo_b.kauppa_api.sale.SaleWithProfitDTO;
import com.grupo_b.kauppa_api.sale.TopSalesDTO;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportGenerator reportGenerator;

    /*
    @Autowired
    public ReportService(ReportGenerator reportGenerator){
        this.reportGenerator = reportGenerator;
    }
    */
    public List<TopSalesDTO> getTopMonthlySale(List<SaleWithProducts> sales){
        SparkSession sparkSession = reportGenerator.getSparkSession();
        return reportGenerator.monthlyTopSales(sparkSession, sales);
    }

    public List<SaleWithProfitDTO> getTopProfitableProducts(List<SaleWithProfit> sales){
        return reportGenerator.mostMonthlyProfitProducts(reportGenerator.getSparkSession(), sales);
    }

}
