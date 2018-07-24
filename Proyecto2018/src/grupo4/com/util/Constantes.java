package grupo4.com.util;

import java.util.Arrays;
import java.util.List;

public class Constantes {

	public static final String KEY = "grupo4";
	public static final String HEADER_TOKEN = "SecurityToken";
	public static final String TOKEN_USER = "Usuario";
	public static final String ROL = "Roles";
	public static final String ADMINISTRADOR_PRIVILEGIOS = "AP";
	public static final String ADMINISTRADOR_SOLO_NODOS = "AN";
	public static final String ADMINISTRADOR_COMUN = "A";
	public static final String SIN_ROL = "SIN_ROL";
	public static final String SUPERVISOR = "SA";
	public static final String USUARIO_COMUN = "UC";
	
	//Tipos Eventos Globales
	public static final int	RAM								=	1;
	public static final int CPU								= 	2;
	public static final int DISCO							= 	3;
	
	//Niveles de alerta al usuario
	public static final int NINGUNA_PANTALLA_SOLO			=	0;
	public static final int MAIL							=	1;
	public static final int SMS								= 	2;
	public static final int MAIL_Y_SMS						=	3;
	
	//Nodos pre definidos
	public static final List<String> nodosPruebaNombres					= Arrays.asList("Servidor Central", "App Montevideo", "Servidor Secundario");
	

}
