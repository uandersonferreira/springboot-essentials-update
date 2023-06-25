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
    public ResponseEntity<Anime> findById(@PathVariable Long id){
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

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        animeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping()
    public ResponseEntity<Void> replace(@RequestBody Anime anime){
        animeService.replace(anime);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        /*
        Quando realizamos um PUT(update/replace) estamos substituindo o ESTADO inteiro do objeto, de forma idempotente.

        O ESTADO são os valores atribuídos aos atributos de um objeto.
        Diferentemente do COMPORTAMENTO que são os métodos da classe,que como o próprio
        nome diz, é o comportamento do objeto.

         */
    }





}//class

/*
@RestController -> Retorna um corpo contento somente String/Json
@Controller -> Retorna uma pagina html inteira

------------------------------------------------------
MÉTODOS IDEMPOTENTES: Significa dizer que, não importa
quantas vezes forem executados(request), o seu resultado final(response)
deve ser o mesmo.

Um método idempotente é um método em que uma requisição idêntica
pode ser feita uma ou mais vezes, em sequência, com o mesmo efeito,
enquanto deixa o servidor no mesmo estado.

- O "Hypertext Transfer Protocol (HTTP) Method Registry" foi
preenchido com os registros abaixo:

   +---------+------+------------+---------------+
   | Method  | Safe | Idempotent | Reference     |
   +---------+------+------------+---------------+
   | CONNECT | no   | no         | Section 4.3.6 |
   | DELETE  | no   | yes        | Section 4.3.5 |
   | GET     | yes  | yes        | Section 4.3.1 |
   | HEAD    | yes  | yes        | Section 4.3.2 |
   | OPTIONS | yes  | yes        | Section 4.3.7 |
   | POST    | no   | no         | Section 4.3.3 |
   | PUT     | no   | yes        | Section 4.3.4 |
   | TRACE   | yes  | yes        | Section 4.3.8 |
   +---------+------+------------+---------------+



 */
