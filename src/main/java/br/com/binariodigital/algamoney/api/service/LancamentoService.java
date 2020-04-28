package br.com.binariodigital.algamoney.api.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.binariodigital.algamoney.api.model.Lancamento;
import br.com.binariodigital.algamoney.api.model.Pessoa;
import br.com.binariodigital.algamoney.api.repository.LancamentoRepository;
import br.com.binariodigital.algamoney.api.repository.PessoaRepository;
import br.com.binariodigital.algamoney.api.service.exception.PessoaInexistenteOuInativaException;

@Service
public class LancamentoService {

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private LancamentoRepository repository;

	public Lancamento salvar(@Valid Lancamento lancamento) {

		Long codigo = lancamento.getPessoa().getCodigo();

		Pessoa pessoa = pessoaRepository.getOne(codigo);
		if (pessoa == null || pessoa.isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}

		return repository.save(lancamento);

	}

}
