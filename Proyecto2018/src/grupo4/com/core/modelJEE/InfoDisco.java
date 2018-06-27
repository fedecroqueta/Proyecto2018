package grupo4.com.core.modelJEE;

public class InfoDisco {

	private String mount;
	private float espacioTotal;
	private float espacioDisponible;
	
	
	public InfoDisco() {
		super();
	}

	public InfoDisco(String mount, float espacioTotal, float espacioDisponible) {
		super();
		this.mount = mount;
		this.espacioTotal = espacioTotal;
		this.espacioDisponible = espacioDisponible;
	}
	
	public String getMount() {
		return mount;
	}
	public void setMount(String mount) {
		this.mount = mount;
	}
	public float getEspacioTotal() {
		return espacioTotal;
	}
	public void setEspacioTotal(float espacioTotal) {
		this.espacioTotal = espacioTotal;
	}
	public float getEspacioDisponible() {
		return espacioDisponible;
	}
	public void setEspacioDisponible(float espacioDisponible) {
		this.espacioDisponible = espacioDisponible;
	}
	
	
	
	
	
}
