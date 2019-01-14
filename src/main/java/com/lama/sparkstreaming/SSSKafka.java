package com.lama.sparkstreaming;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQueryException;

public class SSSKafka {

    public static void main(String[] args) {
        SparkSession sparkSession = SparkSession
                .builder()
                .appName("SSSKafka-Example")
                .master("local[2]")
                .getOrCreate();

        SparkSession.builder().getOrCreate().sparkContext().setLogLevel("TRACE");

        Dataset<Row> df = sparkSession
                .readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", "localhost:9092")
                .option("subscribe", "test")
                .option("includeTimestamp", "true")
                .load()
                .selectExpr("CAST(topic AS STRING)", "CAST(key AS STRING)", "CAST(value AS STRING)", "CAST(timestamp as STRING)");


        try {

            df.writeStream()
                    .outputMode("append")
                    .format("console")
                    .option("truncate", false)
                    .start()
                    .awaitTermination();
        } catch (StreamingQueryException e) {
            e.printStackTrace();
        }
    }
}
