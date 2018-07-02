package grupo4.com.quartz.procesamiento;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import grupo4.com.alertas.Email;
import grupo4.com.core.manejadores.ManejadorAlertas;
import grupo4.com.core.modelJEE.EventoGConf;
import grupo4.com.core.modelJEE.EventoGIdYFechaInactivo;
import grupo4.com.core.modelJEE.SuscripcionEG;
import grupo4.com.servidor.database.BD;
import grupo4.com.util.Log;

public class ProcesarEliminacionInactivos {

	ManejadorAlertas manejadorAlertas = new ManejadorAlertas();

	public ProcesarEliminacionInactivos() {
		super();

	}

	@SuppressWarnings("unused")
	public void ejecutarProcesoAlertas(Log log) {
		Map<Long, EventoGConf> mapEventosConConfiguracion = null;
		List<EventoGIdYFechaInactivo> eventosInactivos = null;
		List<SuscripcionEG> suscripciones = null;
		try {
			log.log("Se procesan eventos inactivos a eliminar...");
			BD base = new BD();
			
			Properties prop = new Properties();
			InputStream input = null;

			// Cargo archivo properties para cargar parametros 
			try {
				String filename = "configuracion.properties";
				input = Email.class.getClassLoader().getResourceAsStream(filename);
				prop.load(input);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			

			// Recupero suscripciones
			//suscripciones = base.getSuscripcionesEGActivas(log);

			// Recupero lista de eventos inactivos
			eventosInactivos = base.getListaEventosInactivos(log);

			// Recorro lista de eventos inactivos si estan ianctivos por mas tiempo que el
			// configurado, se elimina todo
			// y se avisa por Email al SA que lo creo
			for (int i = 0; i < eventosInactivos.size(); i++) {
				EventoGIdYFechaInactivo eventoInactivo = eventosInactivos.get(i);
				String fechaInactividad = eventoInactivo.getFechaInactividad();
				
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date date = format.parse(fechaInactividad);
				Date  now = new Date();
				//Comparo si la fecha que viene es mayor a la fecha configurada
				if((now.getTime() - date.getTime() > TimeUnit.DAYS.toMillis(Long.parseLong(prop.getProperty("FECHA_MAX_INACTIVIDAD"))))){
					
					//eliminio cofiguraciones de ese evento
					boolean elminioConf = base.eliminarConfEventosInactivos(eventoInactivo.getIdEvento(), log);
					
					boolean eliminoSuscripciones = false;
					if(elminioConf) {
						//elimino suscripciones de ese evento
						eliminoSuscripciones = base.elminarSusEventosInactivos(eventoInactivo.getIdEvento(), log);
	
					}
					if(eliminoSuscripciones) {
						//Recupero mail del usuario creador para avisar
						String mailUsuarioCreador = base.recuperarMailUsuarioCreador(eventoInactivo.getUsuarioCreador(), log);
						
						//Mando  mail avisando al creador
						String cuerpoMail = "Evento["+eventoInactivo.getIdEvento()+"]-["+eventoInactivo.getNombreEvento()+"]-ELIMINADO POR INACTIVIDAD MAYOR A ["+prop.getProperty("FECHA_MAX_INACTIVIDAD")+"] DIAS";
						Email.enviarMail(eventoInactivo.getUsuarioCreador(),mailUsuarioCreador, cuerpoMail);
					
						//Por ultimo elimino el cabezal
						base.eliminarCabezalEventoGlobalInactivo(eventoInactivo.getIdEvento(), log);
					}
				}
			}
			log.log("Finaliza procesamiento eliminacion de inactivos...");
		} catch (Throwable t) {
			log.log("No es posible eliminar inactivos debido a [" + t.getMessage() + "]");
		}
	}
}
