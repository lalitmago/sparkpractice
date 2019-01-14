package com.lama.sparkstreaming;

import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SparkStructuredStreamingExample {

    public static void main(String[] args) {
        SparkSession sparkSession = SparkSession
                .builder()
                .appName("Spark-Structured-Streaming-Example")
                .master("local[2]")
                .getOrCreate();

        SparkSession.builder().getOrCreate().sparkContext().setLogLevel("ERROR");

        Dataset<Row> socketDF = sparkSession
                .readStream()
                .format("socket")
                .option("host", "localhost")
                .option("port", "9876")
                .load();

        System.out.println("Is socket DF streaming? : " + socketDF.isStreaming());

        socketDF.printSchema();

        Encoder<DataRecord> dataRecordEncoder = Encoders.bean(DataRecord.class);
        Dataset<DataRecord> transformedDF = socketDF
                .as(Encoders.STRING())
                .map((MapFunction<String, DataRecord>) eachRecord -> {
                    String[] parts = eachRecord.split(",");
                    DataRecord record = new DataRecord();
                    record.setCOL3(Long.parseLong(parts[0]));
                    record.setCOL5(parts[1]);
                    record.setCOL8(Integer.parseInt(parts[2]));
                    return record;
                }, dataRecordEncoder);
                //.flatMap((FlatMapFunction<String, String>) x -> Arrays.asList(x.split("\n")).iterator(), Encoders.STRING());

        Dataset<Row> queryDF = transformedDF.select("COL3", "COL5", "COL8");



        StreamingQuery query = queryDF.writeStream()
                .outputMode("append")
                .format("console")
                .start();

        List<Row> tempList = new ArrayList<>();

        queryDF.writeStream()
                .outputMode("append")
                .foreach(new ForeachWriter<Row>() {
                    @Override
                    public void process(Row value) {
                            tempList.add(value.copy());
                    }

                    @Override
                    public void close(Throwable errorOrNull) {
                        System.out.println("close");
                    }

                    @Override
                    public boolean open(long partitionId, long version) {
                        return false;
                    }
                })
                .start();

        Dataset<Row> aggregatedDF = sparkSession.createDataFrame(tempList, DataRecord.class).sort("COL3");

        aggregatedDF.show();

        try {
            query.awaitTermination();
        } catch (StreamingQueryException e) {
            e.printStackTrace();
        }
    }
}
