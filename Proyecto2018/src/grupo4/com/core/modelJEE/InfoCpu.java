package grupo4.com.core.modelJEE;

public class InfoCpu {
	
	private double cpuLoad;
	private int numeroCpus;
	private String fecha;
	
	
	public InfoCpu() {
		super();
	}
	
	public double getCpuLoad() {
		return cpuLoad;
	}
	public void setCpuLoad(double cpuLoad) {
		this.cpuLoad = cpuLoad;
	}
	public int getNumeroCpus() {
		return numeroCpus;
	}
	public void setNumeroCpus(int numeroCpus) {
		this.numeroCpus = numeroCpus;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
	

}
