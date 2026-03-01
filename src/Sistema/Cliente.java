package Sistema;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

import Logica.Objetos.IFachada;
import Logica.Objetos.Exceptions.CodigoExistenteException;
import Logica.Objetos.Exceptions.PrecioPostreException;
import Logica.Objetos.VObjects.VOPostreIngreso;
import Logica.Objetos.VObjects.VOPostreLightIngreso;

public class Cliente {
	public static void main(String [] args) throws PrecioPostreException, CodigoExistenteException, InterruptedException
	{
		try
		{
			ConfigProperties config = new ConfigProperties();
            int puerto = Integer.parseInt(config.getPuertoServidor());
            String ip = config.getIpServidor();
            
			IFachada fachada = (IFachada) Naming.lookup("//" + ip + ":" + puerto + "/fachada");
			
			 System.out.println("\n[1] Ingresando postre común...");
	            VOPostreIngreso postre1 = new VOPostreIngreso("P-001", "Tiramisú", 250.0);
	            fachada.IngresarPostre(postre1);
	            System.out.println("    ✔ Postre común ingresado: " + postre1.getCodigo());
	            
	            System.out.println("\n[2] Ingresando postre light...");
	            VOPostreLightIngreso postre2 = new VOPostreLightIngreso(
	                "P-002", "Gelatina Stevia", 180.0, "Stevia", "Sin azúcar, apto diabéticos"
	            );
		}
        catch (MalformedURLException e)
		{ 
        	System.err.println("URL mal formada: "   + e.getMessage());
        }
        catch (NotBoundException e)
		{
        	System.err.println("Objeto no publicado: " + e.getMessage());
        }
        catch (RemoteException e)
		{
        	System.err.println("Error remoto: "  + e.getMessage());
        }
	}
}
