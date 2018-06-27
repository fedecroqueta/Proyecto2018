package grupo4.com.servidorSms;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class Sms {
	
	public static boolean enviarSms(String numero, String cuerpoSms) {
		boolean enviado = false;
		Properties prop = new Properties();
		InputStream input = null;

		// Cargo archivo properties para cargar parametros de twilio
		try {
			// input = new FileInputStream("config\\configuracion.properties");
			String filename = "configuracion.properties";
			input = Sms.class.getClassLoader().getResourceAsStream(filename);
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		// Find your Account Sid and Auth Token at twilio.com/console
		String ACCOUNT_SID = prop.getProperty("ACCOUNT_SID");
		String AUTH_TOKEN = prop.getProperty("AUTH_TOKEN");

		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

		Message message = Message.creator(new PhoneNumber(numero), // to
				new PhoneNumber(prop.getProperty("TWILIO_NUMERO")), // from
				cuerpoSms).create();
		
		if(message.getBody() != null) {
			enviado = true;
		}
		
		return enviado;

	}

}
