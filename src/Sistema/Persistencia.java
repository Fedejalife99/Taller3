package Sistema;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import Sistema.ConfigProperties;
import Logica.Objetos.Exceptions.PersistenciaException;
import Logica.Objetos.VObjects.VOPersistencia;

public class Persistencia
{
    private String archPersistencia = null;
    public Persistencia() throws PersistenciaException
    {
    	try
    	{
    		ConfigProperties conf = new ConfigProperties();
    		this.archPersistencia = conf.getNomArch();
    	}catch (IOException e)
    	{
            e.printStackTrace();
            throw new PersistenciaException("Error al Obtener el nombre del archivo.");
    	}
    }
    
    public void respaldarColecciones(VOPersistencia VOP) throws PersistenciaException
    {
        try
        {
            FileOutputStream f = new FileOutputStream(archPersistencia);
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(VOP);
            o.close();
            f.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new PersistenciaException("Error al respaldar las colecciones.");
        }
    }

    public VOPersistencia recuperarColecciones() throws PersistenciaException
    {
        try
        {
        	FileInputStream f = new FileInputStream(archPersistencia);
            ObjectInputStream o = new ObjectInputStream(f);
            VOPersistencia vop = (VOPersistencia) o.readObject();
            o.close();
            f.close();
            return vop;

        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
            throw new PersistenciaException("Error al recuperar postres.");
        }
    }
}