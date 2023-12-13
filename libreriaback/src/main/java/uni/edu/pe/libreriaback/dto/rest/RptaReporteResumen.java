package uni.edu.pe.libreriaback.dto.rest;

import lombok.Data;
import uni.edu.pe.libreriaback.dto.ReporteResumen;

import java.util.List;

@Data
public class RptaReporteResumen {
    private List<ReporteResumen> reportes;
}
