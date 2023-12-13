package uni.edu.pe.libreriaback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReporteResumen {
    private String idtipo;
    private String descripcion;
    private String cantidadtotal;
    private String ventastotal;
}
