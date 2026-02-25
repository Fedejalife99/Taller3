package Sistema;
import Logica.Objetos.Fachada;
import Logica.Objetos.VObjects.*;
import Logica.Objetos.Exceptions.*;
import java.time.LocalDate;
import java.util.List;

public class Principal
{
    private Fachada f;

    public Principal()
    {
        this.f = new Fachada();
    }

    public static void main(String[] args)
    {
        try {

            // 1️⃣ Crear fachada y cargar datos
            Fachada f = new Fachada();

            System.out.println("---- Cargando datos ----");

            VOPostreIngreso p1 = new VOPostreIngreso("P01", "Torta Chocolate", 350);
            VOPostreIngreso p2 = new VOPostreIngreso("P02", "Cheesecake", 400);

            f.IngresarPostre(p1);
            f.IngresarPostre(p2);

            // Guardar datos
            f.RespaldarDatos();
            System.out.println("Datos respaldados correctamente.");

            // 2️⃣ Simular cierre del sistema
            f = null;

            // 3️⃣ Crear nueva instancia (como si reiniciáramos el sistema)
            Fachada f2 = new Fachada();

            // Recuperar datos
            f2.RecuperarDatos();
            System.out.println("Datos recuperados correctamente.");

            // 4️⃣ Verificar que los datos siguen existiendo
            List<VOPostreGeneral> lista = f2.listarPostresGral();

            System.out.println("---- Postres recuperados ----");
            for (VOPostreGeneral vo : lista) {
                System.out.println("Codigo: " + vo.getCodigo() +
                                   " | Nombre: " + vo.getNombre() +
                                   " | Precio: " + vo.getPrecioUnitario());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
