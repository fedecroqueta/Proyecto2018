package grupo4.com.servidor.websocket.realTimeLogs;

import java.io.IOException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.mongodb.util.JSON;

import grupo4.com.core.manejadores.ManejadorLogs;
import grupo4.com.core.modelJEE.InfoMemoria;
import grupo4.com.util.Log;

@ServerEndpoint(value = "/realtime/ram")
public class MemoryRealTime {
	private Session session;
	private ManejadorLogs ml = new ManejadorLogs();
	
	private Integer cont = 10;
	
	@OnOpen
	public void connect(Session session) throws IOException {
		this.session = session;
		sendMessage("Hola");
	}

	@OnClose
	public void close() {
		this.session = null;
	}
	
	@OnMessage
	public void onMessage(String nodo) throws IOException, InterruptedException {		
				
		Thread.sleep(5000);
		
		InfoMemoria memoriaLibre = new InfoMemoria();
		Log log = new Log("LogREST.log", true);
		memoriaLibre = ml.getMemoriaLibre(log, nodo);
		
		sendMessage(memoriaLibre.getMemoriaEnUso() + ";" + memoriaLibre.getMemoriaLibre() + ";" + memoriaLibre.getMemoriaTotal());
		//sendMessage(cont + "," + (cont+20) + "," + (cont+149));
		//cont += 10;
	}
	
	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}
}