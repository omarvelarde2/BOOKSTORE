package uni.edu.pe.libreriaback.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uni.edu.pe.libreriaback.dto.StockPublicacion;
import uni.edu.pe.libreriaback.dto.Venta;
import uni.edu.pe.libreriaback.service.RegistrarVentaService;

@RestController
@RequestMapping("/registrarVenta")
public class RegistrarVentaController {
    @Autowired
    private RegistrarVentaService registrarVentaService;

    @PostMapping("/validarIdpublicacion")
    public boolean validarIdpublicacion(@RequestBody String idPublicacion) {
        return registrarVentaService.validarIdpublicacion(idPublicacion);
    }
    @PostMapping("/calcularDescuento")
    public String obtenerDescuento() {
        return registrarVentaService.calcularDescuento();
    }
    @PostMapping("/calcularVenta")
    public String calcularVenta() {
        return registrarVentaService.calcularVenta();
    }
    @PostMapping("/validarStock")
    public boolean validarStock(@RequestBody StockPublicacion stockPublicacion) {
        return registrarVentaService.validarStock(stockPublicacion);
    }

    @PostMapping("/actualizarStock")
    public String actualizarStock(@RequestBody StockPublicacion stockPublicacion) {
        return registrarVentaService.actualizarStock(stockPublicacion);
    }

    @PostMapping("/generarIdventa")
    public int generarIdventa() {
        return registrarVentaService.generarIdventa();
    }

    @PostMapping
    public Venta registrarVenta(@RequestBody Venta venta) {
        return registrarVentaService.registrarVenta(venta);
    }

    @PostMapping("/gestionarTransaccion")
    public String gestionarTransaccion() {
        return registrarVentaService.gestionarTransaccion();
    }
}
