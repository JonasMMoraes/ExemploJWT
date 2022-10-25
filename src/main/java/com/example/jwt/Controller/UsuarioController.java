package com.example.jwt.Controller;

import com.example.jwt.Model.UsuarioModel;
import com.example.jwt.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder encoder;

    public UsuarioController(UsuarioRepository usuarioRepository, PasswordEncoder encoder) {
        this.usuarioRepository = usuarioRepository;
        this.encoder = encoder;
    }

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody UsuarioModel usuarioModel) {
        usuarioModel.setPassword(encoder.encode(usuarioModel.getPassword()));
        return ResponseEntity.ok(usuarioRepository.save(usuarioModel));
    }

    @GetMapping("/validarSenha")
    public ResponseEntity<?> validarSenha(@RequestParam String login, @RequestParam String password) {

        Optional<UsuarioModel> usuario = usuarioRepository.findByLogin(login);

        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }

        boolean valid = encoder.matches(password, usuario.get().getPassword());
        HttpStatus status = (valid) ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(valid);

    }

}
