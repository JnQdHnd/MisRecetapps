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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Clase entity que gestiona los Roles de los Usuarios en el proyecto.
 * @author Julian Quenard
 * 01-09-2021
 */
@Entity
@Table(name = "artefactos")
public class Artefacto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	//ATRIBUTOS----------------------------------------------------------
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	private String nombre;
	
	private String descripcion;
	
	@NotNull
	private UnidadDeMedida unidadDeConsumo; // Se deberá indicar la unidad de consumo establecida por el fabricante. Ej: Kw/h, m3/h, etc.
	
	@NotNull
	private double consumoEnergetico; // Se deberá indicar la cantidad de consumo establecida por el fabricante. Ej: 0.85Kw/h, 2.56m3/h, etc.
	
	private String ubicacion;
	
	private boolean esHorno;
	
	//METODOS------------------------------------------------------------
		
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	@Override
	public String toString() {
		return "Artefacto [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", unidadDeConsumo="
				+ unidadDeConsumo + ", consumoEnergetico=" + consumoEnergetico + ", ubicacion=" + ubicacion
				+ ", esHorno=" + esHorno + "]";
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public UnidadDeMedida getUnidadDeConsumo() {
		return unidadDeConsumo;
	}
	public void setUnidadDeConsumo(UnidadDeMedida unidadDeConsumo) {
		this.unidadDeConsumo = unidadDeConsumo;
	}
	public double getConsumoEnergetico() {
		return consumoEnergetico;
	}
	public void setConsumoEnergetico(double consumoEnergetico) {
		this.consumoEnergetico = consumoEnergetico;
	}

	public String getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
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
	public boolean isEsHorno() {
		return esHorno;
	}
	public void setEsHorno(boolean esHorno) {
		this.esHorno = esHorno;
	}
	public void setEsHorno(String esHornoText) {
		boolean esHorno = false;
		if(esHornoText.equalsIgnoreCase("true")) {
			esHorno = true;
		}
		else if(esHornoText.equalsIgnoreCase("false")) {
			esHorno = false;
		}
		this.esHorno = esHorno;
	}
}