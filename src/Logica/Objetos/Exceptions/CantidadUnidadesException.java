package Logica.Objetos.Exceptions;

public class CantidadUnidadesException extends Exception{
	
	private String mensaje;
	
	public CantidadUnidadesException(String msj)
	{
		mensaje = msj;
	}
	
	public String darMensaje()
	{
		return mensaje;
	}
}
