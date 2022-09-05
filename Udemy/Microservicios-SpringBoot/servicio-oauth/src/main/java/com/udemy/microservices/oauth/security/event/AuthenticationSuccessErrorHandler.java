package com.udemy.microservices.oauth.security.event;

import brave.Tracer;
import com.udemy.microservices.commons.usuarios.models.entity.Usuario;
import com.udemy.microservices.oauth.services.IUsuarioService;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class AuthenticationSuccessErrorHandler implements AuthenticationEventPublisher {

    @Autowired
    private IUsuarioService usuarioService;

	@Autowired
	private Tracer tracer;

    @Override
    public void publishAuthenticationSuccess(Authentication authentication) {
        if (authentication.getDetails() instanceof WebAuthenticationDetails) {
            return;
        }

        UserDetails user = (UserDetails) authentication.getPrincipal();
        String mensaje = "Success Login: " + user.getUsername();
        System.out.println(mensaje);
        log.info(mensaje);

		Usuario usuario = usuarioService.findByUsername(authentication.getName());

		if (usuario.getIntentos() != null && usuario.getIntentos() > 0) {
			usuario.setIntentos(0);
			usuarioService.update(usuario, usuario.getId());
		}
    }

    @Override
    public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
        String mensaje = "Error en el login: " + exception.getMessage();
        System.out.println(mensaje);
        log.info(mensaje);

		try {
			StringBuilder errors = new StringBuilder();
			errors.append(mensaje);

			Usuario usuario = usuarioService.findByUsername(authentication.getName());
			usuario.setIntentos(Optional.ofNullable(usuario.getIntentos()).orElse(0));

			log.info("Intentos actual es de: " + usuario.getIntentos());
			usuario.setIntentos(usuario.getIntentos() + 1);
			log.info("Intentos después es de: " + usuario.getIntentos());

			errors.append(" - Intentos del login: " + usuario.getIntentos());

			if (usuario.getIntentos() >= 3) {
				String errorMaxIntentos = String.format("El usuario %s deshabilitado por máximos intentos",
						usuario.getUsername());
				log.error(errorMaxIntentos);
				errors.append(" - " + errorMaxIntentos);
				usuario.setEnabled(false);
			}

			usuarioService.update(usuario, usuario.getId());

			tracer.currentSpan().tag("error.mensaje", errors.toString());
		} catch (FeignException e) {
			log.error(String.format("El usuario %s no existe en el sistema", authentication.getName()));
		}
    }

}
