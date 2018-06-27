package grupo4.com.servidor.rest.seguridad.filtros;

import java.io.IOException;
import java.security.Key;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import grupo4.com.servidor.database.BD;
import grupo4.com.servidor.rest.seguridad.anotacion.Secured;
import grupo4.com.util.Constantes;
import grupo4.com.util.Log;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.crypto.MacProvider;

@Provider
@Secured
@Priority(Priorities.AUTHORIZATION)
@SuppressWarnings("unused")
public class FiltroRol implements ContainerRequestFilter {
	
	public static final Key KEY = MacProvider.generateKey();
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		Log log = null;
		boolean valido = false;
		try {
			log = new Log("Roles.log", true);
			// Recupera el token del header de la request
			String token = requestContext.getHeaderString(Constantes.HEADER_TOKEN);
			if (token == null) {
				log.log("No se recibio token");
				requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
				return;
			}
			
			// Valida el token usando la key con la que firmamos el certificado
			Claims claims = Jwts.parser().setSigningKey(Constantes.KEY.getBytes("UTF-8")).parseClaimsJws(token).getBody();

			String user = (String) claims.get(Constantes.TOKEN_USER);
			String rol = (String) claims.get(Constantes.ROL);
			if (rol == null) {
				log.log("No se obtuvo rol en token");
				requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
				return;
			}
			
			//valido el token para el usuario extraido del token
			BD base = new BD();
			valido = base.validarToken(log, user, token);

			// si no es valido no lo dejo pasar y tiro exception
			if (!valido) {
				requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
			}
		} catch (Throwable e) {
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		} finally {
			Log.cerrar(log);
		}
	}
}
