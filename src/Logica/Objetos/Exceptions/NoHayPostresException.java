package Logica.Objetos.Exceptions;

public class NoHayPostresException extends Exception{

	private String mensaje;
	
	public NoHayPostresException (String msj)
	{
		mensaje = msj;
	}
	
	public String darMensaje()
	{
		return mensaje;
	}
}
