package miRecetApp.app.model.entity;

public enum Servicio {
	
	GAS_NATURAL("Gas Natural","METRO3_MINUTO"),
	ENERGIA_ELECTRICA("Energía Eléctrica","KILOWATT_MINUTO"),
	AGUA ("Agua","MILILITRO");
	
	private final String nombre;
	private final String unidadDeBase;
	
	Servicio (String nombre, String unidadDeBase){
		this.nombre = nombre;
		this.unidadDeBase = unidadDeBase;
	}

	public String getUnidadDeBase() {
		return unidadDeBase;
	}	
	
	public String getNombre() {
		return nombre;
	}
	
}
