package grupo4.com.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilFormato {
	
	public static String remplazarEspacios(String conEspacios) {
		String sinEspacios = conEspacios.replaceAll("\\s+","%20");
		return sinEspacios;
	}

	public static String mongoTimeToDate(String epoch) {
		long epochTime = Long.parseLong(epoch);
		Date expiry = new Date(epochTime * 1000);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fechaAPoner = sdf.format(expiry);
		return fechaAPoner;
	}
}
