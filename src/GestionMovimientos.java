import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * En esta clase se incluyen todos lo metodos los cuales hacen consultas o actualizaciones sobre la tabla Movimientos de nuestra base de datos
 * @author Álvaro Gil
 * Fecha:20/05/2022
 */
public class GestionMovimientos {
	
	/**
	 * Este metodo lista todos los movimientos de una cuenta entre dos fecha pasadas por parametro
	 * @param con (Conexion con la base de datos)
	 * @param numCuenta (Cuenta desde la que debemos ver los movimientos)
	 * @param fechaDesde (Fecha desde la que deseamos ver movimientos)
	 * @param fechaHasta (Fecha hasta la que deseamos ver movimientos)
	 */
	public static void listarMovimientos(Connection con,int numCuenta,String fechaDesde,String fechaHasta) {
		String consulta="SELECT * FROM movimientos WHERE numCuenta=? OR numCuentaTransferencia=? AND DATE(fechaYhora) BETWEEN ? AND ? ";
		try {
			PreparedStatement sentencia=con.prepareStatement(consulta);
			sentencia.setInt(1, numCuenta);
			sentencia.setInt(2, numCuenta);
			sentencia.setString(3, fechaDesde);
			sentencia.setString(4, fechaHasta);
			ResultSet rs=sentencia.executeQuery();
			while(rs.next()) {
				int codMovimiento=rs.getInt("codMovimiento");
				numCuenta=rs.getInt("numCuenta");
				double importe=rs.getDouble("importe");
				String fechaYhora=rs.getString("fechaYhora");
				String tipo=rs.getString("tipo");
				int cuentaTransferencia=rs.getInt("numCuentaTransferencia");
				String concepto=rs.getString("concepto");
				System.out.println();
				System.out.println("codMovimiento:"+codMovimiento+" numCuenta:"+numCuenta+" importe:"+importe+" fechaYhora:"+fechaYhora+" tipo:"+tipo+" cuentaTransferencia:"+cuentaTransferencia+" concepto:"+concepto);
			}
			System.out.println();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Este metodo introduce un nuevo movimiento (de tipo ingreso) en la tabla movimientos
	 * @param con (Conexion a la base de datos)
	 * @param numCuenta (Cuenta en la que se ha realizado el ingreso)
	 * @param importe (cantidad que se ha ingresado)
	 * @param concepto (concepto asociado al movimiento)
	 */
	public static void annadirIngreso(Connection con,int numCuenta,double importe,String concepto) {
		//Para la fecha usar CURRENT_TIMESTAMP()
		//Cambiar los campos decimal por double (saldo en cuentas) (importe en movimientos)
		String consulta="INSERT INTO movimientos (numCuenta,importe,fechaYhora,tipo,concepto) VALUES (?,?,CURRENT_TIMESTAMP(),'Ingreso',?)";
		try {
			PreparedStatement sentencia=con.prepareStatement(consulta);
			sentencia.setInt(1, numCuenta);
			sentencia.setDouble(2, importe);
			sentencia.setString(3, concepto);
			
			sentencia.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Este metodo introduce un nuevo movimiento (de tipo retirada) en la tabla movimientos
	 * @param con (Conexion a la base de datos)
	 * @param numCuenta (Cuenta en la que se ha realizado la retirada)
	 * @param importe (cantidad que se ha retirado)
	 * @param concepto (concepto asociado al movimiento)
	 */
	public static void annadirRetirada(Connection con,int numCuenta,double importe,String concepto) {
		String consulta="INSERT INTO movimientos (numCuenta,importe,fechaYhora,tipo,concepto) VALUES (?,?,CURRENT_TIMESTAMP(),'retirada',?)";
		try {
			PreparedStatement sentencia=con.prepareStatement(consulta);
			sentencia.setInt(1, numCuenta);
			sentencia.setDouble(2, importe);
			sentencia.setString(3, concepto);
			
			int insercion=sentencia.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  Este metodo introduce un nuevo movimiento (de tipo transferencia) en la tabla movimientos
	 * @param con (Conexion a la base de datos)
	 * @param cuentaHace (Cuenta que realiza la transferencia)
	 * @param cuentaRecibe (Cuneta que recibe la transferencia)
	 * @param importe (importe de la transferencia)
	 * @param concepto (concepto asociado al movimiento)
	 */
	public static void annadirTransferencia(Connection con,int cuentaHace,int cuentaRecibe,double importe,String concepto) {
		String consulta="INSERT INTO movimientos (numCuenta,importe,fechaYhora,tipo,numCuentaTransferencia,concepto) VALUES (?,?,CURRENT_TIMESTAMP(),'transferencia enviada',?,?)";
		String consulta2="INSERT INTO movimientos (numCuenta,importe,fechaYhora,tipo,numCuentaTransferencia,concepto) VALUES (?,?,CURRENT_TIMESTAMP(),'transferencia recibida',?,?)";
		
		try {
			PreparedStatement sentencia=con.prepareStatement(consulta);
			sentencia.setInt(1, cuentaHace);
			sentencia.setDouble(2, importe);
			sentencia.setInt(3, cuentaRecibe);
			sentencia.setString(4, concepto);
			sentencia.executeUpdate();
			
			PreparedStatement sentencia2=con.prepareStatement(consulta2);
			sentencia2.setInt(1, cuentaRecibe);
			sentencia2.setDouble(2, importe);
			sentencia2.setInt(3, cuentaHace);
			sentencia2.setString(4, concepto);
			sentencia2.executeUpdate();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
