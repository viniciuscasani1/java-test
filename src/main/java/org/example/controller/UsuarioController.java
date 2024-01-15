package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.domain.Usuario;
import org.example.dto.UsuarioDTO;
import org.example.exception.BadRequestException;
import org.example.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Api(tags = "Usuários")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    @ApiOperation(value = "Cria usuário")
    public ResponseEntity create(@RequestBody Usuario usuario) {
        try {
            UsuarioDTO usuarioDTO = usuarioService.create(usuario);
            return new ResponseEntity<>(usuarioDTO, HttpStatus.CREATED);
        } catch (BadRequestException badRequestException) {
            return new ResponseEntity<>(badRequestException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/{id}")
    @ApiOperation(value = "Atualiza usuário")
    public ResponseEntity<UsuarioDTO> update(@PathVariable Long id, @RequestBody Usuario usuario) {
        UsuarioDTO usuarioDTO = usuarioService.update(id, usuario);
        return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Remove usuário pelo ID")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        usuarioService.remove(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ApiOperation(value = "Busca lista de usuários")
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> findAll() {
        List<UsuarioDTO> usuarios = usuarioService.findAll();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{cpf}")
    @ApiOperation(value = "Busca usuário pelo CPF")
    public ResponseEntity<UsuarioDTO> findByCpf(@PathVariable String cpf) {
        UsuarioDTO usuario = usuarioService.findByCpfDTO(cpf);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }
}
