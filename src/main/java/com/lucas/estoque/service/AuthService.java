package com.lucas.estoque.service;

import com.lucas.estoque.dto.LoginRequestDTO;
import com.lucas.estoque.dto.RegisterRequestDTO;
import com.lucas.estoque.dto.TokenResponseDTO;
import com.lucas.estoque.entity.Usuario;
import com.lucas.estoque.exception.DuplicateResourceException;
import com.lucas.estoque.exception.ResourceNotFoundException;
import com.lucas.estoque.repository.UsuarioRepository;
import com.lucas.estoque.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public TokenResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.senha())
        );
        // sem try/catch aqui — deixa a exceção subir para um @ExceptionHandler global
        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return new TokenResponseDTO(jwtService.gerarToken(usuario));
    }

    public TokenResponseDTO register(RegisterRequestDTO dto) {
        if (usuarioRepository.findByEmail(dto.email()).isPresent()) {
            throw new DuplicateResourceException(
                    "Já existe um usuário cadastrado com o email: " + dto.email());
        }
        Usuario usuario = Usuario.builder()
                .nome(dto.nome())
                .email(dto.email())
                .senha(passwordEncoder.encode(dto.senha()))
                .role("USER")
                .build();
        Usuario salvo = usuarioRepository.save(usuario);

        return new TokenResponseDTO(jwtService.gerarToken(salvo));
    }
}


