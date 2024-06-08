package bun.buni.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;

@Configuration
public class ApplicationConfiguration {

    //Propiedades de la conexión:
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.mariadb.jdbc.Driver"); //or -> "org.h2.Driver"
        dataSource.setUrl("jdbc:mariadb://localhost:3306/batch_database");
        dataSource.setUsername("root");
        dataSource.setPassword("454514");
        return dataSource;
    }
}
