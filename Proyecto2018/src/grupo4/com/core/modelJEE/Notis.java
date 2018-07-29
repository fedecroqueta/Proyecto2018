package grupo4.com.core.modelJEE;

public class Notis {

	private long id_noti;
	private long id_evento_global;
	private String fecha_dispara;
	private int tipo;
	private String condicion_dispara;
	private boolean entregada;
	private String usuario_recibe;
	private boolean entregada_angular;
	private String nombre_evento;
	
	public Notis() {
		super();
	}
	
	
	public long getId_noti() {
		return id_noti;
	}


	public void setId_noti(long id_noti) {
		this.id_noti = id_noti;
	}


	public long getId_evento_global() {
		return id_evento_global;
	}
	public void setId_evento_global(long id_evento_global) {
		this.id_evento_global = id_evento_global;
	}
	public String getFecha_dispara() {
		return fecha_dispara;
	}
	public void setFecha_dispara(String fecha_dispara) {
		this.fecha_dispara = fecha_dispara;
	}
	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	public String getCondicion_dispara() {
		return condicion_dispara;
	}
	public void setCondicion_dispara(String condicion_dispara) {
		this.condicion_dispara = condicion_dispara;
	}
	public boolean isEntregada() {
		return entregada;
	}
	public void setEntregada(boolean entregada) {
		this.entregada = entregada;
	}
	public String getUsuario_recibe() {
		return usuario_recibe;
	}
	public void setUsuario_recibe(String usuario_recibe) {
		this.usuario_recibe = usuario_recibe;
	}


	public boolean isEntregada_angular() {
		return entregada_angular;
	}


	public void setEntregada_angular(boolean entregada_angular) {
		this.entregada_angular = entregada_angular;
	}


	public String getNombre_evento() {
		return nombre_evento;
	}


	public void setNombre_evento(String nombre_evento) {
		this.nombre_evento = nombre_evento;
	}
	
	
}
