package mp.cariaso.springboot.integration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
public class IntegrationTestConfiguration {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertiesResolver() {

        PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
        Properties props = new Properties();

        // pointed to: target/classes/application.properties
        try (InputStream input = IntegrationTestConfiguration.class.getResourceAsStream("/application.properties")) {

            props.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Properties testProps = new Properties();
        InputStream testInput = null;

        String environment = props.getProperty("spring.profiles.active");
        System.out.println("ENV: " + environment);

        try {
            // pointed to: target/test-classes/profiles/*
            if ("LOCAL".equals(environment)) {
                testInput = IntegrationTestConfiguration.class.getResourceAsStream("/profiles/local/config-local.properties");
            } else if ("DEV".equals(environment)) {
                testInput = IntegrationTestConfiguration.class.getResourceAsStream("/profiles/dev/config-dev.properties");
            } else if ("QAT".equals(environment)) {
                testInput = IntegrationTestConfiguration.class.getResourceAsStream("/profiles/qat/config-qat.properties");
            }

            testProps.load(testInput);
            props.putAll(testProps);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(testInput != null) {
                    testInput.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        pspc.setProperties(props);

        return pspc;
    }
}
