package database;

import java.util.ArrayList;
import java.util.List;

import modelos.Usuario;
import util.UtilBD;

public class ManejadorBD {

	public boolean validarUsuario (String sa, String password) {
		boolean valido = false;
		try {
			UtilBD utildatabase = new UtilBD();
			valido = utildatabase.validarUsuario( sa, password);
		}catch(Throwable t){
			System.out.println("Error validando usuario debido a ["+t.getMessage()+"]");
		}
		return valido;
	}
	
	public boolean altaAdmin (Usuario u) {
		boolean alta = false;
		try {
			UtilBD utildatabase = new UtilBD();
			alta = utildatabase.persistirUsuario(u);
		}catch(Throwable t){
			System.out.println("Error validando usuario debido a ["+t.getMessage()+"]");
		}
		return alta;
	}
	
	public boolean validarUsernameUnico(String username) {
		boolean unico = false;
		try {
			UtilBD utildatabase = new UtilBD();
			unico = utildatabase.validarUsernameUnico(username);
		}catch(Throwable t){
			System.out.println("Error validando usuario debido a ["+t.getMessage()+"]");
		}
		return unico;
	}
	
	public static List<Usuario>  getAdministradores(){
		List<Usuario> administradores = new ArrayList<Usuario>();
		try {
			UtilBD utildatabase = new UtilBD();
			administradores = utildatabase.getAdministradores();
		}catch(Throwable t){
			System.out.println("Error validando usuario debido a ["+t.getMessage()+"]");
		}
		return administradores;
	}

	public static Usuario getUsuario(String username) {
		Usuario usuario = new Usuario();
		try {
			UtilBD utildatabase = new UtilBD();
			usuario = utildatabase.getUsuario(username);
		}catch(Throwable t){
			System.out.println("Error validando usuario debido a ["+t.getMessage()+"]");
		}
		return usuario;
	}
}
