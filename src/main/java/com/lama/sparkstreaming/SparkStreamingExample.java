package com.lama.sparkstreaming;

import org.apache.spark.SparkConf;

import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

public class SparkStreamingExample {

    public static void main(String[] args) {

        SparkConf conf = new SparkConf()
                .setMaster("local[2]")
                .setAppName("SSS=Test");

        JavaStreamingContext streamingContext = new JavaStreamingContext(conf, Duration.apply(5000));
        SparkSession.builder().getOrCreate().sparkContext().setLogLevel("ERROR");

        JavaDStream<String> inputStream = streamingContext.socketTextStream("localhost", 9876);

        JavaDStream<String> outputStream = inputStream.filter(new Function<String, Boolean>() {
            @Override
            public Boolean call(String s) throws Exception {
                int i = Integer.parseInt(s.split(",")[2]);
                if(i%2 == 0)
                    return true;
                return false;
            }
        });

        outputStream.print();

        streamingContext.start();
        try {
            streamingContext.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
