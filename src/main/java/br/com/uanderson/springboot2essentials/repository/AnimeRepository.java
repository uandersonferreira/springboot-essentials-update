package br.com.uanderson.springboot2essentials.repository;

import br.com.uanderson.springboot2essentials.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeRepository  extends JpaRepository<Anime, Long> {

}//class
