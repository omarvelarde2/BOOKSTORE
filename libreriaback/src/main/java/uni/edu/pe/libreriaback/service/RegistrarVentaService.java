package uni.edu.pe.libreriaback.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import uni.edu.pe.libreriaback.dto.StockPublicacion;
import uni.edu.pe.libreriaback.dto.Venta;

import java.sql.*;

@Service
public class RegistrarVentaService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private Connection conexion;
    // Para los items se hizo uso de los métodos para obtener y cerrar conexión con la BD, ambos definidos abajo

    // Item 1 (Se debe validar que el ID de la publicación exista):
    public boolean validarIdpublicacion(String idPublicacion) {
        // Consulta para la existencia
        String sql = "SELECT COUNT(*) AS Existe\n" +
                "FROM dbo.PUBLICACION\n" +
                "WHERE idpublicacion = ?;";
        try {
            obtenerConexion();
            PreparedStatement st = conexion.prepareStatement(sql);
            st.setString(1, idPublicacion);
            ResultSet rs = st.executeQuery();
            // Si el resultado es igual a cero, la publicación no existe
            boolean existe = rs.next() && rs.getInt("Existe") > 0;
            st.close();
            cerrarConexion(rs, st);
            return existe;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Item 2 (Debe calcular el descuento de manera correcta):
    public String calcularDescuento() {
        try {
            obtenerConexion();
            String sql = "UPDATE dbo.venta\n" +
                    "SET precio = (\n" +
                    "        SELECT precio\n" +
                    "        FROM dbo.publicacion\n" +
                    "        WHERE publicacion.idpublicacion = venta.idpublicacion\n" +
                    "    );\n" +
                    "\n" +
                    "UPDATE dbo.venta\n" +
                    "SET dcto = (\n" +
                    "        SELECT porcentaje\n" +
                    "        FROM dbo.promocion\n" +
                    "        WHERE venta.cantidad BETWEEN cantmin AND cantmax\n" +
                    "    ) * precio;";
            Statement st = conexion.createStatement();
            st.executeUpdate(sql);
            cerrarConexion(null, st);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Descuentos de venta actualizados segun las promociones actuales";
    }

    // Item 3 (Debe calcular el SUBTOTAL, IMPUESTO y TOTAL de manera correcta):
    public String calcularVenta() {
        try {
            obtenerConexion();
            String sql = "UPDATE dbo.venta\n" +
                    "SET total = cantidad * (precio - dcto);\n" +
                    "\n" +
                    "UPDATE dbo.venta\n" +
                    "SET subtotal = total / 1.18;\n" +
                    "\n" +
                    "UPDATE dbo.venta\n" +
                    "SET impuesto = total - subtotal;";
            Statement st = conexion.createStatement();
            st.executeUpdate(sql);
            cerrarConexion(null, st);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Valores de SUBTOTAL, IMPUESTO y TOTAL de venta actualizados correctamente";
    }

    // Item 4 (Se debe validar que exista stock suficiente):
    public boolean validarStock(StockPublicacion stockPublicacion){
        // Recibe el idPublicacion y la cantidad de venta (revisar el DTO)
        try {
            obtenerConexion();
            // Consulta para el stock de la publicacion
            String sql = "SELECT stock\n" +
                    "FROM dbo.PUBLICACION\n" +
                    "WHERE idpublicacion = ?;";
            PreparedStatement st = conexion.prepareStatement(sql);
            st.setString(1, stockPublicacion.getIdPublicacion());
            ResultSet rs = st.executeQuery();
            st.close();
            // Si el resultado (stock) es mayor o igual a la cantidad, se valida
            boolean valido = rs.next() && rs.getInt("stock") >= stockPublicacion.getCantidad();
            cerrarConexion(rs, st);
            return valido;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Item 5 (De actualizar el stock de la publicación de manera correcta):
    public String actualizarStock(StockPublicacion stockPublicacion){
        // Nuevamente recibe el idPublicacion y la cantidad de venta (StockPublicacion)
        try {
            obtenerConexion();
            String sql = "UPDATE dbo.PUBLICACION\n" +
                    "SET stock = stock - ?\n" +
                    "WHERE idpublicacion = ?;";
            PreparedStatement st = conexion.prepareStatement(sql);
            st.setInt(1, stockPublicacion.getCantidad());
            st.setString(2, stockPublicacion.getIdPublicacion());

            st.executeUpdate();
            st.close();
            cerrarConexion(null, st);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "Stock de " + stockPublicacion.getIdPublicacion() + " actualizado correctamente";
    }

    // Item 6 (Debe generar el IDVENTA de manera correcta):
    public int generarIdventa(){
        // Primero se inicializa el nuevo id en 0
        int nuevoId = 0;
        // Consulta para el maximo Idventa
        String sql = "SELECT MAX(idventa) AS Maximo \n" +
                "FROM dbo.VENTA;";
        try {
            obtenerConexion();
            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(sql);
            // Se le suma 1 y se almacena como el nuevo id
            if(resultado.next()){
                nuevoId = resultado.getInt("maximo") + 1;
            }
            cerrarConexion(resultado, sentencia);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return nuevoId;
    }

    // Item 7 (Registrar la venta de manera correcta):
    public Venta registrarVenta(Venta venta){
        // Recibe una venta (revisar el DTO)
        try{
            obtenerConexion();
            String sql = "INSERT INTO dbo.VENTA \n" +
                    "    (idventa, cliente, idempleado, fecha, idpublicacion, cantidad, precio, dcto, subtotal, impuesto, total) \n" +
                    "VALUES (?, ?, ?, GETDATE(), ?, ?, 0, 0, 0, 0, 0);";
            PreparedStatement st = conexion.prepareStatement(sql);
            // Se genera un id nuevo para la venta
            st.setInt(1, generarIdventa());
            // El resto se ingresa manualmente
            st.setString(2, venta.getCliente());
            st.setInt(3, venta.getIdempleado());
            st.setString(4, venta.getIdpublicacion());
            st.setInt(5, venta.getCantidad());

            st.executeUpdate();
            st.close();
            cerrarConexion(null, st);
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return venta;
    }

    // Item 8 ():

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
