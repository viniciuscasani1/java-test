package org.example.converter;

import org.example.domain.Endereco;
import org.example.dto.EnderecoDTO;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class EnderecoConverter {
    public EnderecoDTO toEnderecoRecord(Endereco endereco) {
        if (Objects.isNull(endereco)) {
            return null;
        }
        return new EnderecoDTO(
                endereco.getId(),
                endereco.getRua(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getCep()
        );
    }
}
