package com.guardias.backend.security.controller;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.person.PersonBasicPanelDto;
import com.guardias.backend.entity.Person;
import com.guardias.backend.security.dto.JwtDto;
import com.guardias.backend.security.dto.LoginUsuario;
import com.guardias.backend.security.dto.NuevoUsuario;
import com.guardias.backend.security.entity.Rol;
import com.guardias.backend.security.entity.Usuario;
import com.guardias.backend.security.enums.RolNombre;
import com.guardias.backend.security.jwt.JwtProvider;
import com.guardias.backend.security.service.RolService;
import com.guardias.backend.security.service.UsuarioService;
import com.guardias.backend.service.PersonService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    RolService rolService;
    @Autowired
    PersonService personService;
    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/create")
    public ResponseEntity<?> nuevo(@Valid @RequestBody NuevoUsuario nuevoUsuario, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("campos mal puestos"), HttpStatus.BAD_REQUEST);

        if (usuarioService.existsByNombreUsuario(nuevoUsuario.getNombreUsuario()))
            return new ResponseEntity(new Mensaje("el nombre de usuario ya existe"), HttpStatus.BAD_REQUEST);

        /*
         * // Verificar si ya existe un usuario activo para la persona asociada
         * if (nuevoUsuario.getIdPerson() != null
         * && usuarioService.existeUsuarioActivoParaPersona(nuevoUsuario.getIdPerson()))
         * {
         * return new ResponseEntity<>(new
         * Mensaje("La persona ya tiene un usuario activo"),
         * HttpStatus.BAD_REQUEST);
         * }
         * 
         * // Verificar si la persona tiene un legajo activo
         * if (nuevoUsuario.getIdPerson() != null &&
         * !usuarioService.puedeCrearUsuario(nuevoUsuario.getIdPerson())) {
         * return new ResponseEntity<>(new
         * Mensaje("La persona no tiene un legajo activo"), HttpStatus.BAD_REQUEST);
         * }
         */

        Usuario usuario = new Usuario();

        usuario.setNombreUsuario(nuevoUsuario.getNombreUsuario());
        usuario.setPassword(passwordEncoder.encode(nuevoUsuario.getPassword()));
        Set<Rol> roles = new HashSet<>();
        roles.add(rolService.getByRolNombre(RolNombre.ROLE_USER).get());// por defecto todos van a ser USER

        // Validar y agregar roles enviados
        for (String rolNombre : nuevoUsuario.getRoles()) {
            try {
                RolNombre rolEnum = RolNombre.valueOf(rolNombre); // Validar si el rol existe en el enum
                roles.add(rolService.getByRolNombre(rolEnum).get());
            } catch (IllegalArgumentException e) {
                return new ResponseEntity(new Mensaje("Rol no válido: " + rolNombre), HttpStatus.BAD_REQUEST);
            }
        }
        usuario.setRoles(roles);

        // Asociar la entidad Person
        if (nuevoUsuario.getIdPerson() != null) {
            if (personService.activoById(nuevoUsuario.getIdPerson())) {

                Person person = personService.findById(nuevoUsuario.getIdPerson());
                if (person == null) {
                    return new ResponseEntity<>(new Mensaje("Persona no encontrada"), HttpStatus.BAD_REQUEST);
                }
                usuario.setPerson(person);
            }
        } else {
            return new ResponseEntity<>(new Mensaje("Es obligatorio asociar un usuario a una persona"),
                    HttpStatus.BAD_REQUEST);
        }

        usuario.setActivo(true);
        usuarioService.save(usuario);
        return new ResponseEntity(new Mensaje("Nuevo usuario guardado"), HttpStatus.CREATED);
    }

    @GetMapping("/usuarioActivo/{idPerson}")
    public ResponseEntity<Boolean> verificarUsuarioActivo(@PathVariable Long idPerson) {
        if (idPerson == null) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }

        boolean existeUsuarioActivo = usuarioService.existeUsuarioActivoParaPersona(idPerson);
        return new ResponseEntity<>(existeUsuarioActivo, HttpStatus.OK);
    }

    @GetMapping("/legajoActivo/{idPerson}")
    public ResponseEntity<Boolean> verificarLegajoActivo(@PathVariable Long idPerson) {
        if (idPerson == null) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }

        boolean tieneLegajoActivo = usuarioService.puedeCrearUsuario(idPerson);
        return new ResponseEntity<>(tieneLegajoActivo, HttpStatus.OK);
    }

    @GetMapping("/checkUsername/{nombreUsuario}")
    public ResponseEntity<Boolean> checkUsername(@PathVariable String nombreUsuario) {
        boolean exists = usuarioService.existsByNombreUsuario(nombreUsuario);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody NuevoUsuario nuevoUsuario,
            BindingResult bindingResult) {

        // Validar errores en los campos proporcionados
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(
                    new Mensaje("Campos mal puestos o email inválido"),
                    HttpStatus.BAD_REQUEST);
        }

        // Verificar si el usuario existe
        Usuario usuarioExistente = usuarioService.findById(id).get();
        if (usuarioExistente == null) {
            return new ResponseEntity<>(
                    new Mensaje("Usuario no encontrado"),
                    HttpStatus.NOT_FOUND);
        }

        // Validar si el nombre de usuario ya existe y pertenece a otro usuario
        if (!usuarioExistente.getNombreUsuario().equals(nuevoUsuario.getNombreUsuario())
                && usuarioService.existsByNombreUsuario(nuevoUsuario.getNombreUsuario())) {
            return new ResponseEntity<>(
                    new Mensaje("El nombre de usuario ya existe"),
                    HttpStatus.BAD_REQUEST);
        }

        // Actualizar el nombre de usuario
        usuarioExistente.setNombreUsuario(nuevoUsuario.getNombreUsuario());

        // Actualizar la contraseña si se proporciona
        if (nuevoUsuario.getPassword() != null && !nuevoUsuario.getPassword().isEmpty()) {
            usuarioExistente.setPassword(passwordEncoder.encode(nuevoUsuario.getPassword()));
        }

        // Actualizar roles
        Set<Rol> roles = new HashSet<>();
        for (String rolNombre : nuevoUsuario.getRoles()) {
            try {
                RolNombre rolEnum = RolNombre.valueOf(rolNombre);
                roles.add(rolService.getByRolNombre(rolEnum).get());
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(
                        new Mensaje("Rol no válido: " + rolNombre),
                        HttpStatus.BAD_REQUEST);
            }
        }
        usuarioExistente.setRoles(roles);

        // Actualizar asociación con Person
        if (nuevoUsuario.getIdPerson() != null) {
            if (personService.activoById(nuevoUsuario.getIdPerson())) {
                Person person = personService.findById(nuevoUsuario.getIdPerson());
                if (person == null) {
                    return new ResponseEntity<>(
                            new Mensaje("Persona no encontrada"),
                            HttpStatus.BAD_REQUEST);
                }
                usuarioExistente.setPerson(person);
            } else {
                return new ResponseEntity<>(
                        new Mensaje("La persona asociada no está activa"),
                        HttpStatus.BAD_REQUEST);
            }
        }

        // Guardar los cambios
        usuarioService.save(usuarioExistente);

        return new ResponseEntity<>(
                new Mensaje("Usuario actualizado correctamente"),
                HttpStatus.OK);
    }

    // devuelve un token
    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("campos mal puestos"), HttpStatus.BAD_REQUEST);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(), loginUsuario.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());
        return new ResponseEntity(jwtDto, HttpStatus.OK);

    }

    /* @PreAuthorize("hasRole('ADMIN')") */
    @GetMapping("/list")
    public ResponseEntity<List<Usuario>> list() {
        List<Usuario> list = usuarioService.findAll();
        return new ResponseEntity<List<Usuario>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{nombreUsuario}")
    public ResponseEntity<Usuario> getByNombreUsuario(@PathVariable("nombreUsuario") String nombreUsuario) {
        Optional<Usuario> usuario = usuarioService.findByNombreUsuario(nombreUsuario);
        if (usuario.isPresent()) {
            return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /*
     * La clase "Principal" es parte del paquete java.security, que proporciona una
     * interfaz que representa la identidad de un usuario en un contexto de
     * seguridad, el objeto Principal generalmente contiene el nombre de usuario del
     * usuario autenticado.
     */
    @GetMapping("/detailPersonBasicPanel")
    public ResponseEntity<PersonBasicPanelDto> obtenerPerfil(Principal principal) {
        // Obtiene el nombre de usuario del usuario autenticado
        String username = principal.getName();

        Usuario usuario = usuarioService.findByNombreUsuario(username).get();

        // Convierte la entidad Persona asociada al usuario en un DTO
        PersonBasicPanelDto dto = personService.convertirAPersonaBasicaPanelDTO(usuario.getPerson());

        return new ResponseEntity(dto, HttpStatus.OK);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!usuarioService.activo(id))
            return new ResponseEntity(new Mensaje("el usuario no existe"), HttpStatus.NOT_FOUND);
        Usuario usuario = usuarioService.findById(id).get();
        usuario.setActivo(false);
        usuarioService.save(usuario);
        return new ResponseEntity<>(new Mensaje("Usuario dado de baja correctamente"), HttpStatus.OK);
    }

}
