package br.com.uanderson.springboot2essentials.controller;

import br.com.uanderson.springboot2essentials.domain.Anime;
import br.com.uanderson.springboot2essentials.service.AnimeService;
import br.com.uanderson.springboot2essentials.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping(path = "/{id}")//Quando temos mais de 1 method GET é necessaŕio diferencialos por um 'path' caminho que apronta pra um endpoint /animes/{id}
    public ResponseEntity<Anime> findAnimeById(@PathVariable Long id){
        return new ResponseEntity<>(animeService.findAnimeById(id), HttpStatus.OK);
    }

    @PostMapping
    //@ResponseStatus(HttpStatus.CREATED) //outra forma de retornar o status
    public ResponseEntity<Anime> save(@RequestBody Anime anime){
       /*
       @RequestBody Anime anime -> Aqui o Jackson entra em cena realizando o mapeamento
       do objeto recebido no corpo(body) para um "Anime", para isso o nome dos atributos/propriedades
       devem ser as mesma, caso contrário é necessário informar ao Jackson o nome do atributo que deve ser
       mapeado, através da anotação:  @JsonProperty("nome do atributo JSON vindo do corpo") em cima do atributo da classe "Anime"
        */
        return new ResponseEntity<>(animeService.save(anime), HttpStatus.CREATED);

    }



}//class

/*
@RestController -> Retorna um corpo contento somente String/Json
@Controller -> Retorna uma pagina html inteira
 */
