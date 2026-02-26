package Logica.Objetos;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Logica.Objetos.VObjects.VOPostresCant;

public class SecCantPostres implements Serializable{
	private ArrayList<CantPostre> ACT;
	private static final long serialVersionUID = 1L;
	public SecCantPostres()
	{
		this.ACT =  new ArrayList<CantPostre>();
	}
	public int Largo()
	{
		return ACT.size();
	}
	public void insBack(CantPostre cant)
	{
		this.ACT.add(cant);
	}
	public List<VOPostresCant> listarSCantPostres()
	{
		List<VOPostresCant> lista = new ArrayList<>();
		for(CantPostre p : ACT)
		{
			VOPostresCant nuevo = new VOPostresCant();
			nuevo.setCantidad(p.getCantidad());
			nuevo.setCodigo(p.getPostre().getCodigo());
			nuevo.setNombre(p.getPostre().getNombre());
			nuevo.setPrecioUnitario(p.getPostre().getPrecioUnitario());
			nuevo.setTipoPostre(p.getPostre().darTipo());
			lista.add(nuevo);
		}
		return lista;
	}
	public int darCantidadTotalPostres()
	{
		int total = 0;
		for(CantPostre p : ACT)
		{
			total += p.getCantidad();
		}
		return total;
	}
	public double darRecaudacionPostre(String codigo, LocalDate fecha)
	{
		double total = 0;
		int i = 0;
		while(i < ACT.size() && codigo != ACT.get(i).getPostre().getCodigo())
		{
			i++;
		}
		if(codigo == ACT.get(i).getPostre().getCodigo())
		{
			double precio = ACT.get(i).getPostre().getPrecioUnitario();
			total = precio * ACT.get(i).getCantidad();
		}
		return total;
	}
	
	public void SetCantPostre(String cod, int cant)
	{
	    int i = 0;
	    while(i < ACT.size() && !ACT.get(i).getPostre().getCodigo().equals(cod))
	    {
	        i++;
	    }
	    if(i < ACT.size())
	    {
	        ACT.get(i).setCantidad(cant);
	    }
	}
	
	public void eliminar(int i)
	{
		ACT.remove(i);
	}
	public boolean ExistePostreEnSec(String cod)
	{
		boolean existe = false;
		for(CantPostre cp: ACT)
		{
			if(cp.getPostre().getCodigo().equals(cod))
			{
				existe = true;
			}
		}
		return existe;
	}
	
	public CantPostre darCantPostre(int pos)
	{
		return ACT.get(pos);
	}
	public double recaudadoPorPostre(String cod)
	{
		double montoTotal = 0;
		for (CantPostre cp : ACT) {
			
            if (cp.getPostre().getCodigo().equals(cod)) {
                int cantidad = cp.getCantidad();
                double precio = cp.getPostre().getPrecioUnitario();

                montoTotal += precio * cantidad;
            }
        }
		return montoTotal;
	}
	public int CantidadTotalPostre(String codigo)
	{
		int cantidad = 0;
		for (CantPostre cp : ACT) {
			
            if (cp.getPostre().getCodigo().equals(codigo)) {
                cantidad = cp.getCantidad();
            }
        }
		return cantidad;
	}
	
}