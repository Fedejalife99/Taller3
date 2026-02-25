package Logica.Objetos.Exceptions;

public class PostreNoExisteEnSec extends Exception{
	private String mensaje;
	
	
	public PostreNoExisteEnSec(String msj)
	{
		super(msj);
	}
	
	public String darMensaje(String msj)
	{
		return mensaje;
	}
}
