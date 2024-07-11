package com.guardias.backend.security.controller;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
            return new ResponseEntity(new Mensaje("campos mal puestos o email invalido"), HttpStatus.BAD_REQUEST);

        if (usuarioService.existsByNombreUsuario(nuevoUsuario.getNombreUsuario()))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);
        if (usuarioService.existsByEmail(nuevoUsuario.getEmail()))
            return new ResponseEntity(new Mensaje("ese email ya existe"), HttpStatus.BAD_REQUEST);
        Usuario usuario = new Usuario();

        usuario.setNombre(nuevoUsuario.getNombre());
        usuario.setNombreUsuario(nuevoUsuario.getNombreUsuario());
        usuario.setEmail(nuevoUsuario.getEmail());
        usuario.setPassword(passwordEncoder.encode(nuevoUsuario.getPassword()));

        Set<Rol> roles = new HashSet<>();
        //por defecto todos van a ser USER
        roles.add(rolService.getByRolNombre(RolNombre.ROLE_USER).get());
        if (nuevoUsuario.getRoles().contains("admin"))
            roles.add(rolService.getByRolNombre(RolNombre.ROLE_ADMIN).get());
        usuario.setRoles(roles);

        // Asociar la entidad Person
        if (nuevoUsuario.getIdPerson() != null) {
            if (personService.activoById(nuevoUsuario.getIdPerson())){
                    
                Person person = personService.findById(nuevoUsuario.getIdPerson());
                usuario.setPerson(person);
            }
        }
        
        usuarioService.save(usuario);
        return new ResponseEntity(new Mensaje("Nuevo usuario guardado"), HttpStatus.CREATED);
    }

    //devuelve un token
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<List<Usuario>> list() {
        List<Usuario> list = usuarioService.findAll();
        return new ResponseEntity<List<Usuario>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{nombreUsuario}")
    public ResponseEntity<List<Usuario>> getByNombreUsuario(@PathVariable("nombreUsuario") String nombreUsuario) {
        Usuario usuario = usuarioService.findByNombreUsuario(nombreUsuario).get();
        return new ResponseEntity(usuario, HttpStatus.OK);
    }

    /* La clase Principal es parte del paquete java.security, que proporciona una interfaz que representa la identidad de un usuario en un contexto de seguridad, el objeto Principal generalmente contiene el nombre de usuario del usuario autenticado. */
    @GetMapping("/detailPersonBasicPanel")
    public ResponseEntity<PersonBasicPanelDto> obtenerPerfil(Principal principal) {
        // Obtiene el nombre de usuario del usuario autenticado
        String username = principal.getName();

        Usuario usuario = usuarioService.findByNombreUsuario(username).get();
        
        // Convierte la entidad Persona asociada al usuario en un DTO
        PersonBasicPanelDto dto = personService.convertirAPersonaBasicaPanelDTO(usuario.getPerson());
 
        return new ResponseEntity(dto, HttpStatus.OK);
    }
}
