package Sistema;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.io.IOException;


import Logica.Objetos.Fachada;
import Logica.Objetos.Exceptions.PersistenciaException;

public class Servidor {
	
	public static void main(String [] args) throws PersistenciaException, IOException
	{
		try
		{
            ConfigProperties config = new ConfigProperties();
            int puerto = Integer.parseInt(config.getPuertoServidor());
            String ip = config.getIpServidor();
            
            LocateRegistry.createRegistry(puerto);

            Fachada fachada = new Fachada();

            Naming.rebind("//" + ip + ":" + puerto + "/fachada", fachada);
		}
		catch (RemoteException e)
		{
			e.printStackTrace(); 
		}
        catch (MalformedURLException e)
		{
        	e.printStackTrace();
		}
	}

}
