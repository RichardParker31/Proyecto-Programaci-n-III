package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import modelo.Factura;
import modelo.GeneradorReportes;
import modelo.GestorInventario;
import modelo.Producto;

public class Conexion {
    
    // Estos son los datos que usamos en SQL Developer
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    private static final String USUARIO = "proyecto_umg";
    private static final String CLAVE = "admin123";

    public static Connection conectar() {
        Connection cn = null;
        try {
            // Buscamos el driver en las librerías
            Class.forName("oracle.jdbc.driver.OracleDriver");
            // Intentamos abrir la puerta a la base de datos
            cn = DriverManager.getConnection(URL, USUARIO, CLAVE);
            System.out.println("¡Conectado a Oracle 19c exitosamente!");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: No se encontró el driver JDBC.");
        } catch (SQLException e) {
            System.out.println("Error de SQL: " + e.getMessage());
        }
        return cn;
    }

    // Método para probar la conexión rápido
    public static void main(String[] args) {
        GestorInventario gestor = new GestorInventario();

        // 1. Cargamos inventario a Tabla Hash (Punto 4.1)
        gestor.cargarDesdeOracle(); 

        // 2. Simulamos la venta (Cambiamos a 6003 para evitar el error ORA-00001)
        Factura f = new Factura(6003, "123456-7");
        Producto p = gestor.obtenerProducto(101); 
        if(p != null) {
            f.agregarProducto(p, 1);
            gestor.procesarVenta(f);
        }

        // 3. OBTENER EL RECORRIDO DEL GRAFO (Punto 4.3)
        // Guardamos el resultado en una lista para pasársela al reporte
        List<String> recorrido = gestor.obtenerCaminoGrafo("123456-7");
        
        // Opcional: imprimir en consola para verificar
        gestor.reporteComprasCliente("123456-7");
        
        // 4. GENERAR EL REPORTE EN WORD
        GeneradorReportes reportador = new GeneradorReportes();

        // Ahora 'recorrido' ya existe y se puede enviar al método
        reportador.generarReporteGeneral(gestor.getTablaHashProductos(), "123456-7", recorrido);
    }
}