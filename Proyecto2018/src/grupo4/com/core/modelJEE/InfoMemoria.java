package grupo4.com.core.modelJEE;

public class InfoMemoria {

	private int memoriaLibre;
	private int memoriaTotal;
	private int memoriaEnUso;
	private String fecha;
	
	
	public InfoMemoria(int memoriaLibre, int memoriaTotal, int memoriaEnUso) {
		super();
		this.memoriaLibre = memoriaLibre;
		this.memoriaTotal = memoriaTotal;
		this.memoriaEnUso = memoriaEnUso;
	}
	
	
	
	public InfoMemoria() {
		super();
	}



	public int getMemoriaLibre() {
		return memoriaLibre;
	}
	public void setMemoriaLibre(int memoriaLibre) {
		this.memoriaLibre = memoriaLibre;
	}
	public int getMemoriaTotal() {
		return memoriaTotal;
	}
	public void setMemoriaTotal(int memoriaTotal) {
		this.memoriaTotal = memoriaTotal;
	}
	public int getMemoriaEnUso() {
		return memoriaEnUso;
	}
	public void setMemoriaEnUso(int memoriaEnUso) {
		this.memoriaEnUso = memoriaEnUso;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
	
	
}
