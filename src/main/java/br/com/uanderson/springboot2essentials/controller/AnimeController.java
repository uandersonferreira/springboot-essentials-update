package br.com.uanderson.springboot2essentials.controller;

import br.com.uanderson.springboot2essentials.domain.Anime;
import br.com.uanderson.springboot2essentials.requestDto.AnimePostRequestBody;
import br.com.uanderson.springboot2essentials.requestDto.AnimePutRequestBody;
import br.com.uanderson.springboot2essentials.service.AnimeService;
import br.com.uanderson.springboot2essentials.util.DateUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<Page<Anime>> list(Pageable pageable){
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return new ResponseEntity<>(animeService.listAll(pageable), HttpStatus.OK);
        /*
        Pageable é uma interface fornecida pelo Spring Framework que permite a paginação
        de resultados em consultas de banco de dados. Ela encapsula informações sobre a
        página solicitada, como o número da página, o tamanho da página e as opções de ordenação.
         */
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<Anime>> listAllNoPageable(){
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return new ResponseEntity<>(animeService.listAllNoPageable(), HttpStatus.OK);
        /*
        Pageable é uma interface fornecida pelo Spring Framework que permite a paginação
        de resultados em consultas de banco de dados. Ela encapsula informações sobre a
        página solicitada, como o número da página, o tamanho da página e as opções de ordenação.
         */
    }
    @GetMapping(path = "/{id}")//Quando temos mais de 1 method GET é necessaŕio diferencialos por um 'path' caminho que apronta pra um endpoint /animes/{id}
    public ResponseEntity<Anime> findById(@PathVariable Long id){
        return new ResponseEntity<>(animeService.findAnimeByIdOrThrowBadRequestException(id), HttpStatus.OK);
    }

    @GetMapping(path = "/by-id/{id}")
    //@PreAuthorize("hasRole('ADMIN')")//verifica se o usuário atual logado possui a permissão de "ADMIN"
    //(E mais recomendado utilizar um padrão de url's é aplicar a proteção com um antMatcher)
    public ResponseEntity<Anime> findByIdAuthenticationPrincipal(@PathVariable Long id,
                                                                 @AuthenticationPrincipal UserDetails userDetails){
        log.info("Name user logado: {}", userDetails.getUsername());
        return new ResponseEntity<>(animeService.findAnimeByIdOrThrowBadRequestException(id), HttpStatus.OK);
    }

    @PostMapping()
    //@PreAuthorize("hasRole('ADMIN')")//verifica se o usuário atual logado possui a permissão de "ADMIN"
    //(E mais recomendado utilizar um padrão de url's é aplicar a proteção com um antMatcher)
    //@ResponseStatus(HttpStatus.CREATED) //outra forma de retornar o status
    public ResponseEntity<Anime> save(@RequestBody @Valid AnimePostRequestBody animePostRequestBody){
       /*
       @RequestBody Anime anime -> Aqui o Jackson entra em cena realizando o mapeamento
       do objeto recebido no corpo(body) para um "Anime", para isso o nome dos atributos/propriedades
       devem ser as mesma, caso contrário é necessário informar ao Jackson o nome do atributo que deve ser
       mapeado, através da anotação:  @JsonProperty("nome do atributo JSON vindo do corpo") em cima do atributo da classe "Anime"

        @Valid - È o que garante que as validações dos campos feitas com spring validation,
        estão sendo cumpridas.
        */
        return new ResponseEntity<>(animeService.save(animePostRequestBody), HttpStatus.CREATED);

    }

    @DeleteMapping(path = "/admin/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        animeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping()
    public ResponseEntity<Void> replace(@RequestBody AnimePutRequestBody animePutRequestBody){
        animeService.replace(animePutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        /*
        Quando realizamos um PUT(update/replace) estamos substituindo o ESTADO inteiro do objeto, de forma idempotente.

        O ESTADO são os valores atribuídos aos atributos de um objeto.
        Diferentemente do COMPORTAMENTO que são os métodos da classe,que como o próprio
        nome diz, é o comportamento do objeto.

         */
    }

    @GetMapping(path = "/find")
    public ResponseEntity<List<Anime>> findByName(@RequestParam String name){
        return ResponseEntity.ok(animeService.findByName(name));

        /*
        Quando temos mais de um method utilizando um method http, como as seguir:
        - @GetMapping(path = "/{name}")
        - @GetMapping(path = "/{id}")
        Gera ambiguidade, pois o spring não consegue diferenciar, já que estamos dizendo que
        no mesmo endpoint é aceito dois tipos de dados.

        Neste caso, o recomendavél é criar um novo endpoint que aceite o segundo tipo de dado.

        E umas dessas opções é utilizando o @RequestParam (parametros passados na url), que
        por padrão são obrigatório(s) passar, caso opte pela sua utilização.
        ex:
        - http://localhost:8080/animes/find?name=Naruto Shippuden

       DIREFERENÇA ENTRE  @RequestParam e @PathVariable
        @RequestParam - Parametros passados na url -> animes/find?name=naruto&idade=20&cargo=hokage
            - A anotação @RequestParam é usada para mapear parâmetros de consulta (query parameters) em uma URL.
            - É possível definir valores padrão para os parâmetros usando o atributo
             defaultValue da anotação @RequestParam (@RequestParam(defaultValue = "") String name)

        @PathVariable - Parametros/Valores que fazem parte da url -> animes/1 ( animes/{id} )
            - A anotação @PathVariable é usada para mapear partes variáveis de uma URL.

         */
    }





}//class

/*
@RestController -> Retorna um corpo contento somente String/Json
@Controller -> Retorna uma pagina html inteira
--------------------------------------------------
Em resumo, a anotação @PreAuthorize é usada para verificar a autorização
antes da execução de um método, enquanto a anotação
@PostAuthorize é usada para verificar a autorização após a execução de um método
ex:
    @GetMapping(path = "by-id/{id}")
    @PreAuthorize("hasRole('ADMIN')")//verifica se o usuário atual logado possui a permissão de "ADMIN"
    public ResponseEntity<Anime> findByIdAuthenticationPrincipal(@PathVariable Long id,
                                                                 @AuthenticationPrincipal UserDetails userDetails){
        log.info("Name user logado: {}", userDetails.getUsername());
        return new ResponseEntity<>(animeService.findAnimeByIdOrThrowBadRequestException(id), HttpStatus.OK);
    }
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
