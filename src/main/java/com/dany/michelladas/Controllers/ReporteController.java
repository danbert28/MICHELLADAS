package com.dany.michelladas.Controllers;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.OutputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class ReporteController {



    @Autowired
    private org.springframework.core.io.ResourceLoader resourceLoader;

    @Autowired
    private javax.sql.DataSource dataSource;

    @GetMapping("/reporte/pedidos")
    public void descargarReportePedidos(HttpServletResponse response) throws Exception {
        //public void generarReporte(@RequestParam("puesto") String puesto, HttpServletResponse response) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"reporte_empleados.pdf\"");

        // Carga y compila el archivo .jrxml
        JasperReport report = JasperCompileManager.compileReport(
                resourceLoader.getResource("classpath:/reports/reporte_pedidos.jrxml").getInputStream()
        );

        // Parámetros del reporte (agregar los necesarios según el reporte)
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("REPORT_TITLE", "REPORTE PEDIDOS - MICHELLADAS");
        parameters.put("IMAGE_PATH", "src/main/resources/static/imagenes/Logo.svg"); // Ruta de la imagen
        //parameters.put("PUESTO", puesto); // Parámetro para filtrar por puesto

        //System.out.println("Parámetro recibido: " + puesto);
        //System.out.println("Parámetros enviados al reporte: " + parameters);

        // Obtener conexión desde el DataSource configurado en Spring Boot


        try (Connection connection = dataSource.getConnection()) {
            // Llenar el reporte usando la conexión
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, connection);

            // Exportar a PDF
            OutputStream out = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error al generar el reporte: " + e.getMessage());
        }
    }
}

