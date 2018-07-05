package grupo4.com.servidor.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import grupo4.com.core.modelJEE.EventoGCabezal;
import grupo4.com.core.modelJEE.EventoGConf;
import grupo4.com.core.modelJEE.EventoGIdYFechaInactivo;
import grupo4.com.core.modelJEE.NivelesAlertasEG;
import grupo4.com.core.modelJEE.Notis;
import grupo4.com.core.modelJEE.SuscripcionEG;
import grupo4.com.core.modelJEE.TipoEventos;
import grupo4.com.core.modelJEE.Usuario;
import grupo4.com.util.Constantes;
import grupo4.com.util.Log;
import grupo4.com.util.UtilBase;

public class BD {

	public 	List<EventoGConf> getConfEG(Log log, long idEvento){
		String sql		= "";
		List<EventoGConf> configuraciones = null;
		Connection c 	= null;
		Statement st 	= null;
		ResultSet rs	= null;
		try {
			log.log("Recuperando lista de eventos en BD....");
			configuraciones = new ArrayList<EventoGConf>();
			c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
			sql = "SELECT * FROM conf_eventos_globales WHERE id_evento ="+idEvento;
			st = c.createStatement();
			rs = st.executeQuery(sql);
			//TODO : hacer el while y setear, antes de que se me apagara lo hice
			while(rs.next()){
				EventoGConf conf = new EventoGConf();
				conf.setIdEvento(idEvento);
				conf.setAlerta(rs.getString("alerta"));
				conf.setFechaMod(rs.getString("fecha_ultima_mod"));
				conf.setNivel(rs.getInt("nivel"));
				conf.setTipo(rs.getInt("tipo"));
				
				configuraciones.add(conf);
			}
		}catch(Throwable t) {
			log.log("No fue posible recuperar lista de configuraciones en BD. SQL ["+sql+"]. debido a ["+t.getMessage()+"]");
		}finally {
			UtilBase.cerrarComponentes(log, rs, st, c);
		}
		return configuraciones;
	}
	
	public List<EventoGCabezal> getListaEventosG (Log log, String usaurio){
		String sql		= "";
		List<EventoGCabezal> eventos = null;
		Connection c 	= null;
		Statement st 	= null;
		ResultSet rs	= null;
		try {
			log.log("Recuperando lista de eventos en BD....");
			eventos = new ArrayList<EventoGCabezal>();
			c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
			sql = "SELECT id_evento, nombre_evento, activo, admin_alta FROM eventos_globales ";
			st = c.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				EventoGCabezal eg = new EventoGCabezal();
				String adminAlta = rs.getString("admin_alta");
				long id_evento = rs.getLong("id_evento");
				boolean usuarioCreador = (usaurio.equals(adminAlta)) ? true : false ; 
				boolean estoySuscripto = BD.getSuscripcion(log, id_evento, usaurio);
				
				eg.setEstoySuscripto(estoySuscripto);
				eg.setSoyCreador(usuarioCreador);
				eg.setIdEvento(id_evento);
				eg.setNombreEvento(rs.getString("nombre_evento"));
				eg.setActivo(rs.getBoolean("activo"));
				eventos.add(eg);
			}
		}catch(Throwable t) {
			log.log("No fue posible recuperar lista de eventos en BD. SQL ["+sql+"]. debido a ["+t.getMessage()+"]");
		}finally {
			UtilBase.cerrarComponentes(log, rs, st, c);
		}
		return eventos;
	}
	
	public static  boolean getSuscripcion(Log log,long id_evento, String usaurio) {
		boolean estoySuscripto = false;
		Connection c = null;
		Statement st = null;
		ResultSet  rs = null;
		String sql = "";
		try {
			log.log("Se obtiene si el usuario["+usaurio+" esta suscripto al Evento GLOBAL :["+id_evento+"]");
			c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
			sql = 	"SELECT id_suscripcion_eg FROM suscripciones_eventos_globales "
					+ " WHERE usuario='"+usaurio+"' AND id_evento_global="+id_evento+" AND activa<>false";
			st = c.createStatement();
			rs = st.executeQuery(sql);
			estoySuscripto = (rs.next()) ? true : false;
			log.log("Usuario suscripto = ["+estoySuscripto+"] ");
		} catch(Throwable t) {
			log.log("No es posible conusltar suscripction al EVENTO GLOBAL ["+id_evento+"] para el usuario = ["+usaurio+"]Error:["+t.getMessage()+"].SQL ["+sql+"]", t);
		} finally {
			UtilBase.cerrarComponentes(null, rs, st, c);
		}
		return estoySuscripto;
	}
	
	public long getIDEventoG(Log log, String nombreEvento) {
		String sql		= "";
		long idEvento		= 0;
		Connection c 	= null;
		Statement st 	= null;
		ResultSet rs	= null;
		try {
			c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
			sql = "SELECT id_evento FROM eventos_globales WHERE nombre_evento='"+nombreEvento+"'";
			st = c.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {
				idEvento = rs.getLong("id_evento");
			}
		} catch(Throwable e) {
    		log.log("ERROR al buscar id de  EVENTO GLOBAL ["+nombreEvento+"]. SQL:["+sql+"]. Error:["+e.getMessage()+"]", e);
		} finally {
			UtilBase.cerrarComponentes(log, rs, st, c);
		}
		return idEvento;
	}
	
	public boolean configurarEG(Log log, long idEvento, int tipo, int nivel, String alerta, String usuario) {
		String sql		= "";
		String delete	= "";
		Connection c 	= null;
		Statement st 	= null;
		boolean configurado = false;
		try {
			c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String now = sdf.format(new Date());
			
			delete = "DELETE FROM conf_eventos_globales WHERE id_evento="+idEvento+" AND tipo="+tipo;
			
			sql = "INSERT INTO conf_eventos_globales  (id_evento, tipo, nivel, alerta, fecha_ultima_mod, usuario) "
					+ "VALUES ("+idEvento+", "+tipo+", "+nivel+", '"+alerta+"', '"+now+"', '"+usuario+"')";
			st = c.createStatement();
			
			//hago el delete y compruebo si es modificacion o configuracion por primera vez
			boolean existe = (st.executeUpdate(delete) > 0);
			
    		String logearQ = (existe) ? "Id Evento:["+idEvento+"] se modifica. " : "Id Evento:["+idEvento+"] se configura. primera vez. ";
    		
    		//Hago insert de la congiuracion o modificaion dependiendo que sea que
    		int res = st.executeUpdate(sql);
    		if(res > 0) {
    			configurado = true;
    			log.log(logearQ);
    		} else {
    			log.log(logearQ+"No se pudo configurar.");
    		}
		} catch(Throwable e) {
    		log.log("ERROR al crear EVENTO GLOBAL ["+idEvento+"]. SQL:["+sql+"]. Error:["+e.getMessage()+"]", e);
		} finally {
			UtilBase.cerrarComponentes(log, null, st, c);
		}
		return configurado;
	}
	
	public boolean crearEG(Log log, String nombreEvento, String adminAlta) {
		String sql		= "";
		Connection c 	= null;
		Statement st 	= null;
		int rs 	= 0;
		boolean creado = false;
		try {
			c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String now = sdf.format(new Date());
			//despues modificar la tabla eventos globales y agregar lo de que se sume el id para que sea unico
			sql = "INSERT INTO eventos_globales ( nombre_evento, activo, fecha_creacion, admin_alta) "
					+ "VALUES ('"+nombreEvento+"', false, '"+now+"', '"+adminAlta+"') ";
			st = c.createStatement();
			rs = st.executeUpdate(sql);
			if (rs>0) {
				creado = true;
			}
		} catch(Throwable e) {
    		log.log("ERROR al crear EVENTO GLOBAL o ["+nombreEvento+"]. SQL:["+sql+"]. Error:["+e.getMessage()+"]", e);
		} finally {
			UtilBase.cerrarComponentes(log, null, st, c);
		}
		return creado;
	}
	
	
	public String getRolUsuario(String user) {
		String rol		= "";
		String sql		= "";
		Connection c 	= null;
		Statement st 	= null;
		ResultSet rs 	= null;
		Log log 		= null;
		try {
			log = new Log("Obtener_rol.log",true);
    		log.log("Comienza obtencion de rol  del  Usuario:["+user+"]");
    		sql	= 	"SELECT niver_acceso " +
    				"FROM usuarios "+
    				"WHERE username='"+user+"';";

    		c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
    		st = c.createStatement();
    		rs = st.executeQuery(sql);
    		if(rs.next() ) {
    			rol = rs.getString("niver_acceso");
    		} else {
        		rol = Constantes.SIN_ROL;
    		}
		} catch(Throwable e) {
    		log.log("ERROR al obtener rol del usuario ["+user+"]. SQL:["+sql+"]. Error:["+e.getMessage()+"]", e);
		} finally {
			UtilBase.cerrarComponentes(log, rs, st, c);
		}
		return rol;
	}
	
	/**
	 * Valida token en la base de postgres
	 */
	public boolean validarToken(Log log, String user, String token) {
		Connection c 	= null;
		Statement st 	= null;
		ResultSet rs 	= null;
		String sql		= "";
		try {
    		log.log("Comienza validacion del token. Usuario:["+user+"], Token:["+token+"]");

    		sql	= 	"SELECT username, token " +
    				"FROM autenticacion "+
    				"WHERE username='"+user+"';";

    		c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
    		st = c.createStatement();
    		rs = st.executeQuery(sql);

    		if(rs.next() ) {
    			String tok = rs.getString("token");
        		if(token.equals(tok)) {
            		log.log("Se valida correctamente el token. Usuario:["+user+"], Token:["+token+"]");
            		return true;
        		} else {
            		log.log("ERROR NO se valida correctamente el token. Usuario:["+user+"], Token:["+token+"]");
            		return false;
        		}	
    		} else {
        		log.log(" No existe registro para  Usuario:["+user+"]");
        		return false;
    		}
		} catch(Throwable e) {
    		log.log("ERROR al validar token. SQL:["+sql+"]. Error:["+e.getMessage()+"]", e);
    		return false;
		} finally {
			UtilBase.cerrarComponentes(log, rs, st, c);
		}
	}
	
	/**
	 * Valida usuario en la BD de postgres
	 */
	public boolean validarUsuario(String username, String password) {
		Connection c = null;
		Statement st = null;
		ResultSet rs = null;
		String sql = "";
		boolean valido = false;
		Log log = null;
		try {
			log = new Log("basic_auth.log", true);
			log.log("Se pide validacion de Usuario ["+username+"], Encriptada:["+password+"]");
			c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
			sql = 	"SELECT username "+
					"FROM usuarios "+
					"WHERE username='"+username+"' AND password='"+password+"'";
			st = c.createStatement();
			rs = st.executeQuery(sql);
			valido = (rs.next()) ? true : false;
			log.log("Validacion de usuario ["+username+"] retorna ["+String.valueOf(valido)+"]. SQL:["+sql+"]");
		} catch(Throwable t) {
			log.log("No es posible validar el usuario. Usuario ["+username+"], Password:["+password+"] Error:["+t.getMessage()+"]", t);
		} finally {
			UtilBase.cerrarComponentes(log, rs, st, c);
		}
		return valido;
	}


	public boolean activarEG(Log log, String id_evento, String adminAlta) {
		boolean activado = false;
		Connection c = null;
		Statement st = null;
		int  rs = 0;
		String sql = "";
		try {
			log.log("Se activa Evento GLOBAL :["+id_evento+"]");
			c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
			sql = 	"UPDATE eventos_globales  "+
					"SET activo = true, inactivo_desde= null "+
					"WHERE  id_evento = "+id_evento+" AND admin_alta='"+adminAlta+"';";
			st = c.createStatement();
			rs = st.executeUpdate(sql);
			activado = (rs > 0) ? true : false;
			log.log("Evento  ["+id_evento+"] ACTIVADO");
		} catch(Throwable t) {
			log.log("No es posible activar EVENTO GLOBAL ["+id_evento+"] Error:["+t.getMessage()+"].SQL ["+sql+"]", t);
		} finally {
			UtilBase.cerrarComponentes(null, null, st, c);
		}
		return activado;
	}
	public boolean suscribir_usuario_eg(Log log, long idEvento, String usuario) {
		boolean suscripto = false;
		Connection c = null;
		Statement st = null;
		int  rs = 0;
		String sql = "";
		try {
			log.log("Se intenta suscribir usuario ["+usuario+"] al evento global de id :["+idEvento+"]");
			c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
			c.setAutoCommit(false);
			SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
			String now = sf.format(new Date());
			String deleteSusCanc =  "DELETE FROM suscripciones_eventos_globales WHERE usuario='"+usuario+"' AND id_evento_global="+idEvento;
			
			sql = 	"INSERT INTO suscripciones_eventos_globales (id_evento_global, fecha_suscpricion, usuario, activa) "
					+ " VALUES  ("+idEvento+", '"+now+"', '"+usuario+"', true);";
			
			st = c.createStatement();
			boolean comprobar = (st.executeUpdate(deleteSusCanc) > 0);
        	log.log("Se elimina suscripcion cancelada y se inserta nueva comprobacion ["+comprobar+"] : ");
			rs = st.executeUpdate(sql);
			suscripto = (rs > 0) ? true : false;
			c.commit();
			log.log("Usuario ["+usuario+"] suscrito : ["+suscripto+"] ");
		} catch(Throwable t) {
			try {
            	c.rollback();
        	} catch (Throwable t2) {
            	log.log("Error al realizar rollback : "+t2.getMessage()+"]", t);
        	}
			log.log("No es posible suscribirse usuario a EVENTO GLOBAL ["+idEvento+"] Error:["+t.getMessage()+"].SQL ["+sql+"]", t);
		} finally {
			UtilBase.cerrarComponentes(null, null, st, c);
		}
		return suscripto;
	}
	public boolean deleteSusEG(Log log, long idEvento, String usuario) {
		boolean cancelado = false;
		Connection c = null;
		Statement st = null;
		int  rs = 0;
		String sql = "";
		try {
			log.log("Se intenta cancelar subsripcion de usuario ["+usuario+"] al evento global de id :["+idEvento+"]");
			c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
			SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
			String now = sf.format(new Date());
			sql = 	"UPDATE suscripciones_eventos_globales "
					+ " SET activa = false, fecha_canc = '"+now+"' "
					+ " WHERE usuario = '"+usuario+"' AND id_evento_global="+idEvento;
			st = c.createStatement();
			rs = st.executeUpdate(sql);
			cancelado = (rs > 0) ? true : false;
			log.log("Usuario ["+usuario+"] borra suscripcion : ["+cancelado+"] ");
		} catch(Throwable t) {
			log.log("No es posible suscribirse usuario a EVENTO GLOBAL ["+idEvento+"] Error:["+t.getMessage()+"].SQL ["+sql+"]", t);
		} finally {
			UtilBase.cerrarComponentes(null, null, st, c);
		}
		return cancelado;
	}
	
	//Devuelve lista de usuarios activos
	public List<Usuario> getUsuariosActivos (Log log){
		List<Usuario> usuariosActivos = null;
		Connection c = null;
		Statement st = null;
		ResultSet  rs = null;
		String sql = "";
		try {
			log.log("Se consultan usuarios activos");
			c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
			sql = 	"SELECT username, mail, niver_acceso, numero_cel"
					+ " FROM usuarios"
					+ " WHERE usuarios.descripcion_baja IS NULL ";
			st = c.createStatement();
			rs = st.executeQuery(sql);
			usuariosActivos = new ArrayList<Usuario>();
			while(rs.next()) {
				Usuario u = new Usuario();
				u.setUsername(rs.getString("username"));
				u.setMail(rs.getString("mail"));
				u.setNivel_acceso(rs.getString("niver_acceso"));
				u.setNumero_cel(rs.getString("numero_cel"));
				usuariosActivos.add(u);	
			}
		} catch(Throwable t) {
			log.log("No es posible conusltar nodos activos  Error:["+t.getMessage()+"].SQL ["+sql+"]", t);
		} finally {
			UtilBase.cerrarComponentes(null, rs, st, c);
		}
		return usuariosActivos;
	}
	
	
	public List<String> getNodosActivos (Log log){
		List<String> nodosActivos = null;
		Connection c = null;
		Statement st = null;
		ResultSet  rs = null;
		String sql = "";
		try {
			log.log("Se consultan nodos activos");
			c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
			sql = 	"SELECT nombre "
					+ "FROM nodos "
					+ "WHERE activo = true AND fecha_creacion IS NOT NULL ";
			st = c.createStatement();
			rs = st.executeQuery(sql);
			nodosActivos = new ArrayList<String>();
			while(rs.next()) {
				nodosActivos.add(rs.getString("nombre"));
			}
		} catch(Throwable t) {
			log.log("No es posible conusltar nodos activos  Error:["+t.getMessage()+"].SQL ["+sql+"]", t);
		} finally {
			UtilBase.cerrarComponentes(null, rs, st, c);
		}
		return nodosActivos;
	}
	public boolean desactivarEG(Log log, String id_evento, String adminAlta) {
		boolean desactivar = false;
		Connection c = null;
		Statement st = null;
		int  rs = 0;
		String sql = "";
		try {
			log.log("Se desactiva Evento GLOBAL :["+id_evento+"]");
			c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
			SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
			String ahora = sdf.format(new Date());
			sql = 	"UPDATE eventos_globales  "+
					"SET activo = false, inactivo_desde= '"+ahora+"' "+
					"WHERE  id_evento = "+id_evento+" AND admin_alta='"+adminAlta+"';";
			st = c.createStatement();
			rs = st.executeUpdate(sql);
			desactivar = (rs > 0) ? true : false;
			log.log("Evento  ["+id_evento+"] DESACTIVADO");
		} catch(Throwable t) {
			log.log("No es posible desactiva EVENTO GLOBAL ["+id_evento+"] Error:["+t.getMessage()+"].SQL ["+sql+"]", t);
		} finally {
			UtilBase.cerrarComponentes(null, null, st, c);
		}
		return desactivar;
	}
	public List<SuscripcionEG> getSuscripcionesEGActivas(Log log) {
		List<SuscripcionEG> suscripcionesActivas = null;
		Connection c = null;
		Statement st = null;
		ResultSet  rs = null;
		String sql = "";
		try {
			log.log("Se consultan suscripciones activas");
			c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
			sql = 	"SELECT * "
					+ " FROM suscripciones_eventos_globales "
					+ "WHERE activa=true ";
			st = c.createStatement();
			rs = st.executeQuery(sql);
			suscripcionesActivas = new ArrayList<SuscripcionEG>();
			while(rs.next()) {
				SuscripcionEG sus = new SuscripcionEG();
				sus.setActiva(rs.getBoolean("activa"));
				sus.setFecha_canc(rs.getString("fecha_canc"));
				sus.setFecha_suscripcion(rs.getString("fecha_suscpricion"));
				sus.setId_eventoG(rs.getLong("id_evento_global"));
				sus.setId_sus_eg(rs.getLong("id_suscripcion_eg"));
				sus.setUsuario(rs.getString("usuario"));
				suscripcionesActivas.add(sus);
			}
		} catch(Throwable t) {
			log.log("No es posible conusltar suscripciones activos  Error:["+t.getMessage()+"].SQL ["+sql+"]", t);
		} finally {
			UtilBase.cerrarComponentes(null, rs, st, c);
		}
		return suscripcionesActivas;
	}
	public Map<Long, EventoGConf> getMapEventosYConf(Log log) {
		Map<Long, EventoGConf> eventsYconf = null;
		Connection c = null;
		Statement st = null;
		ResultSet  rs = null;
		String sql = "";
		try {
			log.log("Se consultan eventos y configuraciones ");
			c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
			sql = 	"SELECT * FROM conf_eventos_globales ";
			st = c.createStatement();
			rs = st.executeQuery(sql);
			eventsYconf = new HashMap<Long, EventoGConf>();
			while(rs.next()) {
				//Key del mapa
				long idEvento = rs.getLong("id_evento");
				
				//Valor
				EventoGConf conf = new EventoGConf();
				conf.setAlerta(rs.getString("alerta"));
				conf.setFechaMod(rs.getString("fecha_ultima_mod"));
				conf.setIdEvento(idEvento);
				conf.setNivel(rs.getInt("nivel"));
				conf.setTipo(rs.getInt("tipo"));
				
				eventsYconf.put(idEvento, conf);
				
			}
		} catch(Throwable t) {
			log.log("No es posible conusltar eventos y configuraciones  Error:["+t.getMessage()+"].SQL ["+sql+"]", t);
		} finally {
			UtilBase.cerrarComponentes(null, rs, st, c);
		}
		return eventsYconf;
	}
	public List<TipoEventos> recuperarTipoDeEventos(Log log) {
		List<TipoEventos> listaTiposEventos = null;
		Connection c = null;
		Statement st = null;
		ResultSet  rs = null;
		String sql = "";
		try {
			log.log("Se consultan tipos de eventos");
			c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
			sql = 	"SELECT * FROM tipos_eventos ";
			st = c.createStatement();
			rs = st.executeQuery(sql);
			listaTiposEventos = new ArrayList<TipoEventos>();
			while(rs.next()) {
				TipoEventos tipos = new TipoEventos();
				tipos.setId_tipo(rs.getInt("id_tipo"));
				tipos.setDescripcion(rs.getString("descripcion"));
				tipos.setNombre_tipo(rs.getString("nombre_tipo"));
				
				listaTiposEventos.add(tipos);
			}
		} catch(Throwable t) {
			log.log("No es posible recuperar tipos de eventos. Error:["+t.getMessage()+"].SQL ["+sql+"]", t);
		} finally {
			UtilBase.cerrarComponentes(null, rs, st, c);
		}
		return listaTiposEventos;
	}
	public boolean modificarEG(Log log, long idEvento, int tipo, int nuevoNivel, String nuevaAlerta, String rol,
			String usuario) {
		boolean modificado = false;
		Connection c = null;
		Statement st = null;
		int  rs = 0;
		String sql = null;
		
		try {
			log.log("Se modifica Evento GLOBAL :["+idEvento+"]");
			c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String ahora = sdf.format(new Date());
			if(rol.equals("SA")) {
				sql = "UPDATE conf_eventos_globales "
						+ " SET nivel = "+nuevoNivel+", alerta = '"+nuevaAlerta+"', fecha_ultima_mod = '"+ahora+"' "
						+ " WHERE id_evento = "+idEvento+" and tipo = "+tipo+";";
			}else {
				sql = "UPDATE conf_eventos_globales "
						+ " SET nivel = "+nuevoNivel+", alerta = '"+nuevaAlerta+"', fecha_ultima_mod = '"+ahora+"' "
						+ " WHERE id_evento = "+idEvento+" and tipo = "+tipo
						+ " AND  usuario='"+usuario+"';";
			}

			st = c.createStatement();
			rs = st.executeUpdate(sql);
			modificado = (rs > 0) ? true : false;
			log.log("Evento  ["+idEvento+"] MODIFICADO CON EXITO POR EL USUARIO ["+usuario+"]");
		} catch(Throwable t) {
			log.log("No es posible modificar EVENTO GLOBAL ["+idEvento+"] Error:["+t.getMessage()+"].SQL ["+sql+"]", t);
		} finally {
			UtilBase.cerrarComponentes(null, null, st, c);
		}
		return modificado;
	}
	public boolean deteleConfEG(Log log, long idEvento, int tipo, String rol, String usuario) {
		boolean borrado = false;
		Connection c = null;
		Statement st = null;
		int  rs = 0;
		String sql = null;
		
		try {
			log.log("Se elimina Evento GLOBAL :["+idEvento+"]");
			c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
			if(rol.equals("SA")) {
				sql = "DELETE FROM conf_eventos_globales WHERE id_evento = "+idEvento+" and tipo = "+tipo+";";
			}else {
				sql = "DELETE FROM conf_eventos_globales WHERE id_evento = "+idEvento+" and tipo = "+tipo+" and usuario='"+usuario+"';";
			}
			st = c.createStatement();
			rs = st.executeUpdate(sql);
			borrado = (rs > 0) ? true : false;
			log.log("Evento  ["+idEvento+"] ELIMINADO CON EXITO POR EL USUARIO ["+usuario+"]");
		} catch(Throwable t) {
			log.log("No es posible eliminar EVENTO GLOBAL ["+idEvento+"] Error:["+t.getMessage()+"].SQL ["+sql+"]", t);
		} finally {
			UtilBase.cerrarComponentes(null, null, st, c);
		}
		return borrado;
	}
	public List<NivelesAlertasEG> getNivelesAlertasEG(Log log) {
		List<NivelesAlertasEG> listaNivelesAlertas = null;
		Connection c = null;
		Statement st = null;
		ResultSet  rs = null;
		String sql = "";
		try {
			log.log("Se consultan niveles de alertas de eventos");
			c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
			sql = 	"SELECT * FROM niveles_alertas_eventos ";
			st = c.createStatement();
			rs = st.executeQuery(sql);
			listaNivelesAlertas = new ArrayList<NivelesAlertasEG>();
			while(rs.next()) {
				NivelesAlertasEG niveles = new NivelesAlertasEG();
				niveles.setDescripcion(rs.getString("descripcion"));
				niveles.setId_nivel(rs.getInt("id_nivel"));
				niveles.setNombre(rs.getString("nombre"));
				listaNivelesAlertas.add(niveles);
			}
		} catch(Throwable t) {
			log.log("No es posible recuperar niveles de alertas de eventos. Error:["+t.getMessage()+"].SQL ["+sql+"]", t);
		} finally {
			UtilBase.cerrarComponentes(null, rs, st, c);
		}
		return listaNivelesAlertas;
	}

	public boolean altaUsuario(Log log, String username, String password, String mail, String numero_cel,
			String adminAlta) {
		String sql		= "";
		Connection c 	= null;
		Statement st 	= null;
		int rs 	= 0;
		boolean creado = false;
		try {
			c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String now = sdf.format(new Date());
			sql = "INSERT INTO usuarios (username, password, mail, fecha_alta, usuario_alta, numero_cel, niver_acceso, crea_nodos) "
					+ "VALUES ('"+username+"', '"+password+"', '"+mail+"' ,'"+now+"', '"+adminAlta+"', '"+numero_cel+"', '"+Constantes.USUARIO_COMUN+"', false) ";
			st = c.createStatement();
			rs = st.executeUpdate(sql);
			if (rs>0) {
				creado = true;
			}
		} catch(Throwable e) {
    		log.log("ERROR al dar de alta usuario ["+username+"]. SQL:["+sql+"]. Error:["+e.getMessage()+"]", e);
		} finally {
			UtilBase.cerrarComponentes(log, null, st, c);
		}
		return creado;
	}

	public boolean modificarUsuario(Log log, String usuario, String password, String mail, String numero_cel, String descripcon) {
		boolean modificado = false;
		Connection c = null;
		Statement st = null;
		int  rs = 0;
		String sql = "";
		try {
			log.log("Se modifica Usuario :["+usuario+"]");
			c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String now = sdf.format(new Date());
			sql = 	"UPDATE usuarios  "+
					"SET password = '"+password+"',  mail = '"+mail+"', numero_cel = '"+numero_cel+"', fecha_mod = '"+now+"', descripcion_mod = '"+descripcon+"' "+
					"WHERE  username = '"+usuario+"';";
			st = c.createStatement();
			rs = st.executeUpdate(sql);
			modificado = (rs > 0) ? true : false;
			log.log("Usuario  ["+usuario+"] MODIFICADO");
		} catch(Throwable t) {
			log.log("No es posible modificar usuario ["+usuario+"] Error:["+t.getMessage()+"].SQL ["+sql+"]", t);
		} finally {
			UtilBase.cerrarComponentes(null, null, st, c);
		}
		return modificado;
	}

	public boolean bajaUsuario(Log log, String usuario, String password, String descripcion) {
		boolean dadoDeBaja = false;
		Connection c = null;
		Statement st = null;
		int  rs = 0;
		String sql = "";
		try {
			log.log("Se da de baja Usuario :["+usuario+"]");
			c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String now = sdf.format(new Date());
			sql = 	"UPDATE usuarios  "+
					"SET  fecha_mod = '"+now+"', descripcion_baja = '"+descripcion+"' "+
					"WHERE  username = '"+usuario+"' AND password='"+password+"';";
			st = c.createStatement();
			rs = st.executeUpdate(sql);
			dadoDeBaja = (rs > 0) ? true : false;
			log.log("Usuario  ["+usuario+"] DADO DE BAJA");
		} catch(Throwable t) {
			log.log("No es posible dar de baja usuario ["+usuario+"] Error:["+t.getMessage()+"].SQL ["+sql+"]", t);
		} finally {
			UtilBase.cerrarComponentes(null, null, st, c);
		}
		return dadoDeBaja;
	}

	public Usuario getDatosUsuario(Log log, String usuario) {
		Usuario datosUsuario	= null;
		Connection c 			= null;
		Statement st 			= null;
		ResultSet rs 			= null;
		String sql				= "";
		try {
    		log.log("Comienza obtencion de datos  del  Usuario:["+usuario+"]");
    		sql	= 	"SELECT username, mail, niver_acceso, numero_cel " +
    				"FROM usuarios "+
    				"WHERE username='"+usuario+"';";

    		c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
    		st = c.createStatement();
    		rs = st.executeQuery(sql);
    		datosUsuario = new Usuario();
    		while(rs.next()) {
    			datosUsuario.setMail(rs.getString("mail"));
    			datosUsuario.setNivel_acceso(rs.getString("niver_acceso"));
    			datosUsuario.setNumero_cel(rs.getString("numero_cel"));
    			datosUsuario.setUsername(usuario);
    		}
		} catch(Throwable e) {
    		log.log("ERROR al obtener datos del usuario ["+usuario+"]. SQL:["+sql+"]. Error:["+e.getMessage()+"]", e);
		} finally {
			UtilBase.cerrarComponentes(log, rs, st, c);
		}
		return datosUsuario;
	}

	public boolean insertNotificacion(Log log, Notis notificicacion) {
		String sql		= "";
		Connection c 	= null;
		Statement st 	= null;
		int rs 	= 0;
		boolean insertada = false;
		try {
			c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
			sql = "INSERT INTO notificaciones ( id_evento_global, fecha_dispara, tipo, condicion_dispara, entregada, usuario_recibe) "
					+ "VALUES ("+notificicacion.getId_evento_global()+", '"+notificicacion.getFecha_dispara()+"', "+notificicacion.getTipo()+", '"+notificicacion.getCondicion_dispara()+"', "+notificicacion.isEntregada()+", '"+notificicacion.getUsuario_recibe()+"') ";
			st = c.createStatement();
			rs = st.executeUpdate(sql);
			if (rs>0) {
				insertada = true;
			}
		} catch(Throwable e) {
    		log.log("ERROR al insertar NOTIFICACION de EVENTO ["+notificicacion.getId_evento_global()+"]. SQL:["+sql+"]. Error:["+e.getMessage()+"]", e);
		} finally {
			UtilBase.cerrarComponentes(log, null, st, c);
		}
		return insertada;
		
	}

	public List<Notis> getNotisTodas(Log log) {
		String sql		= "";
		List<Notis> notis = null;
		Connection c 	= null;
		Statement st 	= null;
		ResultSet rs	= null;
		try {
			log.log("Recuperando lista de notificaiones totales en BD....");
			notis = new ArrayList<Notis>();
			c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
			sql = "SELECT * FROM notificaciones ";
			st = c.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				Notis noti = new Notis();
				noti.setId_noti(rs.getLong("id_noti"));
				noti.setCondicion_dispara(rs.getString("condicion_dispara"));
				noti.setEntregada(rs.getBoolean("entregada"));
				noti.setFecha_dispara(rs.getString("fecha_dispara"));
				noti.setId_evento_global(rs.getLong("id_evento_global"));
				noti.setTipo(rs.getShort("tipo"));
				noti.setUsuario_recibe(rs.getString("usuario_recibe"));
				notis.add(noti);
			}
		}catch(Throwable t) {
			log.log("No fue posible recuperar lista de eventos en BD. SQL ["+sql+"]. debido a ["+t.getMessage()+"]");
		}finally {
			UtilBase.cerrarComponentes(null, rs, st, c);
		}
		return notis;
	}

	public List<Notis> getMisNotis(Log log, String usuario) {
		String sql		= "";
		List<Notis> notis = null;
		Connection c 	= null;
		Statement st 	= null;
		ResultSet rs	= null;
		try {
			log.log("Recuperando lista de notificaiones para el usuario ["+usuario+"] en BD....");
			notis = new ArrayList<Notis>();
			c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
			sql = "SELECT * FROM notificaciones WHERE usuario_recibe = '"+usuario+"'";
			st = c.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				Notis noti = new Notis();
				noti.setId_noti(rs.getLong("id_noti"));
				noti.setCondicion_dispara(rs.getString("condicion_dispara"));
				noti.setEntregada(rs.getBoolean("entregada"));
				noti.setFecha_dispara(rs.getString("fecha_dispara"));
				noti.setId_evento_global(rs.getLong("id_evento_global"));
				noti.setTipo(rs.getShort("tipo"));
				noti.setUsuario_recibe(rs.getString("usuario_recibe"));
				notis.add(noti);
			}
		}catch(Throwable t) {
			log.log("No fue posible recuperar lista de notificaciones en BD. SQL ["+sql+"]. debido a ["+t.getMessage()+"]");
		}finally {
			UtilBase.cerrarComponentes(null, rs, st, c);
		}
		return notis;
	}

	public List<EventoGIdYFechaInactivo> getListaEventosInactivos(Log log) {
		String sql		= "";
		List<EventoGIdYFechaInactivo> eventosInactivos = null;
		Connection c 	= null;
		Statement st 	= null;
		ResultSet rs	= null;
		try {
			log.log("Recuperando lista de eventos inactivos en BD....");
			eventosInactivos = new ArrayList<EventoGIdYFechaInactivo>();
			c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
			sql = "SELECT id_evento, nombre_evento, admin_alta, inactivo_desde FROM eventos_globales WHERE activo=false";
			st = c.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				EventoGIdYFechaInactivo eventoInac = new EventoGIdYFechaInactivo();
				eventoInac.setUsuarioCreador(rs.getString("admin_alta"));
				eventoInac.setIdEvento(rs.getLong("id_evento"));
				eventoInac.setFechaInactividad( rs.getString("inactivo_desde"));
				eventoInac.setNombreEvento(rs.getString("nombre_evento"));
				eventosInactivos.add(eventoInac);
			}
		}catch(Throwable t) {
			log.log("No fue posible recuperar lista de eventos inactivos en BD. SQL ["+sql+"]. debido a ["+t.getMessage()+"]");
		}finally {
			UtilBase.cerrarComponentes(log, rs, st, c);
		}
		return eventosInactivos;
	}

	public boolean eliminarConfEventosInactivos(long idEvento, Log log) {
		boolean borrado = false;
		Connection c = null;
		Statement st = null;
		int  rs = 0;
		String sql = null;
		
		try {
			log.log("Se elimina configuraciones de Evento GLOBAL INACTIVOS :["+idEvento+"]");
			c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
			sql = "DELETE FROM conf_eventos_globales WHERE id_evento = "+idEvento+";";
			st = c.createStatement();
			rs = st.executeUpdate(sql);
			borrado = (rs > 0) ? true : false;
			log.log("Evento  ["+idEvento+"] ELIMINADO CON EXITO POR INACTIVIDAD ");
		} catch(Throwable t) {
			log.log("No es posible eliminar EVENTO GLOBAL POR INACTIVIDAD["+idEvento+"] Error:["+t.getMessage()+"].SQL ["+sql+"]", t);
		} finally {
			UtilBase.cerrarComponentes(null, null, st, c);
		}
		return borrado;
	}

	public boolean elminarSusEventosInactivos(long idEvento, Log log) {
		boolean borrado = false;
		Connection c = null;
		Statement st = null;
		int  rs = 0;
		String sql = null;
		
		try {
			log.log("Se eliminas suscripciones de Evento GLOBAL INACTIVOS :["+idEvento+"]");
			c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
			sql = "DELETE FROM suscripciones_eventos_globales WHERE id_evento_global = "+idEvento+";";
			st = c.createStatement();
			rs = st.executeUpdate(sql);
			borrado = (rs > 0) ? true : false;
			log.log("Suscripciones a Evento  ["+idEvento+"] ELIMINADAS CON EXITO POR INACTIVIDAD ");
		} catch(Throwable t) {
			log.log("No es posible eliminar suscripciones de EVENTO GLOBAL POR INACTIVIDAD["+idEvento+"] Error:["+t.getMessage()+"].SQL ["+sql+"]", t);
		} finally {
			UtilBase.cerrarComponentes(null, null, st, c);
		}
		return borrado;
	}

	public String recuperarMailUsuarioCreador(String usuarioCreador, Log log) {
		String mailUsuarioCreador = "";
		Connection c = null;
		Statement st = null;
		ResultSet  rs = null;
		String sql = "";
		try {
			log.log("Se recupera mail del usuario creador["+usuarioCreador+"] del evento global inactivo para avisar");
			c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
			sql = 	"SELECT mail FROM usuarios WHERE usuarios.username='"+usuarioCreador+"' LIMIT 1";
			st = c.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next()) {
				mailUsuarioCreador = rs.getString("mail");
			}
		} catch(Throwable t) {
			log.log("No es posible recuperar mail del usuario debido a. Error:["+t.getMessage()+"].SQL ["+sql+"]", t);
		} finally {
			UtilBase.cerrarComponentes(null, rs, st, c);
		}
		return mailUsuarioCreador;
	}

	public void eliminarCabezalEventoGlobalInactivo(long idEvento, Log log) {
		Connection c = null;
		Statement st = null;
		int  rs = 0;
		String sql = null;
		
		try {
			log.log("Se elimina cabezal de Evento GLOBAL INACTIVOS :["+idEvento+"]");
			c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
			sql = "DELETE FROM eventos_globales WHERE id_evento = "+idEvento+";";
			st = c.createStatement();
			rs = st.executeUpdate(sql);
			boolean borrado = (rs > 0) ? true : false;
			if(borrado)
				log.log("Cabezal de Evento  ["+idEvento+"] ELIMINADAS CON EXITO POR INACTIVIDAD ");
		} catch(Throwable t) {
			log.log("No es posible eliminar suscripciones de EVENTO GLOBAL POR INACTIVIDAD["+idEvento+"] Error:["+t.getMessage()+"].SQL ["+sql+"]", t);
		} finally {
			UtilBase.cerrarComponentes(null, null, st, c);
		}
	}

	public boolean verificarNotiEntregada(Log log, long idEvento, int tipo, String usuarioRecibe) {
		boolean notiYaEntregada = false;
		Connection c = null;
		Statement st = null;
		ResultSet  rs = null;
		String sql = null;
		try {
			log.log("Se consulta si la notificacion debe ser enviada o ya fue procesada :["+idEvento+"]");
			c = Conexion.getInstancia().getConexion(log, UtilBase.DATASOURCE);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String unaHoraAtras = sdf.format(new Date(System.currentTimeMillis() - (1 * 60 * 60 * 1000)));
			sql = "SELECT id_noti "
					+ " FROM notificaciones "
					+ " WHERE id_evento_global = "+idEvento+" "
					+ " AND fecha_dispara>'"+unaHoraAtras+"' "
					+ " AND usuario_recibe='"+usuarioRecibe+"' AND entregada=true;";
			st = c.createStatement();
			rs = st.executeQuery(sql);
			notiYaEntregada = (rs.next()) ? true : false;
			if(notiYaEntregada) {
				log.log("No se debe enviar notificacion de nuevo");
				notiYaEntregada = true;
			}else {
				log.log("Se debe enviar notificacion");
			}
		} catch(Throwable t) {
			log.log("No es posible verificar si se debe enviar notificacion de evento["+idEvento+"] Error:["+t.getMessage()+"].SQL ["+sql+"]", t);
		} finally {
			UtilBase.cerrarComponentes(null, rs, st, c);
		}
		return notiYaEntregada;
	}
	
	
}
