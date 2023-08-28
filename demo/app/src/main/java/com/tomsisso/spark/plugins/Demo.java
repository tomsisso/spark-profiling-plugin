package com.tomsisso.spark.plugins;

import org.apache.spark.sql.SparkSession;

public class Demo {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello world");
//        SparkSession sparkSession = SparkSession.builder().getOrCreate();
        SparkSession sparkSession = SparkSession.builder().master("local[*]").getOrCreate();
        String inputPath = Demo.class.getClassLoader().getResource("org.csv").getPath();
//        String inputPath = "/opt/demo-data";

        sparkSession.read()
                .option("header", true)
                .csv(inputPath)
                .createOrReplaceTempView("input");

//        sparkSession.sql("select * from input").printSchema();

        sparkSession.sql("" +
                        "SELECT Country, count(1) " +
                        "FROM input " +
                        "GROUP BY Country" +
                        "")
                .collectAsList();

        //sleeping to keep spark ui for a while
        Thread.sleep(1000000000);
    }
}