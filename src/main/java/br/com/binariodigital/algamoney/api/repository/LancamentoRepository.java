package br.com.binariodigital.algamoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.binariodigital.algamoney.api.model.Lancamento;
import br.com.binariodigital.algamoney.api.repository.lancamento.LancamentoRepositoryQuery;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery {

}
