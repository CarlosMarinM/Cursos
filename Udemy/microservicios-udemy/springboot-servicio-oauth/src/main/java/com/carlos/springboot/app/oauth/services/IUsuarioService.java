package com.carlos.springboot.app.oauth.services;

import com.carlos.springboot.app.usuarios.commons.models.entity.Usuario;

public interface IUsuarioService {

	public Usuario findByUsername(String username);

	public Usuario update(Usuario usuario, Long id);
}
