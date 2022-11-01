import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Clase principal del programa, en ella encontramos la funcion main y un metodo para conectarse a la base de datos
 * @author Álvaro Gil
 * Fecha: 20/02/2022
 */

public class Main {
	
	/**
	 * Metodo que se conecta a la base de datos con la cual trabajamos en este programa
	 * @return Una conexion con la base de datos
	 */
	public static Connection conectarBD() {
		String ruta="jdbc:mysql://localhost:3306/banco";
		String usuario="root";
		String contrasena="usuario";
		
		try {
			Connection miConexion=DriverManager.getConnection(ruta,usuario,contrasena);
			System.out.println("La conexion a la base de datos se ha realizado con exito\n");
			return miConexion;
		} catch (SQLException e) {
			System.err.println("No ha sido posible conectarse a la base de datos\n");
			return null;
		}
		
	}
	
	/**
	 * Metodo principal de este programa (Contiene el menu principal con el cual operara el usuario)
	 * @param args
	 */
	public static void main (String args[]) {
		Scanner input=new Scanner (System.in);
		
		//Variables para trabajar con opciones de los menus y pedir numeros de cuenta
		int opcionMenu=0,opcionActualizar,numCuenta;
		
		//Variables para trabajar con datos de clientes
		String dni,nombre,telefono,direccion;
		
		Connection conectarBD=conectarBD();
		
		do {
			try {
				System.out.println("OPCIONES PARA CLIENTES");
				System.out.println("1.Dar de alta a un cliente");
				System.out.println("2.Dar de baja a un cliente (Solo es posible si no tiene cuentas corrientes)");
				System.out.println("3.Modificar dato del cliente. (El dni no es posible)\n");
				System.out.println("OPCIONES PARA CUENTAS CORRIENTES");
				System.out.println("4.Dar de alta una cuenta");
				System.out.println("5.Dar de baja una cuenta\n");
				System.out.println("OPCIONES PARA CUENTAS DE CLIENTES");
				System.out.println("6.Identificarse y elegir cuenta\n");
				System.out.println("7.Terminar Programa\n");
				System.out.println("Indique la operacion que desea realizar");
				opcionMenu=input.nextInt();
			} catch (InputMismatchException e) {
				System.err.println("Introduzca una opcion valida");
				input.nextLine();
				opcionMenu=0;
			}
			
			switch(opcionMenu) {
				
				//Dar de alta a un cliente
				case 1:{
					try {
						System.out.println("Introduzca el dni del cliente:");
						input.nextLine();
						dni=input.nextLine();
						System.out.println("Introduzca su nombre");
						nombre=input.nextLine();
						System.out.println("Introduzca su telefono");
						telefono=input.nextLine();
						System.out.println("Introduzca su direccion");
						direccion=input.nextLine();
						GestionClientes.altaCliente(conectarBD,dni,nombre,telefono,direccion);
					} catch (InputMismatchException e) {
						System.err.println("Introduzca valores validos");
						input.nextLine();
					}
					break;
				}
				
				//Dar de baja a un cliente
				case 2:{
					try {
						System.out.println("Introduzca el dni del cliente que desea eliminar");
						input.nextLine();
						dni=input.nextLine();
						GestionClientes.bajaCliente(conectarBD, dni);
					} catch (InputMismatchException e) {
						System.err.println("Introduzca valores validos");
						input.nextLine();
					}
					break;
				}
				
				//Modificar datos de un cliente
				case 3:{
					try {
						System.out.println("Introduzca el dni del cliente que desea modificar");
						input.nextLine();
						dni=input.nextLine();
						
						if(!GestionClientes.existeCliente(conectarBD, dni)) {
							System.err.println("No podemos actualizar un cliente que no existe");
						}else {
							do {
							System.out.println("Introduzca el numero del dato que desea cambiar");
							System.out.println("1.Nombre----------2.Telefono----------3.Direccion");
							opcionActualizar=input.nextInt();
							
							GestionClientes.actualizarCliente(conectarBD, opcionActualizar, dni);
							} while(opcionActualizar<=0 ||opcionActualizar>3);
						}
					} catch (InputMismatchException e) {
						System.err.println("Introduzca valores validos");
						input.nextLine();
					}
					break;
				}
				
				//Dar de alta una cuenta
				case 4:{
					try {
						System.out.println("Introduzca el dni del titular de la cuenta");
						input.nextLine();
						dni=input.nextLine();
						GestionCuenta.altaCuenta(conectarBD, dni);
					} catch (InputMismatchException e) {
						System.err.println("Introduzca valores validos");
						input.nextLine();
					}
					break;
				}
				
				//Dar de baja una cuenta
				case 5:{
					try {
						System.out.println("Introduzca su dni");
						input.nextLine();
						dni=input.nextLine();
						if(!GestionClientes.existeCliente(conectarBD, dni)) {
							System.err.println("El dni no figura en la base de datos");
						}else {
							GestionCuenta.listarCuentasCliente(conectarBD,dni);
							System.out.println("Introduzca el numero de cuenta que desea dar de baja:");
							numCuenta=input.nextInt();
							GestionCuenta.bajaCuenta(conectarBD, numCuenta,dni);
						}
						
					} catch (InputMismatchException e) {
						System.err.println("Introduzca valores validos");
						input.nextLine();
					}
					
					break;
				}
				
				//Identificarse y elegir cuenta
				case 6:{
					try {
						System.out.println("Introduzca su dni:");
						input.nextLine();
						dni=input.nextLine();
						
						if(!GestionClientes.existeCliente(conectarBD, dni)) {
							System.err.println("El dni no figura en la base de datos");
						}else {
							
							if(!GestionCuenta.ClienteTieneCuentas(conectarBD, dni)) {
								System.err.println("Usted no es el titular de ninguna cuenta");
							}else {
								System.out.println("Usted es titular de las siguientes cuentas:");
								GestionCuenta.listarCuentasCliente(conectarBD,dni);
							
								System.out.println("Elija el numero de cuenta con la que desea realizar alguna operacion o consulta");
								numCuenta=input.nextInt();
								
								if(!GestionCuenta.clienteTitularCuenta(conectarBD, dni,numCuenta)) {
									System.err.println("Cuenta introducida no valida");
								}else {
									GestionCuentaCliente.opcionesOperacionesCuenta(conectarBD, numCuenta);
								}
							
							}
							
						}
					} catch (InputMismatchException e) {
						System.err.println("Introduzca valores validos");
						input.nextLine();
					}
					break;
				}
				
				//Terminar programa
				case 7:{
					System.out.println("El programa ha terminado");
					try {
						conectarBD.close();
					} catch (SQLException e) {
						System.err.println("La conexion no se ha podido cerrar con extio");
					}
					break;
				}
				
			}
			
		}while(opcionMenu!=7);
	}
}
