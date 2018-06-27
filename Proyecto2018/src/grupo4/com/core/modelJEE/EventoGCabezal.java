package grupo4.com.core.modelJEE;

public class EventoGCabezal {
	
	private String nombreEvento;
	private long idEvento;
	private boolean activo;
	private boolean estoySuscripto;
	private boolean soyCreador;
	
	public EventoGCabezal() {
		super();
	}

	public EventoGCabezal(String nombreEvento, long idEvento, boolean activo) {
		super();
		this.nombreEvento = nombreEvento;
		this.idEvento = idEvento;
		this.activo = activo;
	}

	
	public boolean isEstoySuscripto() {
		return estoySuscripto;
	}

	public void setEstoySuscripto(boolean estoySuscripto) {
		this.estoySuscripto = estoySuscripto;
	}

	public boolean isSoyCreador() {
		return soyCreador;
	}

	public void setSoyCreador(boolean soyCreador) {
		this.soyCreador = soyCreador;
	}

	public String getNombreEvento() {
		return nombreEvento;
	}

	public void setNombreEvento(String nombreEvento) {
		this.nombreEvento = nombreEvento;
	}

	public long getIdEvento() {
		return idEvento;
	}

	public void setIdEvento(long idEvento) {
		this.idEvento = idEvento;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	
	
	
	

}
