package Logica.Objetos;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Logica.Objetos.Exceptions.CantidadUnidadesException;
import Logica.Objetos.Exceptions.CodigoExistenteException;
import Logica.Objetos.Exceptions.ErrorFechaException;
import Logica.Objetos.Exceptions.ErrorIndiceException;
import Logica.Objetos.Exceptions.FechaInvalidaException;
import Logica.Objetos.Exceptions.NoHayPostresException;
import Logica.Objetos.Exceptions.PersistenciaException;
import Logica.Objetos.Exceptions.PostreNoExisteException;
import Logica.Objetos.Exceptions.PrecioPostreException;
import Logica.Objetos.Exceptions.VentaFinalizadaException;
import Logica.Objetos.Exceptions.VentaNoExisteException;
import Logica.Objetos.VObjects.VOPersistencia;
import Logica.Objetos.VObjects.VOPostreDetallado;
import Logica.Objetos.VObjects.VOPostreGeneral;
import Logica.Objetos.VObjects.VOPostreIngreso;
import Logica.Objetos.VObjects.VOPostreLightIngreso;
import Logica.Objetos.VObjects.VOPostresCant;
import Logica.Objetos.VObjects.VORecaudacionPostreFecha;
import Logica.Objetos.VObjects.VOVentaIngreso;
import Logica.Postres.ColeccionPostres;
import Logica.Postres.Postre;
import Logica.Postres.PostreLight;
import Logica.Ventas.ColeccionVentas;
import Logica.Ventas.Venta;
import Sistema.Persistencia;

public class Fachada {
	private Persistencia p;
	private ColeccionPostres postres;
	private ColeccionVentas ventas;
	private VOPersistencia colecciones;
	
	public Fachada() {
		this.postres = new ColeccionPostres();
		this.ventas = new ColeccionVentas();
		this.p = new Persistencia();
		this.colecciones = new VOPersistencia(this.postres,this.ventas);
		
	}
	public void IngresarPostre(VOPostreIngreso datosPostre) throws PrecioPostreException, CodigoExistenteException
	{
		if(datosPostre.getCodigo().equals(postres.find(datosPostre.getCodigo()).getCodigo()))
		{
			throw new CodigoExistenteException("El prostre a ingresar ya existe");
		}
	    if(datosPostre.getPrecioUnitario() <= 0)
	    {
	        throw new PrecioPostreException("El precio del postre es menor o igual a 0.");
	    }
	    else
	    {
	        if(datosPostre instanceof VOPostreLightIngreso)
	        {
	            VOPostreLightIngreso datosLight = (VOPostreLightIngreso) datosPostre;
	            PostreLight nuevo = new PostreLight(datosLight.getCodigo(), datosLight.getNombre(), datosLight.getPrecioUnitario(), datosLight.getEndulzante(), datosLight.getDescripcion());
	            postres.insert(nuevo);
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
	
	public VOPostreGeneral listarPostreDetallado(String codigo) throws PostreNoExisteException
	{
		if(!postres.member(codigo))
		{
			throw new PostreNoExisteException("El codigo ingresado no pertenece a ningun postre registrado.");
		}
		else
		{
			VOPostreGeneral res;
			if(postres.find(codigo).darTipo() == TipoPostre.COMUN)
			{
				Postre p = postres.find(codigo);
				res = new VOPostreGeneral(p.getCodigo(), p.getNombre(), p.getPrecioUnitario(), p.darTipo());
			}
			else
			{
				PostreLight p = (PostreLight) postres.find(codigo);
				res = new VOPostreDetallado(p.getCodigo(), p.getNombre(), p.getPrecioUnitario(), p.darTipo(), p.getEndulzante(), p.getDescripcion());
			}
			return res;
		}
	}
	
	public void IngresarVenta(VOVentaIngreso v) throws ErrorFechaException
	{
			Venta nueva = null;
			if(!v.getFechaVenta().isAfter(ventas.obtenerUltimaVenta().getFecha()))
			{
				throw new ErrorFechaException("La venta es anterior a la ultima fecha ingresada");
			}
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
	
	public void agregarPostreVenta(String codigoPostre, int cantUnidades, int numVenta) throws PostreNoExisteException, VentaNoExisteException, CantidadUnidadesException, VentaFinalizadaException
	{
		
		if (!postres.member(codigoPostre)) {
			throw new PostreNoExisteException("El código ingresado no corresponde con ningún postre.");
		}
		
		Venta aux = ventas.obtenerPorNum(numVenta);
		if (aux == null) {
			throw new VentaNoExisteException("El número de venta no es correcto.");
		}
		if(aux.isFinalizado())
		{
			throw new VentaFinalizadaException("No se pueden agregar postres a esta venta porque fue finalizada");
		}
		
		if (cantUnidades <= 0 || cantUnidades > 40) {
			throw new CantidadUnidadesException("La cantidad de postres debe estar entre 1 y 40.");
		}
		
		int totalActual = aux.darCantPostres();
		
		if (totalActual == 40) {
			throw new CantidadUnidadesException("No se pueden agregar más postres. La venta ya tiene 40.");
		}
		
		int nuevoTotal = totalActual + cantUnidades;
		
		if (aux.existePostreEnVenta(codigoPostre)) {

		    if (nuevoTotal > 40) {
		        aux.setCantidadPostre(codigoPostre, 40);
		        throw new CantidadUnidadesException("Se limitó la cantidad total a 40 unidades.");
		    }

		    aux.setCantidadPostre(codigoPostre, nuevoTotal);
		    double precio = postres.find(codigoPostre).getPrecioUnitario();
		    aux.setTotal(aux.getTotal() + precio * cantUnidades);

		} else {

		    if (nuevoTotal > 40) {
		        throw new CantidadUnidadesException("No se pueden agregar más de 40 postres a una venta.");
		    }

		    Postre p = postres.find(codigoPostre);
		    CantPostre cantp = new CantPostre(p, nuevoTotal);
		    aux.insertarCantPostre(cantp);
		    aux.setTotal(aux.getTotal() + p.getPrecioUnitario() * cantUnidades);
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
					if(!aux.existePostreEnVenta(codPos))
					{
						throw new PostreNoExisteException("El postre no está asociado a la venta.");
					}
					else
					{
						 int i = 0;
						 boolean encontre = false;
							 while(i<aux.LargoSecuencia() && !encontre)
							 {
								 CantPostre cp = aux.CantPostreIndice(i);
								 
								 if(cp.getPostre().getCodigo().equals(codPos))
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
											 aux.setCantidadPostre(codPos, nuevaCantidad);
											 double restarMonto = cp.getPostre().getPrecioUnitario() * cant;
											 aux.setTotal(aux.getTotal() - restarMonto);
										 }
										 else
										 {
											 aux.eliminarPostreVenta(i);
											 aux.setTotal(aux.getTotal() - cp.getPostre().getPrecioUnitario() * cant);
										 }
										 
									 }
									 encontre=true;
								 }
							 }
					}
				}
			}
		}
	}
	
	
	public double finalizarVenta(int numVenta, boolean cancela) throws VentaNoExisteException, VentaFinalizadaException
	{
	    double total = 0;
	    Venta aux = ventas.obtenerPorNum(numVenta);

	    if(aux == null)
	    {
	        throw new VentaNoExisteException("La venta con el nro indicado no existe.");
	    }
	    else if(aux.isFinalizado())
	    {
	        throw new VentaFinalizadaException("No se puede modificar una venta finalizada.");
	    }
	    else
	    {
	        if(aux.darCantPostres() == 0 || cancela)
	        {
	            ventas.eliminarVenta(numVenta);
	        }
	        else
	        {
	            aux.setFinalizado(true);
	            System.out.println("Total al finalizar: " + aux.getTotal());
	            total = aux.getTotal();
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
		Venta aux = ventas.obtenerPorNum(numVenta);
		
		List<VOPostresCant> listaVO = new ArrayList<>();

		if(ventas.obtenerPorNum(numVenta) == null)
		{
			throw new VentaNoExisteException("La venta no existe.");
		}
		else
		{			
			   for (int i = 0; i < aux.LargoSecuencia(); i++) 
			   {
			        CantPostre cp = aux.CantPostreIndice(i);
			        Postre p = cp.getPostre();

			        VOPostresCant vo = new VOPostresCant(
			            p.getCodigo(),
			            p.getNombre(),
			            p.getPrecioUnitario(),
			            p.darTipo(),
			            cp.getCantidad()
			        );
			      listaVO.add(vo);
			   }
		
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
	    double montoTotal = 0;
	    int cantidad = 0;
	    for (Venta v : ventas.getLista()) {

	        if (v.isFinalizado() && v.getFecha().isEqual(fecha)) {
	        	montoTotal += v.darMontoPostre(codigo, fecha);
	        	cantidad += v.darCantidadPostreVenta(codigo);
	        }
	    }
	    
	    VORecaudacionPostreFecha nuevo = new VORecaudacionPostreFecha(montoTotal, cantidad);
	    return nuevo;
	}
	
	public void RespaldarDatos() throws PersistenciaException
	{
		p.respaldarColecciones(colecciones);
	}
	
	public void RecuperarDatos() throws PersistenciaException
	{
		colecciones = p.recuperarColecciones();
		ventas = colecciones.getVentas();
		postres = colecciones.getPostres();
		
	}
}
	
