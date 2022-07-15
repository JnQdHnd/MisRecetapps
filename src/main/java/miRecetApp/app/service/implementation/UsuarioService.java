package miRecetApp.app.service.implementation;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import miRecetApp.app.model.dao.IUsuarioDao;
import miRecetApp.app.model.entity.Role;
import miRecetApp.app.model.entity.Usuario;

/**
 * Interfaz de servicio  que gestiona los pedidos y modificaciones en la BD correspondiente a los Usuarios.
 * @author Julian Quenard
 * 01-09-2021
 */

@Service("usuarioService")
public class UsuarioService implements UserDetailsService{

	@Autowired
	private IUsuarioDao usuarioDao;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	private Logger logger = LoggerFactory.getLogger(UsuarioService.class);
	
	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("CARGANDO USUARIO");
        Usuario usuario = usuarioDao.findByUsername(username);
        
        if(usuario == null) {
        	logger.error("Error en el Login: no existe el usuario '" + username + "' en el sistema!");
        	throw new UsernameNotFoundException("Username: " + username + " no existe en el sistema!");
        }
        
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        
        for(Role role: usuario.getRoles()) {
        	logger.info("Role: ".concat(role.getAuthority()));
        	authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }
        
        if(authorities.isEmpty()) {
        	logger.error("Error en el Login: Usuario '" + username + "' no tiene roles asignados!");
        	throw new UsernameNotFoundException("Error en el Login: usuario '" + username + "' no tiene roles asignados!");
        }
        
        System.out.println("USUARIO: " + usuario.getUsername());
        System.out.println("CONTRASEÑA: " + usuario.getPassword());
        System.out.println("CONTRASEÑA: " + usuario.getPassword());
        
		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
	}
	
	/**
	 * Método de servicio para guardar nuevos usuarios en la base de datos. El método encripta la clave proporcionada en la vista 
	 * a través de la clase BCryptPasswordEncoder y guarda los roles asignados en la tabla de la BD authorities. 
	 * @param usuario
	 */
	@Transactional
	public void saveUser(Usuario usuario) {		
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setReiteraPassword(usuario.getPassword());
        usuarioDao.save(usuario);
	}
	
	@Transactional(readOnly = true)	
	public List<Usuario> findAll() {		
		return (List<Usuario>) usuarioDao.findAll();
	}
	
	@Transactional(readOnly = true)	
	public Usuario findByUsername(String username) {		
		return (Usuario) usuarioDao.findByUsername(username);
	}

}
