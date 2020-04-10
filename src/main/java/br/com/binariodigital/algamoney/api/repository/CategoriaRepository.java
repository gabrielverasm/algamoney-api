package br.com.binariodigital.algamoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.binariodigital.algamoney.api.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
