package grupo4.com.quartz;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import grupo4.com.quartz.jbos.JobsAlertasEventosGlobales;
import grupo4.com.quartz.jbos.JobsEliminarEventosGlobalesInactivos;
import grupo4.com.util.Log;

@SuppressWarnings("unused")
public class Schedule extends HttpServlet implements ServletContextListener {

	private static final long serialVersionUID = 1L;
	private static Scheduler scheduler = null;
	
	
	//Se inicia con el wildfly
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		Log log = null;		
		try {
			log = new Log("schedule_quartz.log", true);
			log.log("Inicia schedule de jobs...");
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
			
			//Demonio cada 30 segundos
			JobDetail job = JobBuilder.newJob(JobsAlertasEventosGlobales.class).withIdentity("DemonioAlertas", "Alertas").build();
			
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity("triggerDemonio", "Alertas")
					.withSchedule(CronScheduleBuilder.cronSchedule("0/59 0/1 * 1/1 * ? *")).build();
			
			//Demonio cada 1 dia para borrar eventos inactivos por mas de 50 dias
			JobDetail jobEliminarInactivos = JobBuilder.newJob(JobsEliminarEventosGlobalesInactivos.class).withIdentity("DemonioBorrarInactivos", "BorrarInactivos").build();
			
			Trigger triggerEliminar = TriggerBuilder.newTrigger().withIdentity("triggerDemonioEliminar",  "BorrarInactivos")
					//.withSchedule(CronScheduleBuilder.cronSchedule("0 0 1 1/1 * ? *")).build();
					.withSchedule(CronScheduleBuilder.cronSchedule("0/59 0/1 * 1/1 * ? *")).build();
			
			scheduler.scheduleJob(job, trigger);
			scheduler.scheduleJob(jobEliminarInactivos,triggerEliminar );
			
			log.log("Se agenda job:[JobAlertasEventosGlobales]");
		} catch (Throwable t) {
			log.log("Error al ejecutar job debido a ["+t.getMessage()+"]", t);
		} finally {
			Log.cerrar(log);
		}
	}
	
	/**
	 * Este metodo se encarga de implementar codigo cuando TERMINA el wildfly
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		Log log = null;
		try {
			log = new Log("schedule_jobs.log", true);
			log.log("Se detiene scheduler de jobs");
			scheduler.shutdown();
		} catch(Throwable t) {
			log.log("Error cerrando scheduler", t);
		} finally {
			Log.cerrar(log);
		}
	}
}
