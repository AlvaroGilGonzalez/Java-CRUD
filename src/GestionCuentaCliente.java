import java.sql.Connection;
import java.util.Scanner;


/**
 * En esta clase se incluye un metodo el cual contiene un menu con todas las operaciones disponibles para la cuenta de un cliente
 * @author Álvaro Gil
 * Fecha:20/05/2022
 */
public class GestionCuentaCliente {
	
	/**
	 * Este metodo lista todas las operaciones disponibles para una cuenta, ademas permite al cliente elegir una opcion y ejecutar la opcion correspondiente
	 * @param con (Conexion con la base de datos)
	 * @param numCuenta (numero de cuenta con la cual realizaremos las operaciones)
	 */
	public static void opcionesOperacionesCuenta(Connection con,int numCuenta){
		int opcionOperacion,opcionConcepto,numCuentaRecibeTransferencia;
		double dinero;
		String concepto,fechaDesde,fechaHasta;
		Scanner input=new Scanner (System.in);
		do {
			System.out.println("Usted puede realizar las siguientes operaciones y consultas:");
			System.out.println("1.Ver saldo de la cuenta");
			System.out.println("2.Realizar un ingreso en cuenta");
			System.out.println("3.Realizar una retirada de saldo");
			System.out.println("4.Realizar una transferencia");
			System.out.println("5.Listar Movimientos (Entre dos fechas)");
			System.out.println("6.Salir del menu de operaciones y consultas");
			System.out.println("Introduzca la operacion que desea realizar");
			opcionOperacion = input.nextInt();
			
			switch(opcionOperacion) {
			
				//Ver saldo de la cuenta
				case 1:{
					GestionCuenta.verSaldoCuenta(con, numCuenta);
					break;
				}
				
				//Realizar un ingreso en cuenta
				case 2:{
					System.out.println("Introduzca la cantidad de dinero a ingresar");
					dinero=input.nextDouble();
					
					do {
						System.out.println("¿Desea introducir un concepto? (1 para SI y 0 para NO");
						opcionConcepto=input.nextInt();
					}while(opcionConcepto!=0 && opcionConcepto!=1);
					
					if(opcionConcepto==1) {
						System.out.println("Introduzca el concepto");
						input.nextLine();
						concepto=input.nextLine();
					}else {
						concepto=null;
					}
					
					GestionCuenta.ingresoCuenta(con, numCuenta, dinero);
					GestionMovimientos.annadirIngreso(con, numCuenta, dinero,concepto);
					break;
				}
				
				//Realizar una retirada de saldo
				case 3:{
					System.out.println("Introduzca la cantidad de dinero a retirar");
					dinero=input.nextDouble();
					
					do {
						System.out.println("¿Desea introducir un concepto? (1 para SI y 0 para NO");
						opcionConcepto=input.nextInt();
					}while(opcionConcepto!=0 && opcionConcepto!=1);
					
					if(opcionConcepto==1) {
						System.out.println("Introduzca el concepto");
						input.nextLine();
						concepto=input.nextLine();
					}else {
						concepto=null;
					}
					
					GestionCuenta.retiradaCuenta(con, numCuenta, dinero);
					GestionMovimientos.annadirRetirada(con,numCuenta,dinero,concepto);
					break;
				}
				
				//Realizar una transferencia
				case 4:{
					GestionCuenta.listarCuentasNoCliente(con,numCuenta);
					System.out.println("Introduzca la cuenta a la cual se realizará la transferencia");
					numCuentaRecibeTransferencia=input.nextInt();
					System.out.println("Introduzca la cantidad de dinero a transferir");
					dinero=input.nextDouble();
					
					do {
						System.out.println("¿Desea introducir un concepto? (1 para SI y 0 para NO");
						opcionConcepto=input.nextInt();
					}while(opcionConcepto!=0 && opcionConcepto!=1);
					
					if(opcionConcepto==1) {
						System.out.println("Introduzca el concepto");
						input.nextLine();
						concepto=input.nextLine();
					}else {
						concepto=null;
					}
					
					GestionCuenta.realizarTransferencia(con, numCuenta, numCuentaRecibeTransferencia, dinero);
					GestionMovimientos.annadirTransferencia(con, numCuenta, numCuentaRecibeTransferencia, dinero, concepto);
					break;
				}
				
				//Listar movimientos de la cuenta entre dos fechas
				case 5:{
					System.out.println("Introduzca la fecha DESDE la que quiere ver movimientos");
					input.nextLine();
					fechaDesde=input.nextLine();
					System.out.println("Introduzca la fecha HASTA la que quiere ver movimientos");
					fechaHasta=input.nextLine();
					GestionMovimientos.listarMovimientos(con,numCuenta,fechaDesde,fechaHasta);
					break;
				}
				
				//Volver al menu principal
				case 6:{
					System.out.println("Volvemos al menu principal\n");
					break;
				}
			}
		} while (opcionOperacion!=6);
		
	}
}
