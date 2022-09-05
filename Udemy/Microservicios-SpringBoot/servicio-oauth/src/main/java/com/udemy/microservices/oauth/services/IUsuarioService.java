package com.udemy.microservices.oauth.services;


import com.udemy.microservices.commons.usuarios.models.entity.Usuario;

public interface IUsuarioService {

	public Usuario findByUsername(String username);

	public Usuario update(Usuario usuario, Long id);
}
