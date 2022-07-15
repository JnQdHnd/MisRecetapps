package miRecetApp.app.model.entity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Clase entity que gestiona los Roles de los Usuarios en el proyecto.
 * @author Julian Quenard
 * 01-09-2021
 */
@Entity
@Table(name = "artefactos_en_uso")
public class ArtefactoEnUso implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	//ATRIBUTOS----------------------------------------------------------
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private boolean esHorno;
	
	@NotNull
	private Long artefactoId;
	
	@NotNull
	private int minutosDeUso;
	
	private String intensidadDeUso;
	
	private double temperatura;
	
	private UnidadDeMedida unidadDeTemperatura;
	
	//METODOS------------------------------------------------------------
		
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
	
	public Long getArtefactoId() {
		return artefactoId;
	}
	public void setArtefactoId(Long artefactoId) {
		this.artefactoId = artefactoId;
	}
	public void setArtefactoId(String artefactoId) {
		if(artefactoId != null && !artefactoId.isBlank() && Long.parseLong(artefactoId)>0) {
			this.artefactoId = Long.parseLong(artefactoId);
		}		
	}
	
	public int getMinutosDeUso() {
		return minutosDeUso;
	}
	public void setMinutosDeUso(int minutosDeUso) {
		this.minutosDeUso = minutosDeUso;
	}
	public void setMinutosDeUso(String minutosDeUso) {
		if(minutosDeUso != null && !minutosDeUso.isBlank() && Integer.parseInt(minutosDeUso)>0) {
			this.minutosDeUso = Integer.parseInt(minutosDeUso);
		}
		else {
			this.minutosDeUso = 0;
		}
	}
	
	public String getIntensidadDeUso() {
		return intensidadDeUso;
	}
	public void setIntensidadDeUso(String intensidadDeUso) {
		this.intensidadDeUso = intensidadDeUso;
	}
	public double getTemperatura() {
		return temperatura;
	}
	public void setTemperatura(double temperatura) {
		this.temperatura = temperatura;
	}
	public void setTemperatura(String temperatura) {
		if(temperatura != null && !temperatura.isBlank() && Double.parseDouble(temperatura)>0) {
			this.temperatura = Double.parseDouble(temperatura);
		}
		else {
			this.temperatura = 0;
		}
	}	
	public UnidadDeMedida getUnidadDeTemperatura() {
		return unidadDeTemperatura;
	}
	public void setUnidadDeTemperatura(UnidadDeMedida unidadDeTemperatura) {
		this.unidadDeTemperatura = unidadDeTemperatura;
	}
	public void setUnidadDeTemperatura(String unidadDeMedida) {
		if(unidadDeMedida != null && !unidadDeMedida.isBlank() && !unidadDeMedida.equals("0")) {
			UnidadDeMedida um= UnidadDeMedida.valueOf(unidadDeMedida);
			this.unidadDeTemperatura = um;
		}
	}	
	public String getCantidadFormateada(double cantidad) {
		if(cantidad==0) {
			return "0";
		}
		else {
			DecimalFormat formato = new DecimalFormat("0.#", new DecimalFormatSymbols(Locale.ITALY));
			return formato.format(cantidad);
		}		
	}
	
	public String getCantidadFormateada() {
		if(minutosDeUso==0) {
			return "0";
		}
		else {
			DecimalFormat formato = new DecimalFormat("0.#", new DecimalFormatSymbols(Locale.ITALY));
			return formato.format(minutosDeUso);
		}		
	}
	public boolean esHorno() {
		return esHorno;
	}
	public void setEsHorno(boolean esHorno) {
		this.esHorno = esHorno;
	}
	public void setEsHorno(String esHorno) {
		if(esHorno != null && !esHorno.isBlank()) {
			this.esHorno = Boolean.parseBoolean(esHorno);
		}
		else {
			System.out.println("SET HORNO POR DEFECTO EN FALSE, PERO NO ES EL VALOR RECIBIDO...");
			this.esHorno = false;
		}	
	}
	
}
