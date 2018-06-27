package grupo4.com.util;

public class UtilFormato {
	
	public static String remplazarEspacios(String conEspacios) {
		String sinEspacios = conEspacios.replaceAll("\\s+","%20");
		return sinEspacios;
	}

}
