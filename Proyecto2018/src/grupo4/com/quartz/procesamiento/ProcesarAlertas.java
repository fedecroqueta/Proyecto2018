package grupo4.com.quartz.procesamiento;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import grupo4.com.alertas.Email;
import grupo4.com.alertas.Sms;
import grupo4.com.core.manejadores.ManejadorAlertas;
import grupo4.com.core.modelJEE.EventoGConf;
import grupo4.com.core.modelJEE.InfoARevisarNodos;
import grupo4.com.core.modelJEE.Notis;
import grupo4.com.core.modelJEE.SuscripcionEG;
import grupo4.com.core.modelJEE.Usuario;
import grupo4.com.servidor.database.BD;
import grupo4.com.servidor.websocket.realTimeLogs.LogsEndpoint;
import grupo4.com.util.Constantes;
import grupo4.com.util.Log;
import grupo4.com.util.UtilFormato;

@SuppressWarnings("unused")
public class ProcesarAlertas {

	ManejadorAlertas manejadorAlertas = new ManejadorAlertas();
	
	public ProcesarAlertas() {
		super();
	}

	public void ejecutarProcesoAlertas(Log log) {
		List<Usuario> usuariosActivos = null;
		Map<Long, EventoGConf> mapEventosConConfiguracion = null;
		List<SuscripcionEG> suscripciones = null;
		Map<String, InfoARevisarNodos> infoNodos = null;
		List<String> nodos = null;
		try {
			log.log("Se procesan alertas...");
			BD base = new BD();

			// Recupero usuarios
			usuariosActivos = base.getUsuariosActivos(log);

			// Recupero Nodos
			nodos = base.getNodosActivos(log);
			infoNodos = new HashMap<String, InfoARevisarNodos>();

			// Armo Mapa de Nodos y sus informaciones relevantes a cuestionar
			for (int i = 0; i < nodos.size(); i++) {
				String nodo = nodos.get(i);
				InfoARevisarNodos inf = manejadorAlertas.getInfoARevisar(log, nodo);
				infoNodos.put(nodo, inf);
			}

			// Recupero suscripciones
			suscripciones = base.getSuscripcionesEGActivas(log);

			// Recupero mapa de eventos y sus configuraciones
			mapEventosConConfiguracion = base.getMapEventosYConf(log);

			// Hasta aca tengo toda la informacion que preciso para manejar las alertas
			// despues seria algo como consultar por la alerta y tipo, matchear con
			// informacion relevante de los nodos
			// si valor a revisar supera a alerta veo el nivel y veo si mandar sms o mail o
			// ambas o notificaicon en pantalla o todas

			// Para cada evento
			for (Map.Entry<Long, EventoGConf> evento : mapEventosConConfiguracion.entrySet()) {
				long idEvento = evento.getKey();
				EventoGConf confEvento = evento.getValue();

				// Me fijo las suscripciones y me quedo con la del evento
				List<SuscripcionEG> susEg = new ArrayList<SuscripcionEG>();
				for (int i = 0; i < suscripciones.size(); i++) {
					SuscripcionEG s = suscripciones.get(i);
					if (s.getId_eventoG() == idEvento) {
						susEg.add(s);
					}
				}

				// Hasta aca tengo las suscripciones de usuarios que me corresponde
				// para ese evento

				// Ahora de cada suscrpcion saco el usuario que corresponde notificar
				List<Usuario> usuariosANotificar = new ArrayList<Usuario>();
				for (int j = 0; j < susEg.size(); j++) {
					SuscripcionEG sus = susEg.get(j);
					String usernameSuscripto = sus.getUsuario();
					for (int k = 0; k < usuariosActivos.size(); k++) {
						Usuario uActivo = usuariosActivos.get(k);
						if (usernameSuscripto.equals(uActivo.getUsername())) {
							Usuario u = new Usuario();
							u = uActivo;
							usuariosANotificar.add(u);
						}
					}
				}
				int tipo = confEvento.getTipo();
				String alerta = confEvento.getAlerta();
				int nivel = confEvento.getNivel();

				// me fijo nodos que estoy parado y agarro info de ese nodo para comparar
				for (int l = 0; l < nodos.size(); l++) {
					String n = nodos.get(l);
					InfoARevisarNodos info = infoNodos.get(n);
					String signo = alerta.substring(0, 1);
					double valor = Double.parseDouble(alerta.substring(1));

					// Contemplo los signos de las alertas a analizar
					if (signo.equals(">")) {// Mayor
						
						//Empiezo a preparar usuarios notificados para guardar esas ntois en BD
						List<Usuario> usuariosNotificados = new ArrayList<Usuario>();
						
						// Contemplo tipos
						if (tipo == Constantes.RAM) {
							if (info.getFreeMemory() > valor) {
								String cuerpoMailConEspacios = "Se dispara evento "+idEvento+" debido a Memoria RAM mayor de "+valor+" para el nodo "+n;
								String cuerpoMail = UtilFormato.remplazarEspacios(cuerpoMailConEspacios);
								if (info.getFreeMemory() > valor) {
									usuariosNotificados= controlNotificacion(nivel, cuerpoMailConEspacios, usuariosANotificar);
								}
							}	
						} else if (tipo == Constantes.CPU) {
							String cuerpoMailConEspacios = "Se dispara evento ["+idEvento+"] debido a CPU mayor de "+valor+" para el nodo "+n;
							String cuerpoMail = UtilFormato.remplazarEspacios(cuerpoMailConEspacios);
							if (info.getCpuLoad() > valor) {
								usuariosNotificados = controlNotificacion(nivel, cuerpoMailConEspacios, usuariosANotificar);
							}
						} else if (tipo == Constantes.DISCO) {
							String cuerpoMailConEspacios = "Se dispara evento "+idEvento+" debido a DISCO mayor de "+valor+" para el nodo "+n;
							String cuerpoMail = UtilFormato.remplazarEspacios(cuerpoMailConEspacios);
							if (info.getFreeDisk() > valor) {
								usuariosNotificados = controlNotificacion(nivel, cuerpoMailConEspacios, usuariosANotificar);
							}
						}
						
						//Guardo notificaciones entregadas en BD
						guardarNotificacionBD(log, usuariosNotificados, info, valor, idEvento, tipo, signo);		

					} else if (signo.equals("<")) { // Menor

						//Empiezo a preparar usuarios notificados para guardar esas ntois en BD
						List<Usuario> usuariosNotificados = new ArrayList<Usuario>();
						
						// Contemplo tipos
						if (tipo == Constantes.RAM) {
							String cuerpoMailConEspacios = "Se dispara evento "+idEvento+" debido a Memoria RAM menor de "+valor+" para el nodo "+n;
							String cuerpoMail = UtilFormato.remplazarEspacios(cuerpoMailConEspacios);
							if (info.getFreeMemory() < valor) {
								usuariosNotificados= controlNotificacion(nivel, cuerpoMailConEspacios, usuariosANotificar);
							}
						} else if (tipo == Constantes.CPU) {
							String cuerpoMailConEspacios = "Se dispara evento "+idEvento+" debido a CPU menor de "+valor+" para el nodo "+n;
							String cuerpoMail = UtilFormato.remplazarEspacios(cuerpoMailConEspacios);
							if (info.getCpuLoad() < valor) {
								usuariosNotificados = controlNotificacion(nivel, cuerpoMailConEspacios, usuariosANotificar);
							}
						} else if (tipo == Constantes.DISCO) {
							String cuerpoMailConEspacios = "Se dispara evento "+idEvento+" debido a DISCO menor de "+valor+" para el nodo "+n;
							String cuerpoMail = UtilFormato.remplazarEspacios(cuerpoMailConEspacios);
							if (info.getFreeDisk() < valor) {
								usuariosNotificados = controlNotificacion(nivel, cuerpoMailConEspacios, usuariosANotificar);
							}
						}
						
						//Guardo notificaciones entregadas en BD
						guardarNotificacionBD(log, usuariosNotificados, info, valor, idEvento, tipo, signo);		
					} else { // Igual
						
						//Empiezo a preparar usuarios notificados para guardar esas ntois en BD
						List<Usuario> usuariosNotificados = new ArrayList<Usuario>();
						
						// Contemplo tipos
						if (tipo == Constantes.RAM) {
							String cuerpoMailConEspacios = "Se dispara evento "+idEvento+" debido a RAM igual de "+valor+" para el nodo "+n;
							String cuerpoMail = UtilFormato.remplazarEspacios(cuerpoMailConEspacios);
							if (info.getFreeMemory() == valor) {
								usuariosNotificados = controlNotificacion(nivel, cuerpoMailConEspacios, usuariosANotificar);
							}
						} else if (tipo == Constantes.CPU) {
							String cuerpoMailConEspacios = "Se dispara evento "+idEvento+" debido a CPU igual de "+valor+" para el nodo "+n;
							String cuerpoMail = UtilFormato.remplazarEspacios(cuerpoMailConEspacios);
							if (info.getCpuLoad() == valor) {
								usuariosNotificados = controlNotificacion(nivel, cuerpoMailConEspacios, usuariosANotificar);
							}
						} else if (tipo == Constantes.DISCO) {
							String cuerpoMailConEspacios = "Se dispara evento "+idEvento+" debido a DISCO igual de "+valor+" para el nodo "+n;
							String cuerpoMail = UtilFormato.remplazarEspacios(cuerpoMailConEspacios);
							if (info.getFreeDisk() == valor) {
								usuariosNotificados = controlNotificacion(nivel, cuerpoMailConEspacios, usuariosANotificar);
							}
						}
						
						//Guardo notificaciones entregadas en BD
						guardarNotificacionBD(log, usuariosNotificados, info, valor, idEvento, tipo, signo);
					}
				}
			}
			log.log("Finaliza procesamiento alertas...");
		} catch (Throwable t) {
			log.log("No es posible procesar alertas debido a [" + t.getMessage() + "]");
		}
	}
	
	private void guardarNotificacionBD(Log log, List<Usuario> usuariosNotificados, InfoARevisarNodos info, double valor, long idEvento, int tipo, String signo ) {
		BD base = new BD();
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");

		for(int i=0; i< usuariosNotificados.size();i++) {
			Notis notificicacion = new Notis();
			String usuario_recibe_notificacion = usuariosNotificados.get(i).getUsername();
			//Construyo la noti
			notificicacion.setEntregada(true);
			
			//Veo que tipo de info es la indicada para la notificacion
			if(tipo == Constantes.RAM) {
				notificicacion.setCondicion_dispara(info.getFreeMemory()+signo+ valor);
			}else if (tipo == Constantes.CPU) {
				notificicacion.setCondicion_dispara(info.getCpuLoad()+signo+ valor);
			}else {
				notificicacion.setCondicion_dispara(info.getFreeDisk()+signo+ valor);

			}
			
			notificicacion.setFecha_dispara(sdf.format(new Date()));
			notificicacion.setId_evento_global(idEvento);
			notificicacion.setTipo(tipo);
			notificicacion.setUsuario_recibe(usuario_recibe_notificacion);
			
			//Inserto la notificacion en BD
			base.insertNotificacion(log, notificicacion);
		}
	}
	
	/**
	 * Controla que tipo de notificacion mandar y es la encargada de llamar a los manejadores que envien dicha notificacion
	 */
	private List<Usuario> controlNotificacion (int nivel, String cuerpoMail, List<Usuario> usuariosANotificar) throws Exception {
		List<Usuario> usuariosNotificados = new ArrayList<Usuario>();
		for (int us = 0; us < usuariosANotificar.size(); us++) {
			Usuario aNoti = usuariosANotificar.get(us);
			if (nivel == Constantes.MAIL) { // Mail
				Email.enviarMail(aNoti.getUsername(), aNoti.getMail(), cuerpoMail);
			} else if (nivel == Constantes.MAIL_Y_SMS) { // Mail y sms
				Email.enviarMail(aNoti.getUsername(), aNoti.getMail(), cuerpoMail);
				Sms.enviarSms(aNoti.getUsername(), aNoti.getMail());
			} else if (nivel == Constantes.SMS) { // Solo SMS
				Sms.enviarSms(aNoti.getUsername(), aNoti.getMail());
			} else {// Solo pantalla
			}
			usuariosNotificados.add(aNoti);
		}
		return usuariosNotificados;
	}
	
}
