package modelo;

import org.apache.poi.xwpf.usermodel.*;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;

public class GeneradorReportes {

    // Método principal que recibe Hash (Productos), el NIT dinámico y el recorrido del Grafo
    public void generarReporteGeneral(Map<Integer, Producto> productos, String nitCliente, List<String> recorrido) {
        XWPFDocument document = new XWPFDocument();
        try {
            // --- TÍTULO ---
            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun run = title.createRun();
            run.setText("REPORTE DE GESTIÓN COMERCIAL - UMG");
            run.setBold(true);
            run.setFontSize(16);
            run.addBreak();

            // ==========================================================
            // --- TABLA 4.1: PRODUCTOS EN TABLA HASH ---
            // ==========================================================
            XWPFParagraph p1 = document.createParagraph();
            p1.createRun().setText("4.1 REPORTE DE PRODUCTOS REGISTRADOS (TABLA HASH)");
            p1.getRuns().get(0).setBold(true);

            XWPFTable table = document.createTable();
            XWPFTableRow header = table.getRow(0); 
            if (header == null) header = table.createRow();

            header.getCell(0).setText("ID Producto");
            header.addNewTableCell().setText("Nombre");
            header.addNewTableCell().setText("Hash / Posición"); 
            header.addNewTableCell().setText("Tiempo (ns)");

            // Darle color gris a los encabezados de la tabla 1
            for (XWPFTableCell cell : header.getTableCells()) {
                if (cell.getCTTc().getTcPr() == null) cell.getCTTc().addNewTcPr();
                CTShd ctShd = cell.getCTTc().getTcPr().addNewShd();
                ctShd.setFill("D3D3D3"); // Color Gris
                cell.getParagraphs().get(0).setAlignment(ParagraphAlignment.CENTER);
            }

            // Llenar datos de la Tabla Hash 4.1
            for (Producto p : productos.values()) {
                long inicio = System.nanoTime();
                productos.get(p.getId()); // Búsqueda Hash real O(1)
                long fin = System.nanoTime();

                XWPFTableRow row = table.createRow();
                row.getCell(0).setText(String.valueOf(p.getId()));
                row.getCell(1).setText(p.getNombre());
                row.getCell(2).setText(String.valueOf(Integer.valueOf(p.getId()).hashCode()));
                row.getCell(3).setText((fin - inicio) + " ns");
            }

            document.createParagraph().createRun().addBreak(); // Salto de línea

            // ==========================================================
            // --- TABLA 4.2: RELACIÓN PRODUCTO - MARCA ---
            // ==========================================================
            XWPFParagraph p2 = document.createParagraph();
            p2.createRun().setText("4.2 REPORTE DE PRODUCTOS Y SU MARCA");
            p2.getRuns().get(0).setBold(true);

            XWPFTable tableMarca = document.createTable();
            XWPFTableRow headerMarca = tableMarca.getRow(0);
            if (headerMarca == null) headerMarca = tableMarca.createRow();

            headerMarca.getCell(0).setText("Producto");
            headerMarca.addNewTableCell().setText("ID Marca");
            headerMarca.addNewTableCell().setText("Tiempo Búsqueda (ns)");

            // Darle color gris a los encabezados de la tabla 2 para que se vea uniforme
            for (XWPFTableCell cell : headerMarca.getTableCells()) {
                if (cell.getCTTc().getTcPr() == null) cell.getCTTc().addNewTcPr();
                CTShd ctShd = cell.getCTTc().getTcPr().addNewShd();
                ctShd.setFill("D3D3D3"); // Color Gris
                cell.getParagraphs().get(0).setAlignment(ParagraphAlignment.CENTER);
            }

            // Llenar datos de la Relación Marca 4.2
            for (Producto p : productos.values()) {
                long inicioM = System.nanoTime();
                int idM = p.getIdMarca(); // Búsqueda de la relación
                long finM = System.nanoTime();

                XWPFTableRow row = tableMarca.createRow();
                row.getCell(0).setText(p.getNombre());
                row.getCell(1).setText(String.valueOf(idM));
                row.getCell(2).setText((finM - inicioM) + " ns");
            }

            // ==========================================================
            // --- TRAZABILIDAD DE GRAFOS (Punto 4.3) ---
            // ==========================================================
            if (recorrido != null && !recorrido.isEmpty()) {
                escribirSeccionGrafo(document, nitCliente, recorrido);
            }

            // --- GUARDAR ARCHIVO ---
            FileOutputStream out = new FileOutputStream("Reporte_Final_UMG.docx");
            document.write(out);
            out.close();
            System.out.println("Reporte unificado generado exitosamente.");

        } catch (Exception e) {
            System.out.println("Error al generar Word: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método auxiliar para organizar el texto del Grafo
    private void escribirSeccionGrafo(XWPFDocument document, String cliente, List<String> recorrido) {
        XWPFParagraph p = document.createParagraph();
        p.setSpacingBefore(700); 
        XWPFRun r = p.createRun();
        r.setBold(true);
        r.setFontSize(14);
        r.setText("4.3 REPORTE DE TRAZABILIDAD (GRAFOS)");
        r.addBreak();

        XWPFParagraph p2 = document.createParagraph();
        XWPFRun r2 = p2.createRun();
        r2.setText("Recorrido para Cliente: " + cliente);
        r2.addBreak();

        String camino = String.join("\n", recorrido); // Usamos salto de línea para que se lea mejor hacia abajo
        r2.setText("Camino encontrado en Grafo: ");
        r2.addBreak();
        
        XWPFRun r3 = p2.createRun();
        r3.setItalic(true);
        r3.setColor("0000FF"); // Azul para resaltar el camino
        r3.setText(camino);
    }
}