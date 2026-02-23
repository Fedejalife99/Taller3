package Logica.Objetos.VObjects;

public class VORecaudacionPostreFecha {
	private double montoTotal;
	private int cantidadTotal;
	
	public VORecaudacionPostreFecha(double montoTotal, int cantidadTotal) {
		super();
		this.montoTotal = montoTotal;
		this.cantidadTotal = cantidadTotal;
	}

	public double getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(double montoTotal) {
		this.montoTotal = montoTotal;
	}

	public int getCantidadTotal() {
		return cantidadTotal;
	}

	public void setCantidadTotal(int cantidadTotal) {
		this.cantidadTotal = cantidadTotal;
	}
	
	
	
}
