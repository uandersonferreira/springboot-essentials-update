package br.com.uanderson.springboot2essentials.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Anime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "The anime name cannot be empty")// pega campos nulos também
    private String name;


}
/*
Erros de 'serializer', 'BeanSerializer' , 'Serialização':
normalmente é por causa dos getters ou setters, pois o Jackson que mapeia os
objetos irá tentar criar um novo objeto utilizando eles,
mas quando não se têm ele não consegue realizar a criação de novos objetos.

--------------------------------------------------------------------
Jackson - @RequestBody do ControllerAnime
-realiza o mapeado dos atributos
- Se encontrar o JSON como o mesmo nome dos atributos declarados na classe
irá fazer automáticamnete o mapeamento, caso contrário é necessário
utilizar a anotaçõa:
 @JsonProperty("nome do atributo JSON") em cima do atributo da classe
 ex:
 @JsonProperty("name")
 private String nameAnime;
- Leitura: o atributo JSON é {name}, mas eu quero que voçê mapei para  dentro de nameAnime.
 */
