package Logica.Postres;
import java.io.Serializable;

import Logica.Objetos.TipoPostre;

public class Postre implements Serializable{
	private String codigo;
	private String nombre;
	private double precioUnitario;
	private static final long serialVersionUID = 1L;
	public Postre(String cod, String nom, double pre)
	{
		this.codigo = cod;
		this.nombre = nom;
		this.precioUnitario = pre;
	}
	
	public String getCodigo()
	{
		return codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public TipoPostre darTipo()
	{
		return TipoPostre.COMUN;
	}
	
}
