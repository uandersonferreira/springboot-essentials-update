package br.com.uanderson.springboot2essentials.repository;

import br.com.uanderson.springboot2essentials.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimeRepository  extends JpaRepository<Anime, Long> {
    List<Anime> findByName(String name);


    /*
     Utiliza um recurso do spring data chamado 'Query Methods', que permite a criação
     de consultas sql baseadas em palavras chaves, combinadas com os nomes dos
     atributos da class e operadores lógicos.

     link: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods
     */

}//class
