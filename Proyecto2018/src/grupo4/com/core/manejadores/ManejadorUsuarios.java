package grupo4.com.core.manejadores;

import grupo4.com.core.modelJEE.Usuario;
import grupo4.com.servidor.database.BD;
import grupo4.com.util.Log;

public class ManejadorUsuarios {

	public boolean altaUsuario(Log log, String username, String password, String mail, String numero_cel,
			String adminAlta) {
		boolean creado = false;
		try {
			BD base = new BD();
			creado = base.altaUsuario(log, username,password, mail, numero_cel, adminAlta);
		}catch(Throwable t) {
			log.log("-->ERROR");
		}
		return creado;
	}

	public boolean modificarUsuario(Log log, String usuario, String password, String mail, String numero_cel, String descripcion) {
		boolean modificado = false;
		try {
			BD base = new BD();
			modificado = base.modificarUsuario(log, usuario,password, mail, numero_cel, descripcion);
		}catch(Throwable t) {
			log.log("-->ERROR");
		}
		return modificado;
	}

	public boolean bajaUsuario(Log log, String usuario, String password, String descripcion) {
		boolean eliminado = false;
		try {
			BD base = new BD();
			eliminado = base.bajaUsuario(log, usuario,password, descripcion);
		}catch(Throwable t) {
			log.log("-->ERROR");
		}
		return eliminado;
	}

	public Usuario getDatosUsuario(Log log, String username) {
		Usuario usuario = new Usuario();
		try {
			BD base = new BD();
			usuario = base.getDatosUsuario(log, username);
		}catch(Throwable t) {
			log.log("-->ERROR");
		}
		return usuario;
	}

}
