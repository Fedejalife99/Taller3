package Logica.Objetos.Exceptions;

public class DireccionErroneaException extends Exception{

	private String mensaje;
	
	public DireccionErroneaException(String msj)
	{
		mensaje = msj;
	}
	
	public String darMensaje() 
	{
		return mensaje;
	}
}
