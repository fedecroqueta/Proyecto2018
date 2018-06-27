package grupo4.com.core.modelJEE;

public class Usuario {
	
	private String username;
	private String mail;
	private String nivel_acceso;
	private String numero_cel;
	
	
	public Usuario() {
		super();
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getNivel_acceso() {
		return nivel_acceso;
	}
	public void setNivel_acceso(String nivel_acceso) {
		this.nivel_acceso = nivel_acceso;
	}

	public String getNumero_cel() {
		return numero_cel;
	}

	public void setNumero_cel(String numero_cel) {
		this.numero_cel = numero_cel;
	}
	
	
	

}
