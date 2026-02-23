package Logica.Objetos;
import Logica.Objetos.VObjects.*;
import Logica.Postres.*;
import Logica.Ventas.*;
import Logica.Objetos.Exceptions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Fachada {
	private ColeccionPostres postres;
	private ColeccionVentas ventas;
	private SecCantPostres secCantPostres;
	
	public Fachada() {
		this.postres = new ColeccionPostres();
		this.ventas = new ColeccionVentas();
	}
	public void IngresarPostre(VOPostreIngreso datosPostre) throws PrecioPostreException, PostreNoExisteException
	{
		if(datosPostre.getPrecioUnitario()<=0)
		{
			throw new PrecioPostreException("El precio del postre es menor o igual a 0.");
		}
		else
		{
			if(!postres.member(datosPostre.getCodigo()))
			{
				throw new PostreNoExisteException("El postre ya está registrado en el sistema.");
			}
			else
			{				
				Postre nuevo = new Postre(datosPostre.getCodigo(), datosPostre.getNombre(), datosPostre.getPrecioUnitario());
				postres.insert(nuevo);	
			}
		}
	}
	
	public List<VOPostreGeneral> listarPostresGral() throws NoHayPostresException
	{
		if(postres.listarPostresGeneral() == null)
		{
			throw new NoHayPostresException("No hay postres registrados.");
		}
		else
		{	
			return postres.listarPostresGeneral();
		}		
	}
	
	public VOPostreDetallado listarPostreDetallado(String codigo) throws PostreNoExisteException
	{
		if(!postres.member(codigo))
		{
			throw new PostreNoExisteException("El codigo ingresado no pertenece a ningun postre registrado.");
		}
		else
		{			
			PostreLight aux = (PostreLight) postres.find(codigo);
			VOPostreDetallado nuevo = new VOPostreDetallado(aux.getCodigo(), aux.getNombre(), aux.getPrecioUnitario(), aux.darTipo(), aux.getEndulzante(), aux.getDescripcion());
			return nuevo;
		}
	}
	
	public void IngresarVenta(VOVentaIngreso v) throws ErrorFechaException// A consultar este procedimiento porque según el desglose tenemos que tener la secuencia de ventas, y acá hay un VO.
	{
		LocalDate fecVIngreso = v.getFechaVenta();
		
		if(fecVIngreso.isBefore(ventas.obtenerUltimaVenta().getFecha()))
		{
			throw new ErrorFechaException("La fecha es menor a la de la última fecha registrada.");
		}
		else
		{
			Venta nueva = null;
			if(ventas.Largo()==0)
			{
				nueva = new Venta(v.getDireccionEntrega(), 1);
			}
			else
			{
				nueva = new Venta(v.getDireccionEntrega(), ventas.Largo()+1);
			}
			nueva.setFinalizado(false);
			ventas.insBack(nueva);
		}
	}
	
	public void agregarPostreVenta(String codigoPostre, int cantUnidades, int numVenta) throws PostreNoExisteException, VentaNoExisteException, CantidadUnidadesException 
	{
		
		if (!postres.member(codigoPostre)) {
			throw new PostreNoExisteException("El código ingresado no corresponde con ningún postre.");
		}
		
		Venta aux = ventas.obtenerPorNum(numVenta);
		if (aux == null) {
			throw new VentaNoExisteException("El número de venta no es correcto.");
		}
		
		if (cantUnidades <= 0 || cantUnidades > 40) {
			throw new CantidadUnidadesException("La cantidad de postres debe estar entre 1 y 40.");
		}
		
		int totalActual = aux.getSec().darCantidadTotalPostres();
		
		if (totalActual >= 40) {
			throw new CantidadUnidadesException("No se pueden agregar más postres. La venta ya tiene 40.");
		}
		
		int nuevoTotal = totalActual + cantUnidades;
		
		if (aux.getSec().ExistePostreEnSec(codigoPostre)) {
			
			if (nuevoTotal > 40) {
				aux.getSec().SetCantPostre(codigoPostre, 40);
				throw new CantidadUnidadesException("Se limitó la cantidad total a 40 unidades.");
			}
			
			aux.getSec().SetCantPostre(codigoPostre, nuevoTotal);
			
		} else {
			
			if (nuevoTotal > 40) {
				throw new CantidadUnidadesException("No se pueden agregar más de 40 postres a una venta.");
			}
			
			aux.getSec().SetCantPostre(codigoPostre, cantUnidades);
		}
	}
	
	public void eliminarCantPostres(String codPos, int cant, int numVent) throws VentaNoExisteException, VentaFinalizadaException, CantidadUnidadesException, PostreNoExisteException
	{
		Venta aux = ventas.obtenerPorNum(numVent);
		
		if(aux == null)
		{
			throw new VentaNoExisteException("La venta no existe en el sistema.");
		}
		else
		{
			if(aux.isFinalizado()==true)
			{
				throw new VentaFinalizadaException("No se puede modificar una venta finalizada.");
			}
			else
			{
				if(cant < 0)
				{
					throw new CantidadUnidadesException("La cantidad a eliminar debe ser mayor a 0.");
				}
				else
				{
					if(!aux.getSec().ExistePostreEnSec(codPos))
					{
						throw new PostreNoExisteException("El postre no está asociado a la venta.");
					}
					else
					{
						 int i = 0;
						 boolean encontre = false;
						 SecCantPostres auxSec = aux.getSec();
						 
						 if(auxSec.ExistePostreEnSec(codPos))
						 {
							 
							 while(i<auxSec.Largo() && !encontre)
							 {
								 CantPostre cp = auxSec.darCantPostre(i);
								 
								 if(cp.getPostre().getCodigo() == codPos)
								 {
									 if(cp.getCantidad() < cant)
									 {
										 throw new CantidadUnidadesException("No se pueden eliminar más unidades de las existentes.");
									 }
									 else
									 {
										 int nuevaCantidad = cp.getCantidad() - cant;
										 if(nuevaCantidad>0)
										 {
											 auxSec.SetCantPostre(codPos, nuevaCantidad);
											 double restarMonto = cp.getPostre().getPrecioUnitario() * nuevaCantidad;
											 aux.setTotal(aux.getTotal() - restarMonto);
										 }
										 else
										 {
											 // Falta eliminar el postre de la venta.
										 }
										 
									 }
								 }
							 }
						 }
						 else
						 {
							 throw new PostreNoExisteException("El postre no existe.");
						 }
					}
				}
			}
		}
	}
	
	
	public double finalizarVenta(int numVenta, boolean cancela) throws VentaNoExisteException, VentaFinalizadaException
	{
		double total = 0;
		
		if(ventas.obtenerPorNum(numVenta)==null)
		{
			throw new VentaNoExisteException("La venta con el nro indicado no existe.");
		}
		else
		{
			Venta aux = ventas.obtenerPorNum(numVenta);
			
			if(aux.isFinalizado())
			{
				throw new VentaFinalizadaException("No se puede modificar una venta finalizada.");
			}
			else
			{
				if(aux.getSec().darCantidadTotalPostres()==0)
				{
					ventas.eliminarVenta(numVenta);
				}
				else
				{
					if(cancela)
					{
						ventas.eliminarVenta(numVenta);
					}
					else
					{
						aux.setFinalizado(true);
						total = aux.getTotal();
					}
				}
			}
		}
		
		return total;
	}
	
	public List<VOVenta> listarVentasIndic(TipoIndice indice) throws ErrorIndiceException
	{
		if(indice!=TipoIndice.T || indice!=TipoIndice.P || indice!=TipoIndice.F)
		{
			throw new ErrorIndiceException("El indice indicado no es válido.");
		}
	
		return ventas.listarVentas(indice);
	}
	
	public List<VOPostresCant> listarPostresVenta(int numVenta) throws VentaNoExisteException
	{
		List<VOPostresCant> listaVO = new ArrayList<>();

		if(ventas.obtenerPorNum(numVenta) == null)
		{
			throw new VentaNoExisteException("La venta no existe.");
		}
		else
		{			
			listaVO = secCantPostres.listarSCantPostres();
		}
		
		return listaVO;
	}
	
	public VORecaudacionPostreFecha recaudacionPostreFecha(String codigo, LocalDate fecha) throws PostreNoExisteException, FechaInvalidaException 
	{
	    if (!postres.member(codigo)) {
	        throw new PostreNoExisteException("El código de postre no existe.");
	    }

	    if (fecha.isAfter(LocalDate.now())) {
	        throw new FechaInvalidaException("Fecha inválida.");
	    }

	    int cantTotal = 0;
	    double montoTotal = 0;

	    for (Venta v : ventas.getLista()) {

	        if (v.isFinalizado() && v.getFecha().isEqual(fecha)) {

	            for (CantPostre cp : v.getSec().getLista()) {

	                if (cp.getPostre().getCodigo().equals(codigo)) {

	                    int cantidad = cp.getCantidad();
	                    double precio = cp.getPostre().getPrecioUnitario();

	                    cantTotal += cantidad;
	                    montoTotal += precio * cantidad;
	                }
	            }
	        }
	    }
	    
	    VORecaudacionPostreFecha nuevo = new VORecaudacionPostreFecha(montoTotal, cantTotal);
	    
	    
	    
	    return nuevo;
	}
	
	
}
