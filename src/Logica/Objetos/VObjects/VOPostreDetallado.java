package Logica.Objetos.VObjects;
import Logica.Objetos.TipoPostre;

public class VOPostreDetallado extends VOPostreGeneral{
	private String endulzante;
	private String descripcion;
	public VOPostreDetallado(String codigo, String nombre, double precioUnitario, TipoPostre tipoPostre,
			String endulzante, String descripcion) {
		super(codigo, nombre, precioUnitario, tipoPostre);
		this.endulzante = endulzante;
		this.descripcion = descripcion;
	}
	public String getEndulzante() {
		return endulzante;
	}
	public void setEndulzante(String endulzante) {
		this.endulzante = endulzante;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
