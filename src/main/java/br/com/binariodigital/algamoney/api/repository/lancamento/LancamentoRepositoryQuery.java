package br.com.binariodigital.algamoney.api.repository.lancamento;

import java.util.List;

import br.com.binariodigital.algamoney.api.model.Lancamento;
import br.com.binariodigital.algamoney.api.repository.filter.LancamentoFilter;

public interface LancamentoRepositoryQuery {

	public List<Lancamento> filtrar(LancamentoFilter filter);

}
