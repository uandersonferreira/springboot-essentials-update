package br.com.uanderson.springboot2essentials.service;

import br.com.uanderson.springboot2essentials.domain.Anime;
import br.com.uanderson.springboot2essentials.mapper.AnimeMapper;
import br.com.uanderson.springboot2essentials.repository.AnimeRepository;
import br.com.uanderson.springboot2essentials.requestDto.AnimePostRequestBody;
import br.com.uanderson.springboot2essentials.requestDto.AnimePutRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class AnimeService {
    //-@Service representa uma Class que é reponsável pela implementação da REGRA DE NEGÓCIO da aplicação!
    private final AnimeRepository animeRepository;

    public List<Anime> listAll(){
        return animeRepository.findAll();
    }
    public Anime findAnimeByIdOrThrowBadRequestException(long id){
        return animeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Anime not found with id '%s'", id)));
    }

    public Anime save(AnimePostRequestBody animePostRequestBody) {
        Anime anime = AnimeMapper.INSTANCE.toAnime(animePostRequestBody);
        return animeRepository.save(anime);
    }

    public void delete(long id) {
        animeRepository.delete(findAnimeByIdOrThrowBadRequestException(id));
    }

    public void replace(AnimePutRequestBody animePutRequestBody) {
        Anime savedAnime = findAnimeByIdOrThrowBadRequestException(animePutRequestBody.id());//Irá lançar uma exception cao tentem atualizar um anime que não exista no banco
        Anime anime = AnimeMapper.INSTANCE.toAnime(animePutRequestBody);
        anime.setId(savedAnime.getId());//Só uma forma de garantir que estamos pegando o id de um Anime já existente no banco
        animeRepository.save(anime);
    }
}//class
