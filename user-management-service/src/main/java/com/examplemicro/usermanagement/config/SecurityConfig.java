package com.examplemicro.usermanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Configuration de sécurité basée sur Spring Security 6 / OAuth2 Resource Server.
 *
 * NB : l'ancien starter "keycloak-spring-boot-starter" (vu dans le support de cours)
 * est déprécié depuis Keycloak 17 et incompatible avec Spring Boot 3 / Spring Security 6
 * (il dépend de WebSecurityConfigurerAdapter, supprimé). L'approche recommandée
 * aujourd'hui est d'utiliser Spring Security comme "Resource Server" qui valide
 * directement le token JWT émis par Keycloak — même principe, syntaxe modernisée.
 */
@Configuration
public class SecurityConfig {

    @Bean

    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/users/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );
        return http.build();
    }

    /**
     * Convertit les "realm_access.roles" du token Keycloak en GrantedAuthority
     * Spring Security, afin que @PreAuthorize("hasAuthority('ADMIN')") fonctionne.
     */
    @SuppressWarnings("unchecked")
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess == null || realmAccess.get("roles") == null) {
                return List.of();
            }
            Collection<String> roles = (Collection<String>) realmAccess.get("roles");
            return roles.stream()
                    .map(role -> (GrantedAuthority) new SimpleGrantedAuthority(role.toUpperCase()))
                    .collect(Collectors.toList());
        });
        return converter;
    }
}
