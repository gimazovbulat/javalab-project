package ru.itis.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "ru.itis")
@EnableTransactionManagement
@PropertySource("classpath:/application.properties")
public class ApplicationContextImpl {
    private final static String DB_USERNAME = "db.username";
    private final static String DB_PASSWORD = "db.password";
    private final static String DB_DRIVER = "db.driver";
    private final static String DB_URL = "db.url";

    private final static String MAIL_USERNAME = "mail.username";
    private final static String MAIL_PASSWORD = "mail.password";
    private final static String MAIL_DEBUG = "mail.debug";

    final
    Environment environment;

    public ApplicationContextImpl(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(datasource());
    }

    @Bean
    public DataSource datasource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(environment.getRequiredProperty(DB_DRIVER));
        driverManagerDataSource.setUsername(environment.getRequiredProperty(DB_USERNAME));
        driverManagerDataSource.setPassword(environment.getRequiredProperty(DB_PASSWORD));
        driverManagerDataSource.setUrl(environment.getRequiredProperty(DB_URL));
        return driverManagerDataSource;
    }

    @Bean
    public HikariConfig hikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(environment.getProperty(DB_URL));
        config.setUsername(environment.getProperty(DB_USERNAME));
        config.setPassword(environment.getProperty(DB_PASSWORD));
        config.setDriverClassName(environment.getProperty(DB_DRIVER));
        return config;
    }

    @Bean
    public DataSource hikariDataSource() {
        return new HikariDataSource(hikariConfig());
    }

    @Bean
    JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername(environment.getProperty(MAIL_USERNAME));
        mailSender.setPassword(environment.getProperty(MAIL_PASSWORD));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", environment.getProperty(MAIL_DEBUG));

        return mailSender;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    freemarker.template.Configuration ftlConfiguration(){
        freemarker.template.Configuration cfg =
                new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_22);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        return cfg;
    }
}
