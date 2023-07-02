package br.com.uanderson.springboot2essentials.integration;

import br.com.uanderson.springboot2essentials.domain.Anime;
import br.com.uanderson.springboot2essentials.repository.AnimeRepository;
import br.com.uanderson.springboot2essentials.util.AnimeCreator;
import br.com.uanderson.springboot2essentials.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)//geração de portas aleatórias
@AutoConfigureTestDatabase//ativa as configurações para o banco de dados em memória
public class AnimeControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort//forma de pegar a porta aleatória gerada durante a execução dos testes
    private  int portaGerada;
    @Autowired
    AnimeRepository animeRepository;
    @Test
    @DisplayName("List returns list of anime inside page object when successful")
    void list_ReturnsListOfAnimeInsidePageObject_WhenSuccessful(){
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        String expectedName = savedAnime.getName();

        Page<Anime> animePage = testRestTemplate.exchange("/animes",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {}).getBody();

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList())//pega a lista de animePage
                .isNotEmpty()//verifica se não é vazia
                .hasSize(1);//é se seu tamanho é realmente 1 como definimos anteriomente no 'setUp'
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);//verifica se o nome do 1° obj da lista é igual ao nome esperado
    }


}//class
/*
Teste de integração inicia totalmente o servidor da aplicação, simulando um
deploy em produção e no nosso caso,toda vez que for iniciado será numa porta diferente.


 */