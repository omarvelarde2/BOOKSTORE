package uni.edu.pe.libreriaback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Tipo {
    private String idtipo;
    private String descripcion;
    private int contador;
}