package com.openclassrooms.exercicesmauditsecu.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commande")
@PreAuthorize("hasRole('ROLE_SUPERVISEUR') or hasRole('ROLE_COMMANDE')")
public class CommandeController {
	
	@GetMapping
	public String hello() {		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return "hello "+username;
	}
	
	// curl -u commande:commande https://localhost:8080/commande -i
	// ou
	// curl -u superviseur:superviseur https://localhost:8080/commande -i

}
