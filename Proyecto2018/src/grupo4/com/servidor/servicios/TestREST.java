package grupo4.com.servidor.servicios;

import javax.ws.rs.Path;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.Calendar;
import javax.ws.rs.core.Response;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;

import grupo4.com.alertas.Email;
import grupo4.com.alertas.Sms;
import grupo4.com.servidor.database.BD;
import grupo4.com.servidor.database.Persistencia;
import grupo4.com.servidor.websocket.realTimeLogs.LogsEndpoint;
import grupo4.com.util.Constantes;
import grupo4.com.util.Log;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

@SuppressWarnings("unused")
@Path("/test")
public class TestREST {

	//Para Test estado de Servidor
	@GET
	@Path("/prueba")
	public String probar() throws IOException {
		/*LogsEndpoint loge = null;
		loge.sendMessage("La concha de tu madre");*/
		return "Proyecto-2018 Server is Up!";
	}
	
	@POST
	@Path("/mail")
	public Response mailTEST() throws UnirestException {
		JsonNode jsN = new JsonNode(null);
		jsN = Email.enviarMail("","", "");
		return Response.ok(jsN.toString()).build();
	}
	
	@POST
	@Path("/sms/{numeroCelular}/{cuerpoMensaje}")
	public Response smsTEST(@PathParam("numeroCelular") String numero, @PathParam("cuerpoMensaje") String cuerpoMensaje) throws Exception  {
		boolean smsEnviado = Sms.enviarSms(numero, cuerpoMensaje);
		return Response.ok(smsEnviado).build();
	}
	
}
