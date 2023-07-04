package br.com.uanderson.springboot2essentials.integration;

import br.com.uanderson.springboot2essentials.domain.Anime;
import br.com.uanderson.springboot2essentials.domain.DevDojoUserDetails;
import br.com.uanderson.springboot2essentials.repository.AnimeRepository;
import br.com.uanderson.springboot2essentials.repository.DevDojoUserDetailsRepository;
import br.com.uanderson.springboot2essentials.requestDto.AnimePostRequestBody;
import br.com.uanderson.springboot2essentials.util.AnimeCreator;
import br.com.uanderson.springboot2essentials.util.AnimePostRequestBodyCreator;
import br.com.uanderson.springboot2essentials.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)//geração de portas aleatórias
@AutoConfigureTestDatabase//ativa as configurações para o banco de dados em memória
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//faz com que o contexto da applicação precise ser recriado antes da execução do próximo teste
public class AnimeControllerIT {
    //    @LocalServerPort//forma de pegar a porta aleatória gerada durante a execução dos testes
    //    private  int portaGerada;
    @Autowired
    AnimeRepository animeRepository;
    @Autowired
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testRestTemplateRoleUser;
    @Autowired
    @Qualifier(value = "testRestTemplateRoleAdmin")
    private TestRestTemplate testRestTemplateRoleAdmin;
    @Autowired
    DevDojoUserDetailsRepository devDojoUserDetailsRepository;
    private static final DevDojoUserDetails USER = DevDojoUserDetails.builder()
            .name("user")
            .password("$2a$10$9E6VZf579l/FGACFJJQVzOo0rYp0SRvboH7OCR43.hr1hjp5xDJtu")
            .username("user")
            .authorities("ROLE_USER")
            .build();
    private static final DevDojoUserDetails ADMIN = DevDojoUserDetails.builder()
            .name("admin")
            .password("$2a$10$9E6VZf579l/FGACFJJQVzOo0rYp0SRvboH7OCR43.hr1hjp5xDJtu")
            .username("admin")
            .authorities("ROLE_USER,ROLE_ADMIN")
            .build();

    @TestConfiguration
    @Lazy
    static class Config {
        @Bean(value = "testRestTemplateRoleUser")
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("user", "123");
            return new TestRestTemplate(restTemplateBuilder);
        }

        @Bean(value = "testRestTemplateRoleAdmin")
        public TestRestTemplate testRestTemplateRoleAdminCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("admin", "123");
            return new TestRestTemplate(restTemplateBuilder);
        }
                /*
        Precisamos criar um Bean que irá faze o Autowired do TestRestTemplate,
        habilitando a segurança configurada na nossa aplicação.


        A nomeação é comum, quando temos mais de um tipo de bean, que pode
        ser injetado automaticamente.

        .rootUri("http:localhost:"+ port) - Para evitar:
            - java.lang.IllegalArgumentException: URI is not absolute


         @Lazy - Neste caso estamos utilizando a flag
         para fazer com a classe tenha uma inicialização mais lenta,
         fazendo que seja carregada somente, depois da configuração da porta,
         pois o atributo da porta gerado não é estático para pode utilizar diretamento
         no method testRestTemplateRoleUserCreator().


         */
    }

    @Test
    @DisplayName("List returns list of anime inside page object when successful")
    void list_ReturnsListOfAnimeInsidePageObject_WhenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        devDojoUserDetailsRepository.save(USER);

        String expectedName = savedAnime.getName();

        Page<Anime> animePage = testRestTemplateRoleUser.exchange("/animes",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {
                }).getBody();

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList())//pega a lista de animePage
                .isNotEmpty()//verifica se não é vazia
                .hasSize(1);//é se seu tamanho é realmente 1 como definimos anteriomente no 'setUp'
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);//verifica se o nome do 1° obj da lista é igual ao nome esperado
    }

    @Test
    @DisplayName("List all returns list of anime when successful")
    void listAll_ReturnsListOfAnimes_WhenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        devDojoUserDetailsRepository.save(USER);

        String expectedName = savedAnime.getName();

        List<Anime> animes = testRestTemplateRoleUser.exchange("/animes/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Anime>>() {}).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()//verifica se não é vazia
                .hasSize(1);//é se seu tamanho é realmente 1 como definimos anteriomente
        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);//verifica se o nome do 1° obj da lista é igual ao nome esperado
    }

    @Test
    @DisplayName("FindById return anime when successful")
    void findById_ReturnsAnime_WhenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        devDojoUserDetailsRepository.save(USER);

        Long expectedId = savedAnime.getId();

        Anime anime = testRestTemplateRoleUser.getForObject("/animes/{id}", Anime.class, expectedId);

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName return list of anime when successful")
    void findByname_ReturnsListOfAnime_WhenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        devDojoUserDetailsRepository.save(USER);

        String expectedName = savedAnime.getName();

        String url = String.format("/animes/find?name=%s", expectedName);
        List<Anime> animes = testRestTemplateRoleUser.exchange(url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Anime>>() {}).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName return an empty list of anime when anime is not found")
    void findByname_ReturnsEmptyListOfAnime_WhenAnimeIsNotFound() {
        devDojoUserDetailsRepository.save(USER);

        List<Anime> animes = testRestTemplateRoleUser.exchange("/animes/find?name=dbz", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {}).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("Save return anime when successful")
    void save_ReturnsAnime_WhenSuccessful() {
        AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.createAnimePostRequestBody();
        devDojoUserDetailsRepository.save(ADMIN);

        ResponseEntity<Anime> animeResponseEntity = testRestTemplateRoleAdmin.postForEntity("/animes",
                animePostRequestBody,
                Anime.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();
        Assertions.assertThat(animeResponseEntity.getBody().getName()).isNotNull();
    }

    @Test
    @DisplayName("Replace updates return anime when successful")
    void replace_ReturnsAnime_WhenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        devDojoUserDetailsRepository.save(USER);

        savedAnime.setName("novo nome");

        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleUser.exchange("/animes",
                HttpMethod.PUT,
                new HttpEntity<>(savedAnime),
                Void.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("DeleteById removes return anime when successful")
    void deleteById_RemoveAnime_WhenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        devDojoUserDetailsRepository.save(ADMIN);

        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleAdmin.exchange("/animes/admin/{id}",
                HttpMethod.DELETE,
               null,
                Void.class,
                savedAnime.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("DeleteById returns 403 when user is not admin")
    void deleteById_Returns403_WhenUserIsNotAdmin() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        devDojoUserDetailsRepository.save(USER);

        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleUser.exchange("/animes/admin/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                savedAnime.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }


}//class
/*
COMANDO PARA RODAR OS TESTES UNITÁRIOS:
    - mvn test -Pintegration-tests
    - mvn = maven
    - test = ação que queremos
    - P = profile
    - integration-tests = id do profile definido no pom.xml

Teste de integração inicia totalmente o servidor da aplicação, simulando um
deploy em produção e no nosso caso,toda vez que for iniciado será numa porta diferente.

A anotação @DirtiesContext é uma anotação fornecida pelo Spring Framework que indica
ao Spring que o contexto do aplicativo deve ser "sujo" ou "sujado". Isso significa
que a anotação é usada para indicar que o contexto do aplicativo foi modificado durante
a execução de um teste e que o contexto precisa ser recriado antes da execução do próximo teste.

 */