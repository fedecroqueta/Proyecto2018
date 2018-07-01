package grupo4.com.servidor.websocket.realTimeLogs;

import java.io.IOException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.mongodb.util.JSON;

import grupo4.com.core.manejadores.ManejadorLogs;
import grupo4.com.core.modelJEE.InfoDisco;
import grupo4.com.core.modelJEE.InfoMemoria;
import grupo4.com.util.Log;

@ServerEndpoint(value = "/realtime/hhd")
public class DiskRealTime {
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
		
		InfoDisco infoDisco = new InfoDisco();
		Log log = new Log("LogREST.log", true);
		infoDisco = ml.getEspacioEnDisco(log, nodo);

		//String jsonOscuro = "{\"memoriaLibre\":\"" + memoriaLibre.getMemoriaLibre() + "\"," + "\"memoriaTotal\":\"" + memoriaLibre.getMemoriaTotal() + "\", \"memoriaEnUso\":\"" + memoriaLibre.getMemoriaEnUso() + "\"}";
		
		//sendMessage(jsonOscuro);
		
		sendMessage(infoDisco.getEspacioDisponible() + ";" + infoDisco.getEspacioTotal() + ";" + infoDisco.getMount());
		//sendMessage(cont + "," + (cont+20) + "," + (cont+100));
		//cont += 10;
	}
	
	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}
}