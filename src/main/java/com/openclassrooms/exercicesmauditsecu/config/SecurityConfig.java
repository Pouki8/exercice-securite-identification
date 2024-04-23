package com.openclassrooms.exercicesmauditsecu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.openclassrooms.exercicesmauditsecu.filter.CustomFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
	
	//mise à disposition d'un hasher de password, ici l'algorithme Bcrypt
    @Bean
    PasswordEncoder passwordEncoder() {
        return new  BCryptPasswordEncoder();
    }   
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	
        //@formatter:off
        
        http
        // désactivation de certaines fonctionnalités par défaut
        .csrf(csrf -> csrf.disable())
        .formLogin(formlogin -> formlogin.disable())       
        .logout(logout -> logout.disable())
        
        // activation de l'authentification Http Basic       
        // https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/basic.html
        .httpBasic(Customizer.withDefaults())
       
        //Pas de session donc pas de cookie
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))    
        
//        // On place notre filtre custom après le httpbasic
        .addFilterAfter(new CustomFilter(),AuthorizationFilter.class)
         
        
        // Définition des règles d'accès dans la configuration (possible aussi via des annotations dans les classes)
        // c'est aussi un filtre
        // https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html
        .authorizeHttpRequests((authorize) -> {authorize
        .requestMatchers("/logistique/**").hasAnyRole("LOGISTIQUE", "SUPERVISEUR") 
        
        // parce que j'ai mis @PreAuthorize("hasRole('ROLE_SUPERVISEUR') or hasRole('ROLE_COMMANDE')") dans le controller de commande donc pas besoin de :
        // .requestMatchers("/commande/**").hasAnyRole("COMMANDE", "SUPERVISEUR") 
        .requestMatchers("/catalogue/**").permitAll()
        .anyRequest().authenticated();});
                
        
        //@formatter:on
        return http.build();
    }

    // Génération des utilisateurs statiques
    // Si vous voulez vous basez une base de données, un repository donc, alors voilà un exemple pour démarrer
    // https://www.baeldung.com/spring-security-authentication-with-a-database
    @Bean
    UserDetailsService userDetailsService() {

        UserDetails logistique = User.builder().username("logistique").password(passwordEncoder().encode("logistique")).roles("LOGISTIQUE")
                .build();

        UserDetails commande = User.builder().username("commande").password(passwordEncoder().encode("commande")).roles("COMMANDE")
                .build();
        
        UserDetails superviseur = User.builder().username("superviseur").password(passwordEncoder().encode("superviseur")).roles("SUPERVISEUR")
                .build();
        
        return new InMemoryUserDetailsManager(logistique, commande, superviseur);
    }   

}
