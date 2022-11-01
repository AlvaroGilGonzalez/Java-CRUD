import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * En esta clase se incluyen todos lo metodos los cuales hacen consultas o actualizaciones sobre la tabla Clientes de nuestra base de datos
 * @author Álvaro Gil
 * Fecha:20/05/2022
 */

public class GestionClientes {
	
	/**
	 * Metodo para comprobar si un cliente existe en nuestra base de datos
	 * @param con (Conexion con la base de datos)
	 * @param dni (dni del cliente)
	 * @return (true si el cliente existe - false si el cliente no existe)
	 */
	public static boolean existeCliente(Connection con,String dni) {
		boolean clienteExiste=false;
		String c="SELECT * FROM clientes";
		try {
			Statement s=con.createStatement();
			ResultSet rs=s.executeQuery(c);
			
			while(rs.next()) {
				String dniCli=rs.getString("dni");
				if(dni.equalsIgnoreCase(dniCli)) {
					clienteExiste=true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return clienteExiste;
	}
	
	/**
	 * Este metodo da de alta a un nuevo cliente en nuestra base de datos
	 * @param con (Conexion con la base de datos)
	 * @param dni (dni del cliente)
	 * @param nombre (nombre del cliente)
	 * @param telefono (telefono del cliente)
	 * @param direccion (direccion del cliente)
	 */
	public static void altaCliente(Connection con,String dni,String nombre,String telefono,String direccion) {
		
		if(existeCliente(con,dni)) {
			System.err.println("No se puede crear el cliente ya que el dni esta registrado");
		}else {
			String consulta = "INSERT INTO clientes VALUES(?,?,?,?);";
		
			try {
				PreparedStatement sentencia=con.prepareStatement(consulta);
				sentencia.setString(1,dni);
				sentencia.setString(2,nombre);
				sentencia.setString(3,telefono);
				sentencia.setString(4,direccion);
				
				int actualizacion=sentencia.executeUpdate();
				
				if(actualizacion>0) {
					System.out.println("El cliente se ha añadido con exito\n");
				}
				
			} catch (SQLException e) {
				System.err.println("No se ha podido añadir al cliente\n");
			}
		}
		
	}
	
	/**
	 * Este metodo da de baja a un cliente (se comprueba que exista y no tenga cuentas) de nuestra base de datos
	 * @param con (Conexion con la base de datos)
	 * @param dni (dni del cliente a dar de baja)
	 */
	public static void bajaCliente(Connection con,String dni) {
		
		if(!existeCliente(con,dni)) {
			System.err.println("No se puede dar de baja un cliente que no existe");
		}else {
			
			if(GestionCuenta.ClienteTieneCuentas(con, dni)) {
				System.err.println("No se puede dar de baja al cliente ya que su dni figura como titular de una cuenta");
			}else {
				String consulta="DELETE FROM clientes WHERE dni=?;";
				try {
					PreparedStatement sentencia=con.prepareStatement(consulta);
					sentencia.setString(1,dni);
					int actualizacion=sentencia.executeUpdate();
					
					if(actualizacion>0) {
						System.out.println("El cliente se ha eliminado con exito\n");
					}
				} catch (SQLException e) {
					System.err.println("No se ha podido eliminar al cliente\n");
				}
			}
			
		}
		
	}
	
	/**
	 * 
	 * @param con (Conexion con la base de datos)
	 * @param opcion (numero entero, 1=actualizar nombre, 2=actualizar telefono, 3=actualizar direccion)
	 * @param dni (dni del cliente que debemos actualizar)
	 */
	public static void actualizarCliente(Connection con,int opcion,String dni) {
		Scanner input=new Scanner (System.in);

		switch(opcion) {

			case 1:{
				System.out.println("Introduzca el nuevo nombre del cliente");
				String nombre=input.nextLine();
				String consulta="UPDATE clientes SET nombre=? WHERE dni=?";
				try {
					PreparedStatement sentencia=con.prepareStatement(consulta);
					sentencia.setString(1,nombre);
					sentencia.setString(2, dni);
	
					int actualizacion=sentencia.executeUpdate();
	
					if(actualizacion>0) {
						System.out.println("El nombre del cliente se ha actualizado con extio\n");
					}
	
				} catch (SQLException e) {
					System.err.println("No ha sido posible actualizar el nombre del cliente\n");
				}
				break;
			}
	
			case 2:{
				System.out.println("Introduzca el nuevo telefono del cliente");
				String telefono=input.nextLine();
				String consulta="UPDATE clientes SET telefono=? WHERE dni=?";
				try {
					PreparedStatement sentencia=con.prepareStatement(consulta);
					sentencia.setString(1,telefono);
					sentencia.setString(2, dni);
	
					int actualizacion=sentencia.executeUpdate();
	
					if(actualizacion>0) {
						System.out.println("El numero del cliente se ha actualizado con extio\n");
					}
	
				} catch (SQLException e) {
					System.err.println("No ha sido posible actualizar el numero del cliente\n");
				}
				break;
			}
	
			case 3:{
				System.out.println("Introduzca la nueva direccion del cliente");
				String direccion=input.nextLine();
				String consulta="UPDATE clientes SET direccion=? WHERE dni=?";
				try {
					PreparedStatement sentencia=con.prepareStatement(consulta);
					sentencia.setString(1,direccion);
					sentencia.setString(2, dni);
	
					int actualizacion=sentencia.executeUpdate();
	
					if(actualizacion>0) {
						System.out.println("La direccion del cliente se ha actualizado con extio\n");
					}
	
				} catch (SQLException e) {
					System.err.println("No ha sido posible actualizar la direccion del cliente\n");
				}
				break;
			}
		}	
	}
}
