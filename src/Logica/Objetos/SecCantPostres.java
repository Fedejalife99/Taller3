package Logica.Objetos;
import java.util.ArrayList;
import java.time.LocalDate;
import Logica.Objetos.CantPostre;
import Logica.Objetos.VObjects.VOPostresCant;
import java.util.List;
import java.util.ArrayList;

public class SecCantPostres{
	private ArrayList<CantPostre> ACT;
	
	public SecCantPostres()
	{}
	public int Largo()
	{
		return ACT.size();
	}
	public List<CantPostre> getLista()
	{
		return ACT;
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
	
	public void SetCantPostre(String codigo, int cant)
	{
		int i=0;
		while(i < ACT.size() && codigo != ACT.get(i).getPostre().getCodigo())
		{
			i++;
		}
		if(codigo == ACT.get(i).getPostre().getCodigo())
		{
			ACT.get(i).setCantidad(cant);
		}
	}
	
	public void eliminarCantPostre(String codigo, int cant)
	{
		int i=0;
		while(i < ACT.size() && codigo != ACT.get(i).getPostre().getCodigo())
		{
			i++;
		}
		if(codigo == ACT.get(i).getPostre().getCodigo())
		{
			ACT.remove(i);
		}
	}
	public boolean ExistePostreEnSec(String cod)
	{
		boolean existe = false;
		for(CantPostre cp: ACT)
		{
			if(cp.getPostre().getCodigo() == cod)
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
	
}