package br.com.uanderson.springboot2essentials.repository;

import br.com.uanderson.springboot2essentials.domain.Anime;

import java.util.List;

public interface AnimeRepository {
    List<Anime> listAll();
}
