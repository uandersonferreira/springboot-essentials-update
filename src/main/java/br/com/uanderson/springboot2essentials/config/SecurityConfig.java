package br.com.uanderson.springboot2essentials.config;

import br.com.uanderson.springboot2essentials.service.DevDojoUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@Log4j2
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final DevDojoUserDetailsService devDojoUserDetailsService;

    /**
     * BasicAuthenticationFilter
     * UsernamePasswordAuthenticationFilter
     * DefaultLoginPageGeneratingFilter
     * DefaultLogoutPageGeneratingFilter
     * FilterSecurityInterceptor
     * <p>
     * Fazer parte do processo de: Authentication -> Authorization
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean//O que será protegido com o protocolo Http
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        httpSecurity.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));
        http.csrf(AbstractHttpConfigurer::disable);//desabilitando o CSRF
        http.authorizeHttpRequests((authorizationManager -> authorizationManager
                        .requestMatchers( "/swagger-ui/**").permitAll()
                        .requestMatchers("/animes/admin/**").hasRole("ADMIN")//A Ordem de declaração é importante
                        .requestMatchers("/animes/**").hasRole("USER")
                        .anyRequest().authenticated()))
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());
        return http.build();
        /*
        Ao configurar uma requestMatchers(antiga antMatchers) é preciso ter uma atenção:
        - pois a ORDEM DE DECLARAÇÃO é importante
            - Comecando da MENOS restritiva para a MAIS restritiva
        - Podemos definir uma proteção baseado no tipo do method da requisição em uma
           determinada URL
         */
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        log.info("Password encoder {}", passwordEncoder().encode("123"));
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Retorne um PasswordEncoder adequado, como BCryptPasswordEncoder,
        // para codificar as senhas dos usuários
        return new BCryptPasswordEncoder();
    }


    /*
    UMA DAS ALTERNATIVAS PARA AUTENTICAR O USER DO BANCO DE DADOS:

    @Bean
    public UserDetailsService userDetailsService() {
        return devDojoUserDetailsService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    ------------ AUTENTICAÇÃO EM MEMÓRIA --------------

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        log.info("Password encoder {}", passwordEncoder.encode("123"));
        UserDetails admin = User
                .withUsername("Uanderson")
                .password(passwordEncoder.encode("123"))
                .roles("USER", "ADMIN")
                .build();

        UserDetails user = User.withUsername("devdojo")
                .password(passwordEncoder.encode("123"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    }*/



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

BasicAuthenticationFilter: É uma classe responsável por realizar a autenticação básica usando
                           o esquema de autenticação HTTP Basic. Ela extrai as informações de
                            autenticação do cabeçalho da solicitação e as valida com base nas
                             configurações de autenticação do Spring Security.

UsernamePasswordAuthenticationFilter: É uma classe que lida com o processo de autenticação usando
                                     o formulário de login tradicional, onde os usuários inserem
                                      um nome de usuário e senha. Ela captura as informações de
                                      autenticação fornecidas pelo usuário através de uma solicitação
                                      POST e as valida com base nas configurações de autenticação do
                                      Spring Security.

DefaultLoginPageGeneratingFilter: É uma classe responsável por gerar automaticamente uma página de
                                  login padrão se nenhuma página de login personalizada for fornecida.
                                  Essa página de login contém um formulário para entrada de nome de
                                  usuário e senha, que é tratado pelo UsernamePasswordAuthenticationFilter.

DefaultLogoutPageGeneratingFilter: É uma classe responsável por gerar automaticamente uma página de logout
                                   padrão se nenhuma página de logout personalizada for fornecida.
                                   Essa página de logout permite que os usuários efetuem logout do
                                   aplicativo.

FilterSecurityInterceptor: É uma classe que implementa a lógica de autorização do Spring Security.
                           Ela é responsável por interceptar as solicitações, aplicar as regras de
                           autorização configuradas e decidir se um usuário tem permissão para acessar
                           um recurso específico com base em suas configurações de segurança.

 */