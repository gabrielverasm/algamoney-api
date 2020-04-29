package br.com.binariodigital.algamoney.api.resource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.binariodigital.algamoney.api.event.RecursoCriadoEvent;
import br.com.binariodigital.algamoney.api.exceptionhandler.AlgamoneyExceptionHandler.Erro;
import br.com.binariodigital.algamoney.api.model.Lancamento;
import br.com.binariodigital.algamoney.api.repository.LancamentoRepository;
import br.com.binariodigital.algamoney.api.repository.filter.LancamentoFilter;
import br.com.binariodigital.algamoney.api.service.LancamentoService;
import br.com.binariodigital.algamoney.api.service.exception.PessoaInexistenteOuInativaException;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {

	@Autowired
	private LancamentoRepository repository;

	@Autowired
	private LancamentoService service;

	@Autowired
	private ApplicationEventPublisher publisher;

	@Autowired
	private MessageSource messageSource;

	@GetMapping
	public List<Lancamento> pesquisar(LancamentoFilter filter) {
		return repository.filtrar(filter);
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<Optional<Lancamento>> buscarPeloCodigo(@PathVariable Long codigo) {
		Optional<Lancamento> lancamentoSalvo = repository.findById(codigo);

		return repository.existsById(codigo) ? ResponseEntity.ok().body(lancamentoSalvo)
				: ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {

		Lancamento lancamentoSalvo = service.salvar(lancamento);

		publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));

		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);

	}

	@ExceptionHandler({ PessoaInexistenteOuInativaException.class })
	public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex) {

		String msgUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null,
				LocaleContextHolder.getLocale());
		String msgDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(msgUsuario, msgDesenvolvedor));
		return ResponseEntity.badRequest().body(erros);

	}

}
