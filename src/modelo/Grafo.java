package modelo;

import java.util.*;

public class Grafo {
    // Usamos un HashMap para guardar cada Nodo y su lista de conexiones (flechas hacia otros nodos)
    private Map<String, List<String>> listaAdyacencia;
    private Map<String, Boolean> nodosClientes = new HashMap<>(); 
    private Map<String, List<Factura>> adjacencias = new HashMap<>();

    public Grafo() {
        this.listaAdyacencia = new HashMap<>();
    }

    // 1. Método para agregar un punto en el mapa (Cliente, Factura, Producto, etc.)
    public void agregarNodo(String idNodo) {
        listaAdyacencia.putIfAbsent(idNodo, new ArrayList<>());
    }

    // 2. Método para conectar dos nodos (Grafo Dirigido: la flecha va de 'origen' a 'destino')
    public void agregarArista(String origen, String destino) {
        // Asegurarnos de que los nodos existen antes de conectarlos
        agregarNodo(origen);
        agregarNodo(destino);
        
        // Conectar origen con destino
        listaAdyacencia.get(origen).add(destino);
    }

    // 3. Método para ver a quién está conectado un nodo específico
    public List<String> obtenerConexiones(String idNodo) {
        return listaAdyacencia.getOrDefault(idNodo, new ArrayList<>());
    }

    // 4. Método para imprimir todo el grafo (útil para ver si se armó bien)
    public void imprimirGrafo() {
        System.out.println("\n--- ESTRUCTURA DEL GRAFO ---");
        for (String nodo : listaAdyacencia.keySet()) {
            System.out.println(nodo + " ---> " + listaAdyacencia.get(nodo));
        }
    }
    
    // Para quitar el error en .existeCliente(nitCliente)
    public boolean existeCliente(String nit) {
        // Aquí verificas si el NIT existe en tus nodos del grafo
        return nodosClientes.containsKey(nit); 
    }

    // Para quitar el error en .getFacturasDeCliente(nitCliente)
    public List<Factura> getFacturasDeCliente(String nit) {
        // Retorna la lista de facturas conectadas a ese cliente en el grafo
        return adjacencias.get(nit); 
    }
    
    public void agregarVenta(String nit, Factura factura) {
    // 1. Registramos al cliente en el mapa de nodos
    nodosClientes.put(nit, true);
    
    // 2. Creamos la conexión si no existe y agregamos la factura
    adjacencias.computeIfAbsent(nit, k -> new ArrayList<>()).add(factura);
    }
}