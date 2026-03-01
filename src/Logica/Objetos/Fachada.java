package Logica.Objetos;

import java.time.LocalDate;
import Sistema.Monitor;
import java.util.ArrayList;
import java.util.List;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

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
import Logica.Objetos.Exceptions.PersistenciaException;
import Logica.Postres.ColeccionPostres;
import Logica.Postres.Postre;
import Logica.Postres.PostreLight;
import Logica.Ventas.ColeccionVentas;
import Logica.Ventas.Venta;
import Sistema.Persistencia;

public class Fachada extends UnicastRemoteObject implements IFachada{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Persistencia p;
	private ColeccionPostres postres;
	private ColeccionVentas ventas;
	private VOPersistencia colecciones;
	private Monitor monitor = new Monitor();
	
	public Fachada() throws RemoteException, PersistenciaException{
		this.postres = new ColeccionPostres();
		this.ventas = new ColeccionVentas();
		this.p = new Persistencia();
		this.colecciones = new VOPersistencia(this.postres,this.ventas);
		
	}
	public void IngresarPostre(VOPostreIngreso datosPostre) throws RemoteException, PrecioPostreException, CodigoExistenteException, InterruptedException
	{
		monitor.comienzoEscritura();
		if(postres.member(datosPostre.getCodigo()))
		{
			monitor.terminoEscritura();
			throw new CodigoExistenteException("El prostre a ingresar ya existe");
		}
	    if(datosPostre.getPrecioUnitario() <= 0)
	    {
	    	monitor.terminoEscritura();
	        throw new PrecioPostreException("El precio del postre es menor o igual a 0.");
	    }
	    else
	    {
	        if(datosPostre instanceof VOPostreLightIngreso)
	        {
	            VOPostreLightIngreso datosLight = (VOPostreLightIngreso) datosPostre;
	            PostreLight nuevo = new PostreLight(datosLight.getCodigo(), datosLight.getNombre(), datosLight.getPrecioUnitario(), datosLight.getEndulzante(), datosLight.getDescripcion());
	            postres.insert(nuevo);
	            monitor.terminoEscritura();
	        }
	        else
	        {
	            Postre nuevo = new Postre(datosPostre.getCodigo(), datosPostre.getNombre(), datosPostre.getPrecioUnitario());
	            postres.insert(nuevo);
	            monitor.terminoEscritura();
	        }
	    }
	}
	
	public List<VOPostreGeneral> listarPostresGral() throws RemoteException, NoHayPostresException, InterruptedException
	{
		monitor.comienzoLectura();
		if(postres.listarPostresGeneral() == null)
		{
			monitor.terminoLectura();
			throw new NoHayPostresException("No hay postres registrados.");
		}
		else
		{	
			monitor.terminoLectura();
			return postres.listarPostresGeneral();
		}		
	}
	
	public VOPostreGeneral listarPostreDetallado(String codigo) throws RemoteException, PostreNoExisteException, InterruptedException
	{
		monitor.comienzoLectura();
		if(!postres.member(codigo))
		{
			monitor.terminoLectura();
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
			monitor.terminoLectura();
			return res;
		}
	}
	
	public void IngresarVenta(VOVentaIngreso v) throws RemoteException, ErrorFechaException, InterruptedException
	{
		monitor.comienzoEscritura();
	    Venta ultima = ventas.obtenerUltimaVenta();
	    if(ultima != null && v.getFechaVenta().isBefore(ultima.getFecha()))
	    {
	    	monitor.terminoEscritura();
	        throw new ErrorFechaException("La venta es anterior a la ultima fecha ingresada");
	    }
	    Venta nueva = null;
	    if(ventas.Largo() == 0)
	    {
	        nueva = new Venta(v.getDireccionEntrega(),v.getFechaVenta(), 1);
	    }
	    else
	    {
	        nueva = new Venta(v.getDireccionEntrega(), v.getFechaVenta(), ventas.Largo() + 1);
	    }
	    nueva.setFinalizado(false);
	    ventas.insBack(nueva);
	    monitor.terminoEscritura();
	}
	public void agregarPostreVenta(String codigoPostre, int cantUnidades, int numVenta) throws RemoteException, PostreNoExisteException, VentaNoExisteException, CantidadUnidadesException, VentaFinalizadaException, InterruptedException
	{
		monitor.comienzoEscritura();
		if (!postres.member(codigoPostre)) {
			monitor.terminoEscritura();
			throw new PostreNoExisteException("El código ingresado no corresponde con ningún postre.");
		}
		
		Venta aux = ventas.obtenerPorNum(numVenta);
		if (aux == null) {
			monitor.terminoEscritura();
			throw new VentaNoExisteException("El número de venta no es correcto.");
		}
		if(aux.isFinalizado())
		{
			monitor.terminoEscritura();
			throw new VentaFinalizadaException("No se pueden agregar postres a esta venta porque fue finalizada");
		}
		
		if (cantUnidades <= 0 || cantUnidades > 40) {
			monitor.terminoEscritura();
			throw new CantidadUnidadesException("La cantidad de postres debe estar entre 1 y 40.");
		}
		
		int totalActual = aux.darCantPostres();
		
		if (totalActual == 40) {
			monitor.terminoEscritura();
			throw new CantidadUnidadesException("No se pueden agregar más postres. La venta ya tiene 40.");
		}
		
		int nuevoTotal = totalActual + cantUnidades;
		
		if (aux.existePostreEnVenta(codigoPostre)) {

		    if (nuevoTotal > 40) {
		        aux.setCantidadPostre(codigoPostre, 40);
		        monitor.terminoEscritura();
		        throw new CantidadUnidadesException("Se limitó la cantidad total a 40 unidades.");
		    }

		    aux.setCantidadPostre(codigoPostre, nuevoTotal);
		    double precio = postres.find(codigoPostre).getPrecioUnitario();
		    aux.setTotal(aux.getTotal() + precio * cantUnidades);

		} else {

		    if (nuevoTotal > 40) {
		    	monitor.terminoEscritura();
		        throw new CantidadUnidadesException("No se pueden agregar más de 40 postres a una venta.");
		    }

		    Postre p = postres.find(codigoPostre);
		    CantPostre cantp = new CantPostre(p, nuevoTotal);
		    aux.insertarCantPostre(cantp);
		    aux.setTotal(aux.getTotal() + p.getPrecioUnitario() * cantUnidades);
		}
		monitor.terminoEscritura();
	}
	
	public void eliminarCantPostres(String codPos, int cant, int numVent) throws RemoteException, VentaNoExisteException, VentaFinalizadaException, CantidadUnidadesException, PostreNoExisteException, InterruptedException
	{
		monitor.comienzoEscritura();
		Venta aux = ventas.obtenerPorNum(numVent);
		
		if(aux == null)
		{
			monitor.terminoEscritura();
			throw new VentaNoExisteException("La venta no existe en el sistema.");
		}
		else
		{
			if(aux.isFinalizado()==true)
			{
				monitor.terminoEscritura();
				throw new VentaFinalizadaException("No se puede modificar una venta finalizada.");
			}
			else
			{
				if(cant < 0)
				{
					monitor.terminoEscritura();
					throw new CantidadUnidadesException("La cantidad a eliminar debe ser mayor a 0.");
				}
				else
				{
					if(!aux.existePostreEnVenta(codPos))
					{
						monitor.terminoEscritura();
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
										 monitor.terminoEscritura();
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
								 i++;
								 
							 }
					}
					monitor.terminoEscritura();
				}
			}
		}
	}
	
	
	public double finalizarVenta(int numVenta, boolean cancela) throws RemoteException, VentaNoExisteException, VentaFinalizadaException, InterruptedException
	{
		monitor.comienzoEscritura();
	    double total = 0;
	    Venta aux = ventas.obtenerPorNum(numVenta);

	    if(aux == null)
	    {
	    	monitor.terminoEscritura();
	        throw new VentaNoExisteException("La venta con el nro indicado no existe.");
	    }
	    else if(aux.isFinalizado())
	    {
	    	monitor.terminoEscritura();
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
	    monitor.terminoEscritura();
	    return total;
	    
	}
	
	public List<VOVenta> listarVentasIndic(TipoIndice indice) throws RemoteException, ErrorIndiceException, InterruptedException
	{
		monitor.comienzoLectura();
		 if(indice != TipoIndice.T && indice != TipoIndice.P && indice != TipoIndice.F)
		 {
			 	monitor.terminoLectura();
		        throw new ErrorIndiceException("El indice indicado no es valido.");
		 }
		 monitor.terminoLectura();
		 return ventas.listarVentas(indice);
	}
	
	public List<VOPostresCant> listarPostresVenta(int numVenta) throws RemoteException, VentaNoExisteException, InterruptedException
	{
		monitor.comienzoLectura();
		Venta aux = ventas.obtenerPorNum(numVenta);
		
		List<VOPostresCant> listaVO = new ArrayList<>();

		if(ventas.obtenerPorNum(numVenta) == null)
		{
			monitor.terminoLectura();
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
		monitor.terminoLectura();
		return listaVO;
	}
	
	public VORecaudacionPostreFecha recaudacionPostreFecha(String codigo, LocalDate fecha) throws RemoteException, PostreNoExisteException, FechaInvalidaException, InterruptedException
	{
		monitor.comienzoLectura();
	    if (!postres.member(codigo)) 
	    {
	    	monitor.terminoLectura();
	        throw new PostreNoExisteException("El código de postre no existe.");
	    }

	    if (fecha.isAfter(LocalDate.now())) {
	    	monitor.terminoLectura();
	        throw new FechaInvalidaException("Fecha inválida.");
	    }
	    double montoTotal = 0;
	    int cantidad = 0;
	    for (Venta v : ventas.getLista()) {

	        if (v.isFinalizado() && v.getFecha().isEqual(fecha)) {
	        	montoTotal += v.darMontoPostre(codigo);
	        	cantidad += v.darCantidadPostreVenta(codigo);
	        }
	    }
	    
	    VORecaudacionPostreFecha nuevo = new VORecaudacionPostreFecha(montoTotal, cantidad);
	    monitor.terminoLectura();
	    return nuevo;
	}
	
	public void RespaldarDatos() throws RemoteException, PersistenciaException, InterruptedException
	{
		monitor.comienzoEscritura();
		p.respaldarColecciones(colecciones);
		monitor.terminoEscritura();
	}
	
	public void RecuperarDatos()throws RemoteException, InterruptedException
	{
		monitor.comienzoLectura();
		try
		{
			colecciones = p.recuperarColecciones();
			ventas = colecciones.getVentas();
			postres = colecciones.getPostres();
			monitor.terminoLectura();
		}
		catch(PersistenciaException e)
		{
			monitor.terminoLectura();
			monitor.comienzoEscritura();
			postres = new ColeccionPostres();
	        ventas = new ColeccionVentas();
	        colecciones = new VOPersistencia(postres, ventas);
	        monitor.terminoEscritura();
		}
		
	}
}
	
