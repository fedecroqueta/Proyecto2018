package grupo4.com.quartz.jbos;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import grupo4.com.alertas.Email;
import grupo4.com.quartz.ControlarProcesamiento;
import grupo4.com.quartz.procesamiento.ProcesarAlertas;
import grupo4.com.util.Log;

public class JobsAlertasEventosGlobales implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Log log = new Log("JobsAlertasEventosGlobales.log", true);;
		Properties prop = new Properties();
		InputStream input = null;

		// Cargo archivo properties para cargar parametros de twilio
		try {
			// input = new FileInputStream("config\\configuracion.properties");
			String filename = "configuracion.properties";
			input = Email.class.getClassLoader().getResourceAsStream(filename);
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		String demonioActivado = prop.getProperty("DEMONIO_PRENDIDO");
		if (ControlarProcesamiento.getInstance().estaProcesando() || demonioActivado.equals("N")) {
			log.log("No procesa. Esta actualmente procesando o el demonio se encuentra apagado");
			Log.cerrar(log);
			return;
		}
		
		try {
			log.log("Inicia ejecucion [JobsAlertasEventosGlobales]...");
			ControlarProcesamiento.getInstance().comienzaProcesamiento();
			long ini = System.currentTimeMillis();
			ProcesarAlertas procesarAlertas = new ProcesarAlertas();
			procesarAlertas.ejecutarProcesoAlertas(log);
			long fin = System.currentTimeMillis();
			long demora = fin - ini;
			log.log("Finaliza ejecucion [JobsAlertasEventosGlobales]. Demora ["+demora+"]");
		} finally {
			ControlarProcesamiento.getInstance().finalizaProcesamiento();
			Log.cerrar(log);
		}
	}
	
}
