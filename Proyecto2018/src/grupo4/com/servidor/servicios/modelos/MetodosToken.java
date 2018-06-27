package grupo4.com.servidor.servicios.modelos;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

import grupo4.com.servidor.database.BD;
import grupo4.com.util.Constantes;
import grupo4.com.util.Log;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class MetodosToken {

	/**
	 * Genera el JWT
	 * @param user
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String crearToken(String user) throws UnsupportedEncodingException {
    	// calculamos la fecha de expiración del token
    	Date fecha_ahora = new Date();
    	Calendar calendario = Calendar.getInstance();
    	calendario.setTime(fecha_ahora);
    	//60 minutos de expiracion
    	calendario.add(Calendar.MINUTE, 120);
        Date fecha_exp = calendario.getTime();
        
        BD base = new BD();
        String rol = base.getRolUsuario(user);
		// creamos el token
        String jwtToken = Jwts.builder()
        		.claim(Constantes.ROL, rol)
        		.claim(Constantes.TOKEN_USER, user)
                .setSubject("Token valido")
                .setIssuer("Grupo4-Proyecto2018")
                .setIssuedAt(fecha_ahora)
                .setExpiration(fecha_exp)
                .signWith(SignatureAlgorithm.HS512, Constantes.KEY.getBytes("UTF-8")).compact();
                
        return jwtToken;
    }
	
	public static String getRol(Log log, String token) {
		String rol = "";
		String usuario = "";
		try {
			log.log("Inicia recuperacion de rol ...");
			Claims claims 	= Jwts.parser().setSigningKey(Constantes.KEY.getBytes("UTF-8")).parseClaimsJws(token).getBody();
			usuario = (String) claims.get(Constantes.TOKEN_USER);
			rol = (String) claims.get(Constantes.ROL);
			log.log("Rol para el usuario ["+usuario+"] recuperado con exito ["+rol+"]");
		}catch(Throwable t) {
			log.log("No fue posible recuperar el Rol para el usuario ["+usuario+"] debido a ["+t.getMessage()+"]",t);
		}
		return rol;
	}
	
	public static String getUsuario(Log log, String token) {
		String usuario = "";
		try {
			log.log("Inicia recuperacion de usuario ...");
			Claims claims 	= Jwts.parser().setSigningKey(Constantes.KEY.getBytes("UTF-8")).parseClaimsJws(token).getBody();
			usuario = (String) claims.get(Constantes.TOKEN_USER);
			log.log(" usuario ["+usuario+"] recuperado con exito ["+usuario+"]");
		}catch(Throwable t) {
			log.log("No fue posible recuperar  el usuario  debido a ["+t.getMessage()+"]",t);
		}
		return usuario;
	}
	
}
