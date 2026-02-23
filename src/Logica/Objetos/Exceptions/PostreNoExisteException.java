package Logica.Objetos.Exceptions;

public class PostreNoExisteException extends Exception {
	private String mensaje;
	
	
	public PostreNoExisteException(String msj)
	{
		mensaje = msj;
	}
	
	public String darMensaje(String msj)
	{
		return mensaje;
	}
	
	
}
