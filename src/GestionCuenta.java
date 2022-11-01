import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * En esta clase se incluyen todos lo metodos los cuales hacen consultas o actualizaciones sobre la tabla Cuentas de nuestra base de datos
 * @author Álvaro Gil
 * Fecha:20/05/2022
 */

public class GestionCuenta {
	
	/**
	 * Metodo que nos imprime por pantalla el saldo de una cuenta
	 * @param con (Conexion con la base de datos)
	 * @param numCuenta (cuenta de la que deseamos ver el saldo)
	 */
	public static void verSaldoCuenta(Connection con,int numCuenta) {
		String consulta="SELECT saldo FROM cuentas WHERE numCuenta=?";
		try {
			PreparedStatement miStatement=con.prepareStatement(consulta);
			miStatement.setInt(1,numCuenta);
			ResultSet rs=miStatement.executeQuery();
			
			while(rs.next()){
				String saldo=rs.getString("saldo");
				System.out.println("El saldo de su cuenta es de: "+saldo+" €\n");
			}
			
		} catch (SQLException e) {
			System.err.println("No sa sido posible listar las cuentas");
		}
	}
	
	/**
	 * Metodo que da de alta una nueva cuenta
	 * @param con (Conexion con la base de datos)
	 * @param dniTitular (dni del que sera el titular de la cuenta)
	 */
	public static void altaCuenta(Connection con,String dniTitular) {
		if(!GestionClientes.existeCliente(con,dniTitular)) {
			System.err.println("El dni introducido no pertenece a ningun cliente");
		}else {
			String consulta = "INSERT INTO cuentas (dniCliente,saldo,estado) VALUES(?,0,'activa'); ";
		
			try {
				PreparedStatement sentencia=con.prepareStatement(consulta);
				sentencia.setString(1,dniTitular);
				
				int actualizacion=sentencia.executeUpdate();
				
				if(actualizacion>0) {
					System.out.println("La cuenta se ha dado de alta con exito\n");
				}
				
			} catch (SQLException e) {
				System.err.println("No se ha podido dar de alta la cuenta\n");
			}
		}
	}
	
	/**
	 * Metodo que da de baja una nueva cuenta (comprobamos que el cliente sea el titular de la cuenta)
	 * @param con (Conexion con la base de datos)
	 * @param numCuenta (numero de la cuenta a dar de baja)
	 * @param dni (dni del titular de la cuenta)
	 */
	public static void bajaCuenta(Connection con,int numCuenta,String dni) {
		
		if(!clienteTitularCuenta(con,dni,numCuenta)) {
			System.err.println("El cliente no es el titular de la cuenta introducida");
		}else {
			String consulta = "UPDATE cuentas SET estado='baja' WHERE numCuenta=?; ";
		
			try {
				PreparedStatement sentencia=con.prepareStatement(consulta);
				sentencia.setInt(1,numCuenta);
				
				int actualizacion=sentencia.executeUpdate();
				
				if(actualizacion>0) {
					System.out.println("La cuenta se ha dado de baja con exito\n");
				}
				
			} catch (SQLException e) {
				System.err.println("No se ha podido dar de baja la cuenta\n");
			}
		}
		
	}
	
	/**
	 * Metodo para realizar un ingreso en una cuenta
	 * @param con (Conexion con la base de datos)
	 * @param numCuenta (Numero de cuenta a la que realizamos el ingreso)
	 * @param dinero (cantidad de dinero a ingresar)
	 */
	public static void ingresoCuenta(Connection con,int numCuenta,double dinero) {
		String consulta="UPDATE cuentas SET saldo=saldo+? WHERE numCuenta=?;";
		
		try {
			PreparedStatement sentencia=con.prepareStatement(consulta);
			sentencia.setDouble(1,dinero);
			sentencia.setInt(2, numCuenta);
			
			int actualizacion=sentencia.executeUpdate();
			
			if(actualizacion>0) {
				System.out.println("El ingreso se ha realizado con exito\n");
			}
			
		} catch (SQLException e) {
			System.err.println("No se ha podido realizar el ingreso\n");
		}
	}
	
	/**
	 * Metodo para realizar un retirada de dinero en una cuenta
	 * @param con (Conexion con la base de datos)
	 * @param numCuenta (Numero de cuenta a la que se realiza la retirada)
	 * @param dinero (cantidad de dinero a retirar)
	 */
	public static void retiradaCuenta(Connection con,int numCuenta,double dinero) {
		String consulta="UPDATE cuentas SET saldo=saldo-? WHERE numCuenta=?;";
		
		try {
			PreparedStatement sentencia=con.prepareStatement(consulta);
			sentencia.setDouble(1,dinero);
			sentencia.setInt(2, numCuenta);
			
			int actualizacion=sentencia.executeUpdate();
			
			if(actualizacion>0) {
				System.out.println("La retirada se ha realizado con exito\n");
			}
			
		} catch (SQLException e) {
			System.err.println("No se ha podido realizar la retirada\n");
		}
	}
	
	/**
	 * Metodo para realizar una transferencia entre dos cuentas
	 * @param con (Conexion con la base de datos)
	 * @param cuentaHace (Cuenta que hace la transferencia)
	 * @param cuentaRecibe (Cuenta que recibe la transferencia)
	 * @param cantidad (Cantidad de dinero a transferir)
	 */
	public static void realizarTransferencia(Connection con,int cuentaHace,int cuentaRecibe,double cantidad) {
		
		if(!existeCuenta(con,cuentaRecibe)) {
			System.err.println("No se puede realizar una transferencia a una cuenta que no existe");
		}else {
			String consulta="UPDATE cuentas SET saldo=saldo-? WHERE numCuenta=?";
			String consulta2="UPDATE cuentas SET saldo=saldo+? WHERE numCuenta=?";
		
			try {
				PreparedStatement sentencia=con.prepareStatement(consulta);
				sentencia.setDouble(1,cantidad);
				sentencia.setInt(2, cuentaHace);
				int actualizacion1=sentencia.executeUpdate();
				
				PreparedStatement sentencia2=con.prepareStatement(consulta2);
				sentencia2.setDouble(1,cantidad);
				sentencia2.setInt(2, cuentaRecibe);
				int actualizacion2=sentencia2.executeUpdate();
				
				if(actualizacion1>0 && actualizacion2>0) {
					System.out.println("La transferencia se ha realizado con exito\n");
				}
				
			} catch (SQLException e) {
				System.err.println("No se ha podido realizar la transferencia\n");
			}
		}
		
	}
	
	/**
	 * Metodo para comprobar si una cuenta existe
	 * @param con (Conexion con la base de datos)
	 * @param cuenta(numero de la cuenta que comprobaremos si existe)
	 * @return (true si la cuenta existe, false si la cuenta no existe)
	 */
	public static boolean existeCuenta(Connection con,int cuenta) {
		boolean cuentaExiste=false;
		String c="SELECT * FROM cuentas";
		try {
			Statement s=con.createStatement();
			ResultSet rs=s.executeQuery(c);
			
			while(rs.next()) {
				int numCuenta=rs.getInt("numCuenta");
				if(numCuenta==cuenta) {
					cuentaExiste=true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cuentaExiste;
	}
	
	/**
	 * Metodo que comprueba si el cliente es el titular de la cuenta introducida por parametro
	 * @param con (Conexion con la base de datos)
	 * @param dni (dni del cliente)
	 * @param cuenta (numero de cuenta)
	 * @return (true si el cliente es el titular de la cuenta, false si el cliente no es el titular de la cuenta)
	 */
	public static boolean clienteTitularCuenta(Connection con,String dni,int cuenta) {
		boolean tieneCuenta=false;
		String c="SELECT * FROM cuentas WHERE dniCliente=? AND numCuenta=?";
		try {
			PreparedStatement s=con.prepareStatement(c);
			s.setString(1,dni);
			s.setInt(2, cuenta);
			ResultSet rs=s.executeQuery();
			
			while(rs.next()) {
				int numCuenta=rs.getInt("numCuenta");
				if(numCuenta==cuenta) {
					tieneCuenta=true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tieneCuenta;
	}
	
	/**
	 * Metodo que comprueba si el cliente es titular de al menos una cuenta almacenada en nuestra base de datos
	 * @param con (Conexion con la base de datos)
	 * @param dni (dni del cliente)
	 * @return (true si el cliente es el titular de alguna cuneta, false si no es titular de ninguna cuenta)
	 */
	public static boolean ClienteTieneCuentas(Connection con,String dni) {
		boolean tieneCuenta=false;
		String consulta="SELECT * FROM cuentas WHERE estado='activa'";
		try {
			PreparedStatement sentencia=con.prepareStatement(consulta);
			ResultSet rs=sentencia.executeQuery();
			
			while(rs.next()) {
				String dniCli=rs.getString("dniCliente");
				
				if(dni.equalsIgnoreCase(dniCli)) {
					tieneCuenta=true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tieneCuenta;
	}
	
	/**
	 * Metodo que nos imprime por pantalla una lista con todas las cuentas almacenadas en nuestras bases de datos y sus titulares
	 * @param con (Conexion con la base de datos)
	 */
	public static void listarCuentas(Connection con) {
		String consulta="SELECT * FROM cuentas WHERE estado='activa'";
		try {
			PreparedStatement miStatement=con.prepareStatement(consulta);
			ResultSet rs=miStatement.executeQuery();
			
			System.out.println("numCuenta----------------------dniTitular");
			while(rs.next()){
				String numCuenta=rs.getString("numCuenta");
				String dniTitular=rs.getString("dniCliente");
				
				System.out.println(numCuenta+"------------------------------"+dniTitular);
			}
			
		} catch (SQLException e) {
			System.err.println("No sa sido posible listar las cuentas");
		}
	}
	
	/**
	 * Metodo que lista todas las cuentas de la base de datos menos aquella que se ha introducido por parametro)
	 * @param con
	 * @param cuenta
	 */
	public static void listarCuentasNoCliente(Connection con,int cuenta) {
		String consulta="SELECT * FROM cuentas WHERE estado='activa' AND numCuenta!=?";
		try {
			PreparedStatement miStatement=con.prepareStatement(consulta);
			miStatement.setInt(1,cuenta);
			ResultSet rs=miStatement.executeQuery();
			
			System.out.println("numCuenta----------------------saldo");
			while(rs.next()){
				String numCuenta=rs.getString("numCuenta");
				Double saldo=rs.getDouble("saldo");
				
				System.out.println(numCuenta+"------------------------------"+saldo);
			}
			
		} catch (SQLException e) {
			System.err.println("No sa sido posible listar las cuentas");
		}
	}
	
	/**
	 * Metodo que lista todas las cuentas de las cuales un cliente (introducido por parametro) es titular
	 * @param con (Conexion con la base de datos)
	 * @param dni (dni del clinete)
	 */
	public static void listarCuentasCliente(Connection con,String dni) {
		if(!GestionClientes.existeCliente(con, dni)) {
			System.err.println("El dni no se encuentra asociado a ningun cliente");
		}else {
			String consulta="SELECT * FROM cuentas WHERE estado='activa' AND dniCliente=?";
			try {
				PreparedStatement miStatement=con.prepareStatement(consulta);
				miStatement.setString(1, dni);
				ResultSet rs=miStatement.executeQuery();
				
				System.out.println("numCuenta----------------------saldo");
				while(rs.next()){
					String numCuenta=rs.getString("numCuenta");
					Double saldo=rs.getDouble("saldo");
					
					System.out.println(numCuenta+"------------------------------"+saldo);
				}
				
			} catch (SQLException e) {
				System.err.println("No sa sido posible listar las cuentas");
			}
		}
		
	}
}
