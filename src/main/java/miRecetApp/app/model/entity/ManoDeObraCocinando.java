package miRecetApp.app.model.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "trabajadores_cocinando")
public class ManoDeObraCocinando implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	//ATRIBUTOS----------------------------------------------------------
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "receta_id")	
	private Receta receta;

	@NotNull
	private Long trabajadorId;

	@NotNull
	private int minutosTrabajadosEnReceta;
	
	//METODOS------------------------------------------------------------
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Receta getReceta() {
		return receta;
	}

	public void setRecetaId(Receta receta) {
		this.receta = receta;
	}	

	public Long getTrabajadorId() {
		return trabajadorId;
	}

	public void setTrabajadorId(Long trabajadorId) {
		this.trabajadorId = trabajadorId;
	}

	public void setReceta(Receta receta) {
		this.receta = receta;
	}

	public int getMinutosTrabajadosEnReceta() {
		return minutosTrabajadosEnReceta;
	}

	public void setMinutosTrabajadosEnReceta(int minutosTrabajadosEnReceta) {
		this.minutosTrabajadosEnReceta = minutosTrabajadosEnReceta;
	}
}
