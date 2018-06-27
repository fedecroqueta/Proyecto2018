package grupo4.com.core.modelJEE;

public class InfoCabezalNodo {
	
	private String distro;
	private String ipAddress;
	private String ipPublica;
	private int cantCpus;
	private int totalRAM;
	
	
	public InfoCabezalNodo() {
		super();
	}
	
	public InfoCabezalNodo(String distro, String ipAddress, String ipPublica, int cantCpus, int totalRAM) {
		super();
		this.distro = distro;
		this.ipAddress = ipAddress;
		this.ipPublica = ipPublica;
		this.cantCpus = cantCpus;
		this.totalRAM = totalRAM;
	}
	
	public String getDistro() {
		return distro;
	}
	public void setDistro(String distro) {
		this.distro = distro;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getIpPublica() {
		return ipPublica;
	}
	public void setIpPublica(String ipPublica) {
		this.ipPublica = ipPublica;
	}
	public int getCantCpus() {
		return cantCpus;
	}
	public void setCantCpus(int cantCpus) {
		this.cantCpus = cantCpus;
	}
	public int getTotalRAM() {
		return totalRAM;
	}
	public void setTotalRAM(int totalRAM) {
		this.totalRAM = totalRAM;
	}
	
	

}
