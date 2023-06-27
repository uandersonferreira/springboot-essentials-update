package br.com.uanderson.springboot2essentials.client;

import br.com.uanderson.springboot2essentials.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        //retorna a resposta completa como uma instância de ResponseEntity
        ResponseEntity<Anime> entity = new RestTemplate()
                .getForEntity("http://localhost:8080/animes/{id}", Anime.class, 1);//Também poderiamos pegar o body direto por aqui usando .getBody()
        log.info("getForEntity: {}", entity);

        //Retornar somente o body() - O object(Anime) em si
        Anime object = new RestTemplate()
                .getForObject("http://localhost:8080/animes/{id}", Anime.class, 1);//Se tivesse mais de uma variável, podemos passar-lás separadas por vírgula
        log.info("getForObject: {}", object);

        Anime[] animes = new RestTemplate()
                .getForObject("http://localhost:8080/animes/all", Anime[].class);
        log.info("getForObject + Array: {}", Arrays.toString(animes));

        //@formatter:off
        ResponseEntity<List<Anime>> exchange = new RestTemplate()
                .exchange("http://localhost:8080/animes/all",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {});
        //@formatter:on
        log.info("exchange + ResponseEntity<List<Anime>>: {}", exchange.getBody());
        /*
        url: A URL para a qual você deseja enviar a solicitação.
        method: O tipo de método HTTP que você deseja usar, como GET, POST, PUT, DELETE, etc.
        requestEntity: Um objeto HttpEntity que representa a solicitação HTTP, contendo o corpo
                       da solicitação, cabeçalhos personalizados, etc.
        responseType: A classe Java que você deseja usar para mapear o corpo da resposta.
         */

        //UTILIZNADO O postForEntity();
        Anime kingdom = Anime.builder().name("Kingdom").build();
        ResponseEntity<Anime> kingdowmSaved = new RestTemplate().postForEntity("http://localhost:8080/animes", kingdom, Anime.class);
        log.info("postForEntity: {}", kingdowmSaved);

        //UTILIZANDO O exchange();
        Anime samuraiChamploo = Anime.builder().name("Samurai Champloo").build();
        ResponseEntity<Anime> samuraiChamplooSaved = new RestTemplate().exchange("http://localhost:8080/animes",
                HttpMethod.POST,
                new HttpEntity<>(samuraiChamploo),//HttpEntity - representa uma entidade HTTP completa, contendo um corpo ou cabeçalhos ou ambos
                Anime.class
        );//Também podemos pegar o objeto em si(Anime) se utilizamos '.getBody()'

        log.info("exchange + POST: {}", samuraiChamplooSaved, createJsonHeader());


    }//main

    private static HttpHeaders createJsonHeader(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return  httpHeaders;
        /*
         Podemos setar mais coisas ao header caso queiramos, como:
            - token
            - as credenciais, username, password
            - elementos permitidos num header em geral
         */
    }//method createJsonHeader

}//clas
/*
RestTemplate - E usado para consumo de serviços RESTful, Api externas de outras aplicações.
                 Ele fornece métodos convenientes para enviar requisições HTTP para endpoints
                 REST e receber as respostas correspondentes.

getForObject:
 - O método getForObject faz uma solicitação HTTP GET e retorna diretamente o corpo da resposta
mapeado para o tipo especificado.
- Ele converte automaticamente a resposta JSON (ou outro formato) para um objeto Java, usando a
conversão de dados do Spring.
- Se a resposta for um JSON, por exemplo,o RestTemplate usará um HttpMessageConverter adequado
para fazer a conversão para o tipo especificado.
- Se a resposta for vazia, o getForObject retornará null.

getForEntity:
- O método getForEntity faz uma solicitação HTTP GET e retorna a resposta completa como uma instância
de ResponseEntity.
- ResponseEntity contém o corpo da resposta, bem como informações adicionais, como o
código de status, cabeçalhos da resposta, etc.
- Com ResponseEntity, você tem acesso a todas as informações da resposta HTTP, permitindo um maior
controle e flexibilidade no processamento da resposta.
- Se a resposta for vazia, o getForEntity retornará uma instância de ResponseEntity com corpo vazio,
 mas ainda com as informações de cabeçalho e código de status.

 */