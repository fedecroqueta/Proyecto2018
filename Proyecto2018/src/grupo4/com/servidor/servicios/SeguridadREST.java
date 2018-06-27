package grupo4.com.servidor.servicios;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import grupo4.com.core.modelJEE.TokenEnBody;
import grupo4.com.servidor.database.BD;
import grupo4.com.servidor.database.Persistencia;
import grupo4.com.servidor.rest.seguridad.anotacion.Authentication;
import grupo4.com.servidor.servicios.modelos.MetodosToken;
import grupo4.com.util.Constantes;
import grupo4.com.util.Log;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@SuppressWarnings("unused")
@Path("/seguridad")
public class SeguridadREST {

	@Authentication
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/token")
    public Response getToken(@HeaderParam(HttpHeaders.AUTHORIZATION) String auth) {
		Log log = null;
        try {
    		log = new Log("SeguridadREST.log", true);
    		log.log("-> [Se pide Token]");

    		if ((auth != null) && (auth.length() > 6)) {
        		auth = auth.substring(6);
        	} else {
        		log.log("No se recibio header:["+HttpHeaders.AUTHORIZATION+"]. imposible continuar");
                return Response.status(Response.Status.UNAUTHORIZED).build();
        	}
        	
			byte [] bdecoded = new sun.misc.BASE64Decoder().decodeBuffer(auth);
			String decoded = new String(bdecoded);
			String [] ps = decoded.split(":");
			String user = ps[0];

			Persistencia p		= new Persistencia();
    		BD consultas		= new BD();
    		
    		//Devuelve los ROLES , van a poder ser Usuario, Administrador y Supervisor(?)
    		//String roles 		= consultas.getRoles(log, user);


			// se genera token jwt
            String token = MetodosToken.crearToken(user);

    		log.log("Usuario:["+user+"], Se genero token:["+token+"]");
    		p.persistirToken(log, user, token);
    		
    		TokenEnBody tokenJSON = new TokenEnBody();
    		tokenJSON.setSecurityToken(token);
            return Response.ok(tokenJSON).header(Constantes.HEADER_TOKEN, token).header("Access-Control-Allow-Origin", "*")
        			.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").header("Access-Control-Expose-Origin", "*")
        			.allow("OPTIONS").build();
        } catch (Throwable t) {
        	log.log("Error inesperado generando token", t);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } finally {
        	log.cerrarLog();
        }
    }
}
