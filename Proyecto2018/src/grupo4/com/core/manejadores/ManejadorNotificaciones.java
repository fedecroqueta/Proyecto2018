package grupo4.com.core.manejadores;

import java.util.ArrayList;
import java.util.List;

import grupo4.com.core.modelJEE.Notis;
import grupo4.com.servidor.database.BD;
import grupo4.com.util.Log;

public class ManejadorNotificaciones {

	public List<Notis> getNotisTodas(Log log) {
		List<Notis> notisTodas = new ArrayList<Notis>();
		try {
			BD base = new BD();
			notisTodas =  base.getNotisTodas(log);
		}catch(Throwable t) {
			log.log("-->ERROR");
		}
		return notisTodas;
	}

	public List<Notis> getMisNotis(Log log, String usuario) {
		List<Notis> notisSoloMias = new ArrayList<Notis>();
		try {
			BD base = new BD();
			notisSoloMias =  base.getMisNotis(log, usuario);
		}catch(Throwable t) {
			log.log("-->ERROR");
		}
		return notisSoloMias;
	}
	

}
