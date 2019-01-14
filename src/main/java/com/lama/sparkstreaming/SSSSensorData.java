package com.lama.sparkstreaming;

import org.apache.hadoop.mapred.OutputFormat;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;


import java.util.ArrayList;
import java.util.List;

public class SSSSensorData {

    public static void main(String[] args) {
        SparkSession sparkSession = SparkSession
                .builder()
                .appName("SSS-Sensor-Data-Example")
                .master("local[2]")
                .getOrCreate();

        SparkSession.builder().getOrCreate().sparkContext().setLogLevel("ERROR");

        StructType schema = new StructType()
                .add("event_time", DataTypes.TimestampType)
                .add("device_id", DataTypes.IntegerType)
                .add("signal_strength", DataTypes.IntegerType);

        Dataset<Row> df = sparkSession
                .readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", "localhost:9092")
                .option("subscribe", "test")
                .schema(schema)
                .load()
                .selectExpr("CAST(value AS STRING");

        Column column = df.col("value")
                ;

                //.selectExpr("CAST(topic AS STRING)", "CAST(key AS STRING)", "CAST(value AS STRING)");

        try {

            //df.as()


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
