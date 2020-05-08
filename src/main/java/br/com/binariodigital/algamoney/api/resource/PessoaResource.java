package br.com.binariodigital.algamoney.api.resource;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.binariodigital.algamoney.api.event.RecursoCriadoEvent;
import br.com.binariodigital.algamoney.api.model.Pessoa;
import br.com.binariodigital.algamoney.api.repository.PessoaRepository;
import br.com.binariodigital.algamoney.api.service.PessoaService;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {

	@Autowired
	private PessoaRepository repository;

	@Autowired
	private PessoaService service;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read')")
	public List<Pessoa> listar() {
		return repository.findAll();
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
	public ResponseEntity<Pessoa> criar(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {

		Pessoa pessoaSalva = repository.save(pessoa);

		publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalva.getCodigo()));

		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);

	}

	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read')")
	public ResponseEntity<Optional<Pessoa>> buscarPeloCodigo(@PathVariable Long codigo) {

		Optional<Pessoa> pessoaBuscada = repository.findById(codigo);

		return repository.existsById(codigo) ? ResponseEntity.ok().body(pessoaBuscada)
				: ResponseEntity.notFound().build();

	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_REMOVER_PESSOA') and #oauth2.hasScope('write')")
	public void remover(@PathVariable Long codigo) {
		repository.deleteById(codigo);
	}

	@PutMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
	public ResponseEntity<Pessoa> atualizar(@PathVariable Long codigo, @Valid @RequestBody Pessoa pessoa) {

		Pessoa pessoaSalva = service.atualizar(codigo, pessoa);

		return ResponseEntity.status(HttpStatus.OK).body(pessoaSalva);

	}

	@PutMapping("/{codigo}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarPropriedadeAtivo(@PathVariable Long codigo, @RequestBody Boolean ativo) {

		service.atualizarPropriedadeAtivo(codigo, ativo);

	}

}
