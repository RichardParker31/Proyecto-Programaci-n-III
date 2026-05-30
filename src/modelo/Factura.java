package modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Factura {
    private int idFactura;
    private String nitCliente;
    private Date fecha;
    private ArrayList<Detalle> detalles;

    public Factura(int idFactura, String nitCliente) {
        this.idFactura = idFactura;
        this.nitCliente = nitCliente;
        this.fecha = new Date(); // Fecha actual
        this.detalles = new ArrayList<>();
    }

    public void agregarProducto(Producto p, int cantidad) {
        double subtotal = p.getPrecio() * cantidad;
        this.detalles.add(new Detalle(p.getId(), cantidad, subtotal));
    }

    // Getters necesarios para guardar en la base de datos
    public int getIdFactura() { return idFactura; }
    public String getNitCliente() { return nitCliente; }
    public ArrayList<Detalle> getDetalles() { return detalles; }

    // Clase interna para el detalle
    public class Detalle {
        public int idProducto;
        public int cantidad;
        public double subtotal;

        public Detalle(int idProducto, int cantidad, double subtotal) {
            this.idProducto = idProducto;
            this.cantidad = cantidad;
            this.subtotal = subtotal;
        }
    }
    
    // Para quitar el error en .getProductos()
    private List<Producto> productos = new ArrayList<>();

    public List<Producto> getProductos() {
        return this.productos;
    }
}