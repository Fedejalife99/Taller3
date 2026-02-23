package Logica.Objetos.Exceptions;

public class PrecioPostreException extends Exception {
	private String mensaje;
	
	public PrecioPostreException (String msj)
	{
		mensaje = msj;
	}
	
	public String darMensaje()
	{
		return mensaje;
	}
}
