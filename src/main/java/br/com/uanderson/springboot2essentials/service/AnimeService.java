package br.com.uanderson.springboot2essentials.service;

import br.com.uanderson.springboot2essentials.domain.Anime;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AnimeService {
    //-@Service representa uma Class que é reponsável pela implementação da REGRA DE NEGÓCIO da aplicação!

    public List<Anime> listAll(){
        return List.of(
                new Anime(1L,"Naruto"),
                new Anime(2L,"Boruto"),
                new Anime(3L,"Boku no Hero Academy")
        );
    }
}
