package grupo4.com.servidor.servicios;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import grupo4.com.core.manejadores.ManejadorNotificaciones;
import grupo4.com.core.modelJEE.Notis;
import grupo4.com.servidor.rest.seguridad.anotacion.Secured;
import grupo4.com.servidor.servicios.modelos.MetodosToken;
import grupo4.com.util.Constantes;
import grupo4.com.util.Log;

@Path("/notis")
public class NotificacionesREST {

	ManejadorNotificaciones mnotis = new ManejadorNotificaciones();
	
	@Secured
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getNotisTodas")
	public Response getNotisTodas(@HeaderParam(Constantes.HEADER_TOKEN) String token) {
		Log log = null;
		List<Notis> notificaciones = new ArrayList<Notis>();
		try {
			log = new Log("NotificacionesREST.log", true);
			log.log("Comienza recuperacion de todas las notificacones ]");
			String rol	= MetodosToken.getRol(log, token);
			if(rol.equals(Constantes.ADMINISTRADOR_COMUN)||rol.equals(Constantes.ADMINISTRADOR_PRIVILEGIOS)||rol.equals(Constantes.ADMINISTRADOR_SOLO_NODOS) ||rol.equals(Constantes.SUPERVISOR) ) {
				notificaciones = mnotis.getNotisTodas(log);
				return Response.ok(notificaciones).build();	
			}else {
				return Response.ok("No tienes suficiente nivel de acceso para recuperar notificaciones totales").build();	
			}
		} catch (Throwable t) {
			log.log("Error recuperando lista de  notificaciones debido a["+t.getMessage()+"]", t);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			log.log("Termina recuperacion  de notificacionesL :[" + notificaciones.toString() + "]");
			Log.cerrar(log);
		}
	}
	
	@Secured
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getMisNotis")
	public Response getMisNotis(@HeaderParam(Constantes.HEADER_TOKEN) String token) {
		Log log = null;
		List<Notis> notificaciones = new ArrayList<Notis>();
		try {
			log = new Log("NotificacionesREST.log", true);
			log.log("Comienza recuperacion de todas las notificacones ]");
			String usuario = MetodosToken.getUsuario(log, token);
			notificaciones = mnotis.getMisNotis(log, usuario, false);
			return Response.ok(notificaciones).build();	
		} catch (Throwable t) {
			log.log("Error recuperando lista de  notificaciones debido a["+t.getMessage()+"]", t);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			log.log("Termina recuperacion  de notificacionesL :[" + notificaciones.toString() + "]");
			Log.cerrar(log);
		}
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/testNotisAngular")
	public Response testNotisAngular() {
		Log log = null;
		List<Notis> notificaciones = new ArrayList<Notis>();
		try {
			log = new Log("NotificacionesREST.log", true);
			log.log("Comienza recuperacion de todas las notificacones para angular ]");
			
			notificaciones = mnotis.getNotisParaFront(log);
			return Response.ok(notificaciones).build();	
		} catch (Throwable t) {
			log.log("Error recuperando lista de  notificaciones para angular debido a["+t.getMessage()+"]", t);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			log.log("Termina recuperacion  de notificacionesL :[" + notificaciones.toString() + "]");
			Log.cerrar(log);
		}
	}
}
