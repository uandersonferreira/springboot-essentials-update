package br.com.uanderson.springboot2essentials.controller;

import br.com.uanderson.springboot2essentials.domain.Anime;
import br.com.uanderson.springboot2essentials.service.AnimeService;
import br.com.uanderson.springboot2essentials.util.AnimeCreator;
import br.com.uanderson.springboot2essentials.util.DateUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)//Informando que queremos herdar os comportamentos do Junit para utilizar com Spring boot em um contexto mais isolado
class AnimeControllerTest {

    @InjectMocks//@InjectMocks - Utiliza-se quando queremos testar a classe em si.
    private AnimeController animeController;
    @Mock//@Mock - Utiliza-se para todas as injenções de dependências(DI) que estão contidas na classe que queremos testar.
    private AnimeService animeServiceMock;
    @Mock
    private DateUtil dateUtilMock;

    @BeforeEach
    void setUp(){//Faz configurações padrões que serão executadas antes dos @Test
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any()))//Quando animeservicemock listar todos, não importanto o argumento passado
                .thenReturn(animePage);//então deve-se retornar o animePage.
    }
    @Test
    @DisplayName("List returns list of anime inside page object when successful")
    void list_ReturnsListOfAnimeInsidePageObject_WhenSuccessful(){
        String expectedName = AnimeCreator.createValidAnime().getName();

        Page<Anime> animePage = animeController.list(null).getBody();

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList())//pega a lista de animePage
                .isNotEmpty()//verifica se não é vazia
                .hasSize(1);//é se seu tamanho é realmente 1 como definimos anteriomente no 'setUp'
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);//verifica se o nome do 1° obj da lista é igual ao nome esperado
    }



}//class

/*
@ExtendWith(SpringExtension.class) - Informando que queremos herdar os comportamentos
                                    do Junit para utilizar com Spring boot em um contexto
                                     mais isolado.

@SpringBootTest - é usada no ecossistema do Spring para testar a aplicação como um todo,
                  em um contexto mais amplo. Necessita que o banco e a aplicação esteja rodando
                  ou seja, pois  tenta inicializar a aplicação  para realizar os testes.



@InjectMocks - Utiliza-se quando queremos testar a classe em si.
@Mock - Utiliza-se para todas as classes que estão dentro da
classe que queremos testar (Que de alguma forma está sendo necessário sua utilização).

Ex:
  Queremos testar a class AnimeController e dentro dela estamos utilizando uma
  referência das classes:
     - DateUtil dateUtil;
     - AnimeService animeService;

E dentro do AnimeService estamos utilizando uma referência da class(Interface):
   - AnimeRepository animeRepository


then - então
when - quando
that - isso/esse
assertThat - afirmar isso
inside - dentro

 */