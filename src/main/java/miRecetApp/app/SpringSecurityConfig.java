package miRecetApp.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import miRecetApp.app.auth.handler.LogginSuccessHandler;
import miRecetApp.app.service.implementation.UsuarioService;

/**
 * Esta clase define las pautas de seguridad del proyecto a mediante Spring Security.
 *
 * @author Julian Quenard
 * @version 1
 */

@EnableGlobalMethodSecurity(securedEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	//Atributos
	@Autowired
	private LogginSuccessHandler successHandler;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UsuarioService usuarioService;

	
	/**
	 * Método en que se asignan los roles de usuario que 
	 * definiran el nivel de acceso a las URLs del proyecto.
	 */
	@Override 
	protected void configure(HttpSecurity http) throws Exception {
	  
	  http.authorizeRequests()
	  .antMatchers("/css/**", "/js/**", "/locale", "/usuario/**", "/h2/**", "/datos/**").permitAll()
	  .antMatchers("/artefacto/**").hasAnyRole("USER")
	  .antMatchers("/receta/**").hasAnyRole("USER")
	  .antMatchers("/producto/**").hasAnyRole("USER")
	  .antMatchers("/gastos/**").hasAnyRole("USER")
	  .antMatchers("/manoDeObra/**").hasAnyRole("USER")
	  .antMatchers("/gastoDivisible/**").hasAnyRole("USER")
	  .antMatchers("/gastoIndivisible/**").hasAnyRole("USER")
	  .anyRequest().authenticated()				 
	  .and().csrf().ignoringAntMatchers("/h2/**","/datos/**")
      .and().headers().frameOptions().sameOrigin()
	  .and().formLogin().successHandler(successHandler).loginPage("/login").permitAll()
	  .and().logout().permitAll()
	  .and().exceptionHandling().accessDeniedPage("/error_403"); 
	}
	 
	/**
	 * Método en que se asignan los roles de usuario que 
	 * definiran el nivel de acceso a las URLs del proyecto.
	 * 
	 * @throws Exception
	 */
	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception {
		
		builder.userDetailsService(usuarioService).passwordEncoder(passwordEncoder);
		
	}

}
