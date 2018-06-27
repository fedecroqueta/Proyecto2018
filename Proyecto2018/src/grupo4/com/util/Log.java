package grupo4.com.util;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.io.FileOutputStream;


public class Log {

	private PrintStream archivo;
	private SimpleDateFormat formato;
	private FileOutputStream fos;
	private String nombreBase;
	private String nomLogHoy;
	private String logPath;
	private boolean append;

	/**
	 * Constructor 
	 * @param nomArchivoLog
	 * @param append
	 */
	public Log(String nombreLog, boolean append) {
		this.nombreBase = nombreLog;
		this.nomLogHoy = getLogFileName();
		this.logPath = System.getProperty("dir.base") + File.separator + "logs" + File.separator;
		this.append = append;
		crearArchivo();
	}
	
	public Log(String nombreLog, boolean append, boolean logPropio) {
		this(nombreLog, append);
	}
	
	private String getLogFileName() {
		if (this.nombreBase .endsWith(".log")) {
			try {
				String nomArchivo			= this.nombreBase.substring(0, this.nombreBase.length()-4);
				SimpleDateFormat format2	= new SimpleDateFormat("yyyyMM");
				String stFecha 				= format2.format(new Date()); 
				nomArchivo 					= stFecha + "_" + nomArchivo +".log";
				return nomArchivo;
			} catch (Throwable t) {
				return this.nombreBase;
			}
		} else {
			return this.nombreBase;
		}
	}
	
	private void verificarNuevoArchivo() {
		String nuevoNom = getLogFileName();
		if (!nuevoNom.equals(this.nomLogHoy)) {
			this.nomLogHoy = nuevoNom;
			crearArchivo();
		}
	}
	
	private void crearArchivo() {
		File logFilePath = new File(logPath, nomLogHoy);
		try {
			fos 	= new FileOutputStream(logFilePath, append);
			archivo = new PrintStream(fos, true);
			formato 	= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		} catch (Throwable t) {
			System.err.println("Error: No fue posible crear archivo para log:[" + logFilePath + "]: " + t.getMessage());
			t.printStackTrace();
		}
	}
	
	public void log(String msg) {
		log(msg, null);
	}

	
	public void log(String msg, Throwable t) {
		if (archivo != null) {
			verificarNuevoArchivo();
			try {
				String fecha = formato.format(new Date());
				msg = fecha + "  " + msg;
				archivo.println(msg);
				if (t != null) {
					t.printStackTrace(archivo);
				}
				archivo.flush();
			} catch (Throwable e) {
				System.err.println("Error logueando informacion:[" + e.getMessage() + "]");
				e.printStackTrace();
			}

		} else if (t != null) {
			System.err.println(msg);
			t.printStackTrace();
		}
	}

	public void cerrarLog() {
		if (archivo != null) {
			try {
				archivo.close();
			} catch (Throwable t) {
				t.printStackTrace();
			}
			archivo = null;
		}
		if (fos != null) {
			try {
				fos.close();
			} catch (Throwable t) {
				t.printStackTrace();
			}
			fos = null;
		}
	}
	
	public static void cerrar(Log log) {
		if (log != null) {
			log.cerrarLog();
		}
	}
	
}
