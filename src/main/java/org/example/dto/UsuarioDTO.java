package org.example.dto;

import org.example.domain.Status;

import java.io.Serializable;
import java.time.LocalDateTime;

public record UsuarioDTO(
        Long id,
        String cpf,
        String nome,
        String username,
        LocalDateTime dataNascimento,
        EnderecoDTO endereco,
        Status status,
        LocalDateTime dataCriacao,
        UsuarioDTO usuarioCriacao,
        LocalDateTime dataAtualizacao,
        UsuarioDTO usuarioAtualizacao,
        LocalDateTime dataRemocao,
        UsuarioDTO usuarioRemocao
) implements Serializable {
}