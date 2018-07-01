package grupo4.com.servidor.websocket.realTimeLogs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	
	@OnOpen
	public void connect(Session session) throws IOException {
		this.session = session;
		sendMessage("Echo: ");
	}

	@OnClose
	public void close() {
		this.session = null;
	}
	
	@OnMessage
	public void onMessage(String msg) throws IOException, InterruptedException {		
		
		Thread.sleep(5000);
		
		Log log = new Log("adsads.log", true);
		List<Notis> notis = mnotis.getMisNotis(log, "fede");
		//notis = new ArrayList<Notis>();
		
		sendMessage(manejoNotifiacionesTemporal(notis));
		
		/*		
		if(!notis.isEmpty()) {
			sendMessage(notis.get(1).getCondicion_dispara());
		}
		else {
			sendMessage("");
		}
		*/
		//this.session.getAsyncRemote().sendText("Echo: " + msg);
	}
	
	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}
	
	//Esta funcion es con fines de probar la aplicacion, se debe remplazar con una validacion a nivel de BD
	private String manejoNotifiacionesTemporal(List<Notis> notificaciones) {
		for(Notis n : notificaciones) {
			if(!notificacionesRepetidasTemp.contains(n.getCondicion_dispara())) {
				notificacionesRepetidasTemp.add(n.getCondicion_dispara());
				return n.getCondicion_dispara();
			}
		}
		return "";
	}
	
}
