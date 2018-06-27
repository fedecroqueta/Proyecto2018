package grupo4.com.core.modelJEE;

public class SuscripcionEG {
	
	private long id_sus_eg;
	private long id_eventoG;
	private String fecha_suscripcion;
	private String usuario;
	private boolean activa;
	private String fecha_canc;
	

	public SuscripcionEG() {
		super();
	}
	
	public long getId_sus_eg() {
		return id_sus_eg;
	}
	public void setId_sus_eg(long id_sus_eg) {
		this.id_sus_eg = id_sus_eg;
	}
	public long getId_eventoG() {
		return id_eventoG;
	}
	public void setId_eventoG(long id_eventoG) {
		this.id_eventoG = id_eventoG;
	}
	public String getFecha_suscripcion() {
		return fecha_suscripcion;
	}
	public void setFecha_suscripcion(String fecha_suscripcion) {
		this.fecha_suscripcion = fecha_suscripcion;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public boolean isActiva() {
		return activa;
	}
	public void setActiva(boolean activa) {
		this.activa = activa;
	}
	public String getFecha_canc() {
		return fecha_canc;
	}
	public void setFecha_canc(String fecha_canc) {
		this.fecha_canc = fecha_canc;
	}
	
	
	

}
