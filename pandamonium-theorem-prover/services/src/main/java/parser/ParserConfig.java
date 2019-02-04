package parser;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan
@PropertySource("classpath:development.properties")
public class ParserConfig {

    @Bean
    public ParserService parserService() {
        return new ParserService();
    }
}
