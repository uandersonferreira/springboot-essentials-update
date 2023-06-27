package br.com.uanderson.springboot2essentials.repository;

import br.com.uanderson.springboot2essentials.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimeRepository  extends JpaRepository<Anime, Long> {
    List<Anime> findByName(String name);


    /*
     - Por estamos utilizando o JpaRepository, temos tantos os method
       de CrudRepository, quanto de PaginationAndSortingRepository.

     List<Anime> findByName(String name) -> Utiliza um recurso do spring data chamado 'Query Methods',
     que permite a criação de consultas sql baseadas em palavras chaves, combinadas com os nomes dos
     atributos da class e operadores lógicos.

     link: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods

    Populando table Para teste de paginação:

    INSERT INTO db_anime.anime (name) VALUES ('Steins Gate');
    INSERT INTO db_anime.anime (name) VALUES ('DBZ');
    INSERT INTO db_anime.anime (name) VALUES ('Overlord');
    INSERT INTO db_anime.anime (name) VALUES ('Cowboy Bebop');
    INSERT INTO db_anime.anime (name) VALUES ('Drifters');
    INSERT INTO db_anime.anime (name) VALUES ('Hellsing');
    INSERT INTO db_anime.anime (name) VALUES ('Berserk');
    INSERT INTO db_anime.anime (name) VALUES ('Tokyo Ghoul');
    INSERT INTO db_anime.anime (name) VALUES ('Boku no hero');
    INSERT INTO db_anime.anime (name) VALUES ('Hunter × Huntern');

     */

}//class
