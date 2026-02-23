package Logica.Postres;
import java.util.*;

import Logica.Objetos.VObjects.VOPostreGeneral;

public class ColeccionPostres{
    private TreeMap<String, Postre> ABB;

    public ColeccionPostres(){
        ABB  = new TreeMap<>();
    }
    
    public boolean member (String codigo)
    { 
    	return ABB.containsKey(codigo); 
    }

    public void insert (Postre p)
    {
    	ABB.put(p.getCodigo(), p);
    }
    
    public Postre find (String clave)
    {
    	return ABB.get(clave);
    }
    
    public List<VOPostreGeneral> listarPostresGeneral() {

        List<VOPostreGeneral> listaVO = new ArrayList<>();

        for (Postre p : ABB.values()) {

            listaVO.add(new VOPostreGeneral(
                p.getCodigo(),
                p.getNombre(),
                p.getPrecioUnitario(),
                p.darTipo()
            ));
        }

        return listaVO;
    }
    
    

}
