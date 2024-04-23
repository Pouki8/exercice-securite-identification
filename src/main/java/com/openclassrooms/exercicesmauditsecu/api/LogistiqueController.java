package com.openclassrooms.exercicesmauditsecu.api;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logistique")
public class LogistiqueController {
	
	@GetMapping
	public String hello() {		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return "hello "+username;
	}
	
	// curl -u logistique:logistique https://localhost:8080/logistique -i
	// ou
	// curl -u superviseur:superviseur https://localhost:8080/logistique -i

}
