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

@Entity
@Table(name = "ingredientes")
public class Ingrediente implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	//ATRIBUTOS----------------------------------------------------------
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;	
	@NotNull
	private Long productoId;	
	@NotNull
	private double cantidad;	
	@NotNull
	private UnidadDeMedida unidadDeMedida;
	
	//METODOS------------------------------------------------------------
		

	public String getCantidadFormateada(double cantidad) {
		if(cantidad==0) {
			return "0";
		}
		else {
			DecimalFormat formato = new DecimalFormat("0.#", new DecimalFormatSymbols(Locale.ITALY));
			return formato.format(cantidad);
		}		
	}

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
	
	public Long getProductoId() {
		return productoId;
	}

	public void setProductoId(Long productoId) {
		this.productoId = productoId;
	}
	public void setProductoId(String productoId) {
		if(productoId != null && !productoId.isBlank() && Long.parseLong(productoId)>0) {
			this.productoId = Long.parseLong(productoId);
		}		
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}
	public void setCantidad(String cantidad) {
		if(cantidad != null && !cantidad.isBlank() && Double.parseDouble(cantidad)>0) {
			this.cantidad = Double.parseDouble(cantidad);
		}
		else {
			this.cantidad = 0;
		}
	}

	public UnidadDeMedida getUnidadDeMedida() {
		return unidadDeMedida;
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
	
	public String getCantidadFormateado() {
		if(cantidad==0) {
			return "0";
		}
		else {
			DecimalFormat formato = new DecimalFormat("#####0.##", new DecimalFormatSymbols(Locale.ITALY));
			return formato.format(cantidad);
		}
	}

	@Override
	public String toString() {
		return "Ingrediente [productoId=" + productoId + ", cantidad=" + cantidad + ", unidadDeMedida=" + unidadDeMedida
				+ "]";
	}

	
	
	
}
