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
@Table(name = "gastos_divisibles")
public class GastoDivisible implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	//ATRIBUTOS----------------------------------------------------------
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String nombre;
	
	private boolean porDefecto;
	
	private String descripcion;
	
	@NotNull
	private UnidadDeMedida unidadDeMedidaDelGasto; // Ej.: Para el pago de la energía electrica, la unidad de medida es el Kw/h.
	
	@NotNull
	private double costoDeLaUnidadConsumida; // Indicar el valor monetario de la unidad de medida que figura en la factura de pago. Ej: Para la energía electrica, $3.20 pesos por Kw/h consumidos.
	
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
	public UnidadDeMedida getUnidadDeMedidaDelGasto() {
		return unidadDeMedidaDelGasto;
	}
	public void setUnidadDeMedidaDelGasto(UnidadDeMedida unidadDeMedidaDelGasto) {
		this.unidadDeMedidaDelGasto = unidadDeMedidaDelGasto;
	}
	public double getCostoDeLaUnidadConsumida() {
		return costoDeLaUnidadConsumida;
	}
	public void setCostoDeLaUnidadConsumida(double costoDeLaUnidadConsumida) {
		this.costoDeLaUnidadConsumida = costoDeLaUnidadConsumida;
	}
	
	public String getPrecioFormateado(double precio) {
		if(precio==0) {
			return "0,00";
		}
		else {
			DecimalFormat formato = new DecimalFormat("###,##0.00###", new DecimalFormatSymbols(Locale.ITALY));
			return formato.format(precio);
		}		
	}
	public boolean isPorDefecto() {
		return porDefecto;
	}
	public void setPorDefecto(boolean porDefecto) {
		this.porDefecto = porDefecto;
	}
	
	
}
