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
 * Clase entity que gestiona los trabajadores en el proyecto.
 * @author Julian Quenard
 * 01-09-2021
 */
@Entity
@Table(name = "trabajadores")
public class ManoDeObra implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	//ATRIBUTOS----------------------------------------------------------
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String nombre;
	
	@NotNull
	private String apellido;
	
	@NotNull
	private double remuneracionPorHora;
	
	@NotNull
	private boolean trabajaEnLaCocina;
	
	public boolean isTrabajaEnLaCocina() {
		return trabajaEnLaCocina;
	}
	public void setTrabajaEnLaCocina(boolean trabajaEnLaCocina) {
		this.trabajaEnLaCocina = trabajaEnLaCocina;
	}

	private int periodicidadDeCobroEnDias; // Indicar cada cuantos días cobra el trabajador.
	private int horasTrabajadasPorDiaPromedio;
	private double remuneracionPorPeriodo; // Indicar el monto percibido cada vez que cobra.
	private double cargasSociales; // Indicar el porcentaje de la remuneracion destinado a las cargas sociales, aportes jubilatorios, sindicales, etc.
	private int minutosTrabajadosEnReceta;
	
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
	public int getPeriodicidadDeCobroEnDias() {
		return periodicidadDeCobroEnDias;
	}
	public void setPeriodicidadDeCobroEnDias(int periodicidadDeCobroEnDias) {
		this.periodicidadDeCobroEnDias = periodicidadDeCobroEnDias;
	}
	public double getRemuneracionPorPeriodo() {
		return remuneracionPorPeriodo;
	}
	public void setRemuneracionPorPeriodo(double remuneracionPorPeriodo) {
		this.remuneracionPorPeriodo = remuneracionPorPeriodo;
	}
	public double getCargasSociales() {
		return cargasSociales;
	}
	public void setCargasSociales(double cargasSociales) {
		this.cargasSociales = cargasSociales;
	}
	public int getMinutosTrabajadosEnReceta() {
		return minutosTrabajadosEnReceta;
	}
	public void setMinutosTrabajadosEnReceta(int minutosTrabajadosEnReceta) {
		this.minutosTrabajadosEnReceta = minutosTrabajadosEnReceta;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public double getRemuneracionPorHora() {
		return remuneracionPorHora;
	}
	public void setRemuneracionPorHora(double remuneracionPorHora) {
		this.remuneracionPorHora = remuneracionPorHora;
	}
	public int getHorasTrabajadasPorDiaPromedio() {
		return horasTrabajadasPorDiaPromedio;
	}
	public void setHorasTrabajadasPorDiaPromedio(int horasTrabajadasPorDiaPromedio) {
		this.horasTrabajadasPorDiaPromedio = horasTrabajadasPorDiaPromedio;
	}
	
	public String getRemuneracionFormateada(double remuneracion) {
		if(remuneracion==0) {
			return "0,00";
		}
		else {
			DecimalFormat formato = new DecimalFormat("###,##0.00", new DecimalFormatSymbols(Locale.ITALY));
			return formato.format(remuneracion);
		}		
	}
	
	public String getCargasFormateadas(double cargas) {
		if(cargas==0) {
			return "0";
		}
		else {
			DecimalFormat formato = new DecimalFormat("0.#", new DecimalFormatSymbols(Locale.ITALY));
			return formato.format(cargas);
		}		
	}

}
