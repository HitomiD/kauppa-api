package com.grupo_b.kauppa_api.report;

import com.grupo_b.kauppa_api.sale.SaleWithProfit;
import com.grupo_b.kauppa_api.sale.SaleWithProducts;
import com.grupo_b.kauppa_api.sale.SaleWithProfitDTO;
import com.grupo_b.kauppa_api.sale.TopSalesDTO;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import static org.apache.spark.sql.functions.*;

import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportGenerator {


    public SparkSession getSparkSession(){
        return SparkSession.builder().master("local").getOrCreate();
    }

    public List<TopSalesDTO> monthlyTopSales(SparkSession sparkSession, List<SaleWithProducts> sales){

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

    public List<SaleWithProfitDTO> mostMonthlyProfitProducts(SparkSession sparkSession, List<SaleWithProfit> sales){

        Dataset<SaleWithProfit> salesWithCost = sparkSession.createDataset(sales, Encoders.bean(SaleWithProfit.class));
        salesWithCost.show();

        Dataset <Row> explodedSales = salesWithCost
                .select(date_format(to_date(col("date"), //Discard day keeping year and month
                                "yyyy-MM-dd"),"yyyy-MM").as("date"),
                        explode(col("products")).as("P"), //Explode each sales product collection
                        col("P.name").as("product"),
                        col("P.quantity"),
                        col("P.cost"),
                        col("P.price"))
                .drop(col("P"))
                .groupBy(col("date"),
                        col("product"),
                        col("cost"),
                        col("price"))
                .agg(sum("quantity").as("quantity"))
                .withColumn("totalProfit",
                        col("quantity").multiply(col("price"))
                                .minus(col("quantity").multiply(col("cost"))));

        explodedSales.show();

        WindowSpec dateAndProfitWindow = Window //Window function for ranking products by profit
                .partitionBy("date")
                .orderBy(col("date").asc(),
                        col("totalProfit").desc());

        //explodedSales = explodedSales.withColumn("rank", rank().over(dateAndProfitWindow));

        List<SaleWithProfitDTO> rankedSales = explodedSales.as(Encoders.bean(SaleWithProfitDTO.class)).collectAsList();

        return rankedSales;
    }
}
