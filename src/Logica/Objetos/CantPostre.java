package Logica.Objetos;
import java.io.Serializable;

import Logica.Postres.Postre;

public class CantPostre implements Serializable{
	private static final long serialVersionUID = 1L;
	private Postre postre;
	private int cantidad;
	
	public CantPostre(Postre p, int cant) {
		super();
		this.postre = p;
		this.cantidad = cant;
	}
	
	public Postre getPostre() {
		return postre;
	}
	
	public void setPostre(Postre postre) {
		this.postre = postre;
	}
	
	public int getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

}
