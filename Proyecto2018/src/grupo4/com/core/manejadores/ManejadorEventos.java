package grupo4.com.core.manejadores;

import grupo4.com.servidor.database.BD;
import grupo4.com.util.Log;

import java.util.ArrayList;
import java.util.List;

import grupo4.com.core.modelJEE.EventoGCabezal;
import grupo4.com.core.modelJEE.EventoGConf;
import grupo4.com.core.modelJEE.NivelesAlertasEG;
import grupo4.com.core.modelJEE.TipoEventos;

public class ManejadorEventos {

	public 	List<EventoGConf> getConfEG(Log log, long idEvento){
		List<EventoGConf> configuraciones = new ArrayList<EventoGConf>();
		try {
			BD base = new BD();
			configuraciones = base.getConfEG(log, idEvento);
		}catch(Throwable t) {
			log.log("-->ERROR");
		}
		return configuraciones;
	}
	List<EventoGConf> configuraciones = new ArrayList<EventoGConf>();
	
	public List<EventoGCabezal> getListaEventosG (Log log, String usuario){
		List<EventoGCabezal> eventos = null;
		try {
			BD base = new BD();
			eventos =  base.getListaEventosG(log, usuario);
		}catch(Throwable t) {
			log.log("-->ERROR");
		}
		return eventos;
	}
	
	public boolean configurarEG(Log log, String nombreEvento, int tipo, int nivel, String alerta, String usuario) {
		boolean creado = false;
		try {
			BD base = new BD();
			long id_evento = base.getIDEventoG(log, nombreEvento);
			if(id_evento > 0 ) {
				creado = base.configurarEG(log, id_evento, tipo, nivel, alerta, usuario);	
			}else {
				log.log("No es posible configurar EVENTO GLOBAL , id evento incorrecto");
			}
		}catch(Throwable t) {
			log.log("-->ERROR");
		}
		return creado;
	}
	
	public boolean crearEG(Log log, String nombreEvento, String adminAlta) {
		boolean creado = false;
		try {
			BD base = new BD();
			creado = base.crearEG(log, nombreEvento, adminAlta);
		}catch(Throwable t) {
			log.log("-->ERROR");
		}
		return creado;
	}
	
	public boolean activarEG(Log log, String id_evento, String adminAlta) {
		boolean activado = false;
		try {
			BD base = new BD();
			activado = base.activarEG(log,id_evento, adminAlta);
		}catch(Throwable t) {
			log.log("-->ERROR");
		}
		return activado;
	}
	public boolean suscribirseEG(Log log, long idEvento, String usuario) {
		boolean suscripto = false;
		try {
			BD base = new BD();
			suscripto = base.suscribir_usuario_eg(log, idEvento, usuario);
		}catch(Throwable t) {
			log.log("-->ERROR");
		}
		return suscripto;
	}
	public boolean cancelarSusEG(Log log, long idEvento, String usuario) {
		boolean cancelada = false;
		try {
			BD base = new BD();
			cancelada = base.deleteSusEG(log, idEvento, usuario);
		}catch(Throwable t) {
			log.log("-->ERROR");
		}
		return cancelada;
	}
	
	
	public boolean desactivarEG(Log log, String id_evento, String adminAlta) {
		boolean desactivar = false;
		try {
			BD base = new BD();
			desactivar = base.desactivarEG(log,id_evento, adminAlta);
		}catch(Throwable t) {
			log.log("-->ERROR");
		}
		return desactivar;
	}
	
	public List<TipoEventos> getTiposEventos(Log log) {
		List<TipoEventos> tipoEventos = new ArrayList<TipoEventos>();
		try {
			BD base = new BD();
			tipoEventos = base.recuperarTipoDeEventos(log);
		}catch(Throwable t) {
			log.log("-->ERROR");
		}
		return tipoEventos;
	}
	public boolean modificarEG(Log log, long idEvento, int tipo, int nuevoNivel, String nuevaAlerta, String rol,
			String usuario) {
		boolean modificado = false;
		try {
			BD base = new BD();
			modificado = base.modificarEG(log, idEvento, tipo, nuevoNivel, nuevaAlerta, rol, usuario);
		}catch(Throwable t) {
			log.log("-->ERROR");
		}
		return modificado;
	}
	public boolean deteleConfEG(Log log, long idEvento, int tipo, String rol, String usuario) {
		boolean borrado = false;
		try {
			BD base = new BD();
			borrado = base.deteleConfEG(log, idEvento, tipo, rol, usuario);
		}catch(Throwable t) {
			log.log("-->ERROR");
		}
		return borrado;
	}
	public List<NivelesAlertasEG> getNivelesAlertasEG(Log log) {
		List<NivelesAlertasEG> listaNivelesEG = null;
		try {
			BD base = new BD();
			listaNivelesEG = base.getNivelesAlertasEG(log);
		}catch(Throwable t) {
			log.log("-->ERROR");
		}
		return listaNivelesEG;
	}
}
