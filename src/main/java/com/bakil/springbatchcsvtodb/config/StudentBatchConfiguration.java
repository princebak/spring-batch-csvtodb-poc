package com.bakil.springbatchcsvtodb.config;

import com.bakil.springbatchcsvtodb.model.Student;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class StudentBatchConfiguration {
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    DataSource dataSource;

    @Bean
    public FlatFileItemReader<Student> readFromCsv(){
        FlatFileItemReader<Student> reader = new FlatFileItemReader<>();
        reader.setResource( new FileSystemResource("C://Users/USER/Desktop/data/csv_input.csv"));
        reader.setLineMapper( new DefaultLineMapper<Student>(){
            {
                setLineTokenizer(new DelimitedLineTokenizer(){
                    {
                        setNames(Student.fields());
                    }
                });
                setFieldSetMapper( new BeanWrapperFieldSetMapper<Student>(){
                    {
                        setTargetType(Student.class);
                    }
                });
            }
        });

        return reader;
    }

    @Bean
    public JdbcBatchItemWriter<Student> writerIntoDb(){
        JdbcBatchItemWriter<Student> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource( dataSource );
        writer.setSql("INSERT INTO student_tbl(id,firstName,lastName,email) values(:id,:firstName,:lastName,:email)");
        writer.setItemSqlParameterSourceProvider( new BeanPropertyItemSqlParameterSourceProvider<>() );

        return writer;
    }

    @Bean
    public Step step(){
        return stepBuilderFactory.get("step").<Student, Student>chunk(10)
                .reader( readFromCsv() )
                .writer( writerIntoDb() )
                .build();
    }

    @Bean
    public Job job(){
        return jobBuilderFactory.get("job").flow(step()).end().build();
    }
}
