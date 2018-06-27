package grupo4.com.servidor.servicios;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import grupo4.com.core.manejadores.ManejadorEventos;
import grupo4.com.core.modelJEE.EventoGCabezal;
import grupo4.com.core.modelJEE.EventoGConf;
import grupo4.com.core.modelJEE.NivelesAlertasEG;
import grupo4.com.core.modelJEE.TipoEventos;
import grupo4.com.servidor.rest.seguridad.anotacion.Secured;
import grupo4.com.servidor.servicios.modelos.MetodosToken;
import grupo4.com.util.Constantes;
import grupo4.com.util.Log;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@SuppressWarnings("unused")
@Path("/eventos")
public class EventosREST {

	ManejadorEventos meve = new ManejadorEventos();
	
	@Secured
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/cancela_sus_evento_global/{idEvento}")
	public Response cancelar_sus_EG(@HeaderParam(Constantes.HEADER_TOKEN) String token, @PathParam("idEvento") long idEvento) {
		Log log = null;
		String usuario	= null; 
		boolean resultado = false;
		try {
			log = new Log("EventosREST.log", true);
			Claims claims 	= Jwts.parser().setSigningKey(Constantes.KEY.getBytes("UTF-8")).parseClaimsJws(token).getBody();
			usuario = (String) claims.get(Constantes.TOKEN_USER);
			log.log("Comienza cancelacion suscripcion a evento global de id ["+idEvento+" ] para el usaurio ["+usuario+"]");
		
			resultado = meve.cancelarSusEG(log, idEvento, usuario);
			return Response.ok(resultado).build();
		}catch(Throwable t) {
			log.log("Error recuperando conf  de  EVENTO GLOBAL["+idEvento+"] debido a["+t.getMessage()+"]", t);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}finally {
			log.log("Termina recuperacion  de configuracion del  EVENTO GLOBAL :[" + idEvento + "]");
			Log.cerrar(log);
		}
	}
	
	@Secured
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/modificarConfEG/{idEvento}/{tipo}/{nuevoNivel}/{nuevaAlerta}")
	public Response modificarConfEG(@HeaderParam(Constantes.HEADER_TOKEN) String token, @PathParam("idEvento") long idEvento,  @PathParam("tipo") int tipo,
			 @PathParam("nuevoNivel") int nuevoNivel, @PathParam("nuevaAlerta") String nuevaAlerta ) {
		Log log = null;
		String usuario	= null;
		String rol 		= null;
		boolean resultado = false;
		try {
			log = new Log("EventosREST.log", true);
			Claims claims 	= Jwts.parser().setSigningKey(Constantes.KEY.getBytes("UTF-8")).parseClaimsJws(token).getBody();
			usuario = (String) claims.get(Constantes.TOKEN_USER);
			rol 	= (String) claims.get(Constantes.ROL);
			log.log("Comienza modificacion de EG de id ["+idEvento+" ] para el usaurio ["+usuario+"]");
		
			resultado = meve.modificarEG(log, idEvento, tipo, nuevoNivel, nuevaAlerta, rol, usuario);
			return Response.ok(resultado).build();
		}catch(Throwable t) {
			log.log("Error recuperando conf  de  EVENTO GLOBAL["+idEvento+"] debido a["+t.getMessage()+"]", t);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}finally {
			log.log("Termina recuperacion  de configuracion del  EVENTO GLOBAL :[" + idEvento + "]");
			Log.cerrar(log);
		}
	}
	
	@Secured
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/deleteConfEG/{idEvento}/{tipo}")
	public Response deleteConfEG(@HeaderParam(Constantes.HEADER_TOKEN) String token, @PathParam("idEvento") long idEvento,  @PathParam("tipo") int tipo) {
		Log log = null;
		String usuario	= null;
		String rol 		= null;
		boolean resultado = false;
		try {
			log = new Log("EventosREST.log", true);
			Claims claims 	= Jwts.parser().setSigningKey(Constantes.KEY.getBytes("UTF-8")).parseClaimsJws(token).getBody();
			usuario = (String) claims.get(Constantes.TOKEN_USER);
			rol 	= (String) claims.get(Constantes.ROL);
			log.log("Comienza delete de conf de EG de id ["+idEvento+" ] para el usaurio ["+usuario+"]");
		
			resultado = meve.deteleConfEG(log, idEvento, tipo , rol, usuario);
			return Response.ok(resultado).build();
		}catch(Throwable t) {
			log.log("Error recuperando conf  de  EVENTO GLOBAL["+idEvento+"] debido a["+t.getMessage()+"]", t);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}finally {
			log.log("Termina recuperacion  de configuracion del  EVENTO GLOBAL :[" + idEvento + "]");
			Log.cerrar(log);
		}
	}
	
	
	
	@Secured
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/sus_eg/{idEvento}")
	public Response suscripcionEG(@HeaderParam(Constantes.HEADER_TOKEN) String token, @PathParam("idEvento") long idEvento) {
		Log log = null;
		String usuario	= null; 
		boolean resultado = false;
		try {
			log = new Log("EventosREST.log", true);
			log.log("Comienza suscripcion a evento global de id ["+idEvento+" ]");
			Claims claims 	= Jwts.parser().setSigningKey(Constantes.KEY.getBytes("UTF-8")).parseClaimsJws(token).getBody();
			usuario = (String) claims.get(Constantes.TOKEN_USER);
			resultado = meve.suscribirseEG(log, idEvento, usuario);
			return Response.ok(resultado).build();
		}catch(Throwable t) {
			log.log("Error recuperando conf  de  EVENTO GLOBAL["+idEvento+"] debido a["+t.getMessage()+"]", t);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}finally {
			log.log("Termina recuperacion  de configuracion del  EVENTO GLOBAL :[" + idEvento + "]");
			Log.cerrar(log);
		}
	}
	
	
	@Secured
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getConfEG/{idEvento}")
	public Response getConfEG (@HeaderParam(Constantes.HEADER_TOKEN) String token, @PathParam("idEvento") long idEvento) {
		Log log = null;
		List<EventoGConf> configuraciones = new ArrayList<EventoGConf>();
		try {
			log = new Log("EventosREST.log", true);
			log.log("Comienza recuperacion de eventos globales ]");
			configuraciones = meve.getConfEG(log, idEvento);
			if(configuraciones != null) {
				return Response.ok(configuraciones).build();	
			}else {
				return Response.ok("CONFIGURACION VACIA").build();	
			}
		}catch(Throwable t) {
			log.log("Error recuperando conf  de  EVENTO GLOBAL["+idEvento+"] debido a["+t.getMessage()+"]", t);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}finally {
			log.log("Termina recuperacion  de configuracion del  EVENTO GLOBAL :[" + idEvento + "]");
			Log.cerrar(log);
		}
	}
	
	
	@Secured
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getListaEventosG")
	public Response getListaEventosG(@HeaderParam(Constantes.HEADER_TOKEN) String token) {
		Log log = null;
		List<EventoGCabezal> eventos = new ArrayList<EventoGCabezal>();
		try {
			log = new Log("EventosREST.log", true);
			log.log("Comienza recuperacion de eventos globales ]");
			String usuario = MetodosToken.getUsuario(log, token);
			eventos = meve.getListaEventosG(log, usuario);
			if(!eventos.isEmpty() && eventos!= null) {
				return Response.ok(eventos).build();	
			}else {
				return Response.ok("NO EXISTEN EVENTOS").build();	
			}
		} catch (Throwable t) {
			log.log("Error recuperando lista de  EVENTO GLOBAL debido a["+t.getMessage()+"]", t);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			log.log("Termina recuperacion  de EVENTOS GLOBAL :[" + eventos.toString() + "]");
			Log.cerrar(log);
		}
	}
	
	@Secured
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/confEventoG/{nombreEvento}/{tipo}/{nivel}/{alerta}")
	public Response configurarEventoG(@HeaderParam(Constantes.HEADER_TOKEN) String token, @PathParam("nombreEvento") String nombreEvento,@PathParam("tipo") int tipo,@PathParam("nivel") int nivel, @PathParam("alerta") String alerta ) {
		Log log = null;
		boolean configurado = false;
		try {
			log = new Log("EventosREST.log", true);
			log.log("-> [Comienza configuracion del EVENTO GLOBAL ["+nombreEvento+"]");
			String usuario = MetodosToken.getUsuario(log, token);
			String rol = MetodosToken.getRol(log, token);
			String adminAlta = MetodosToken.getUsuario(log, token);
			if(rol.equals(Constantes.ADMINISTRADOR_COMUN)||rol.equals(Constantes.ADMINISTRADOR_PRIVILEGIOS)||rol.equals(Constantes.ADMINISTRADOR_SOLO_NODOS) ||rol.equals(Constantes.SUPERVISOR) ) {
				configurado = meve.configurarEG(log,nombreEvento, tipo, nivel, alerta, usuario );
				return Response.ok(configurado).build();	
			}else {
				return Response.ok("No tienes suficiente nivel de acceso para configurar el evento eventos").build();	
			}
		} catch (Throwable t) {
			log.log("Error configurando EVENTO GLOBAL debido a["+t.getMessage()+"]", t);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			log.log("Termina configuracion de EVENTO GLOBAL :[" + nombreEvento + "] resultado ["+configurado+"]");
			Log.cerrar(log);
		}
	}
	 
	@Secured
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/crearEG/{nombre}")
	public Response crear(@HeaderParam(Constantes.HEADER_TOKEN) String token, @PathParam("nombre") String nombreEvento) {
		Log log = null;
		boolean creado = false;
		try {
			log = new Log("EventosREST.log", true);
			log.log("-> [Comienza creacion de EVENTO GLOBAL .");
			String rol = MetodosToken.getRol(log, token);
			String adminAlta = MetodosToken.getUsuario(log, token);
			if(rol.equals(Constantes.ADMINISTRADOR_COMUN)||rol.equals(Constantes.ADMINISTRADOR_PRIVILEGIOS)||rol.equals(Constantes.ADMINISTRADOR_SOLO_NODOS) ||rol.equals(Constantes.SUPERVISOR) ) {
				creado = meve.crearEG(log,nombreEvento, adminAlta);
				return Response.ok(creado).build();	
			}else {
				return Response.ok("No tienes suficiente nivel de acceso para crear eventos").build();	
			}
		} catch (Throwable t) {
			log.log("Error creando EVENTO GLOBAL debido a["+t.getMessage()+"]", t);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			log.log("Termina creacion de EVENTO GLOBAL :[" + creado + "] ");
			Log.cerrar(log);
		}
	}
	
	
	@Secured
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/activarEG/{id_evento}")
	public Response activarEG(@HeaderParam(Constantes.HEADER_TOKEN) String token, @PathParam("id_evento") String id_evento) {
		Log log = null;
		boolean creado = false;
		try {
			log = new Log("EventosREST.log", true);
			log.log("-> [Comienza activacion de EVENTO GLOBAL ["+id_evento+"]");
			String rol = MetodosToken.getRol(log, token);
			String adminAlta = MetodosToken.getUsuario(log, token);
			if(rol.equals(Constantes.ADMINISTRADOR_COMUN)||rol.equals(Constantes.ADMINISTRADOR_PRIVILEGIOS)||rol.equals(Constantes.ADMINISTRADOR_SOLO_NODOS) ||rol.equals(Constantes.SUPERVISOR) ) {
				creado = meve.activarEG(log,id_evento, adminAlta);
				return Response.ok(creado).build();	
			}else {
				return Response.ok("No tienes suficiente nivel de acceso para activar eventos").build();	
			}
		} catch (Throwable t) {
			log.log("Error activando EVENTO GLOBAL["+id_evento+"] debido a["+t.getMessage()+"]", t);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			log.log("Termina activacion de EVENTO GLOBAL :[" + creado + "] ");
			Log.cerrar(log);
		}
	}
	
	@Secured
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/desactivarEG/{id_evento}")
	public Response desactivarEG(@HeaderParam(Constantes.HEADER_TOKEN) String token, @PathParam("id_evento") String id_evento) {
		Log log = null;
		boolean creado = false;
		try {
			log = new Log("EventosREST.log", true);
			log.log("-> [Comienza desactivacion de EVENTO GLOBAL ["+id_evento+"]");
			String rol = MetodosToken.getRol(log, token);
			String adminAlta = MetodosToken.getUsuario(log, token);
			if(rol.equals(Constantes.ADMINISTRADOR_COMUN)||rol.equals(Constantes.ADMINISTRADOR_PRIVILEGIOS)||rol.equals(Constantes.ADMINISTRADOR_SOLO_NODOS) ||rol.equals(Constantes.SUPERVISOR) ) {
				creado = meve.desactivarEG(log,id_evento, adminAlta);
				return Response.ok(creado).build();	
			}else {
				return Response.ok("No tienes suficiente nivel de acceso para activar eventos").build();	
			}
		} catch (Throwable t) {
			log.log("Error activando EVENTO GLOBAL["+id_evento+"] debido a["+t.getMessage()+"]", t);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			log.log("Termina activacion de EVENTO GLOBAL :[" + creado + "] ");
			Log.cerrar(log);
		}
	}
	
	@Secured
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getTiposEventos")
	public Response getTiposEventos(@HeaderParam(Constantes.HEADER_TOKEN) String token) {
		Log log = null;
		List<TipoEventos> tiposEventos = new ArrayList<TipoEventos>();
		try {
			log = new Log("EventosREST.log", true);
			log.log("Comienza recuperacion de tipos  de  EVENTOS GLOBALES ]");
			tiposEventos = meve.getTiposEventos(log);
			return Response.ok(tiposEventos).build();	
		}catch(Throwable t) {
			log.log("Error recuperando tipos  de  EVENTOS GLOBALES debido a["+t.getMessage()+"]", t);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}finally {
			log.log("Termina recuperacion  de tipos de  EVENTOS GLOBALES ");
			Log.cerrar(log);
		}
	}
	
	@Secured
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getNivelesAlertasEG")
	public Response getNivelesAlertasEG(@HeaderParam(Constantes.HEADER_TOKEN) String token) {
		Log log = null;
		List<NivelesAlertasEG> tiposEventos = new ArrayList<NivelesAlertasEG>();
		try {
			log = new Log("EventosREST.log", true);
			log.log("Comienza recuperacion de niveles  de  EVENTOS GLOBALES ]");
			tiposEventos = meve.getNivelesAlertasEG(log);
			return Response.ok(tiposEventos).build();	
		}catch(Throwable t) {
			log.log("Error recuperando niveles  de  EVENTOS GLOBALES debido a["+t.getMessage()+"]", t);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}finally {
			log.log("Termina recuperacion  de niveles de  EVENTOS GLOBALES ");
			Log.cerrar(log);
		}
	}
}
