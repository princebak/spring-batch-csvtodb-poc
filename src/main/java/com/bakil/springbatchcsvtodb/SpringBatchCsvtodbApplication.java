package com.bakil.springbatchcsvtodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBatchCsvtodbApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchCsvtodbApplication.class, args);
    }

   /* @Bean
    public IntegrationDataSourceScriptDatabaseInitializer customIntegrationDataSourceInitializer(DataSource dataSource) {
        return new IntegrationDataSourceScriptDatabaseInitializer(dataSource, new DatabaseInitializationSettings());
    }*/
}
