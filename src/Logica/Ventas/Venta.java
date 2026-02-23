package Logica.Ventas;
import java.time.LocalDate;
import Logica.Objetos.SecCantPostres;


public class Venta {
	private int numeroVenta;
	private LocalDate Fecha;
	private String direccion;
	private double total;
	private boolean finalizado;
	private SecCantPostres sec;
	
	public Venta(String direccion, int numeroVenta) 
	{
		this.numeroVenta = numeroVenta;
		Fecha = LocalDate.now();
		this.direccion = direccion;
	}
	public int getNumeroVenta() {
		return numeroVenta;
	}
	public void setNumeroVenta(int numeroVenta) {
		this.numeroVenta = numeroVenta;
	}
	public LocalDate getFecha() {
		return Fecha;
	}
	public void setFecha(LocalDate fecha) {
		Fecha = fecha;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public boolean isFinalizado() {
		return finalizado;
	}
	public void setFinalizado(boolean finalizado) {
		this.finalizado = finalizado;
	}
	public SecCantPostres getSec() {
		return sec;
	}
	public void setSec(SecCantPostres sec) {
		this.sec = sec;
	}
	
}
