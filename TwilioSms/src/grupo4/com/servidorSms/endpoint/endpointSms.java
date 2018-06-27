package grupo4.com.servidorSms.endpoint;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import grupo4.com.servidorSms.Sms;

@Path("/endpointSms")
public class endpointSms {
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/sms/{numeroCelular}/{cuerpoMensaje}")
	public Response smsTEST(@PathParam("numeroCelular") String numero, @PathParam("cuerpoMensaje") String cuerpoMensaje)  {
		boolean smsEnviado = Sms.enviarSms(numero, cuerpoMensaje);
		return Response.ok(smsEnviado).build();
	}

}
