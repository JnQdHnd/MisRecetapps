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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import miRecetApp.app.validator.CodigoBarras;

/**
 * Clase entity que gestiona los Productos en el proyecto.
 * @author Julian Quenard
 * 01-09-2021
 */
@Entity
@Table(name = "productos")
public class Producto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	//ATRIBUTOS----------------------------------------------------------
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	private String nombre;
	
	@NotNull
	private UnidadDeMedida unidadDeMedida;
	
	@CodigoBarras
	private String codigoDeBarra;
	
	@NotNull(message = "¡Debe indicar el porcentaje de desperdicio!")
	private double desperdicio;
	private String descripcion;
	
	@NotNull(message = "¡Debe indicar la cantidad de la medida utilizada para el precio!")
	private double cantidad;
	
	@NotNull(message = "¡Debe indicar el precio!")
	private double precio;
	
	private double precioSinDesperdicio;
	
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

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}
	

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public UnidadDeMedida getUnidadDeMedida() {
		return unidadDeMedida;
	}

	public String getCodigoDeBarra() {
		return codigoDeBarra;
	}

	public void setCodigoDeBarra(String codigoDeBarra) {
		this.codigoDeBarra = codigoDeBarra;
	}

	public void setUnidadDeMedida(UnidadDeMedida unidadDeMedida) {
		this.unidadDeMedida = unidadDeMedida;
	}
	
	public void setUnidadDeMedida(String unidadDeMedida) {
		if(unidadDeMedida != null && !unidadDeMedida.isBlank() && !unidadDeMedida.equals("0")) {
			UnidadDeMedida um= UnidadDeMedida.valueOf(unidadDeMedida);
			this.unidadDeMedida = um;
		}
	}	

	public double getDesperdicio() {
		return desperdicio;
	}

	public void setDesperdicio(double desperdicio) {
		this.desperdicio = desperdicio;
	}
	
	public String getPrecioFormateado() {
		if(precio==0) {
			return "0,00";
		}
		else {
			DecimalFormat formato = new DecimalFormat("###,##0.00", new DecimalFormatSymbols(Locale.ITALY));
			return formato.format(precio);
		}
	}
	
	public String getPrecioFormateado(double precio) {
		if(precio==0) {
			return "0,00";
		}
		else {
			DecimalFormat formato = new DecimalFormat("###,##0.00", new DecimalFormatSymbols(Locale.ITALY));
			return formato.format(precio);
		}		
	}
	
	public String getPrecioSinDesperdicioFormateado() {
		if(precio==0) {
			return "0,00";
		}
		else {
			DecimalFormat formato = new DecimalFormat("###,##0.00", new DecimalFormatSymbols(Locale.ITALY));
			return formato.format(precioSinDesperdicio);
		}
	}

	public double getPrecioSinDesperdicio() {
		return precioSinDesperdicio;
	}

	public void setPrecioSinDesperdicio(double precioSinDesperdicio) {
		this.precioSinDesperdicio = precioSinDesperdicio;
	}
	
	public void setPrecioSinDesperdicioDesdeCantidad() {
		double cantidadSinDesperdicio = this.cantidad - (this.desperdicio * this.cantidad / 100);
		this.precioSinDesperdicio = this.cantidad * this.precio / cantidadSinDesperdicio;
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
