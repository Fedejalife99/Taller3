package Logica.Objetos.Exceptions;

public class CodigoExistenteException extends Exception{
	private String mensaje;
	
	public CodigoExistenteException(String msj)
	{
		super(msj);
	}
	
	public String darMensaje()
	{
		return mensaje;
	}
}
