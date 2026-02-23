package Logica.Objetos.Exceptions;

public class ErrorIndiceException extends Exception{
	private String mensaje;
	
	public ErrorIndiceException(String msj)
	{
		mensaje = msj;
	}
	
	public String darMensaje()
	{
		return mensaje;
	}
}
