package modelo;

public class Producto {
    private int id;
    private String nombre;
    private double precio;
    private int idMarca;

    public Producto(int id, String nombre, double precio, int idMarca) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.idMarca = idMarca;
    }

    // Getters para obtener los datos (los necesitaremos para la factura)
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public int getIdMarca() { 
        return idMarca; 
    }

    @Override
    public String toString() {
        return "ID: " + id + " | " + nombre + " | Q" + precio;
    }
}