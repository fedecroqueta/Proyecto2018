package grupo4.com.core.modelJEE;

public class EventoGIdYFechaInactivo {
	
	private long idEvento;
	private String fechaInactividad;
	private String usuarioCreador;
	private String nombreEvento;
	
	public long getIdEvento() {
		return idEvento;
	}
	public void setIdEvento(long idEvento) {
		this.idEvento = idEvento;
	}
	public String getFechaInactividad() {
		return fechaInactividad;
	}
	public void setFechaInactividad(String fechaInactividad) {
		this.fechaInactividad = fechaInactividad;
	}
	public String getUsuarioCreador() {
		return usuarioCreador;
	}
	public void setUsuarioCreador(String usuarioCreador) {
		this.usuarioCreador = usuarioCreador;
	}
	public String getNombreEvento() {
		return nombreEvento;
	}
	public void setNombreEvento(String nombreEvento) {
		this.nombreEvento = nombreEvento;
	}
	

}
