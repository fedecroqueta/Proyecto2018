package grupo4.com.servidor.rest.seguridad.filtros;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import grupo4.com.servidor.database.BD;
import grupo4.com.servidor.rest.seguridad.anotacion.Authentication;


@Provider
@Authentication
@Priority(Priorities.AUTHENTICATION)
@SuppressWarnings("unused")
public class FiltroAutenticacion implements ContainerRequestFilter{
	  
	@Context
    private ResourceInfo resourceInfo;
     
    private static final String AUTHORIZATION_PROPERTY 	= "Authorization";
    private static final String AUTHENTICATION_SCHEME 	= "Basic";
    private static final Response ACCESS_DENIED 		= Response.status(Response.Status.UNAUTHORIZED).entity("Grupo4 - Acceso Denegado").build();
    private static final Response ACCESS_FORBIDDEN 		= Response.status(Response.Status.FORBIDDEN).entity("Grupo4 -Elemento blockeado").build();
 
    @Override
	public void filter(ContainerRequestContext requestContext)  {
		Method method = resourceInfo.getResourceMethod();
		
		 //Acceso permitidos para todos, acceso LIBRE
        if( ! method.isAnnotationPresent(PermitAll.class)) {
            
        	if(method.isAnnotationPresent(DenyAll.class)){
                requestContext.abortWith(ACCESS_FORBIDDEN);
                return;
            }
            final MultivaluedMap<String, String> headers = requestContext.getHeaders();
            final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);
              
            //Si no tiene authorization blockea todo
            if(authorization == null || authorization.isEmpty()) {
                requestContext.abortWith(ACCESS_DENIED);
                return;
            }
            final String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");
			String usernameAndPassword;
			try {
				usernameAndPassword = new String(new sun.misc.BASE64Decoder().decodeBuffer(encodedUserPassword));
			} catch (IOException e) {
				e.printStackTrace();
                requestContext.abortWith(ACCESS_DENIED);
                return;
			}
            
            //Splitea el username y password
            final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
            final String username = tokenizer.nextToken();
            final String password = tokenizer.nextToken();
              
            if( ! validarUsuario(username, password)) {
                requestContext.abortWith(ACCESS_DENIED);
                return;
            }
        }
	}

	private  boolean validarUsuario(String username, String password) {
		boolean valido = false;
		BD base = new BD();
		try {
			valido = base.validarUsuario(username, password);
		} catch(Throwable t) {
			System.out.println("Error autenticando usuario:["+username+"]");
			t.printStackTrace();
		}
		if (!valido) {
			System.err.println("No fue posible autenticar Usuario:["+username+"], Password:["+password+"]");
		}
		return valido;
	}
}
