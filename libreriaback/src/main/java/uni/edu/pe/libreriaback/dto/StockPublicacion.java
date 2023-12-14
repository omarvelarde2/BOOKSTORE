package uni.edu.pe.libreriaback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockPublicacion {
    private String idpublicacion;
    private int cantidad;
}
