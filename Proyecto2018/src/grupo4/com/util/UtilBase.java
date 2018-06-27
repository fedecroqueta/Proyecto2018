package grupo4.com.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class UtilBase {
	
	public static final String DATASOURCE	= System.getProperty("database.ds");
	
	/**
	 * Cierra los componentes de las consultas y persistencia
	 * @param log
	 * @param rs
	 * @param st
	 * @param c
	 */
	public static void cerrarComponentes(Log log, ResultSet rs, Statement st, Connection c) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Throwable t) {
			log.log("Error cerrando ResultSet", t);
		}
		try {
			if (st != null) {
				st.close();
			}
		} catch (Throwable t) {
			log.log("Error cerrando Statement", t);
		}
		try {
			if (c != null) {
				c.close();
			}
		} catch (Throwable t) {
			log.log("Error cerrando Connection", t);
		}
		
	}

}
