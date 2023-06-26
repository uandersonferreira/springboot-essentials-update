package br.com.uanderson.springboot2essentials.service;

import br.com.uanderson.springboot2essentials.domain.Anime;
import br.com.uanderson.springboot2essentials.exeption.BadRequestException;
import br.com.uanderson.springboot2essentials.mapper.AnimeMapper;
import br.com.uanderson.springboot2essentials.repository.AnimeRepository;
import br.com.uanderson.springboot2essentials.requestDto.AnimePostRequestBody;
import br.com.uanderson.springboot2essentials.requestDto.AnimePutRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {
    //-@Service representa uma Class que é reponsável pela implementação da REGRA DE NEGÓCIO da aplicação!
    private final AnimeRepository animeRepository;

    public List<Anime> listAll(){
        return animeRepository.findAll();
    }
    public List<Anime> findByName(String name){
        return animeRepository.findByName(name);
    }

    public Anime findAnimeByIdOrThrowBadRequestException(long id){
        return animeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(String.format("Anime not found with id '%s'", id)));
    }

    @Transactional //Habilita o princípio da atomicidade(rollback(): que Cancela uma transação se ocorre erros)
    public Anime save(AnimePostRequestBody animePostRequestBody) {
        Anime animeSaved = animeRepository.save(AnimeMapper.INSTANCE.toAnime(animePostRequestBody));
        return animeSaved;

                /*
        Uma transação garante que todo processo deve ser executado com êxito,
        é 'tudo ou nada' (princípio da atomicidade).

        @Transactional: Garante que se ocorre um erro durante a execução de uma
        transação a mesma será cancelada, totalmente, não persistindo no banco de dados.


        É recomenda-se sempre usar a anotação, quando temos methods que fazer alguma persistência
        no banco de dados, afim de, garantir o principio da atomicidade.

         - begin(): Inicia uma transação;
         - commit(): Finaliza uma transação;
         - rollback(): Cancela uma transação.

         OBSERVAÇÃO:
        - Exceções não checadas (unchecked exceptions): As exceções não checadas, ou seja, as subclasses
        de RuntimeException ou Error, geralmente fazem com que a transação seja marcada para
        rollback (desfazer). Isso significa que todas as operações realizadas dentro da transação são
        desfeitas e as alterações no banco de dados são revertidas.

        Exceções checadas (checked exceptions): As exceções checadas, ou seja, as subclasses de Exception
        normalmente não são consideradas pelo mecanismo de transação do Spring. Isso significa que, se uma
        exceção checada for lançada dentro de um método anotado com @Transactional, a transação não será
        marcada para rollback automaticamente.
        A exceção será propagada para o chamador do método e cabe a ele decidir como lidar com a exceção.

        No entanto, podemos configurar o comportamento de captura das exceções checadas usando a
        anotação  @Transactional com a propriedade 'rollbackFor' informando a intância da class Exception:
            - @Transactional(rollbackFor = Exception.class): Anotação para captar
            exceções checadas
         */
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
