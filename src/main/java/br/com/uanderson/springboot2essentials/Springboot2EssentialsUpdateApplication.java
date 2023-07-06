package br.com.uanderson.springboot2essentials;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Springboot2EssentialsUpdateApplication {
	public static void main(String[] args) {
		SpringApplication.run(Springboot2EssentialsUpdateApplication.class, args);
	}

	/*
		Configuração de bean para fornecimento das metricas
		pro grafana através do Dashboard JVM (Micrometer) - 4701
	 */
	@Bean
	MeterRegistryCustomizer<MeterRegistry> configurer(
			@Value("${spring.application.name}") String applicationName) {
		return (registry) -> registry.config().commonTags("application", applicationName);
	}


}
/*
	OBSERVATION: Sempre que for criar uma BRANCH e trabalhar nela,
	fazer o commit das alterações feitas, para que fiquem somente na branch.
*/
