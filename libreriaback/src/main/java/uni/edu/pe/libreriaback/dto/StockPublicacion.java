package uni.edu.pe.libreriaback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockPublicacion {
    private String idPublicacion;
    private int cantidad;
}
