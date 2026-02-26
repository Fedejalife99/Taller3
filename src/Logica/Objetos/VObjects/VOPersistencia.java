package Logica.Objetos.VObjects;
import java.io.Serializable;

import Logica.Postres.ColeccionPostres;
import Logica.Ventas.ColeccionVentas;

public class VOPersistencia implements Serializable {
	private static final long serialVersionUID = 0;
	private ColeccionPostres postres;
	private ColeccionVentas ventas;
	
	public VOPersistencia(ColeccionPostres postres, ColeccionVentas ventas) {
		super();
		this.postres = postres;
		this.ventas = ventas;
	}

	public ColeccionPostres getPostres() {
		return postres;
	}

	public ColeccionVentas getVentas() {
		return ventas;
	}
	
}
