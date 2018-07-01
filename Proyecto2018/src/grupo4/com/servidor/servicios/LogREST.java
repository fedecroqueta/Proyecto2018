package grupo4.com.servidor.servicios;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.Calendar;
import javax.ws.rs.core.Response;

import grupo4.com.servidor.database.BD;
import grupo4.com.servidor.database.Persistencia;
import grupo4.com.servidor.rest.seguridad.anotacion.Secured;
import grupo4.com.util.Constantes;
import grupo4.com.util.Log;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import  grupo4.com.core.manejadores.ManejadorLogs;
import grupo4.com.core.modelJEE.InfoCabezalNodo;
import grupo4.com.core.modelJEE.InfoCpu;
import grupo4.com.core.modelJEE.InfoDisco;
import grupo4.com.core.modelJEE.InfoMemoria;
import grupo4.com.core.modelJSON.LogAgente;

@SuppressWarnings("unused")
@Path("/info")
public class LogREST {
	
	ManejadorLogs ml = new ManejadorLogs();
	
	
	@Secured
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/cabezal/{nodo}")
	public Response getInfoCabezal(@PathParam("nodo") String nodo,@HeaderParam(Constantes.HEADER_TOKEN) String token) {
			
		Log log = null;
		InfoCabezalNodo infCabeza = null;
		try {
			log = new Log("LogREST.log", true);
			log.log("-> [Recuperando info de cabezal de ["+nodo+"].");
			infCabeza = ml.getInfoCabezalNodo(log, nodo);
			return Response.ok(infCabeza).build();
		} catch (Throwable t) {
			log.log("Error inesperado comprobando actividad de dispositivo ["+nodo+"]", t);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			log.log("Respuesta -> Caracterisitcas del Nodo:[" + nodo + "] son :["+infCabeza+"]");
			Log.cerrar(log);
		}
	}
	
	@Secured
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/free/{nodo}")
	public Response memoriaLibre(@PathParam("nodo") String nodo,@HeaderParam(Constantes.HEADER_TOKEN) String token) {
			
		Log log = null;
		InfoMemoria memoriaLibre = null;
		try {
			memoriaLibre = new InfoMemoria();
			log = new Log("LogREST.log", true);
			log.log("-> [Comprobando memoria libre del nodo["+nodo+"].");
			memoriaLibre = ml.getMemoriaLibre(log, nodo);
			return Response.ok(memoriaLibre).build();
		} catch (Throwable t) {
			log.log("Error inesperado comprobando actividad de dispositivo ["+nodo+"]", t);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			log.log("Respuesta -> Memoria Libre de Nodo:[" + nodo + "] es :["+memoriaLibre+"] MB");
			Log.cerrar(log);
		}
	}
	
	@Secured
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/freeEntreFechas/{nodo}/{fechaDesde}/{fechaHasta}")
	public Response getMemoriaLibreEntreFechas(@PathParam("nodo") String nodo, @PathParam("fechaDesde") String fechaDesde, @PathParam("fechaHasta") String fechaHasta, @HeaderParam(Constantes.HEADER_TOKEN) String token) {
		Log log = null;
		List<InfoMemoria> infoMemoriaPorFechas = null;
		try {
			log = new Log("LogREST.log", true);
			log.log("-> [Comprobando informacio del disco  del nodo["+nodo+"] entre fechas ["+fechaDesde+"] y ["+fechaHasta+"]");
			infoMemoriaPorFechas = ml.getMemoriaLibreEntreFechas(log, nodo, fechaDesde, fechaHasta);
			return Response.ok(infoMemoriaPorFechas).build();
		} catch (Throwable t) {
			log.log("Error inesperado comprobando inforamcion de disco entre fechas ["+nodo+"]", t);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			log.log("Respuesta -> Info CPU del Nodo:[" + nodo + "] es :["+infoMemoriaPorFechas+"]");
			Log.cerrar(log);
		}
	}
	
	
	
	@Secured
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/infoDisco/{nodo}")
	public Response getInfoDisco(@PathParam("nodo") String nodo,@HeaderParam(Constantes.HEADER_TOKEN) String token) {
			
		Log log = null;
		InfoDisco infoDisco = null;
		try {
			log = new Log("LogREST.log", true);
			log.log("-> [Comprobando informacio del disco duro  del nodo["+nodo+"].");
			infoDisco = ml.getEspacioEnDisco(log, nodo);
			return Response.ok(infoDisco).build();
		} catch (Throwable t) {
			log.log("Error inesperado comprobando inforamcion de disco duro ["+nodo+"]", t);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			log.log("Respuesta -> Info de disco duro del Nodo:[" + nodo + "] es :["+infoDisco+"]");
			Log.cerrar(log);
		}
	}
	
	@Secured
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/infoDiscoEntreFechas/{nodo}/{fechaDesde}/{fechaHasta}")
	public Response getinfoDiscoEntreFechas(@PathParam("nodo") String nodo, @PathParam("fechaDesde") String fechaDesde, @PathParam("fechaHasta") String fechaHasta, @HeaderParam(Constantes.HEADER_TOKEN) String token) {
		Log log = null;
		List<InfoDisco> InfoDiscoPorFechas = null;
		try {
			log = new Log("LogREST.log", true);
			log.log("-> [Comprobando informacio del disco  del nodo["+nodo+"] entre fechas ["+fechaDesde+"] y ["+fechaHasta+"]");
			InfoDiscoPorFechas = ml.getInfoDiscoEntreFechas(log, nodo, fechaDesde, fechaHasta);
			return Response.ok(InfoDiscoPorFechas).build();
		} catch (Throwable t) {
			log.log("Error inesperado comprobando inforamcion de disco entre fechas ["+nodo+"]", t);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			log.log("Respuesta -> Info CPU del Nodo:[" + nodo + "] es :["+InfoDiscoPorFechas+"]");
			Log.cerrar(log);
		}
	}
	
	@Secured
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/infoCpu/{nodo}")
	public Response getinfoCpu(@PathParam("nodo") String nodo,@HeaderParam(Constantes.HEADER_TOKEN) String token) {
			
		Log log = null;
		InfoCpu InfoCpu = null;
		try {
			log = new Log("LogREST.log", true);
			log.log("-> [Comprobando informacio del CPU  del nodo["+nodo+"].");
			InfoCpu = ml.getInfoCpu(log, nodo);
			return Response.ok(InfoCpu).build();
		} catch (Throwable t) {
			log.log("Error inesperado comprobando inforamcion de CPU ["+nodo+"]", t);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			log.log("Respuesta -> Info CPU del Nodo:[" + nodo + "] es :["+InfoCpu+"]");
			Log.cerrar(log);
		}
	}
	
	@Secured
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/infoCpuEntreFechas/{nodo}/{fechaDesde}/{fechaHasta}")
	public Response getinfoCpuEntreFechas(@PathParam("nodo") String nodo, @PathParam("fechaDesde") String fechaDesde, @PathParam("fechaHasta") String fechaHasta, @HeaderParam(Constantes.HEADER_TOKEN) String token) {
		Log log = null;
		List<InfoCpu> InfoCpu = null;
		try {
			log = new Log("LogREST.log", true);
			log.log("-> [Comprobando informacio del CPU  del nodo["+nodo+"].");
			InfoCpu = ml.getInfoCpuEntreFechas(log, nodo, fechaDesde, fechaHasta);
			return Response.ok(InfoCpu).build();
		} catch (Throwable t) {
			log.log("Error inesperado comprobando inforamcion de CPU ["+nodo+"]", t);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			log.log("Respuesta -> Info CPU del Nodo:[" + nodo + "] es :["+InfoCpu+"]");
			Log.cerrar(log);
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/infoAgente/{nodo}")
	public Response getAgente(@PathParam("nodo") String nodo) {
		
		List<LogAgente> agente = ml.getAgenteUsuario(nodo);
		
		return Response.ok(agente).build();
	}
	

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/infoAgente/{nodo}/{fechaInicio}/{fechaFin}")
	public Response getAgente(@PathParam("nodo") String nodo, @PathParam("fechaInicio") String fechaInicio, @PathParam("fechaFin") String fechaFin) {
		try {
			List<LogAgente> agente = ml.getAgenteUsuarioPorFecha(nodo, fechaInicio, fechaFin);
			
			return Response.ok(agente).build();
		}
		catch( Throwable t) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

}
