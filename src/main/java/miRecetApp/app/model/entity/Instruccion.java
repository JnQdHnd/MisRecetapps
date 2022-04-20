package miRecetApp.app.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Clase entity que gestiona las instrucciones de las recetas.
 * @author Julian Quenard
 *
 */
@Entity
@Table(name = "instrucciones")
public class Instruccion implements Serializable {
	
	
	//ATRIBUTOS ------------------------------------------
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 600)
	private String instruccion;
	
	private Long orden;
	
	private String foto;
	
	//METODOS ----------------------------------------------
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public void setId(String id) {
		if(id != null && !id.isBlank() && Long.parseLong(id)>0) {
			this.id = Long.parseLong(id);
		}		
	}

	public String getInstruccion() {
		return instruccion;
	}

	public void setInstruccion(String instruccion) {
		this.instruccion = instruccion;
	}

	public Long getOrden() {
		return orden;
	}

	public void setOrden(Long orden) {
		this.orden = orden;
	}
	public void setOrden(String orden) {
		if(orden != null && !orden.isBlank() && Long.parseLong(orden)>0) {
			this.orden = Long.parseLong(orden);
		}		
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}
	
	

}
