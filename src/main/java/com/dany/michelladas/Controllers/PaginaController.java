package com.dany.michelladas.Controllers;
import com.dany.michelladas.Service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class PaginaController {

    private final ProductoService productoService;

    public PaginaController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/")
    public String mostrarHome(Model model) {
        model.addAttribute("productos", productoService.listarProductos());
        return "michelladas"; // este es el nombre del HTML en templates
    }


}

