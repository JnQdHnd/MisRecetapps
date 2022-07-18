package miRecetApp.app.model.entity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.internal.constraintvalidators.bv.notempty.NotEmptyValidatorForCharSequence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Clase entity que gestiona las Recetas en el proyecto.
 * @author Julian Quenard
 * 01-09-2021
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "recetas")
public class Receta implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	//ATRIBUTOS----------------------------------------------------------
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	private String nombre;
	
	@OneToMany(targetEntity=Ingrediente.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Ingrediente> ingredientes;
	
	@OneToMany(targetEntity=ArtefactoEnUso.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<ArtefactoEnUso> artefactosUtilizados;
	
	@OneToMany(targetEntity=ManoDeObraCocinando.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<ManoDeObraCocinando> manoDeObra;
	
	@OneToMany(targetEntity=Instruccion.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Instruccion> instrucciones;
	
	private String autor;
	
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "recetaCompartida_usuario",
    joinColumns = @JoinColumn(name = "receta_id"),
    inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    private List<Usuario> usuariosAutorizados;
	
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "recetaFavorita_usuario",
   	joinColumns = @JoinColumn(name = "receta_id"),
    inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    private List<Usuario> usuariosFanaticos;
	
	@OneToMany(targetEntity=Preparacion.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Preparacion> preparaciones;
	
	private boolean esPublica;
	
	private double porciones;
	
	private double costo;
	
	@Transient
	private boolean esFavorita;
	
	private String preparacionEnRecetaNombre;
	
	//METODOS------------------------------------------------------------
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public void removeId() {
		this.id = null;
	}
	
	public void cleanId() {
		this.id = null;
	}
	
	public void setId(String id) {
		if(id != null && !id.isBlank() && Long.parseLong(id)>0) {
			this.id = Long.parseLong(id);
		}
	}	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public List<Ingrediente> getIngredientes() {
		return ingredientes;
	}
	public void setIngredientes(List<Ingrediente> ingredientes) {
		this.ingredientes = ingredientes;
	}
	public List<ArtefactoEnUso> getArtefactosUtilizados() {
		return artefactosUtilizados;
	}
	public void setArtefactosUtilizados(List<ArtefactoEnUso> artefactosUtilizados) {
		this.artefactosUtilizados = artefactosUtilizados;
	}
	public List<ManoDeObraCocinando> getManoDeObra() {
		return manoDeObra;
	}
	public void setManoDeObra(List<ManoDeObraCocinando> manoDeObra) {
		this.manoDeObra = manoDeObra;
	}
	
	public double getCosto() {
		return costo;
	}
	public void setCosto(double costo) {
		this.costo = costo;
	}
	public void setCosto(String costo) {
		if(costo != null && !costo.isBlank() && Double.parseDouble(costo)>0) {
			this.costo = Double.parseDouble(costo);
		}
	}	
	public double getPorciones() {
		return porciones;
	}
	public void setPorciones(double porciones) {
		this.porciones = porciones;
	}
	public void setPorciones(String porciones) {
		if(porciones != null && !porciones.isBlank() && Double.parseDouble(porciones)>0) {
			this.porciones = Double.parseDouble(porciones);
		}
	}
	
	public boolean isEsPublica() {
		return esPublica;
	}
	public void setEsPublica(boolean esPublica) {
		this.esPublica = esPublica;
	}
	
	public List<Usuario> getUsuariosAutorizados() {
		return usuariosAutorizados;
	}
	public void setUsuariosAutorizados(List<Usuario> recetasCompartidas) {
		this.usuariosAutorizados = recetasCompartidas;
	}
	
	public void addUsuarioAutorizado(Usuario usuario) {
        this.usuariosAutorizados.add(usuario);
        usuario.getRecetasCompartidas().add(this);
    }
 
    public void removeUsuarioAutorizado(Usuario usuario) {
        this.usuariosAutorizados.remove(usuario);
        usuario.getRecetasCompartidas().remove(this);
    }
	
	public String getAutor() {
		return autor;
	}
	public void setAutor(String autor) {
		this.autor = autor;
	}
	public List<Instruccion> getInstrucciones() {
		return instrucciones;
	}
	public void setInstrucciones(List<Instruccion> instrucciones) {
		this.instrucciones = instrucciones;
	}
	
	public String getPorcionesFormateado() {
		if(porciones==0) {
			return "0";
		}
		else {
			DecimalFormat formato = new DecimalFormat("###,##0.##", new DecimalFormatSymbols(Locale.ITALY));
			return formato.format(porciones);
		}
	}
	public boolean isEsFavorita() {
		return esFavorita;
	}
	public void setEsFavorita(boolean esFavorita) {
		this.esFavorita = esFavorita;
	}
	public List<Usuario> getUsuariosFanaticos() {
		return usuariosFanaticos;
	}
	public void setUsuariosFanaticos(List<Usuario> usuariosFanaticos) {
		this.usuariosFanaticos = usuariosFanaticos;
	}	
	public void addUsuarioFanatico(Usuario usuario) {
		 this.usuariosFanaticos.add(usuario); 
		 usuario.getRecetasFavoritas().add(this);
	} 	 
	public void removeUsuarioFanatico(Usuario usuario) {
		this.usuariosFanaticos.remove(usuario);
		usuario.getRecetasFavoritas().remove(this);
	}
	public List<Preparacion> getPreparaciones() {
		return preparaciones;
	}
	public void setPreparaciones(List<Preparacion> preparaciones) {
		this.preparaciones = preparaciones;
	}

	public String getPreparacionEnRecetaNombre() {
		return preparacionEnRecetaNombre;
	}

	public void setPreparacionEnRecetaNombre(String preparacionEnRecetaNombre) {
		this.preparacionEnRecetaNombre = preparacionEnRecetaNombre;
	}
	
	public String simplificaNombreDePreparacion() {
		String soloNombrePreparacion = "";
		if(preparacionEnRecetaNombre != null && !preparacionEnRecetaNombre.isEmpty()) {
			System.out.println("SIMPLIFICANDO NOMBRE DE PREPARACION");
			String aEliminar = " (en " + preparacionEnRecetaNombre + ")";
			soloNombrePreparacion = nombre.replace(aEliminar, "");
			System.out.println("soloNombrePreparacion: " + soloNombrePreparacion);			
		}
		return soloNombrePreparacion;
	}

	@Override
	public String toString() {
		return "Receta [id=" + id + ", nombre=" + nombre + ", ingredientes=" + ingredientes + ", artefactosUtilizados="
				+ artefactosUtilizados + ", manoDeObra=" + manoDeObra + ", instrucciones=" + instrucciones + ", autor="
				+ autor + ", usuariosAutorizados=" + usuariosAutorizados + ", usuariosFanaticos=" + usuariosFanaticos
				+ ", preparaciones=" + preparaciones + ", esPublica=" + esPublica + ", porciones=" + porciones
				+ ", costo=" + costo + ", esFavorita=" + esFavorita + ", preparacionEnRecetaNombre="
				+ preparacionEnRecetaNombre + "]";
	}	
}
