package br.com.uanderson.springboot2essentials.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
@Configuration
@Log4j2
public class SecurityConfig {
    @Bean//O que será protegido com o protocolo Http
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));
        httpSecurity.csrf(AbstractHttpConfigurer::disable);//desabilitando o CSRF
        httpSecurity.authorizeHttpRequests((authorizationManager -> authorizationManager
                        .anyRequest().authenticated()))
                .httpBasic(Customizer.withDefaults());

        return httpSecurity.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        log.info("Password encoder {}", passwordEncoder.encode("test"));

        UserDetails userDetails = User
                .withUsername("Uanderson")
                .password(passwordEncoder.encode("123"))
                .roles("USER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(userDetails);
    }



    }//class
/*
O CSRF (Cross-Site Request Forgery) é um tipo de ataque que ocorre quando um
       invasor explora a confiança de um usuário autenticado para realizar ações
       não autorizadas em seu nome. Esse tipo de ataque geralmente envolve o envi
       de solicitações não autorizadas de um site malicioso para um site confiável
       no qual o usuário já está autenticado.

Para prevenir ataques CSRF em um aplicativo Spring Boot, o framework fornece o suporte
para geração e validação de tokens CSRF. O CSRF token (ou token de proteção CSRF) é um
valor único e aleatório que é gerado e associado a cada sessão de usuário. Esse token é
enviado ao cliente (geralmente como um cookie ou um cabeçalho HTTP) e deve ser incluído
em todas as solicitações que alteram o estado do servidor.

 */