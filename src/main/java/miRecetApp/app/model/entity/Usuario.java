package miRecetApp.app.model.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import miRecetApp.app.validator.PasswordContenido;
import miRecetApp.app.validator.PasswordIdentica;
import miRecetApp.app.validator.UsuarioExisteEnDB;


/**
 * Clase entity que gestiona los Usuarios en el proyecto.
 * @author Julian Quenard
 *
 */

@Entity
@Table(name = "users")
@PasswordIdentica
public class Usuario implements Serializable {
	
	//ATRIBUTOS
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 30, unique = true)
	@NotBlank
	@Size(min = 4)
	@Pattern(regexp = "^[^\\s]+$")
	@UsuarioExisteEnDB
	private String username;
	
	@Column(length = 60)
	@NotBlank
	@PasswordContenido
	private String password;
	
	@Transient
	private String reiteraPassword;

	private Boolean enabled;
	
	@NotBlank
	@Email
	private String email;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "usuario_id")
	private List<Role> roles;
	
	@OneToMany(mappedBy="id", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Receta> recetasFavoritas;
	
	@OneToMany(mappedBy="id", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Receta> recetasCompartidas;
	
	//METODOS
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	public void addRole(Role role) {
		roles.add(role);
	}
	
	public void removeRole(Role role) {
		roles.remove(role);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getReiteraPassword() {
		return reiteraPassword;
	}

	public void setReiteraPassword(String reiteraPassword) {
		this.reiteraPassword = reiteraPassword;
	}
    
	public Set<Receta> getRecetasFavoritas() {
		return recetasFavoritas;
	}

	public void setRecetasFavoritas(Set<Receta> recetasFavoritas) {
		this.recetasFavoritas = recetasFavoritas;
	}
	
	 public void addRecetaFavoritas(Receta receta) {
		 this.recetasFavoritas.add(receta); 
	 }
	 
 
    public void removeRecetaFavoritas(Receta receta) {
        this.recetasFavoritas.remove(receta);
    }

	public Set<Receta> getRecetasCompartidas() {
		return recetasCompartidas;
	}

	public void setRecetasCompartidas(Set<Receta> recetasCompartidas) {
		this.recetasCompartidas = recetasCompartidas;
	}

}
