package com.ken.flinklearn;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.springframework.stereotype.Component;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;


import java.util.Properties;

//@Component
@Slf4j
public class KafkaRunner implements ApplicationRunner
{
    @Override
    public void run(ApplicationArguments args) throws Exception {
        try{

            /****************************************************************************
             *                 Setup Flink environment.
             ****************************************************************************/

            // Set up the streaming execution environment
            final StreamExecutionEnvironment streamEnv
                    = StreamExecutionEnvironment.getExecutionEnvironment();

            /****************************************************************************
             *                  Read Kafka Topic Stream into a DataStream.
             ****************************************************************************/

            //Set connection properties to Kafka Cluster
            Properties properties = new Properties();
            properties.setProperty("bootstrap.servers", "localhost:29092");
            properties.setProperty("group.id", "flink.learn.realtime");

            //Setup a Kafka Consumer on Flnk
            FlinkKafkaConsumer<String> kafkaConsumer =
                    new FlinkKafkaConsumer<>
                            ("flink.kafka.streaming.source", //topic
                                    new SimpleStringSchema(), //Schema for data
                                    properties); //connection properties

            //Setup to receive only new messages
            kafkaConsumer.setStartFromLatest();

            //Create the data stream
            DataStream<String> auditTrailStr = streamEnv
                    .addSource(kafkaConsumer);

            //Convert each record to an Object
            DataStream<Tuple2<String, Integer>> userCounts
                    = auditTrailStr
                    .map(new MapFunction<String,Tuple2<String,Integer>>() {

                        @Override
                        public Tuple2<String,Integer> map(String auditStr) {
                            System.out.println("--- Received Record : " + auditStr);
                            AuditTrail at = new AuditTrail(auditStr);
                            return new Tuple2<String,Integer>(at.getUser(),at.getDuration());
                        }
                    })

                    .keyBy(0)  //By user name
                    .reduce((x,y) -> new Tuple2<String,Integer>( x.f0, x.f1 + y.f1));

            //Print User and Durations.
            userCounts.print();

            /****************************************************************************
             *                  Setup data source and execute the Flink pipeline
             ****************************************************************************/
            //Start the Kafka Stream generator on a separate thread
            System.out.println("Starting Kafka Data Generator...");
            Thread kafkaThread = new Thread(new KafkaStreamDataGenerator());
            kafkaThread.start();

            // execute the streaming pipeline
            streamEnv.execute("Flink Windowing Example");

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
