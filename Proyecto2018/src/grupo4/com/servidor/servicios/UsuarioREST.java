package grupo4.com.servidor.servicios;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import javax.ws.rs.core.Response;

import grupo4.com.core.manejadores.ManejadorUsuarios;
import grupo4.com.core.modelJEE.EventoGConf;
import grupo4.com.core.modelJEE.Usuario;
import grupo4.com.servidor.database.BD;
import grupo4.com.servidor.database.Persistencia;
import grupo4.com.servidor.rest.seguridad.anotacion.Secured;
import grupo4.com.servidor.servicios.modelos.MetodosToken;
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
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

@SuppressWarnings("unused")
@Path("/usuario")
public class UsuarioREST {
	
	
	
	ManejadorUsuarios musu = new ManejadorUsuarios();
	
	
	@Secured
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/alta/{username}/{password}/{mail}/{numero_cel}")
	public Response altaUsuario(@HeaderParam(Constantes.HEADER_TOKEN) String token, @PathParam("username") String username,
			@PathParam("password") String password, @PathParam("mail") String mail, @PathParam("numero_cel") String numero_cel) {
		Log log = null;
		boolean creado = false;
		try {
			log = new Log("UsuarioREST.log", true);
			log.log("-> [Comienza alta de usuario ["+username+"]");
			String rol = MetodosToken.getRol(log, token);
			String adminAlta = MetodosToken.getUsuario(log, token);
			if(rol.equals(Constantes.ADMINISTRADOR_COMUN)||rol.equals(Constantes.ADMINISTRADOR_PRIVILEGIOS)||rol.equals(Constantes.ADMINISTRADOR_SOLO_NODOS) ||rol.equals(Constantes.SUPERVISOR) ) {
				creado = musu.altaUsuario(log,username, password, mail, numero_cel, adminAlta);
				return Response.ok(creado).build();	
			}else {
				return Response.ok("No tienes suficiente nivel de acceso para dar de alta usuarios").build();	
			}
		} catch (Throwable t) {
			log.log("Error dando de alta usuario debido a["+t.getMessage()+"]", t);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			log.log("Termina alta de usuario  :[" + creado + "] ");
			Log.cerrar(log);
		}
	}
	
	@Secured
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/modificacion/{password}/{mail}/{numero_cel}/{descripcion}")
	public Response activarEG(@HeaderParam(Constantes.HEADER_TOKEN) String token,@PathParam("password") String password, @PathParam("mail") String mail,
			@PathParam("numero_cel") String numero_cel, @PathParam("descripcion") String descripcion) {
		Log log = null;
		boolean creado = false;
		String usuario = "";
		try {
			log = new Log("UsuarioREST.log", true);
			String rol = MetodosToken.getRol(log, token);
			usuario = MetodosToken.getUsuario(log, token);
			log.log("-> [Comienza modificacion de usuario ["+usuario+"]");
			creado = musu.modificarUsuario(log,usuario, password, mail, numero_cel, descripcion);
			return Response.ok(creado).build();	
		} catch (Throwable t) {
			log.log("Error modificando usaurio ["+usuario+"] debido a["+t.getMessage()+"]", t);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			log.log("Termina modificacion de Usuario :[" + creado + "] ");
			Log.cerrar(log);
		}
	}
	
	@Secured
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/baja/{password}/{descripcion}")
	public Response bajaUsuario(@HeaderParam(Constantes.HEADER_TOKEN) String token,@PathParam("password") String password, @PathParam("descripcion") String descripcion) {
		Log log = null;
		boolean eliminado = false;
		String usuario = "";
		try {
			log = new Log("UsuarioREST.log", true);
			String rol = MetodosToken.getRol(log, token);
			usuario = MetodosToken.getUsuario(log, token);
			log.log("-> [Comienza eliminacion de usuario ["+usuario+"] descripcion de baja ["+descripcion+"]");
			eliminado = musu.bajaUsuario(log, usuario, password, descripcion);
			return Response.ok(eliminado).build();	
		} catch (Throwable t) {
			log.log("Error eliminacion usaurio ["+usuario+"] debido a["+t.getMessage()+"]", t);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			log.log("Termina eliminacion de Usuario :[" + eliminado + "] ");
			Log.cerrar(log);
		}
	}
	
	@Secured
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getDatosUsuario")
	public Response getDatosUsuario (@HeaderParam(Constantes.HEADER_TOKEN) String token) {
		Log log = null;
		Usuario usuario = new Usuario();
		String username = "";
		try {
			log = new Log("UsuarioREST.log", true);
			username = MetodosToken.getUsuario(log, token);
			log.log("Comienza recuperacion de datos de usuario para el usuario ["+username+"]");
			usuario = musu.getDatosUsuario(log, username);
			return Response.ok(usuario).build();	
		}catch(Throwable t) {
			log.log("Error recuperando datos de usuario para el usuarioL["+username+"] debido a["+t.getMessage()+"]", t);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}finally {
			log.log("Termina recuperacion  de datos de usuario :[" + username + "]");
			Log.cerrar(log);
		}
	}
	
	 @SuppressWarnings("restriction")
	public Response login(@HeaderParam(HttpHeaders.AUTHORIZATION) String aut) {
			Log log = null;
	        try {
	    		log = new Log("login.log", true);
	    		log.log("-> [Comienza login y solicitud de JWT]");

	    		if ((aut != null) && (aut.length() > 6)) {
	    			aut = aut.substring(6);
	        	} else {
	        		log.log("No se recibio header de Basic Atuh:["+HttpHeaders.AUTHORIZATION+"]. Imposible continuar");
	                return Response.status(Response.Status.UNAUTHORIZED).build();
	        	}
	        	
				byte [] bdecoded = new sun.misc.BASE64Decoder().decodeBuffer(aut);
				String decoded = new String(bdecoded);
				String [] ps = decoded.split(":");
				String user = ps[0];

				Persistencia p		= new Persistencia();
	    		BD metodosBase		= new BD();
	    		//String roles 		= metodosBase.getRoles(log, user);
	    		

				// se genera token jwt
	            String jwt = crearJWT(user);

	    		log.log("Usuario:["+user+"], Se genero token:["+jwt+"]");
	    		p.persistirToken(log, user, jwt);
	    		
	            return Response.ok().header(Constantes.HEADER_TOKEN, jwt).build();
	 
	        } catch (Throwable t) {
	        	log.log("Error inesperado generando token", t);
	            return Response.status(Response.Status.UNAUTHORIZED).build();
	        } finally {
	        	Log.cerrar(log);
	        }
	    }
	 
	 private String crearJWT(String user) throws UnsupportedEncodingException {
		 	//Le metemos 120 minutos de expiracion al token, que seria lo valido de la sesion
	    	Date fecha_ahora = new Date();
	    	Calendar calendario = Calendar.getInstance();
	    	calendario.setTime(fecha_ahora);
	    	calendario.add(Calendar.MINUTE, 120);
	        Date fecha_exp = calendario.getTime();
	        
	        BD base = new BD();
	        String rol = base.getRolUsuario(user);
			//Se meten las claims del JWT
	        String token = Jwts.builder()
	        		.claim(Constantes.ROL, rol)
	        		.claim(Constantes.TOKEN_USER, user)
	                .setSubject("Token valido")
	                .setIssuer("Doits")
	                .setIssuedAt(fecha_ahora)
	                .setExpiration(fecha_exp)
	                .signWith(SignatureAlgorithm.HS512, Constantes.KEY.getBytes("UTF-8")).compact();
	                
	        return token;
	    }
	 
	//Para Test estado de Servidor
	@GET
	@Path("/prueba")
	public String probar() {
		return "Proyecto-2018 Server is Up!";
	}

}
