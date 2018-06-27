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

import grupo4.com.core.manejadores.ManejadorInfra;
import  grupo4.com.core.manejadores.ManejadorLogs;
import grupo4.com.core.modelJEE.InfoCabezalNodo;
import grupo4.com.core.modelJEE.InfoDisco;

@SuppressWarnings("unused")
@Path("/infra")
public class InfraREST {

	ManejadorInfra minfra = new ManejadorInfra();
	
	@Secured
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/listarNodos")
	public Response getListadeNodos(@HeaderParam(Constantes.HEADER_TOKEN) String token) {
		Log log = null;
		List<String> listaNodos = null;
		try {
			log = new Log("InfraREST.log", true);
			log.log("-> [Recuperando lista de nodos .");
			listaNodos = minfra.getColeccionesAsignadas(log);
			return Response.ok(listaNodos).build();
		} catch (Throwable t) {
			log.log("Error retornando lista de Nodos debido a  ["+t.getMessage()+"]", t);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			log.log("Respuesta -> Lista de nodos :[" + listaNodos.toString() + "] ");
			Log.cerrar(log);
		}
	}
	
}
