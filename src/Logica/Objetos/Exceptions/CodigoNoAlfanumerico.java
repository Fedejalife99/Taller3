package Logica.Objetos.Exceptions;

public class CodigoNoAlfanumerico extends Exception{
	private String mensaje;
	
	
	public CodigoNoAlfanumerico(String msj)
	{
		super(msj);
	}
	
	public String darMensaje(String msj)
	{
		return mensaje;
	}
}
