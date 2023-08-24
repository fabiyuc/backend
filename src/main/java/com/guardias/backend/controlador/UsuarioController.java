package com.guardias.backend.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.guardias.backend.modelo.Usuarios;
import com.guardias.backend.repositorio.UsuarioRepositorio;

//@RestController
//@RequestMapping("/user")
//@CrossOrigin(origins = "http://localhost:4200")

public class UsuarioController {

    //@Autowired
    private UsuarioRepositorio repo;

  //  @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Usuarios userData) {
        // Usuario usu = repo.getById(userData.getId_usuario());
        System.out.println("############");
        // System.out.println(userData.getIdUsuario());

        // Usuario user = repo.findByIdUsuario(userData.getIdUsuario());
        // Usuario user = repo.findByIdUsuario(userData.g);
        Usuarios user = repo.findByUsuario(userData.getUsuario());
        System.out.println("el usuario userData es: " + userData.getUsuario());
        System.out.println("### El usuario es: " + user.getUsuario());
        if (user.getContrasena().equals(userData.getContrasena())) {

            return ResponseEntity.ok(user);

        }
        return (ResponseEntity<?>) ResponseEntity.internalServerError();
    }

    /*
     * @CrossOrigin(origins = "http://localhost:4200")
     * 
     * @PostMapping("/login")
     * public ModelAndView loginUser(@RequestBody Usuario userData) {
     * // Usuario usu = repo.getById(userData.getId_usuario());
     * System.out.println("############");
     * System.out.println("El usuario userata es" + userData.getIdUsuario());
     * 
     * Usuario user = repo.findByUsuario(userData.getUsuario());
     * System.out.println("### El usuario es: " + user.getUsuario());
     * System.out.println("### El user es: " + user.getContrasena());
     * System.out.println("### El userData es: " + userData.getContrasena());
     * if (user.getContrasena().equals(userData.getContrasena())) {
     * System.out.println("Hola if");
     * // return ResponseEntity.ok(user);
     * return new ModelAndView("redirect:/home-page");
     * }
     * return new ModelAndView("redirect:/home-page");
     * // (ResponseEntity<?>) ResponseEntity.internalServerError();
     * }
     */

    /*
     * private UsuarioServicio usuServicio;
     * 
     * @GetMapping("user/{username}/{password}")
     * public int UserLogin(@PathVariable("username") String
     * username1, @PathVariable("password") String password1){
     * 
     * int flag = usuServicio.loginValidation(username1, password1);
     * 
     * if (flag==0) {
     * return 0;
     * }
     * return flag;
     * }
     */
}
