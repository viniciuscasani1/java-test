package org.example.service;

import org.example.domain.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioService userService;

    @Override
    public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
        Usuario usuario = userService.findByCpf(cpf);

        if (usuario.getCpf().equals(cpf)) {
            return new User(cpf, usuario.getPassword(),
                    new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with cpf: " + cpf);
        }
    }
}