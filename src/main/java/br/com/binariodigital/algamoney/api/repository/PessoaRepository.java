package br.com.binariodigital.algamoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.binariodigital.algamoney.api.model.Pessoa;
import br.com.binariodigital.algamoney.api.repository.pessoa.PessoaRepositoryQuery;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>, PessoaRepositoryQuery {

}
