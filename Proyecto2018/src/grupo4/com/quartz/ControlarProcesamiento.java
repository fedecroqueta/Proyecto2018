package grupo4.com.quartz;

public class ControlarProcesamiento {
private static ControlarProcesamiento instance;
	
	private boolean procesando = false;
	
	private ControlarProcesamiento() {
	}
	
	public static ControlarProcesamiento getInstance() {
		if (instance == null) {
			instance = new ControlarProcesamiento();
		}
		return instance;
	}
	

	public boolean estaProcesando() {
		return procesando;
	}
	
	public void comienzaProcesamiento() {
		procesando = true;
	}

	public void finalizaProcesamiento() {
		procesando = false;
	}

}
