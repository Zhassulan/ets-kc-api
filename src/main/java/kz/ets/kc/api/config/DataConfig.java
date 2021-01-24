package kz.ets.kc.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = {"kz.ets.kc.api.journal"})
@EnableTransactionManagement
public class DataConfig {
}
