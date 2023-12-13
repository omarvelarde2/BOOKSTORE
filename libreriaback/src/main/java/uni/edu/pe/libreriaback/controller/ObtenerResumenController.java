package uni.edu.pe.libreriaback.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uni.edu.pe.libreriaback.dto.rest.RptaReporteResumen;
import uni.edu.pe.libreriaback.service.ObtenerResumenService;

@RestController
@RequestMapping("/obtenerResumen")
public class ObtenerResumenController {
    @Autowired
    private ObtenerResumenService obtenerResumenService;

    @PostMapping
    public RptaReporteResumen obtenerResumen(){
        RptaReporteResumen rsps = new RptaReporteResumen();
        rsps.setReportes(obtenerResumenService.obtenerResumen());
        return rsps;
    }
}
