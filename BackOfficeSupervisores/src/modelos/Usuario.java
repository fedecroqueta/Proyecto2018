package modelos;


public class Usuario {

	private String username;
	private String password;
	private String mail;
	private String numero_celular;
	private String nivel_acceso;
	private boolean crea_nodos;
	private String fecha_alta;
	private String fecha_mod;
	private String descripcion_mod;
	private String descripcion_baja;
	
	
	public Usuario(String username, String password, String mail, String nivel_acceso, boolean crea_nodos,
			String fecha_alta, String fecha_mod, String descripcion_mod, String descripcion_baja) {
		super();
		this.username = username;
		this.password = password;
		this.mail = mail;
		this.nivel_acceso = nivel_acceso;
		this.crea_nodos = crea_nodos;
		this.fecha_alta = fecha_alta;
		this.fecha_mod = fecha_mod;
		this.descripcion_mod = descripcion_mod;
		this.descripcion_baja = descripcion_baja;
	}
	
	
	public Usuario() {
		super();
	}


	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public boolean isCrea_nodos() {
		return crea_nodos;
	}
	public void setCrea_nodos(boolean crea_nodos) {
		this.crea_nodos = crea_nodos;
	}
	public String getFecha_alta() {
		return fecha_alta;
	}
	public void setFecha_alta(String fecha_alta) {
		this.fecha_alta = fecha_alta;
	}
	public String getFecha_mod() {
		return fecha_mod;
	}
	public void setFecha_mod(String fecha_mod) {
		this.fecha_mod = fecha_mod;
	}
	public String getDescripcion_mod() {
		return descripcion_mod;
	}
	public void setDescripcion_mod(String descripcion_mod) {
		this.descripcion_mod = descripcion_mod;
	}
	public String getDescripcion_baja() {
		return descripcion_baja;
	}
	public void setDescripcion_baja(String descripcion_baja) {
		this.descripcion_baja = descripcion_baja;
	}


	public String getNumero_celular() {
		return numero_celular;
	}


	public void setNumero_celular(String numero_celular) {
		this.numero_celular = numero_celular;
	}
	
	
}
