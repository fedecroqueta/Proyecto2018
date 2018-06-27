package grupo4.com.alertas;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class Email {

	public static JsonNode enviarMail(String username, String mail, String cuerpoMail) throws UnirestException {
		Properties prop = new Properties();
		InputStream input = null;
		
		//Cargo archivo properties para cargar parametros de mailgun
		try {
			//input = new FileInputStream("config\\configuracion.properties");
			String filename = "configuracion.properties";
    		input = Email.class.getClassLoader().getResourceAsStream(filename);
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		// Armo todo para enviar el mail
		HttpResponse<JsonNode> request = Unirest
				.post("https://api.mailgun.net/v3/" + prop.getProperty("DOMAIN_NAME") + "/messages")
				.basicAuth("api", prop.getProperty("API_KEY"))
				.queryString("from", "Logtek Staff <" + prop.getProperty("USER_DOMAIN") + ">")
				.queryString("to", "Querido " + username + ", " + mail)
				.queryString("subject", "Alerta se dispara evento").queryString("text", cuerpoMail).asJson();

		return request.getBody();
	}
}
