package com.openclassrooms.exercicesmauditsecu.api;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/catalogue")
public class CatalogueController {
	
	@GetMapping
	public String hello() {		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return "hello "+username;
	}
	
	// curl https://localhost:8080/catalogue -i
	// ou n'importe quel user du role commande ou logistique ou superviseur

}
