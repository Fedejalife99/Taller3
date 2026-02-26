package Sistema;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigProperties {
    static private Properties propiedades;

    public ConfigProperties() throws IOException {
        propiedades = new Properties();
        try (FileInputStream fis = new FileInputStream("./.properties")) 
        {
            propiedades.load(fis);
        }
    }
    
    public String getNomArch()
    {
    	return propiedades.getProperty("nomarchivo");
    }
    public String getIpServidor() 
    {
        return propiedades.getProperty("ipServidor");
    }

    public String getPuertoServidor() 
    {
        return propiedades.getProperty("puertoServidor");
    }
}