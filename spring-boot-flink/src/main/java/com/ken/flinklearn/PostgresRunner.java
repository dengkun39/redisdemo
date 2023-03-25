package com.ken.flinklearn;

import com.ververica.cdc.connectors.postgres.PostgreSQLSource;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PostgresRunner implements ApplicationRunner
{

    @Override
    public void run(ApplicationArguments args) throws Exception {
        SourceFunction postgreSQLSource = PostgreSQLSource.<String>builder()
                .hostname("localhost")
                .port(5432)
                .database("postgres") // set captured database
                .tableList("postgres.market_price") // set captured table
                .username("user1")
                .password("pwd")
                .decodingPluginName("pgoutput")
                .deserializer(new JsonDebeziumDeserializationSchema()) // converts SourceRecord to JSON String
                .build();

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env
        .addSource(postgreSQLSource)
        .print().setParallelism(1); // use parallelism 1 for sink to keep message ordering

        env.execute("Print Postgres Snapshot + WAL");
    }
}
