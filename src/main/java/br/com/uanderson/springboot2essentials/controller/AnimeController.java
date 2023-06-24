package br.com.uanderson.springboot2essentials.controller;

import br.com.uanderson.springboot2essentials.domain.Anime;
import br.com.uanderson.springboot2essentials.service.AnimeService;
import br.com.uanderson.springboot2essentials.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("animes")
@Log4j2
@RequiredArgsConstructor //Cria um construtor com todos os atributos finais de um class
//@AllArgsConstructor//Cria um construtor com todos os atributos não finais de uma class
public class AnimeController {
    private final DateUtil dateUtil;
    private final AnimeService animeService;

    @GetMapping
    public ResponseEntity<List<Anime>> list(){
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return new ResponseEntity<>(animeService.listAll(), HttpStatus.OK);
    }
    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> findAnimeById(@PathVariable Long id){
        return new ResponseEntity<>(animeService.findAnimeById(id), HttpStatus.OK);
    }


}//class

/*
@RestController -> Retorna um corpo contento somente String/Json
@Controller -> Retorna uma pagina html inteira
 */
