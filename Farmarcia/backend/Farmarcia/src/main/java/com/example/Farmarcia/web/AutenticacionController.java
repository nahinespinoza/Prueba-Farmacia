package com.example.Farmarcia.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Farmarcia.dto.LoginRequestDto;
import com.example.Farmarcia.dto.LoginResponseDto;
import com.example.Farmarcia.dto.RegisterRequestDto;
import com.example.Farmarcia.dto.UsarioResponseDto;
import com.example.Farmarcia.entity.UsuarioEntity;
import com.example.Farmarcia.service.AutenticacionService;
import com.example.Farmarcia.security.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Operaciones relacionadas con la autenticación de usuarios")
public class AutenticacionController {

    private final AutenticacionService autenticacionService;
    private final AuthService authService;

    public AutenticacionController(AutenticacionService autenticacionService, AuthService authService) {
        this.autenticacionService = autenticacionService;
        this.authService = authService;
    }

    @Operation(summary = "Crear un nuevo usuario", description = "Crea un usuario y lo retorna")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping("/register")
    public ResponseEntity<UsuarioEntity> crearUsuario(@RequestBody RegisterRequestDto user) {
        UsuarioEntity nuevoUsuario = autenticacionService.crearUsuario(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario y devuelve un token JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> iniciarSesion(@RequestBody LoginRequestDto loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @Operation(summary = "Obtener información del usuario logeado", description = "Devuelve la información del usuario logeado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Información del usuario obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    public ResponseEntity<UsarioResponseDto> obtenerUsuarioLogeado() {
        return ResponseEntity.ok(autenticacionService.obtenerUsuarioLogeado());
    }

}
