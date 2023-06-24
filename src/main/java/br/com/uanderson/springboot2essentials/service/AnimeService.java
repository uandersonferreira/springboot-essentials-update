package br.com.uanderson.springboot2essentials.service;

import br.com.uanderson.springboot2essentials.domain.Anime;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Service
public class AnimeService {
    //-@Service representa uma Class que é reponsável pela implementação da REGRA DE NEGÓCIO da aplicação!
    private List<Anime> animes = List.of(
            new Anime(1L,"Naruto"),
            new Anime(2L,"Boruto"),
            new Anime(3L,"Boku no Hero Academy")
    );

    public List<Anime> listAll(){
        return animes;
    }
    public Anime findAnimeById(Long id){
        return animes.stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Anime not found with id '%s'", id)));
    }

}//class
