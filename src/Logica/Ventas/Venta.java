package Logica.Ventas;
import java.io.Serializable;
import java.time.LocalDate;

import Logica.Objetos.CantPostre;
import Logica.Objetos.SecCantPostres;


public class Venta implements Serializable{
	private static final long serialVersionUID = 1L;
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
		this.sec = new SecCantPostres();
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
	public int darCantPostres()
	{
		return this.sec.darCantidadTotalPostres();
	}
	public void setCantidadPostre(String cod, int cant)
	{
		sec.SetCantPostre(cod, cant);
	}
	public boolean existePostreEnVenta(String cod)
	{
		return sec.ExistePostreEnSec(cod);
	}
	public void insertarCantPostre(CantPostre cp)
	{
		sec.insBack(cp);
	}
	public int  LargoSecuencia()
	{
		return sec.Largo();
	}
	
	public CantPostre CantPostreIndice(int indice)
	{
		return sec.darCantPostre(indice);
	}
	public void eliminarPostreVenta(int indice)
	{
		sec.eliminar(indice);
	}
	public double darMontoPostre(String codigo, LocalDate fec)
	{
		return sec.recaudadoPorPostre(codigo, fec);
	}
	
	public int darCantidadPostreVenta(String codigo)
	{
		 return sec.CantidadTotalPostre(codigo);
	}
}
