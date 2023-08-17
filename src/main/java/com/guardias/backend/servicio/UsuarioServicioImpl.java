/* package com.guardias.backend.servicio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Service;

@Service
public class UsuarioServicioImpl implements UsuarioServicio {

    Connection connection;
    int flag = 0;

    public UsuarioServicioImpl() throws SQLException {
        // connection = DBUtil.getConnection();
    }

    @Override
    public int loginValidation(String username, String password) {

        try {
            PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM USUARIO WHERE usuario = '" + username + "'");
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                if (rs.getString(2).equals(username) && rs.getString(3).equals(password)) {
                    flag = 1;
                } else {
                    System.out.println("No Usuario");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flag;
    }

}
/*
 * private UsuarioRepositorio usuarioRepositorio;
 * 
 * public UsuarioServicioImpl(UsuarioRepositorio usuarioRepositorio) {
 * 
 * this.usuarioRepositorio = usuarioRepositorio;
 * }
 * 
 * @Override
 * public Usuario guardar(UsuarioRegistroDTO registroDTO) {
 * 
 * Usuario usuario = new
 * Usuario(registroDTO.getUsuario(),registroDTO.getContrasena(),
 * false,registroDTO.getFechaAlta(),registroDTO.getFechaUltimaSesion(),
 * registroDTO.getIntentosFallidos());
 * return usuario;
 * 
 * }
 */
 