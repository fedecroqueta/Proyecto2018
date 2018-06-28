package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import modelos.Usuario;
import vistas.shared.SharedInfo;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@SuppressWarnings("unused")
public class UtilBD {

	public List<Usuario> getAdministradores() {
		String sql = "";
		List<Usuario> administradores = null;
		Connection c = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			administradores = new ArrayList<Usuario>();
			c = getConexionPostgres();
			sql = "SELECT * FROM usuarios WHERE usuarios.niver_acceso<>'UC' AND usuarios.niver_acceso<>'SA'";
			st = c.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				Usuario admin = new Usuario();
				admin.setCrea_nodos(rs.getBoolean("crea_nodos"));
				admin.setDescripcion_baja(rs.getString("descripcion_baja"));
				admin.setDescripcion_mod(rs.getString("descripcion_mod"));
				admin.setFecha_alta(rs.getString("fecha_alta"));
				admin.setFecha_mod(rs.getString("fecha_mod"));
				admin.setMail(rs.getString("mail"));
				admin.setNivel_acceso(rs.getString("niver_acceso"));
				admin.setNumero_celular(rs.getString("numero_cel"));
				admin.setPassword(rs.getString("password"));
				admin.setUsername(rs.getString("username"));
				administradores.add(admin);
			}
		} catch (Throwable t) {
			System.out.println("ERROR recuperando administradores debido a [" + t.getMessage() + "]");
		} finally {
			cerrarComponentes(rs, st, c);
		}
		return administradores;
	}

	public Usuario getUsuario(String username) {
		String sql = "";
		Usuario usuario = null;
		Connection c = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			usuario = new Usuario();
			c = getConexionPostgres();
			sql = "SELECT * FROM usuarios WHERE usuarios.niver_acceso<>'UC' AND username='"+username+"'";
			st = c.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {
				Usuario admin = new Usuario();
				usuario.setCrea_nodos(rs.getBoolean("crea_nodos"));
				usuario.setDescripcion_baja(rs.getString("descripcion_baja"));
				usuario.setDescripcion_mod(rs.getString("descripcion_mod"));
				usuario.setFecha_alta(rs.getString("fecha_alta"));
				usuario.setFecha_mod(rs.getString("fecha_mod"));
				usuario.setMail(rs.getString("mail"));
				usuario.setNivel_acceso(rs.getString("niver_acceso"));
				usuario.setNumero_celular(rs.getString("numero_cel"));
				usuario.setPassword(rs.getString("password"));
			}
		} catch (Throwable t) {
			System.out.println("ERROR recuperando usuario debido a [" + t.getMessage() + "]");
		} finally {
			cerrarComponentes(rs, st, c);
		}
		return usuario;
	}

	public boolean persistirUsuario(Usuario u) {
		boolean valido = false;
		Connection c = null;
		Statement st = null;
		int rs = 0;
		String sql = null;
		try {
			c = getConexionPostgres();

			sql = "INSERT INTO  usuarios (username, password, mail, niver_acceso, crea_nodos, fecha_alta, fecha_mod, descripcion_mod, descripcion_baja, usuario_alta) "
					+ "VALUES ('" + u.getUsername() + "', '" + u.getPassword() + "', '" + u.getMail() + "', '"
					+ u.getNivel_acceso() + "'," + u.isCrea_nodos() + " , '" + u.getFecha_alta()
					+ "', null, null, null, '" + SharedInfo.getUsername() + "');";

			st = c.createStatement();
			rs = st.executeUpdate(sql);
			if (rs > 0) {
				valido = true;
			}
		} catch (Throwable t) {
			System.out.println("Error validando usuario   debido a [" + t.getMessage() + "]");
		} finally {
			cerrarComponentes(null, st, c);
		}
		return valido;
	}

	public boolean validarUsuario(String username, String password) {
		boolean valido = false;
		Connection c = null;
		Statement st = null;
		ResultSet rs = null;
		String sql = null;
		try {
			c = getConexionPostgres();
			sql = "SELECT username " + "FROM usuarios " + "WHERE niver_acceso='SA' " + "AND username ='" + username
					+ "' " + "AND password ='" + password + "' " + "AND descripcion_baja is NULL";
			st = c.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {
				valido = true;
			}
		} catch (Throwable t) {
			System.out.println("Error validando usuario   debido a [" + t.getMessage() + "]");
		} finally {
			cerrarComponentes(rs, st, c);
		}
		return valido;
	}

	public boolean validarUsernameUnico(String username) {
		boolean unico = false;
		Connection c = null;
		Statement st = null;
		ResultSet rs = null;
		String sql = null;
		try {
			c = getConexionPostgres();
			sql = "SELECT username " + "FROM usuarios " + "WHERE username ='" + username + "' ";
			st = c.createStatement();
			rs = st.executeQuery(sql);
			if (!rs.next()) {
				unico = true;
			}
		} catch (Throwable t) {
			System.out.println("Error validando username unico  debido a [" + t.getMessage() + "]");
		} finally {
			cerrarComponentes(rs, st, c);
		}
		return unico;
	}

	// -------------------- CONEXION-----------------------------------------
	/**
	 * Retorna la conexion a la base de datos leyendo archivo de properties
	 * 
	 * @throws IOException
	 */
	public Connection getConexionPostgres() throws SQLException, IOException {
		Connection connection = null;
		Properties prop = new Properties();
		InputStream input = null;
		try {
			// Extraigo credenciales y parametros del archivo de configuracion
			input = new FileInputStream("backoffice.properties");
			prop.load(input);
			String database = prop.getProperty("bd_postgresql");
			String usuario = prop.getProperty("usuario");
			String password = prop.getProperty("password");
			String puerto = prop.getProperty("puerto");
			String host = prop.getProperty("host");

			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection("jdbc:postgresql://" + host + ":" + puerto + "/" + database,
					usuario, password);
		} catch (Throwable t) {
			throw new SQLException("No se pudo obtener conexion con postgre debido a :[" + t.getMessage() + "]", t);
		} finally {
			if (input != null) {
				input.close();
			}
		}
		return connection;
	}

	public static void cerrarComponentes(ResultSet rs, Statement st, Connection c) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Throwable t) {
			System.out.println("Error cerrando ResultSet  debido a [" + t.getMessage() + "]");
		}
		try {
			if (st != null) {
				st.close();
			}
		} catch (Throwable t) {
			System.out.println("Error cerrando Statement debido a [" + t.getMessage() + "]");
		}
		try {
			if (c != null) {
				c.close();
			}
		} catch (Throwable t) {
			System.out.println("Error cerrando Connection debido a [" + t.getMessage() + "]");
		}

	}

}
