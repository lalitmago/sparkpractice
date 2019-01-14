package com.lama.sparkstreaming;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;


public class KafkaSampleProducer {

        public static void main(String[] args) throws InterruptedException {
            Properties props = new Properties();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
            KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);

            Path path = FileSystems.getDefault().getPath("data/data-sensors.txt");

            List<String> lines = null;

            try {
                lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < lines.size(); i++) {
                ProducerRecord<String, String> data;

                data = new ProducerRecord<String, String>("test", 0, null, lines.get(i));

                producer.send(data);
                Thread.sleep(1000);
            }
            producer.close();
        }

}
