import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigProperties {
    private Properties propiedades;

    public ConfigProperties(String rutaArchivo) throws IOException {
        propiedades = new Properties();
        try (FileInputStream fis = new FileInputStream(rutaArchivo)) {
            propiedades.load(fis);
        }
    }
    
    public String getNomArch()
    {
    	return propiedades.getProperty("nomArch");
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