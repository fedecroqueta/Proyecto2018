package grupo4.com.servidor.database;

import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import grupo4.com.util.Log;
import grupo4.com.util.UtilBase;

public class Persistencia {

	/**
	 * Persiste Token relacionado a un usuario en postgres
	 */
 	public void persistirToken(Log log, String user,  String token) throws Exception {
		Connection c 	= null;
		Statement st	= null;
        try {
    		log.log("Comienza persistencia del token. Usuario:["+user+"], Token:["+token+"]");
    		
    		c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
    		c.setAutoCommit(false);
    		
    		//Se fija si ya hay uno y lo borra
    		String deleteRegistro =  "DELETE FROM autenticacion WHERE username='"+user+"';";
    		
    		//Seteo formato de fecha 
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		
    		//Insert el token, si ya habia uno fue borrado asi que inserta con exito aunque ya existiera uno del usuario
    		String insert = "INSERT INTO autenticacion(username, token, fecha) "+ 
							"VALUES ('"+user+"','"+token+"','"+sdf.format(new Date())+"')";
    		
    		st = c.createStatement();
    		boolean existe = (st.executeUpdate(deleteRegistro) > 0);
    		String stLog = (existe) ? "Usuario:["+user+"] solicita un nuevo Token. " : "Usuario:["+user+"] solicita un Token por primera vez. ";
    		int res = st.executeUpdate(insert);
    		if(res > 0) {
    			log.log(stLog+"Se persiste token. Usuario:["+user+"], Token:["+token+"]");
    		} else {
    			log.log(stLog+"No se pudo persistir token. Usuario:["+user+"], Token:["+token+"]");
    		}
    		c.commit();
        } catch (Throwable e) {
        	log.log("Error al persistir Token en base de datos debido a  ["+e.getMessage()+"]", e);
        	try {
            	c.rollback();
        	} catch (Throwable t) {
            	log.log("Error al realizar rollback de la transaccion debido a ["+t.getMessage()+"]", e);
        	}
        	throw new Exception("No fue posible persistir token");
        } finally {
        	UtilBase.cerrarComponentes(log, null, st, c);
        }
	}
}
