package Logica.Ventas;


import Logica.Objetos.VOVenta;
import Logica.Objetos.TipoIndice;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class ColeccionVentas {
    private LinkedList<Venta> LPPF;

    public ColeccionVentas(){
        LPPF  = new LinkedList<>();
    }
    
    public LinkedList<Venta> getLista() {
        return LPPF;
    }
	
    public void insBack(Venta v)
    {
    	LPPF.add(v);
    }
    
    public Venta obtenerPorNum(int numVenta) {
    	Venta aux = null;
    	
        for (Venta v : LPPF) {
            if (v.getNumeroVenta() == numVenta) {
                aux = v;
            }
        }

        return aux;
    }

    
    public List<VOVenta> listarVentas(TipoIndice indic)
    {
        List<VOVenta> listaVO = new ArrayList<>();
	     for(Venta v: LPPF)
	     {
	    	 
	    	 if(indic == TipoIndice.T)
	    	 {
	        	listaVO.add(new VOVenta(v.getFecha(),v.getDireccion(),v.getNumeroVenta(),
	      				v.getTotal(),
	  					v.isFinalizado()));
	    	 }
	    	 else if(indic == TipoIndice.P)
	    	 {
	    		 if(!v.isFinalizado())
	    		 {
	    			 listaVO.add(new VOVenta(v.getFecha(),v.getDireccion(),v.getNumeroVenta(),
	 	      				v.getTotal(),
	 	  					v.isFinalizado()));
	    		 }
	    		 
	    	 }
	    	 else
	    	 {
	    		 if(v.isFinalizado())
	    		 {
	    			 listaVO.add(new VOVenta(v.getFecha(),v.getDireccion(),v.getNumeroVenta(),
		 	      				v.getTotal(),
		 	  					v.isFinalizado()));
	    		 }
	    	 }
	    	 
        }
	     return listaVO;

    }
    
    
    
    public int Largo()
    {
    	return LPPF.size();
    }
	
    public Venta obtenerUltimaVenta() {
        if (LPPF.isEmpty()) {
            return null;
        }
        return LPPF.getLast();
    }
    
    public void eliminarVenta(int numVenta)
    {
    	LPPF.remove(numVenta);
    }

}
