package uni.edu.pe.libreriaback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Venta {
    private String cliente;
    private int idempleado;
    private String idpublicacion;
    private int cantidad;
}
