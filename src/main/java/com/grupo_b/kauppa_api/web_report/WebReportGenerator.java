package com.grupo_b.kauppa_api.web_report;

import com.grupo_b.kauppa_api.product.Product;
import com.grupo_b.kauppa_api.sale.SaleWithProducts;
import com.grupo_b.kauppa_api.sale.TopSalesDTO;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;
import org.apache.spark.sql.functions;
import org.springframework.stereotype.Service;


import java.util.List;

import static org.apache.spark.sql.functions.*;

@Service
public class WebReportGenerator {
    public SparkSession getSparkSession(){
        return SparkSession.builder().master("local").getOrCreate();
    }

    /**
     * Returns an ordered list of products from most to least units sold
     * @param sparkSession
     * @param sales
     * @return
     */
    public List<TopSalesDTO> yearlyTopProductsByMonth(SparkSession sparkSession, List<SaleWithProducts> sales){

        Dataset<SaleWithProducts> salesDataset = sparkSession.createDataset(sales, Encoders.bean(SaleWithProducts.class));


        Dataset<Row> explodedSales1 = salesDataset
                .select(date_format(to_date(col("date"), //Discard day keeping year and month
                                "yyyy-MM-dd"),"yyyy-MM").as("date"),
                        explode(col("products")).as("P"), //Explode each sales product collection
                        col("P.name").as("product"),
                        col("P.quantity"))
                .drop(col("P"))
                .groupBy(col("date"),col("product"))
                .agg(sum("quantity").as("quantity"));

        WindowSpec dateAndProductWindow = Window //Window function for ranking products by month
                .partitionBy("date")
                .orderBy(col("date").asc(),
                        col("quantity").desc());

        Dataset<TopSalesDTO> orderedMonthlySales = explodedSales1
                .withColumn("rank", rank().over(dateAndProductWindow))
                .as(Encoders.bean(TopSalesDTO.class));
        orderedMonthlySales.show();
        return orderedMonthlySales.collectAsList();
    }

    public List<Product> mostToLeastSoldProducts(SparkSession sparkSession, List<SaleWithProducts> sales){

        Dataset<SaleWithProducts> salesDataset = sparkSession.createDataset(sales, Encoders.bean(SaleWithProducts.class));
        salesDataset.show();
        Dataset<Row> explodedSales1 = salesDataset
                .select(explode(col("products")).as("P"), //Explode each sales product collection
                        col("P.name").as("name"),
                        col("P.quantity"))
                .drop(col("P"))
                .groupBy(col("name"))
                .agg(sum("quantity").as("quantity"));
        explodedSales1.show();

        Dataset<Product> orderedMonthlySales = explodedSales1
                .select("*")
                .orderBy(functions.desc("quantity"))
                .as(Encoders.bean(Product.class));
        orderedMonthlySales.show();
        return orderedMonthlySales.collectAsList();
    }
}
