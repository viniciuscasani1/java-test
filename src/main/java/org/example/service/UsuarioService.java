package org.example.service;

import org.example.converter.UsuarioConverter;
import org.example.domain.Endereco;
import org.example.domain.Role;
import org.example.domain.Status;
import org.example.domain.Usuario;
import org.example.dto.UsuarioDTO;
import org.example.exception.AppException;
import org.example.exception.BadRequestException;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.RoleRepository;
import org.example.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioConverter usuarioConverter;

    public UsuarioDTO create(Usuario usuario) {
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new BadRequestException("Nome de usuário já utilizado");
        }

        if (usuarioRepository.existsByCpf(usuario.getCpf())) {
            throw new BadRequestException("Já existe um usuário com este CPF");
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new AppException("User Role not set."));

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setRoles(Collections.singleton(userRole));

        usuario.setStatus(Status.ATIVO);
        usuario.setDataCriacao(LocalDateTime.now());
        usuario.setUsuarioCriacao(getAuthenticatedUser());
        return usuarioConverter.toUsuarioRecord(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioDTO update(Long id, Usuario usuario) {

        Optional<Usuario> usuarioExistenteOpt = usuarioRepository.findById(id);

        if (usuarioExistenteOpt.isPresent()) {
            Usuario usuarioExistente = usuarioExistenteOpt.get();
            usuarioExistente.setNome(usuario.getNome());
            usuarioExistente.setCpf(usuario.getCpf());
            usuarioExistente.setDataNascimento(usuario.getDataNascimento());

            if (Objects.nonNull(usuario.getEndereco())) {

                Endereco endereco = getEndereco(usuario, usuarioExistente);

                usuarioExistente.setEndereco(endereco);
            }

            usuarioExistente.setDataAtualizacao(LocalDateTime.now());
            usuarioExistente.setUsuarioAtualizacao(getAuthenticatedUser());
            return usuarioConverter.toUsuarioRecord(usuarioRepository.save(usuarioExistente));
        }

        throw new ResourceNotFoundException("User ", "id", id);
    }

    private static Endereco getEndereco(Usuario usuario, Usuario usuarioExistente) {
        Endereco endereco = usuarioExistente.getEndereco();
        if (Objects.isNull(endereco)) {
            endereco = new Endereco();
        }
        Endereco newEndereco = usuario.getEndereco();
        endereco.setCep(newEndereco.getCep());
        endereco.setEstado(newEndereco.getEstado());
        endereco.setRua(newEndereco.getRua());
        endereco.setCidade(newEndereco.getCidade());
        endereco.setNumero(newEndereco.getNumero());
        endereco.setCep(newEndereco.getCep());
        endereco.setBairro(newEndereco.getBairro());
        return endereco;
    }

    public void remove(Long id) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);

        usuarioOpt.ifPresent(usuario -> {
            usuario.setStatus(Status.REMOVIDO);
            usuario.setDataRemocao(LocalDateTime.now());
            usuario.setUsuarioRemocao(getAuthenticatedUser());
            usuarioRepository.save(usuario);
        });
    }

    public List<UsuarioDTO> findAll() {
        return usuarioConverter.toUsuarioRecords(usuarioRepository.findAll());
    }

    public UsuarioDTO findByCpfDTO(String cpf) {
        return usuarioConverter.toUsuarioRecord(usuarioRepository.findByCpf(cpf).orElse(null));
    }

    public Usuario findByCpf(String cpf) {
        return usuarioRepository.findByCpf(cpf).orElse(null);
    }

    private Usuario getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            Optional<Usuario> user = usuarioRepository.findByUsername(userDetails.getUsername());
            if (user.isPresent()) {
                return user.get();
            }
        }

        throw new AppException("Nenhum usuário autenticado encontrado");
    }
}
