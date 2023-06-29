package br.com.uanderson.springboot2essentials.controller;

import br.com.uanderson.springboot2essentials.domain.Anime;
import br.com.uanderson.springboot2essentials.requestDto.AnimePostRequestBody;
import br.com.uanderson.springboot2essentials.requestDto.AnimePutRequestBody;
import br.com.uanderson.springboot2essentials.service.AnimeService;
import br.com.uanderson.springboot2essentials.util.AnimeCreator;
import br.com.uanderson.springboot2essentials.util.AnimePostRequestBodyCreator;
import br.com.uanderson.springboot2essentials.util.AnimePutRequestBodyCreator;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)//Informando que queremos herdar os comportamentos do Junit para utilizar com Spring boot em um contexto mais isolado
class AnimeControllerTest {

    @InjectMocks//@InjectMocks - Utiliza-se quando queremos testar a classe em si.
    private AnimeController animeController;
    @Mock//@Mock - Utiliza-se para todas as injenções de dependências(DI) que estão contidas na classe que queremos testar.
    private AnimeService animeServiceMock;
    @Mock
    private DateUtil dateUtilMock;

    @BeforeEach
    void setUp() {//Faz configurações padrões que serão executadas antes dos @Test
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any()))//Quando animeservicemock listar todos, não importanto o argumento passado
                .thenReturn(animePage);//então deve-se retornar o animePage.

        BDDMockito.when(animeServiceMock.listAllNoPageable())
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeServiceMock.findAnimeByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))//Qualquer argumento do type Long
                .thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))//Qualquer argumento do type String
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeServiceMock.save(ArgumentMatchers.any(AnimePostRequestBody.class)))//Qualquer object do type AnimePostRequestBody
                .thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.doNothing()//Não faça nada
                .when(animeServiceMock) //quando animeServiceMock chamar 'replace'
                .replace(ArgumentMatchers.any(AnimePutRequestBody.class));//passando um AnimePutRequestBody

        BDDMockito.doNothing()//Não faça nada
                .when(animeServiceMock) //quando animeServiceMock chamar 'delete'
                .delete(ArgumentMatchers.anyLong());//passando qualquer LOng

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

    @Test
    @DisplayName("List all returns list of anime when successful")
    void listAll_ReturnsListOfAnimes_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime().getName();
        List<Anime> animes = animeController.listAllNoPageable().getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindById return anime when successful")
    void findById_ReturnsAnime_WhenSuccessful() {
        Long expectedId = AnimeCreator.createValidAnime().getId();

        Anime anime = animeController.findById(1L).getBody();

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName return list of anime when successful")
    void findByname_ReturnsListOfAnime_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime().getName();

        List<Anime> animeList = animeController.findByName("nameAnime").getBody();

        Assertions.assertThat(animeList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName return an empty list of anime when anime is not found")
    void findByname_ReturnsEmptyListOfAnime_WhenAnimeIsNotFound() {
        //Sobreescrevendo o comportanto definito no @BeforeEach do method findByname() para retornar uma lista vazia.
        //Comportamentos locais têm preferência ao serem executados.
        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))//Qualquer argumento do type String
                .thenReturn(Collections.emptyList());

        List<Anime> animeList = animeController.findByName("nameAnime").getBody();

        Assertions.assertThat(animeList)
                .isNotNull() //pois mesmo sendo vazia, existe a referência de um objeto
                .isEmpty();
    }

    @Test
    @DisplayName("Save return anime when successful")
    void save_ReturnsAnime_WhenSuccessful() {
        Anime anime = animeController.save(AnimePostRequestBodyCreator.createAnimePostRequestBody()).getBody();

        Assertions.assertThat(anime)
                .isNotNull()
                .isEqualTo(AnimeCreator.createValidAnime());
    }

    @Test
    @DisplayName("Replace updates return anime when successful")
    void replace_ReturnsAnime_WhenSuccessful() {
        //1° forma de testar um method sem retorno(void)
        Assertions.assertThatCode(() -> animeController.replace(AnimePutRequestBodyCreator.createAnimePutRequestBody()))
                .doesNotThrowAnyException();

        //2° forma de testar um method sem retorno(void)
        ResponseEntity<Void> entity = animeController.replace(AnimePutRequestBodyCreator.createAnimePutRequestBody());

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("DeleteById removes return anime when successful")
    void deleteById_RemoveAnime_WhenSuccessful() {
        //1° forma de testar um method sem retorno(void)
        Assertions.assertThatCode(() -> animeController.deleteById(1L))
                .doesNotThrowAnyException();

        //2° forma de testar um method sem retorno(void)
        ResponseEntity<Void> entity = animeController.deleteById(1L);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
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