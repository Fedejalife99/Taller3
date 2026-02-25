package Logica.Objetos.Exceptions;

public class SecCantPostreVacia extends Exception{
	private String mensaje;
	
	public SecCantPostreVacia(String msj)
	{
		super(msj);
	}
	
	public String darMensaje()
	{
		return mensaje;
	}
}
