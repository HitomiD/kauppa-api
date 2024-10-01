package com.grupo_b.kauppa_api.web_report;


import com.grupo_b.kauppa_api.product.Product;
import com.grupo_b.kauppa_api.sale.SaleRequestDTO;
import com.grupo_b.kauppa_api.sale.SaleWithProducts;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WebReportService {
    @Autowired
    private WebReportGenerator webReportGenerator;

    public List<Product> getTopMonthlySales(ArrayList<SaleRequestDTO> requestDTOSales){
        SparkSession sparkSession = webReportGenerator.getSparkSession();

        ArrayList<SaleWithProducts> sales = saleRequestToSaleWithProducts(requestDTOSales);

        return webReportGenerator.mostToLeastSoldProducts(sparkSession, sales);
    }

    public ArrayList<SaleWithProducts> saleRequestToSaleWithProducts(ArrayList<SaleRequestDTO> salesRequestDTOList){
        //Map the ProductRequestDTO to a normal Product class object
        ArrayList<SaleWithProducts> salesList = new ArrayList<SaleWithProducts>();
        for (int i=0; i < salesRequestDTOList.size(); i++){
            SaleWithProducts saleWithProducts = new SaleWithProducts();
            saleWithProducts.setId(salesRequestDTOList.get(i).getId());
            saleWithProducts.setDate(salesRequestDTOList.get(i).getDate());

            //Regular expression to parse the products from the string field
            Pattern pattern = Pattern.compile("(\\w+)\\s\\((\\d+)\\)");
            Matcher matcher = pattern.matcher(salesRequestDTOList.get(i).getProducts());
            ArrayList<Product> productsInvolved = new ArrayList<Product>();
            while (matcher.find()) {
                Product product = new Product();
                product.setName(matcher.group(1));
                product.setQuantity(Long.parseLong(matcher.group(2)));
                productsInvolved.add(product);
            }

            saleWithProducts.setProducts(productsInvolved);
            salesList.add(saleWithProducts);
        }
        return salesList;
    }
}
