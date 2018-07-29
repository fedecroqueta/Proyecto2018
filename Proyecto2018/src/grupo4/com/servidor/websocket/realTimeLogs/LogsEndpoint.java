package grupo4.com.servidor.websocket.realTimeLogs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import grupo4.com.core.manejadores.ManejadorNotificaciones;
import grupo4.com.core.modelJEE.Notis;
import grupo4.com.util.Log;

@ServerEndpoint(value = "/alert")
public class LogsEndpoint {
	private Session session;
	private Integer cont = 5;
	private ManejadorNotificaciones mnotis = new ManejadorNotificaciones();
	private List<String> notificacionesRepetidasTemp = new ArrayList<String>();
	
	private static final Map<Integer, String> tiposEventos = new HashMap<Integer, String>();
	
	
	@OnOpen
	public void connect(Session session) throws IOException {
		this.session = session;
		sendMessage("Disfruta de SmartAlert!");
	}

	@OnClose
	public void close() {
		this.session = null;
	}
	
	@OnMessage
	public void onMessage(String msg) throws IOException, InterruptedException {		
		
		Thread.sleep(5000);
		Log log = new Log("WebSocket.log", true);
		//List<Notis> notis = mnotis.getMisNotis(log, "" , true);
		List<Notis> notis = mnotis.getNotisParaFront(log);
		if(notis != null) {
			sendMessage(manejoNotifiacionesTemporal(notis));
		}
		
	}
	
	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}
	
	//Esta funcion es con fines de probar la aplicacion, se debe remplazar con una validacion a nivel de BD
	private String manejoNotifiacionesTemporal(List<Notis> notificaciones) {
		Log log = null;
		try {
			log = new Log("controlNotisAngular.log", true);
			
			tiposEventos.put(1, "RAM");
			tiposEventos.put(2, "CPU");
			tiposEventos.put(3, "DISCO");
			
			for(Notis n : notificaciones) {
				if(!notificacionesRepetidasTemp.contains(n.getCondicion_dispara())) {
					notificacionesRepetidasTemp.add(n.getCondicion_dispara());
					return "EVENTO ("+n.getNombre_evento().toUpperCase()+"). CAUSA "+tiposEventos.get(n.getTipo())+" "+n.getCondicion_dispara().substring(35)+". FECHA "+n.getFecha_dispara();
				}
			}
		}catch(Throwable t) {
			log.log("No se pudo enviar notificacion a angular debido a ["+t.getMessage()+"]", t);
		}
		return "";
	}
	
}
