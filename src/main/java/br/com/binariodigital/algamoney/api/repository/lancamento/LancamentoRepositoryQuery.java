package br.com.binariodigital.algamoney.api.repository.lancamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.binariodigital.algamoney.api.model.Lancamento;
import br.com.binariodigital.algamoney.api.repository.filter.LancamentoFilter;
import br.com.binariodigital.algamoney.api.repository.projection.ResumoLancamento;

public interface LancamentoRepositoryQuery {

	public Page<Lancamento> filtrar(LancamentoFilter filter, Pageable pageable);

	public Page<ResumoLancamento> resumir(LancamentoFilter filter, Pageable pageable);

}
