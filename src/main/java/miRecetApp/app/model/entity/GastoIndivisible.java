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
 * Clase entity que gestiona los gastos indivisibles en el proyecto.
 * @author Julian Quenard
 * 01-09-2021
 */
@Entity
@Table(name = "gastos_indivicibles")
public class GastoIndivisible implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	//ATRIBUTOS----------------------------------------------------------
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String nombre;
	
	private String descripcion;
	
	@NotNull
	private double periodicidadDelPagoEnDias; // Indicar cada cuantos días se realiza el pago.
	
	@NotNull
	private double costoPorPeriodo; // Indicar el monto pagado en cada periodo establecido.
	
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
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public double getPeriodicidadDelPagoEnDias() {
		return periodicidadDelPagoEnDias;
	}
	public void setPeriodicidadDelPagoEnDias(double periodicidadDelPagoEnDias) {
		this.periodicidadDelPagoEnDias = periodicidadDelPagoEnDias;
	}
	public double getCostoPorPeriodo() {
		return costoPorPeriodo;
	}
	public void setCostoPorPeriodo(double costoPorPeriodo) {
		this.costoPorPeriodo = costoPorPeriodo;
	}
	
	public String getPrecioFormateado(double precio) {
		if(precio==0) {
			return "0,00";
		}
		else {
			DecimalFormat formato = new DecimalFormat("###,##0.00#", new DecimalFormatSymbols(Locale.ITALY));
			return formato.format(precio);
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
	
}
