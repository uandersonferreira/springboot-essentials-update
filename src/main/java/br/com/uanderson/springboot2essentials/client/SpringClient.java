package br.com.uanderson.springboot2essentials.client;

import br.com.uanderson.springboot2essentials.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        //retorna a resposta completa como uma instância de ResponseEntity
        ResponseEntity<Anime> entity = new RestTemplate()
                .getForEntity("http://localhost:8080/animes/{id}", Anime.class, 8);//Também poderiamos pegar o body direto por aqui usando .getBody()
        log.info(entity);

        //Retornar somente o body() - O object(Anime) em si
        Anime object = new RestTemplate()
                .getForObject("http://localhost:8080/animes/{id}", Anime.class, 4,1);//Se tivesse mais de uma variável, podemos passar-lás separadas por vírgula
        log.info(object);

    }
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