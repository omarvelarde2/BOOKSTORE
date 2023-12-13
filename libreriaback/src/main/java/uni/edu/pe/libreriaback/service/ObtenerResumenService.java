package uni.edu.pe.libreriaback.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import uni.edu.pe.libreriaback.dto.ReporteResumen;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ObtenerResumenService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private Connection conexion;
    // Se hace uso de los métodos obtener y cerrar conexión además de un extractReport(), todos definidos abajo
    // Se obtiene los pedidos en una lista con un query que suma los importes y hace un SELECT
    public List<ReporteResumen> obtenerResumen() {
        List<ReporteResumen> list = new ArrayList<>();
        String sql = "SELECT\n" +
                "    t.idtipo,\n" +
                "    t.descripcion,\n" +
                "    t.contador AS cantidadTotal,\n" +
                "    SUM(v.total) AS ventasTotal\n" +
                "FROM\n" +
                "    dbo.TIPO t\n" +
                "INNER JOIN\n" +
                "    dbo.PUBLICACION p ON t.idtipo = p.idtipo\n" +
                "INNER JOIN\n" +
                "    dbo.VENTA v ON p.idpublicacion = v.idpublicacion\n" +
                "GROUP BY\n" +
                "    t.idtipo, t.descripcion, t.contador;\n";
        try {
            obtenerConexion();
            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(sql);
            // while(.next) para obtener un reporte mientras haya uno siguiente y luego agregarlo a la lista
            while (resultado.next()){
                list.add(extractReport(resultado));
            }
            cerrarConexion(resultado,sentencia);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }
    // Método para extraer un único reporte de resumen
    private ReporteResumen extractReport(ResultSet resultado) throws SQLException {
        ReporteResumen report = new ReporteResumen(
                resultado.getString("idtipo"),
                resultado.getString("descripcion"),
                resultado.getString("cantidadtotal"),
                resultado.getString("ventastotal")
        );
        return report;
    }
    // Método para obtener conexión con la base de datos usando jdbc template
    private void obtenerConexion(){
        try {
            this.conexion = jdbcTemplate.getDataSource().getConnection();
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }
    // Método para cerrar la conexión con la base de datos
    private void cerrarConexion(ResultSet resultado, Statement sentencia){
        try {
            if(resultado != null) resultado.close();
            if(sentencia != null) sentencia.close();
            this.conexion.commit();
            this.conexion.close();
            this.conexion = null;
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }
}
