package Logica.Objetos.Exceptions;

public class VentaNoExisteException extends Exception {
	private String mensaje;
	
	public VentaNoExisteException(String msj)
	{
		mensaje = msj;
	}
	
	public String darMensaje()
	{
		return mensaje;
	}
}
