package grupo4.com.servidor.database;

import javax.naming.InitialContext;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import grupo4.com.util.Log;


public class Conexion {

private static Conexion instance;
	
	private InitialContext contexto;
	
	private Conexion() {
		try {
			 this.contexto = new InitialContext();
		} catch (Throwable t) {
			System.err.println("Error creando Conexion");
			t.printStackTrace();
		}
	}
	
	public static Conexion getInstancia() {
		if (instance == null) {
			instance = new Conexion();
		}
		return instance;
	}
	
	//Devuelve la conexion a la bd tomando ds
	public Connection getConexion(Log log, String ds) throws SQLException {
		try {
			DataSource datasource = (DataSource) this.contexto.lookup(ds);
			return datasource.getConnection();	
		} catch (Throwable t) {
			throw new SQLException("No se puede conectar a :["+ds+"] debido a ["+t.getMessage()+"]", t);
		}
	}
	
}
