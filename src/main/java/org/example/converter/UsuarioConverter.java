package org.example.converter;

import org.example.domain.Usuario;
import org.example.dto.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class UsuarioConverter {

    @Autowired
    private EnderecoConverter enderecoConverter;

    public UsuarioDTO toUsuarioRecord(Usuario usuario) {
        if (Objects.isNull(usuario)) {
            return null;
        }
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getCpf(),
                usuario.getNome(),
                usuario.getUsername(),
                usuario.getDataNascimento(),
                enderecoConverter.toEnderecoRecord(usuario.getEndereco()),
                usuario.getStatus(),
                usuario.getDataCriacao(),
                toActiveRecordWithoutRelationships(usuario.getUsuarioCriacao()),
                usuario.getDataAtualizacao(),
                toActiveRecordWithoutRelationships(usuario.getUsuarioAtualizacao()),
                usuario.getDataRemocao(),
                toActiveRecordWithoutRelationships(usuario.getUsuarioRemocao())
        );
    }
    public UsuarioDTO toActiveRecordWithoutRelationships(Usuario usuario) {
        if (Objects.isNull(usuario)) {
            return null;
        }
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getCpf(),
                usuario.getNome(),
                usuario.getUsername(),
                usuario.getDataNascimento(),
               null,
                usuario.getStatus(),
                null,
                null,
               null,
                null,
                null,
                null
        );
    }

    public List<UsuarioDTO> toUsuarioRecords(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(this::toUsuarioRecord)
                .collect(Collectors.toList());
    }
}
