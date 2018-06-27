package grupo4.com.core.modelJEE;

public class EventoGConf {
 
	private long idEvento;
	private int tipo;
	private int nivel;
	private String alerta;
	private String fechaMod;
	
	public EventoGConf(long idEvento, int tipo, int nivel, String alerta, String fechaMod) {
		super();
		this.idEvento = idEvento;
		this.tipo = tipo;
		this.nivel = nivel;
		this.alerta = alerta;
		this.fechaMod = fechaMod;
	}
	
	public EventoGConf() {
		super();
	}

	public long getIdEvento() {
		return idEvento;
	}

	public void setIdEvento(long idEvento) {
		this.idEvento = idEvento;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public String getAlerta() {
		return alerta;
	}

	public void setAlerta(String alerta) {
		this.alerta = alerta;
	}

	public String getFechaMod() {
		return fechaMod;
	}

	public void setFechaMod(String fechaMod) {
		this.fechaMod = fechaMod;
	}
	
	
}
