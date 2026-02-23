package Logica.Objetos.Exceptions;

public class FechaInvalidaException extends Exception{
	private String mensaje;
	
	public FechaInvalidaException(String msj)
	{
		mensaje = msj;
	}
	
	public String darMensaje() 
	{
		return mensaje;
	}
}
