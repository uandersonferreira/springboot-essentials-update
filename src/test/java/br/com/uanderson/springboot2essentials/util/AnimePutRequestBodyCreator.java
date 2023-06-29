package br.com.uanderson.springboot2essentials.util;

import br.com.uanderson.springboot2essentials.requestDto.AnimePostRequestBody;
import br.com.uanderson.springboot2essentials.requestDto.AnimePutRequestBody;

public class AnimePutRequestBodyCreator {
    public static AnimePutRequestBody createAnimePutRequestBody(){
        return AnimePutRequestBody.builder()
                .id(AnimeCreator.createValidUpdateAnime().getId())
                .name(AnimeCreator.createValidUpdateAnime().getName())
                .build();
    }
}
