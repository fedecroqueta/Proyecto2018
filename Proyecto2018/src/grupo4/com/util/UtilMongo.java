package grupo4.com.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import grupo4.com.alertas.Email;

@SuppressWarnings("unused")
public class UtilMongo {

	/**
	 * Retorna cadena de conexion a BD logtek
	 * @return
	 */
	public static String ConMongoLogtek() {
		Properties prop = new Properties();
		InputStream input = null;
		String con = "";
		
		// Cargo archivo properties para cargar parametros de twilio
		try {
			// input = new FileInputStream("config\\configuracion.properties");
			String filename = "configuracion.properties";
			input = Email.class.getClassLoader().getResourceAsStream(filename);
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		 con =  prop.getProperty("CON_STRING_MONGO");
		return con;
	}
}
