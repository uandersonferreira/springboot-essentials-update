package br.com.uanderson.springboot2essentials.repository;

import br.com.uanderson.springboot2essentials.domain.Anime;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("Tests for Anime Repository")//é usada no JUnit para fornecer um nome descritivo aos testes ou às classes de teste.
class AnimeRepositoryTest {
    @Autowired
    private  AnimeRepository animeRepository;

    //Conversão de criação de nomes de Test (Recomendando pelo Devdojo)
    //NOME DO METHOD + O QUE PRECISA FAZER ? + QUANDO ISSO DEVE ACONTECER ? || separando por underscore
    //EX: save_PersistAnime_WhenSuccessful
    @Test
    @DisplayName("Save persist anime when Successful")
    void save_PersistAnime_WhenSuccessful(){
        Anime animeToBeSaved = createdAnime();
        Anime animeSaved = this.animeRepository.save(animeToBeSaved);

        Assertions.assertThat(animeSaved).isNotNull();
        Assertions.assertThat(animeSaved.getId()).isNotNull();
        Assertions.assertThat(animeSaved.getName()).isEqualTo(animeToBeSaved.getName());
    }

    @Test
    @DisplayName("Save update anime when Successful")
    void save_UpdatesAnime_WhenSuccessful(){
        Anime animeToBeSaved = createdAnime();
        Anime animeSaved = this.animeRepository.save(animeToBeSaved);
        animeSaved.setName("Overlord");

        Anime animeUpdated = this.animeRepository.save(animeSaved);

        Assertions.assertThat(animeUpdated).isNotNull();
        Assertions.assertThat(animeUpdated.getId()).isNotNull();
        Assertions.assertThat(animeUpdated.getName()).isEqualTo(animeSaved.getName());
    }

    @Test
    @DisplayName("Save and update anime saved when Successful")
    void delete_RemovesAnime_WhenSuccessful(){
        Anime animeToBeSaved = createdAnime();
        Anime animeSaved = this.animeRepository.save(animeToBeSaved);
        /*
            Lógica para testar um method sem retorno (delete):
             - Buscamos o anime salvo pelo id através do method findById é esse anime
               será retornando em um Optional, pois têm a possibilidade de ser null/vazio.
               Então é isso que iremos testar, se o Optional é realmente vazio como deve ser
               já que deletamos o anime do id informado.
         */
        this.animeRepository.delete(animeSaved);//deleta o anime salvo
        Optional<Anime> animeOptional = this.animeRepository.findById(animeSaved.getId());

        Assertions.assertThat(animeOptional).isEmpty();//verifico sé o anime é realmente vazio
    }

    @Test
    @DisplayName("Find by name returns list of anime when Successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful(){
        Anime animeToBeSaved = createdAnime();
        Anime animeSaved = this.animeRepository.save(animeToBeSaved);

        String name = animeSaved.getName();
        List<Anime> animes = this.animeRepository.findByName(name);

        Assertions.assertThat(animes)
                .isNotEmpty()
                .contains(animeSaved);
    }

    @Test
    @DisplayName("Find by name returns empty list when no anime is found")
    void findByName_ReturnsEmptyList_WhenAnimeIsNotFound(){
        List<Anime> animes = this.animeRepository.findByName("xaxaxa");
        Assertions.assertThat(animes).isEmpty();
    }

    @Test
    @DisplayName("Save throw ConstraintViolationException when name is empty")
    void save_ThrowConstraintViolationException_WhenNameIsEmpty(){
        Anime anime = new Anime();

/*        Aqui testamos somente se a Exception está sendo gerada, ao chamar o method
        save passando um anime com 'name' inválido.
        Assertions.assertThatThrownBy(() -> this.animeRepository.save(anime))
                .isInstanceOf(ConstraintViolationException.class);*/

        /*
        Aqui informamos que estamos esperando uma ConstraintViolationException,
        que é lançanda quando chamamos o method save passando um anime com 'name' inválido
        e que esperamos que contenha uma mensagem de erro, definida na nossa validação
        anteriomente.
         */
        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> this.animeRepository.save(anime))
                .withMessageContaining("The anime name cannot be empty");

    }

    private Anime createdAnime(){
        return Anime.builder()
                .name("Hajime no Ippo")
                .build();
    }

}//class

/*
@DataJpaTest - A anotação @DataJpaTest no Spring Boot é usada para testar a camada
               de persistência de dados, especificamente a interação com o banco de
               dados usando o Spring Data JPA. Quando você usa @DataJpaTest em um teste,
               o Spring Boot configura automaticamente um ambiente de teste que inclui
               um banco de dados em memória e o suporte para transações.

Empty (vazio): Em geral, "empty" refere-se a uma coleção, uma string ou um array que
              não contém elementos ou caracteres.


Null (nulo): "Null" é um valor especial em Java que representa a ausência de um objeto
             ou a falta de referência para um objeto. É diferente de um objeto vazio ou
              uma coleção vazia

 */