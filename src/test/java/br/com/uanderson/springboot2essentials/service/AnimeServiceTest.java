package br.com.uanderson.springboot2essentials.service;

import br.com.uanderson.springboot2essentials.controller.AnimeController;
import br.com.uanderson.springboot2essentials.domain.Anime;
import br.com.uanderson.springboot2essentials.exeption.BadRequestException;
import br.com.uanderson.springboot2essentials.repository.AnimeRepository;
import br.com.uanderson.springboot2essentials.requestDto.AnimePostRequestBody;
import br.com.uanderson.springboot2essentials.requestDto.AnimePutRequestBody;
import br.com.uanderson.springboot2essentials.util.AnimeCreator;
import br.com.uanderson.springboot2essentials.util.AnimePostRequestBodyCreator;
import br.com.uanderson.springboot2essentials.util.AnimePutRequestBodyCreator;
import br.com.uanderson.springboot2essentials.util.DateUtil;
import jakarta.validation.ConstraintViolationException;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)//Informando que queremos herdar os comportamentos do Junit para utilizar com Spring boot em um contexto mais isolado
class AnimeServiceTest {
    @InjectMocks//@InjectMocks - Utiliza-se quando queremos testar a classe em si.
    private AnimeService animeService;
    @Mock//@Mock - Utiliza-se para todas as injenções de dependências(DI) que estão contidas na classe que queremos testar.
    private AnimeRepository animeRepositoryMock;
    @Mock
    private DateUtil dateUtilMock;

    @BeforeEach
    void setUp() {//Faz configurações padrões que serão executadas antes dos @Test
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))//Quando animeservicemock listar todos, não importanto o argumento passado
                .thenReturn(animePage);//então deve-se retornar o animePage.

        BDDMockito.when(animeRepositoryMock.findAll())
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))//Qualquer argumento do type Long
                .thenReturn(Optional.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))//Qualquer argumento do type String
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class)))//Qualquer object do type AnimePostRequestBody
                .thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.doNothing()//Não faça nada (pois o method é void)
                .when(animeRepositoryMock) //quando animeRepositoryMock chamar 'delete'
                .delete(ArgumentMatchers.any(Anime.class));//passando qualquer object Anime

    }
    @Test
    @DisplayName("ListAll returns list of anime inside page object when successful")
    void listAll_ReturnsListOfAnimeInsidePageObject_WhenSuccessful(){
        String expectedName = AnimeCreator.createValidAnime().getName();

        Page<Anime> animePage = animeService.listAll(PageRequest.of(1,1));

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList())//pega a lista de animePage
                .isNotEmpty()//verifica se não é vazia
                .hasSize(1);//é se seu tamanho é realmente 1 como definimos anteriomente no 'setUp'
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);//verifica se o nome do 1° obj da lista é igual ao nome esperado
    }

    @Test
    @DisplayName("List all no pageable returns list of anime when successful")
    void listAllNoPageable_ReturnsListOfAnimes_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime().getName();
        List<Anime> animes = animeService.listAllNoPageable();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findAnimeByIdOrThrowBadRequestException return anime when successful")
    void findAnimeByIdOrThrowBadRequestException_ReturnsAnime_WhenSuccessful() {
        Long expectedId = AnimeCreator.createValidAnime().getId();

        Anime anime = animeService.findAnimeByIdOrThrowBadRequestException(1L);

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findAnimeByIdOrThrowBadRequestException return anime when successful")
    void findAnimeByIdOrThrowBadRequestException_ThrowsBadRequestException_WhenAnimeIsNotFound() {
        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))//Não importa o argumento do type Long
                .thenReturn(Optional.empty());//deve-se retorne um Optional vazio

        Long expectedId = AnimeCreator.createValidAnime().getId();

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> this.animeService.findAnimeByIdOrThrowBadRequestException(expectedId))
                .withMessageContaining(String.format("Anime not found with id '%s", expectedId));

    }

    @Test
    @DisplayName("findByName return list of anime when successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime().getName();

        List<Anime> animeList = animeService.findByName("nameAnime");

        Assertions.assertThat(animeList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName return an empty list of anime when anime is not found")
    void findByName_ReturnsEmptyListOfAnime_WhenAnimeIsNotFound() {
        //Sobreescrevendo o comportanto definito no @BeforeEach do method findByname() para retornar uma lista vazia.
        //Comportamentos locais têm preferência ao serem executados.
        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))//Qualquer argumento do type String
                .thenReturn(Collections.emptyList());

        List<Anime> animeList = animeService.findByName("nameAnime");

        Assertions.assertThat(animeList)
                .isNotNull() //pois mesmo sendo vazia, existe a referência de um objeto
                .isEmpty();
    }

    @Test
    @DisplayName("Save return anime when successful")
    void save_ReturnsAnime_WhenSuccessful() {
        Anime anime = animeService.save(AnimePostRequestBodyCreator.createAnimePostRequestBody());

        Assertions.assertThat(anime)
                .isNotNull()
                .isEqualTo(AnimeCreator.createValidAnime());
    }

    @Test
    @DisplayName("Replace updates return anime when successful")
    void replace_ReturnsAnime_WhenSuccessful() {
        //forma de testar um method sem retorno(void)
        Assertions.assertThatCode(() -> animeService.replace(AnimePutRequestBodyCreator.createAnimePutRequestBody()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("DeleteById removes return anime when successful")
    void deleteById_RemoveAnime_WhenSuccessful() {
        //forma de testar um method sem retorno(void)
        Assertions.assertThatCode(() -> animeService.delete(1L))
                .doesNotThrowAnyException();
    }


}