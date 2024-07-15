package br.com.alura.forumalura.controller;

import br.com.alura.forumalura.domain.topico.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@RestController
@RequestMapping("topicos")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {

    @Autowired
    private TopicoRepository repository;

    //Cadastrar tópico
    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroTopico dados,
                                    UriComponentsBuilder uriBuilder){

        var topico = new Topico(dados);
        repository.save(topico);

        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoTopico(topico));

    }

    //Exibir todos os tópicos máximo 10 elementos ordenando pelo titulo e id
    @GetMapping
    public ResponseEntity<Page<DadosListagemTopico>> listar(@PageableDefault(size = 10,
            sort = {"id", "titulo"})
                                                            Pageable paginacao){

        var page = repository.findAllByEstadoTopicoTrue(paginacao)
                .map(DadosListagemTopico::new);

        return ResponseEntity.ok(page);

    }

    //Detalhar tópicos
    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id){

        var topico = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoTopico(topico));

    }

    //Atualizar tópico
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoTopico dados,
                                    @PathVariable Long id){

        var topico = repository.getReferenceById(id);
        topico.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosAtualizacaoTopico(topico));

    }

    //Deletar tópico
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deletar(@PathVariable Long id){

        Optional<Topico> topico = repository.findById(id);

        if (topico.isPresent()){

            repository.deleteById(id);

            return ResponseEntity.ok().build();

        }

        return ResponseEntity.noContent().build();


    }

}
