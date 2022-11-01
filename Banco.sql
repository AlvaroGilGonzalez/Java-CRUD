DROP DATABASE IF EXISTS banco;
CREATE DATABASE BANCO CHARACTER SET utf8mb4;
USE banco;
CREATE TABLE Clientes(
dni VARCHAR(9) PRIMARY KEY,
nombre VARCHAR(20),
telefono VARCHAR(11),
direccion VARCHAR(50)
);

CREATE TABLE Cuentas(
numCuenta  Integer PRIMARY KEY auto_increment,
dniCliente VARCHAR(9),
saldo decimal,
estado VARCHAR(6)
);

CREATE TABLE Movimientos(
codMovimientos Integer PRIMARY KEY auto_increment,
numCuenta INTEGER,
importe decimal ,
fechaYhora DATETIME ,
tipo VARCHAR(50),
numCuentaTransferencia INTEGER,
concepto VARCHAR(100)
);

