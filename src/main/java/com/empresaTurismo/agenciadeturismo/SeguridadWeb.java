package com.empresaTurismo.agenciadeturismo;

import com.empresaTurismo.agenciadeturismo.servicio.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SeguridadWeb {

    @Autowired
    public UsuarioService usuarioService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usuarioService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public SecurityFilterChain filtroCadena(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(autoriza -> autoriza
                                                    .requestMatchers("/admin/*")
                                                    .hasRole("ADMIN")
                                                    .requestMatchers("/css/*", "/js/*", "/img/*", "/**").permitAll())
                                .formLogin(autoriza -> autoriza.loginPage("/login")
                                                    .loginProcessingUrl("/logincheck")
                                                    .usernameParameter("email")
                                                    .passwordParameter("password")
                                                    .defaultSuccessUrl("/")
                                                    .permitAll())
                                .logout(autoriza -> autoriza
                                                    .logoutUrl("/logout")
                                                    .logoutSuccessUrl("/")
                                                    .permitAll())
                                .csrf(csrfCustomizer -> csrfCustomizer
                                                    .disable())
                                .build();

    }
}
//@Autowired
//public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//    // Este método se utiliza para configurar la autenticación global.
//    // Autowired se utiliza para inyectar el objeto AuthenticationManagerBuilder.
//    // Se configura un UserDetailsService (usuarioService) para obtener detalles de usuario y se utiliza un BCryptPasswordEncoder para codificar las contraseñas.
//    auth.userDetailsService(usuarioService)
//        .passwordEncoder(new BCryptPasswordEncoder());
//}
//
//@Bean
//public SecurityFilterChain filtroCadena(HttpSecurity http) throws Exception {
//    // Este método se utiliza para configurar las reglas de seguridad HTTP.
//    // Se utiliza el objeto HttpSecurity que se inyecta a través del método.
//    return http.authorizeHttpRequests(autoriza -> autoriza
//            // Se especifican las reglas de autorización para diferentes URL.
//            .requestMatchers("/admin/*").hasRole("ADMIN") // Se requiere el rol ADMIN para acceder a las URL bajo /admin/*
//            .requestMatchers("/css/*", "/js/*", "/img/*", "/**").permitAll()) // Se permite el acceso público a las URL especificadas de recursos estáticos
//            // Configuración del formulario de inicio de sesión
//            .formLogin(autoriza -> autoriza.loginPage("/login") // Se especifica la página de inicio de sesión
//            .loginProcessingUrl("/logincheck") // Se especifica la URL de procesamiento del inicio de sesión
//            .usernameParameter("email") // Se especifica el parámetro para el nombre de usuario
//            .passwordParameter("password") // Se especifica el parámetro para la contraseña
//            .defaultSuccessUrl("/") // Se especifica la URL a la que se redirige después del inicio de sesión exitoso
//            .permitAll()) // Se permite el acceso público a la página de inicio de sesión
//            // Configuración del proceso de cierre de sesión
//            .logout(autoriza -> autoriza
//            .logoutUrl("/logout") // Se especifica la URL de cierre de sesión
//            .logoutSuccessUrl("/") // Se especifica la URL a la que se redirige después del cierre de sesión exitoso
//            .permitAll()) // Se permite el acceso público a la URL de cierre de sesión
//            // Se deshabilita CSRF (Cross-Site Request Forgery) para evitar problemas en las aplicaciones de una sola página (SPA) y otras aplicaciones que no usan cookies.
//            .csrf(csrfCustomizer -> csrfCustomizer
//            .disable())
//            // Se construye y se devuelve el filtro de seguridad configurado.
//            .build();
//}
