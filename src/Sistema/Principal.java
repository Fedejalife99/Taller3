package Sistema;
import Logica.Objetos.Fachada;
import Logica.Objetos.TipoIndice;
import Logica.Objetos.VOVenta;
import Logica.Objetos.VObjects.*;
import Logica.Objetos.Exceptions.*;
import java.time.LocalDate;
import java.util.List;

public class Principal
{
    private Fachada f;

    public Principal() throws PersistenciaException
    {
        this.f = new Fachada();
    }

    public static void main(String[] args) throws InterruptedException
    {
        Principal p = null;
        try
        {
            p = new Principal();
            p.f.RecuperarDatos();
            System.out.println("OK: Sistema inicializado correctamente.");
        }
        catch (PersistenciaException e)
        {
            System.out.println("ERROR al inicializar: " + e.getMessage());
            return;
        }

        // ══════════════════════════════════════════════════════════
        // R1: Alta de nuevo postre
        // Excepciones: PrecioPostreException, CodigoExistenteException
        // ══════════════════════════════════════════════════════════
        System.out.println("\n=== R1: Alta de nuevo postre ===");

        // Caso valido: postre comun
        try
        {
            p.f.IngresarPostre(new VOPostreIngreso("chocoint001", "Pasion de chocolate intenso", 250));
            System.out.println("OK: Postre comun ingresado.");
        }
        catch (PrecioPostreException | CodigoExistenteException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // Caso valido: postre light
        try
        {
            p.f.IngresarPostre(new VOPostreLightIngreso("maracuya123", "Explosion de maracuya", 320, "Stevia", "Sin gluten, apto para personas celiacas"));
            System.out.println("OK: Postre light ingresado.");
        }
        catch (PrecioPostreException | CodigoExistenteException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // PrecioPostreException: precio negativo
        try
        {
            p.f.IngresarPostre(new VOPostreIngreso("p003", "Postre invalido", -10));
            System.out.println("ERROR: Deberia haber lanzado PrecioPostreException.");
        }
        catch (PrecioPostreException e)
        {
            System.out.println("OK PrecioPostreException precio negativo: " + e.getMessage());
        }
        catch (CodigoExistenteException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // PrecioPostreException: precio cero
        try
        {
            p.f.IngresarPostre(new VOPostreIngreso("p004", "Postre gratis", 0));
            System.out.println("ERROR: Deberia haber lanzado PrecioPostreException.");
        }
        catch (PrecioPostreException e)
        {
            System.out.println("OK PrecioPostreException precio cero: " + e.getMessage());
        }
        catch (CodigoExistenteException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // CodigoExistenteException: codigo duplicado
        try
        {
            p.f.IngresarPostre(new VOPostreIngreso("chocoint001", "Duplicado", 100));
            System.out.println("ERROR: Deberia haber lanzado CodigoExistenteException.");
        }
        catch (CodigoExistenteException e)
        {
            System.out.println("OK CodigoExistenteException: " + e.getMessage());
        }
        catch (PrecioPostreException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // ══════════════════════════════════════════════════════════
        // R2: Listado general de postres (ordenado alfanumericamente)
        // Excepciones: NoHayPostresException
        // ══════════════════════════════════════════════════════════
        System.out.println("\n=== R2: Listado general de postres ===");

        try
        {
            List<VOPostreGeneral> lista = p.f.listarPostresGral();
            System.out.println("OK: Postres listados (deben aparecer ordenados por codigo):");
            for (VOPostreGeneral postre : lista)
            {
                System.out.println("  Codigo: " + postre.getCodigo() + " | Nombre: " + postre.getNombre() + " | Precio: " + postre.getPrecioUnitario() + " | Tipo: " + postre.getTipo());
            }
        }
        catch (NoHayPostresException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // ══════════════════════════════════════════════════════════
        // R3: Listado detallado de un postre
        // Excepciones: PostreNoExisteException
        // ══════════════════════════════════════════════════════════
        System.out.println("\n=== R3: Listado detallado de un postre ===");

        // Caso valido: postre comun
        try
        {
            VOPostreGeneral detalle = p.f.listarPostreDetallado("chocoint001");
            System.out.println("OK postre comun: Codigo: " + detalle.getCodigo() + " | Nombre: " + detalle.getNombre() + " | Precio: " + detalle.getPrecioUnitario() + " | Tipo: " + detalle.getTipo());
        }
        catch (PostreNoExisteException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // Caso valido: postre light muestra endulzante y descripcion
        try
        {
            VOPostreGeneral detalle = p.f.listarPostreDetallado("maracuya123");
            if (detalle instanceof VOPostreDetallado)
            {
                VOPostreDetallado detalleLight = (VOPostreDetallado) detalle;
                System.out.println("OK postre light: Codigo: " + detalleLight.getCodigo() + " | Nombre: " + detalleLight.getNombre() + " | Endulzante: " + detalleLight.getEndulzante() + " | Descripcion: " + detalleLight.getDescripcion());
            }
        }
        catch (PostreNoExisteException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // PostreNoExisteException: codigo inexistente
        try
        {
            p.f.listarPostreDetallado("inexistente");
            System.out.println("ERROR: Deberia haber lanzado PostreNoExisteException.");
        }
        catch (PostreNoExisteException e)
        {
            System.out.println("OK PostreNoExisteException: " + e.getMessage());
        }

        // ══════════════════════════════════════════════════════════
        // R4: Comienzo de nueva venta
        // Excepciones: ErrorFechaException
        // Verificar: fecha igual o posterior a la ultima venta
        //            si no hay ventas previas no debe romper
        // ══════════════════════════════════════════════════════════
        System.out.println("\n=== R4: Comienzo de nueva venta ===");

        // Caso valido: primera venta (no hay ventas previas)
        try
        {
            p.f.IngresarVenta(new VOVentaIngreso("Av. Italia 1234", LocalDate.now()));
            System.out.println("OK: Primera venta ingresada con fecha de hoy.");
        }
        catch (ErrorFechaException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // Caso valido: segunda venta con misma fecha (igual es valido segun letra)
        try
        {
            p.f.IngresarVenta(new VOVentaIngreso("Av. Brasil 456", LocalDate.now()));
            System.out.println("OK: Segunda venta ingresada con misma fecha.");
        }
        catch (ErrorFechaException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // Caso valido: tercera venta con fecha posterior
        try
        {
            p.f.IngresarVenta(new VOVentaIngreso("Av. Uruguay 789", LocalDate.now().plusDays(1)));
            System.out.println("OK: Tercera venta ingresada con fecha posterior.");
        }
        catch (ErrorFechaException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // ErrorFechaException: fecha anterior a la ultima venta
        try
        {
            p.f.IngresarVenta(new VOVentaIngreso("Av. Artigas 000", LocalDate.now().minusDays(5)));
            System.out.println("ERROR: Deberia haber lanzado ErrorFechaException.");
        }
        catch (ErrorFechaException e)
        {
            System.out.println("OK ErrorFechaException fecha anterior: " + e.getMessage());
        }

        // ══════════════════════════════════════════════════════════
        // R5: Agregado de postre a una venta
        // Excepciones: PostreNoExisteException, VentaNoExisteException,
        //              CantidadUnidadesException, VentaFinalizadaException
        // ══════════════════════════════════════════════════════════
        System.out.println("\n=== R5: Agregado de postre a una venta ===");

        // Caso valido: agregar postres a venta 1
        try
        {
            p.f.agregarPostreVenta("chocoint001", 3, 1);
            p.f.agregarPostreVenta("maracuya123", 2, 1);
            System.out.println("OK: Postres agregados a venta 1.");
        }
        catch (PostreNoExisteException | VentaNoExisteException | CantidadUnidadesException | VentaFinalizadaException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // Caso valido: agregar el mismo postre incrementa cantidad
        try
        {
            p.f.agregarPostreVenta("chocoint001", 1, 1);
            System.out.println("OK: Cantidad de postre existente incrementada.");
        }
        catch (PostreNoExisteException | VentaNoExisteException | CantidadUnidadesException | VentaFinalizadaException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // PostreNoExisteException
        try
        {
            p.f.agregarPostreVenta("inexistente", 1, 1);
            System.out.println("ERROR: Deberia haber lanzado PostreNoExisteException.");
        }
        catch (PostreNoExisteException e)
        {
            System.out.println("OK PostreNoExisteException: " + e.getMessage());
        }
        catch (VentaNoExisteException | CantidadUnidadesException | VentaFinalizadaException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // VentaNoExisteException
        try
        {
            p.f.agregarPostreVenta("chocoint001", 1, 999);
            System.out.println("ERROR: Deberia haber lanzado VentaNoExisteException.");
        }
        catch (VentaNoExisteException e)
        {
            System.out.println("OK VentaNoExisteException: " + e.getMessage());
        }
        catch (PostreNoExisteException | CantidadUnidadesException | VentaFinalizadaException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // CantidadUnidadesException: cantidad negativa
        try
        {
            p.f.agregarPostreVenta("chocoint001", -1, 1);
            System.out.println("ERROR: Deberia haber lanzado CantidadUnidadesException.");
        }
        catch (CantidadUnidadesException e)
        {
            System.out.println("OK CantidadUnidadesException cantidad negativa: " + e.getMessage());
        }
        catch (PostreNoExisteException | VentaNoExisteException | VentaFinalizadaException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // CantidadUnidadesException: superar 40 unidades
        try
        {
            p.f.agregarPostreVenta("chocoint001", 40, 1);
            System.out.println("ERROR: Deberia haber lanzado CantidadUnidadesException por superar 40.");
        }
        catch (CantidadUnidadesException e)
        {
            System.out.println("OK CantidadUnidadesException superar 40: " + e.getMessage());
        }
        catch (PostreNoExisteException | VentaNoExisteException | VentaFinalizadaException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // ══════════════════════════════════════════════════════════
        // R9: Listado de postres de una venta
        // Excepciones: VentaNoExisteException
        // ══════════════════════════════════════════════════════════
        System.out.println("\n=== R9: Listado de postres de una venta ===");

        // Caso valido
        try
        {
            List<VOPostresCant> listaVenta = p.f.listarPostresVenta(1);
            System.out.println("OK: Postres en venta 1 (deben aparecer en orden de ingreso):");
            for (VOPostresCant pc : listaVenta)
            {
                System.out.println("  Codigo: " + pc.getCodigo() + " | Nombre: " + pc.getNombre() + " | Precio: " + pc.getPrecioUnitario() + " | Tipo: " + pc.getTipoPostre() + " | Cantidad: " + pc.getCantidad());
            }
        }
        catch (VentaNoExisteException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // VentaNoExisteException
        try
        {
            p.f.listarPostresVenta(999);
            System.out.println("ERROR: Deberia haber lanzado VentaNoExisteException.");
        }
        catch (VentaNoExisteException e)
        {
            System.out.println("OK VentaNoExisteException: " + e.getMessage());
        }

        // ══════════════════════════════════════════════════════════
        // R6: Eliminacion de postre de una venta
        // Excepciones: VentaNoExisteException, VentaFinalizadaException,
        //              CantidadUnidadesException, PostreNoExisteException
        // ══════════════════════════════════════════════════════════
        System.out.println("\n=== R6: Eliminacion de postre de una venta ===");

        // Caso valido: eliminar algunas unidades
        try
        {
            p.f.eliminarCantPostres("chocoint001", 1, 1);
            System.out.println("OK: Unidades eliminadas correctamente.");
        }
        catch (VentaNoExisteException | VentaFinalizadaException | CantidadUnidadesException | PostreNoExisteException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // Caso valido: eliminar todas las unidades quita el postre de la venta
        try
        {
            p.f.eliminarCantPostres("maracuya123", 2, 1);
            System.out.println("OK: Postre eliminado totalmente de la venta.");
            List<VOPostresCant> listaVenta = p.f.listarPostresVenta(1);
            System.out.println("  Postres restantes en venta 1:");
            for (VOPostresCant pc : listaVenta)
            {
                System.out.println("  Codigo: " + pc.getCodigo() + " | Cantidad: " + pc.getCantidad());
            }
        }
        catch (VentaNoExisteException | VentaFinalizadaException | CantidadUnidadesException | PostreNoExisteException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // VentaNoExisteException
        try
        {
            p.f.eliminarCantPostres("chocoint001", 1, 999);
            System.out.println("ERROR: Deberia haber lanzado VentaNoExisteException.");
        }
        catch (VentaNoExisteException e)
        {
            System.out.println("OK VentaNoExisteException: " + e.getMessage());
        }
        catch (VentaFinalizadaException | CantidadUnidadesException | PostreNoExisteException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // PostreNoExisteException: postre no asociado a la venta
        try
        {
            p.f.eliminarCantPostres("inexistente", 1, 1);
            System.out.println("ERROR: Deberia haber lanzado PostreNoExisteException.");
        }
        catch (PostreNoExisteException e)
        {
            System.out.println("OK PostreNoExisteException: " + e.getMessage());
        }
        catch (VentaNoExisteException | VentaFinalizadaException | CantidadUnidadesException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // CantidadUnidadesException: eliminar mas de los existentes
        try
        {
            p.f.eliminarCantPostres("chocoint001", 999, 1);
            System.out.println("ERROR: Deberia haber lanzado CantidadUnidadesException.");
        }
        catch (CantidadUnidadesException e)
        {
            System.out.println("OK CantidadUnidadesException: " + e.getMessage());
        }
        catch (VentaNoExisteException | VentaFinalizadaException | PostreNoExisteException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // ══════════════════════════════════════════════════════════
        // R7: Finalizacion de una venta
        // Excepciones: VentaNoExisteException, VentaFinalizadaException
        // ══════════════════════════════════════════════════════════
        System.out.println("\n=== R7: Finalizacion de una venta ===");

        // Caso valido: confirmar venta
        try
        {
            double total = p.f.finalizarVenta(1, false);
            System.out.println("OK: Venta 1 confirmada. Total: $" + total);
        }
        catch (VentaNoExisteException | VentaFinalizadaException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // VentaFinalizadaException: intentar modificar venta ya finalizada
        try
        {
            p.f.agregarPostreVenta("chocoint001", 1, 1);
            System.out.println("ERROR: Deberia haber lanzado VentaFinalizadaException.");
        }
        catch (VentaFinalizadaException e)
        {
            System.out.println("OK VentaFinalizadaException: " + e.getMessage());
        }
        catch (PostreNoExisteException | VentaNoExisteException | CantidadUnidadesException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // Caso valido: cancelar venta (se elimina)
        try
        {
            double total = p.f.finalizarVenta(2, true);
            System.out.println("OK: Venta 2 cancelada. Total: $" + total);
        }
        catch (VentaNoExisteException | VentaFinalizadaException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // Caso valido: venta sin postres se elimina aunque se confirme
        try
        {
            double total = p.f.finalizarVenta(3, false);
            System.out.println("OK: Venta 3 sin postres eliminada. Total: $" + total);
        }
        catch (VentaNoExisteException | VentaFinalizadaException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // VentaNoExisteException
        try
        {
            p.f.finalizarVenta(999, false);
            System.out.println("ERROR: Deberia haber lanzado VentaNoExisteException.");
        }
        catch (VentaNoExisteException e)
        {
            System.out.println("OK VentaNoExisteException: " + e.getMessage());
        }
        catch (VentaFinalizadaException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // ══════════════════════════════════════════════════════════
        // R8: Listado de ventas por indicador T, P, F
        // Excepciones: ErrorIndiceException
        // ══════════════════════════════════════════════════════════
        System.out.println("\n=== R8: Listado de ventas ===");

        // Caso valido: todas las ventas
        try
        {
            List<VOVenta> todasVentas = p.f.listarVentasIndic(TipoIndice.T);
            System.out.println("OK T - Todas las ventas:");
            for (VOVenta v : todasVentas)
            {
                System.out.println("  Nro: " + v.getNumeroVenta() + " | Fecha: " + v.getFechaVenta() + " | Direccion: " + v.getDireccionEntrega() + " | Total: $" + v.getMontoTotal() + " | Finalizado: " + v.isFinalizado());
            }
        }
        catch (ErrorIndiceException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // Caso valido: ventas en proceso
        try
        {
            List<VOVenta> enProceso = p.f.listarVentasIndic(TipoIndice.P);
            System.out.println("OK P - Ventas en proceso:");
            for (VOVenta v : enProceso)
            {
                System.out.println("  Nro: " + v.getNumeroVenta() + " | Fecha: " + v.getFechaVenta() + " | Total: $" + v.getMontoTotal());
            }
        }
        catch (ErrorIndiceException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // Caso valido: ventas finalizadas
        try
        {
            List<VOVenta> finalizadas = p.f.listarVentasIndic(TipoIndice.F);
            System.out.println("OK F - Ventas finalizadas:");
            for (VOVenta v : finalizadas)
            {
                System.out.println("  Nro: " + v.getNumeroVenta() + " | Fecha: " + v.getFechaVenta() + " | Total: $" + v.getMontoTotal());
            }
        }
        catch (ErrorIndiceException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // ══════════════════════════════════════════════════════════
        // R10: Recaudacion por postre y fecha
        // Excepciones: PostreNoExisteException, FechaInvalidaException
        // ══════════════════════════════════════════════════════════
        System.out.println("\n=== R10: Recaudacion por postre y fecha ===");

        // Caso valido
        try
        {
            VORecaudacionPostreFecha rec = p.f.recaudacionPostreFecha("chocoint001", LocalDate.now());
            System.out.println("OK: Cantidad vendida: " + rec.getCantidadTotal() + " | Monto total: $" + rec.getMontoTotal());
        }
        catch (PostreNoExisteException | FechaInvalidaException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // Caso valido: postre sin ventas en esa fecha devuelve 0
        try
        {
            VORecaudacionPostreFecha rec = p.f.recaudacionPostreFecha("maracuya123", LocalDate.now().minusDays(1));
            System.out.println("OK sin ventas: Cantidad: " + rec.getCantidadTotal() + " | Monto: $" + rec.getMontoTotal());
        }
        catch (PostreNoExisteException | FechaInvalidaException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // PostreNoExisteException
        try
        {
            p.f.recaudacionPostreFecha("inexistente", LocalDate.now());
            System.out.println("ERROR: Deberia haber lanzado PostreNoExisteException.");
        }
        catch (PostreNoExisteException e)
        {
            System.out.println("OK PostreNoExisteException: " + e.getMessage());
        }
        catch (FechaInvalidaException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // FechaInvalidaException: fecha futura
        try
        {
            p.f.recaudacionPostreFecha("chocoint001", LocalDate.now().plusDays(1));
            System.out.println("ERROR: Deberia haber lanzado FechaInvalidaException.");
        }
        catch (FechaInvalidaException e)
        {
            System.out.println("OK FechaInvalidaException: " + e.getMessage());
        }
        catch (PostreNoExisteException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }

        // ══════════════════════════════════════════════════════════
        // R11: Respaldo de datos
        // Excepciones: PersistenciaException
        // ══════════════════════════════════════════════════════════
        System.out.println("\n=== R11: Respaldo de datos ===");
        try
        {
            p.f.RespaldarDatos();
            System.out.println("OK: Datos respaldados correctamente.");
        }
        catch (PersistenciaException e)
        {
            System.out.println("ERROR PersistenciaException: " + e.getMessage());
        }

        // ══════════════════════════════════════════════════════════
        // R12: Recuperacion de datos (se ejecuta al iniciar el sistema)
        // Se prueba creando una nueva instancia y verificando que
        // los datos persisten
        // ══════════════════════════════════════════════════════════
        System.out.println("\n=== R12: Recuperacion de datos ===");
        try
        {
            Principal p2 = new Principal();
            p2.f.RecuperarDatos();
            List<VOPostreGeneral> listaRecuperada = p2.f.listarPostresGral();
            System.out.println("OK: Datos recuperados correctamente. Postres en sistema:");
            for (VOPostreGeneral postre : listaRecuperada)
            {
                System.out.println("  Codigo: " + postre.getCodigo() + " | Nombre: " + postre.getNombre());
            }
        }
        catch (PersistenciaException | NoHayPostresException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}