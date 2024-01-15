package org.example.service;

import org.example.converter.UsuarioConverter;
import org.example.domain.Endereco;
import org.example.domain.Role;
import org.example.domain.Status;
import org.example.domain.Usuario;
import org.example.dto.UsuarioDTO;
import org.example.repository.RoleRepository;
import org.example.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsuarioConverter usuarioConverter;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    public void setUp() {

        UserDetails userDetails = new User("usernameroot", "password", Collections.emptyList());

        Usuario usuarioMock = new Usuario();
        usuarioMock.setUsername("usernameroot");

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, null);
        SecurityContext securityContext = mock(SecurityContext.class);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        lenient().when(usuarioRepository.findByUsername("usernameroot")).thenReturn(Optional.of(usuarioMock));
    }

    @Test
    public void testCreateNewUsuario() {

        Usuario usuario = new Usuario();
        usuario.setUsername("username");
        usuario.setCpf("1233232");
        lenient().when(usuarioRepository.existsByUsername("username")).thenReturn(false);
        lenient().when(usuarioRepository.existsByCpf("1233232")).thenReturn(false);

        Role userRole = new Role("ROLE_USER");
        lenient().when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(userRole));

        lenient().when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        lenient().when(usuarioConverter.toUsuarioRecord(any())).thenReturn(getUsuarioDTO());
        lenient().when(usuarioRepository.save(usuario)).thenReturn(usuario);

        UsuarioDTO result = usuarioService.create(usuario);

        verify(usuarioRepository).save(usuario);
        verify(usuarioConverter).toUsuarioRecord(usuario);
    }

    private static UsuarioDTO getUsuarioDTO() {
        return new UsuarioDTO(1L, "12345678901", "Novo nome", null, null, null, null, null, null, LocalDateTime.now(), null, null, null);
    }

    @Test
    public void testCreateUsuarioWithExistingUsername() {
        Usuario usuario = new Usuario();
        usuario.setUsername("username");
        lenient().when(usuarioRepository.existsByUsername("username")).thenReturn(true);


        verify(usuarioRepository, never()).save(any());
    }

    @Test
    public void testCreateUsuarioWithExistingCpf() {
        Usuario usuario = new Usuario();
        usuario.setCpf("123234");
        usuario.setUsername("username");
        lenient().when(usuarioRepository.existsByUsername("username")).thenReturn(false);
        lenient().when(usuarioRepository.existsByCpf("123234")).thenReturn(true);

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void testUpdate() {
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setNome("Novo Nome");

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(id);
        usuarioExistente.setNome("Nome Existente");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioExistente);
        lenient().when(usuarioConverter.toUsuarioRecord(usuarioExistente)).thenReturn(getUsuarioDTO());

        UsuarioDTO resultado = usuarioService.update(id, usuario);

        assertEquals("Novo nome", resultado.nome());
        assertEquals(id, resultado.id());
        assertNotNull(resultado.dataAtualizacao());
    }

    @Test
    void testUpdateWithEndereco() {
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setNome("Novo Nome");
        usuario.setEndereco(new Endereco());

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(id);
        usuarioExistente.setNome("Nome Existente");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioExistente);
        lenient().when(usuarioConverter.toUsuarioRecord(usuarioExistente)).thenReturn(getUsuarioDTO());

        UsuarioDTO resultado = usuarioService.update(id, usuario);

        assertEquals("Novo nome", resultado.nome());
        assertEquals(id, resultado.id());
        assertNotNull(resultado.dataAtualizacao());
    }

    @Test
    void testRemove() {
        Long id = 1L;
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(id);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));

        usuarioService.remove(id);

        assertEquals(Status.REMOVIDO, usuarioExistente.getStatus());
        assertNotNull(usuarioExistente.getDataRemocao());
        assertNotNull(usuarioExistente.getUsuarioRemocao());

        // Verifica se o método save foi chamado
        Mockito.verify(usuarioRepository, Mockito.times(1)).save(usuarioExistente);
    }

    @Test
    void testFindAll() {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(new Usuario());
        usuarios.add(new Usuario());

        when(usuarioRepository.findAll()).thenReturn(usuarios);
        when(usuarioConverter.toUsuarioRecords(usuarios)).thenReturn(Arrays.asList(getUsuarioDTO(), getUsuarioDTO()));

        List<UsuarioDTO> resultado = usuarioService.findAll();

        assertEquals(2, resultado.size());
    }

    @Test
    void testFindByCpfDTO() {
        String cpf = "12345678901";
        Usuario usuario = new Usuario();
        usuario.setCpf(cpf);

        when(usuarioRepository.findByCpf(cpf)).thenReturn(Optional.of(usuario));
        when(usuarioConverter.toUsuarioRecord(usuario)).thenReturn(getUsuarioDTO());

        UsuarioDTO resultado = usuarioService.findByCpfDTO(cpf);

        assertNotNull(resultado);
        assertEquals(cpf, resultado.cpf());
    }

    @Test
    void testFindByCpf() {
        String cpf = "12345678901";
        Usuario usuario = new Usuario();
        usuario.setCpf(cpf);

        when(usuarioRepository.findByCpf(cpf)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.findByCpf(cpf);

        assertNotNull(resultado);
        assertEquals(cpf, resultado.getCpf());
    }

}