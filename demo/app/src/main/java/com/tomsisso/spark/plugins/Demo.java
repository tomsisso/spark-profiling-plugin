package com.tomsisso.spark.plugins;

import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

import java.util.concurrent.TimeUnit;

public class Demo {
    public static void main(String[] args) throws InterruptedException {
        SparkSession sparkSession = SparkSession.builder().getOrCreate();
        String inputPath = "/opt/spark-data/";
//        String inputPath = Demo.class.getClassLoader().getResource("org.csv").getPath();

        sparkSession.read()
                .option("header", true)
                .csv(inputPath)
                .repartition(10)
                .createOrReplaceTempView("input");

        sparkSession.sql("" +
                        "SELECT Country, count(1) " +
                        "FROM input " +
                        "GROUP BY Country" +
                        "")
                .write()
                .mode(SaveMode.Overwrite)
                .parquet(inputPath + "output");

        //sleeping to keep spark ui for a while
        Thread.sleep(TimeUnit.MINUTES.toMillis(5));
    }
}