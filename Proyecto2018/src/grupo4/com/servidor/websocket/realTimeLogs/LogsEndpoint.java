package grupo4.com.servidor.websocket.realTimeLogs;

import java.io.IOException;
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
	private ManejadorNotificaciones mnotis = new ManejadorNotificaciones();
	
	private String getMsg() {
		
		Log log = new Log("adsads.log", true);
		List<Notis> notis = mnotis.getMisNotis(log, "fede");
		return notis.get(1).getCondicion_dispara();
	}
	
	
	@OnOpen
	public void connect(Session session) throws IOException {
		this.session = session;
		
		onMessage(getMsg());
	}

	@OnClose
	public void close() {
		this.session = null;
	}
	
	@OnMessage
	public void onMessage(String msg) {
		
		this.session.getAsyncRemote().sendText("Echo:" + msg);
	}
	
	public  void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}
	
	
}
