package br.com.uanderson.springboot2essentials.service;

import br.com.uanderson.springboot2essentials.domain.Anime;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AnimeService {
    //-@Service representa uma Class que é reponsável pela implementação da REGRA DE NEGÓCIO da aplicação!

    private static List<Anime> animes;//Lista imutável, onde todas as instâncias da classe terão acesso
                                      // à mesma lista, independentemente de quantos objetos sejam criados
    static {
        //Bloco static é iniciado primeiro, por isso se cria um intancia de arrayList e atribui a animes
        animes = new ArrayList<>(//Criar uma instância de arrayList para que seja possível
                                 // manipular a lista 'animes' que é imutável, senão gera um 'UnsupportedOperationException', pois não aceita modificações
                List.of(
                        new Anime(1L,"Naruto"),
                        new Anime(2L,"Boruto"),
                        new Anime(3L,"Boku no Hero Academy")
                )
        );
    }

    public List<Anime> listAll(){
        return animes;
    }
    public Anime findAnimeById(long id){
        return animes.stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Anime not found with id '%s'", id)));
    }

    public Anime save(Anime anime) {
        long id = ThreadLocalRandom.current().nextLong(3, 1000000);
        anime.setId(id);
        animes.add(anime);
        return anime;
    }
}//class
