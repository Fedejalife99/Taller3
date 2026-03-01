import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
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

import Logica.Objetos.VObjects.VOPostreGeneral;
import Logica.Objetos.VObjects.VOPostreIngreso;
import Logica.Objetos.VObjects.VOPostresCant;
import Logica.Objetos.VObjects.VORecaudacionPostreFecha;
import Logica.Objetos.VObjects.VOVentaIngreso;

public interface IFachada extends Remote 
{
    public void IngresarPostre(VOPostreIngreso datosPostre) throws RemoteException, PrecioPostreException, CodigoExistenteException, InterruptedException;
    
    public List<VOPostreGeneral> listarPostresGral() throws RemoteException, NoHayPostresException, InterruptedException;
    
    public VOPostreGeneral listarPostreDetallado(String codigo) throws RemoteException, PostreNoExisteException, InterruptedException;
    
    public void IngresarVenta(VOVentaIngreso v) throws RemoteException, ErrorFechaException, InterruptedException;
    
    public void agregarPostreVenta(String codigoPostre, int cantUnidades, int numVenta) throws RemoteException, PostreNoExisteException, VentaNoExisteException, CantidadUnidadesException, VentaFinalizadaException, InterruptedException;
    
    public void eliminarCantPostres(String codPos, int cant, int numVent) throws RemoteException, VentaNoExisteException, VentaFinalizadaException, CantidadUnidadesException, PostreNoExisteException, InterruptedException;
    
    public double finalizarVenta(int numVenta, boolean cancela) throws RemoteException, VentaNoExisteException, VentaFinalizadaException, InterruptedException;
    
    public List<VOVenta> listarVentasIndic(TipoIndice indice) throws RemoteException, ErrorIndiceException, InterruptedException;
    
    public List<VOPostresCant> listarPostresVenta(int numVenta) throws RemoteException, VentaNoExisteException, InterruptedException;
    
    public VORecaudacionPostreFecha recaudacionPostreFecha(String codigo, LocalDate fecha) throws RemoteException, PostreNoExisteException, FechaInvalidaException, InterruptedException;
    
    public void RespaldarDatos() throws RemoteException, PersistenciaException, InterruptedException;
    
    public void RecuperarDatos() throws RemoteException, InterruptedException;
}
