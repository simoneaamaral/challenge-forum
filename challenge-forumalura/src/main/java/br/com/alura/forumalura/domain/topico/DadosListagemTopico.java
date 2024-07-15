package br.com.alura.forumalura.domain.topico;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record DadosListagemTopico(
        Long id,
        String titulo,
        String mensagem,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime data

) {

    public DadosListagemTopico(Topico topico){
        this(topico.getId(), topico.getTitulo(), topico.getMensagem(), topico.getData());
    }

}
