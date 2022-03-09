package miRecetApp.app.util.paginator;

/**
 * Clase que se utilizará para gestiona la página en el paginator de la vista.
 * @author Julian Quenard
 *
 */
public class PageItem {

	private int numero;
	private boolean actual;

	public PageItem(int numero, boolean actual) {
		this.numero = numero;
		this.actual = actual;
	}

	public int getNumero() {
		return numero;
	}

	public boolean isActual() {
		return actual;
	}
	
}
