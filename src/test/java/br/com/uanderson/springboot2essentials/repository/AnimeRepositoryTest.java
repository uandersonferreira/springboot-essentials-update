package br.com.uanderson.springboot2essentials.repository;

import br.com.uanderson.springboot2essentials.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("Tests for Anime Repository")//é usada no JUnit para fornecer um nome descritivo aos testes ou às classes de teste.
class AnimeRepositoryTest {
    @Autowired
    private  AnimeRepository animeRepository;

    //Conversão de criação de nomes de Test (Recomendando pelo Devdojo)
    //NOME DO METHOD + O QUE PRECISA FAZER ? + QUANDO ISSO DEVE ACONTECER ? || separando por underscore
    //EX: save_PersistAnime_WhenSuccessful
    @Test
    @DisplayName("Save creates anime when Successful")
    void save_PersistAnime_WhenSuccessful(){
        Anime animeToBeSaved = createdAnime();
        Anime animeSaved = this.animeRepository.save(animeToBeSaved);

        Assertions.assertThat(animeSaved).isNotNull();
        Assertions.assertThat(animeSaved.getId()).isNotNull();
        Assertions.assertThat(animeSaved.getName()).isEqualTo(animeToBeSaved.getName());

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

 */