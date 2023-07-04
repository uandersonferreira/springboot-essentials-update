package br.com.uanderson.springboot2essentials.util;

import br.com.uanderson.springboot2essentials.domain.Anime;

public class AnimeCreator {
    /*
     Aqui centralizamos a criação dos tipos de animes que precisamos para realizar
     os teste.
     */
    public static Anime createAnimeToBeSaved(){
        return Anime.builder()
                .name("Hajime no Ippo")
                .build();
    }

    public static Anime createValidAnime(){
        return Anime.builder()
                .name("Hajime no Ippo")
                .id(1L)
                .build();
    }

    public static Anime createValidUpdateAnime(){
        return Anime.builder()
                .name("Hajime no Ippo 2")
                .id(1L)
                .build();
    }


}
