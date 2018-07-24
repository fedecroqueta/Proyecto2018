package grupo4.com.core.manejadores;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import grupo4.com.core.modelJEE.InfoCabezalNodo;
import grupo4.com.core.modelJEE.InfoCpu;
import grupo4.com.core.modelJEE.InfoDisco;
import grupo4.com.core.modelJEE.InfoMemoria;
import grupo4.com.core.modelJSON.LogAgente;
import grupo4.com.util.Log;
import grupo4.com.util.UtilFormato;
import  grupo4.com.util.UtilMongo;

@SuppressWarnings("unused")
public class ManejadorLogs {

	
	/**
	 * Devuelve info de cabezal de un nodo
	 * @param log
	 * @param nodo
	 * @return
	 */
	public InfoCabezalNodo getInfoCabezalNodo(Log log, String nodo) {
		MongoClient mongoClient = null;
		InfoCabezalNodo inf = null;
		try {
			String con = UtilMongo.ConMongoLogtek();
			MongoClientURI connectionString = new MongoClientURI(con);
			mongoClient = new MongoClient(connectionString);
			MongoDatabase database = mongoClient.getDatabase("logtek");
			MongoCollection<Document> col = database.getCollection(nodo);
			
			FindIterable<Document> ultimo =   col.find().limit(1);
			for (Document docIte : ultimo) {
				inf = new InfoCabezalNodo();
				inf.setDistro(docIte.getString("distro"));
				String cantCpus = docIte.getString("NumberOfCPUs");
				inf.setCantCpus(Integer.parseInt(cantCpus));
				inf.setIpAddress(docIte.getString("IPAddress"));
				inf.setIpPublica(docIte.getString("PublicIP"));
				String totS = docIte.getString("TotalMemoryMB");
				long tot = Long.parseLong(totS);
				int totMB = (int) (tot/1024);
				inf.setTotalRAM(totMB);
			}
		}catch(Throwable t){
			log.log("No es posible devolver cabezal del nodo ["+nodo+"] debido a ["+t.getMessage()+"]");
		}finally {
			mongoClient.close();
		}
		return inf;
	}
	
	/**
	 * Devuelve memoria libre de un nodo
	 * @param log
	 * @param nodo
	 * @return
	 */
	public InfoMemoria getMemoriaLibre(Log log, String nodo) {
		InfoMemoria resultado = null;
		MongoClient mongoClient = null;
		try {
			resultado = new InfoMemoria();
			String con = UtilMongo.ConMongoLogtek();
			MongoClientURI connectionString = new MongoClientURI(con);
			mongoClient = new MongoClient(connectionString);
			MongoDatabase database = mongoClient.getDatabase("logtek");
			MongoCollection<Document> col = database.getCollection(nodo);
			
			FindIterable<Document> ultimo =   col.find().sort(new Document("_id", -1)).limit(1);
			int auxRes =0;
			int auxMemTotal = 0;
			int auxMemEnUso = 0;
			String fechaAPoner = "";
			for (Document docIte : ultimo) {
				String ult = docIte.getString("FreeMemory");
				String ultiMemoriaTotal = docIte.getString("TotalMemoryMB");
				String ultMemEnUso = docIte.getString("MemoryInUse");
				fechaAPoner = UtilFormato.mongoTimeToDate(docIte.getString("Time"));

				int auxBytes = Integer.parseInt(ult);
				auxRes = auxBytes/1024;
				auxMemTotal = Integer.parseInt(ultiMemoriaTotal);
				auxMemTotal = auxMemTotal/1024;
				auxMemEnUso = Integer.parseInt(ultMemEnUso);
				auxMemEnUso = auxMemEnUso/1024;
			}
			resultado.setFecha(fechaAPoner);
			resultado.setMemoriaEnUso(auxMemEnUso);
			resultado.setMemoriaLibre(auxRes);
			resultado.setMemoriaTotal(auxMemTotal);
		}catch(Throwable t) {
			log.log("No es posible devolver memoria libre debido a ["+t.getMessage()+"]");
		}finally {
			mongoClient.close();
		}
		return resultado;
	}
	
	public List<InfoMemoria> getMemoriaLibreEntreFechas(Log log, String nodo, String fechaDesde, String fechaHasta) {
		MongoClient mongoClient = null;
		final List<InfoMemoria> resultado = new ArrayList<InfoMemoria>();
		try {
			String con = UtilMongo.ConMongoLogtek();
			MongoClientURI connectionString = new MongoClientURI(con);
			mongoClient = new MongoClient(connectionString);
			MongoDatabase database = mongoClient.getDatabase("logtek");
			MongoCollection<Document> col = database.getCollection(nodo);
			
			DateFormat format = new SimpleDateFormat("yyyyMMdd");
			Date fechaDesdeAParsear = format.parse(fechaDesde);
			Date fechaHastaAParsear = format.parse(fechaHasta);
			long fechaDesdeEpoch = fechaDesdeAParsear.getTime() / 1000;
			long fechaHastaEpoch = fechaHastaAParsear.getTime() / 1000;
			long epoch = System.currentTimeMillis()/1000;
			
			//Filta por fecha mayor o igual y menor o igual a la que llega por paramtero
			FindIterable<Document> lista = col.find(Filters.and(Filters.gte("Time", String.valueOf(fechaDesdeEpoch)), Filters.lte("Time",  String.valueOf(fechaHastaEpoch)))).sort(new BasicDBObject("Time", 1));
			
			int tamaLista = 0;
			int auxRes =0;
			int auxMemTotal = 0;
			int auxMemEnUso = 0;
			for (Document docIte : lista) {
				tamaLista++;
				InfoMemoria info = new InfoMemoria();
				String ult = docIte.getString("FreeMemory");
				String ultiMemoriaTotal = docIte.getString("TotalMemoryMB");
				String ultMemEnUso = docIte.getString("MemoryInUse");
				//Tratamiento de fecha
				String fechaAPoner = UtilFormato.mongoTimeToDate(docIte.getString("Time"));
				int auxBytes = Integer.parseInt(ult);
				auxRes = auxBytes/1024;
				auxMemTotal = Integer.parseInt(ultiMemoriaTotal);
				auxMemTotal = auxMemTotal/1024;
				auxMemEnUso = Integer.parseInt(ultMemEnUso);
				auxMemEnUso = auxMemEnUso/1024;
				info.setMemoriaEnUso(auxMemEnUso);
				info.setMemoriaLibre(auxRes);
				info.setMemoriaTotal(auxMemTotal);
				info.setFecha(fechaAPoner);
				resultado.add(info);
			}
		}catch(Throwable t){
			log.log("No es posible devolver info de disco de ["+nodo+"] debido a ["+t.getMessage()+"]");
		}finally {
			mongoClient.close();
		}
		return resultado;
	}
	
	
	/**
	 * Retorna espacio libre en disco de un nodo
	 * @param log
	 * @param nodo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public InfoDisco getEspacioEnDisco (Log log, String nodo) {
		InfoDisco resultado = null;
		MongoClient mongoClient = null;
		try {
			String con = UtilMongo.ConMongoLogtek();
			MongoClientURI connectionString = new MongoClientURI(con);
			mongoClient = new MongoClient(connectionString);
			MongoDatabase database = mongoClient.getDatabase("logtek");
			MongoCollection<Document> col = database.getCollection(nodo);
			
			List<Document> ue = (List<Document>) col.find().into(new ArrayList<Document>());
			for (Document entrada : ue) {	 
				List<Document> detalles = (List<Document>) entrada.get("DiskArray");
				for (Document detalle : detalles) {
					resultado = new InfoDisco();
					resultado.setMount(detalle.getString("mount"));
					String espacioDispo = detalle.getString("spaceavail");
					String espacioTotal = detalle.getString("spacetotal");
					String spliEsDispo = espacioDispo.split("G")[0];
					String spliEsTot = espacioTotal.split("G")[0];
					String fechaAPoner = UtilFormato.mongoTimeToDate(entrada.getString("Time"));
					resultado.setFecha(fechaAPoner);
					resultado.setEspacioDisponible(Float.parseFloat(spliEsDispo));
					resultado.setEspacioTotal(Float.parseFloat(spliEsTot)); 
				}
			}
		}catch(Throwable t) {
			log.log("No es posible devolver espacio en disco libre debido a ["+t.getMessage()+"]");
		}finally {
			mongoClient.close();
		}
		return resultado;
	}
	
	
	public List<InfoCpu> getInfoCpuEntreFechas(Log log, String nodo, String fechaDesde, String fechaHasta) {
		MongoClient mongoClient = null;
		final List<InfoCpu> resultado = new ArrayList<InfoCpu>();
		try {
			String con = UtilMongo.ConMongoLogtek();
			MongoClientURI connectionString = new MongoClientURI(con);
			mongoClient = new MongoClient(connectionString);
			MongoDatabase database = mongoClient.getDatabase("logtek");
			MongoCollection<Document> col = database.getCollection(nodo);
			
			DateFormat format = new SimpleDateFormat("yyyyMMdd");
			Date fechaDesdeAParsear = format.parse(fechaDesde);
			Date fechaHastaAParsear = format.parse(fechaHasta);
			long fechaDesdeEpoch = fechaDesdeAParsear.getTime() / 1000;
			long fechaHastaEpoch = fechaHastaAParsear.getTime() / 1000;
			long epoch = System.currentTimeMillis()/1000;
			
			//FindIterable<Document> lista = col.find(Filters.gte("Time", String.valueOf(fechaDesdeEpoch)));

			//Filta por fecha mayor o igual y menor o igual a la que llega por paramtero
			FindIterable<Document> lista = col.find(Filters.and(Filters.gte("Time", String.valueOf(fechaDesdeEpoch)), Filters.lte("Time",  String.valueOf(fechaHastaEpoch)))).sort(new BasicDBObject("Time", 1));
			
			int tamaLista = 0;
			for (Document docIte : lista) {
				tamaLista++;
				InfoCpu info = new InfoCpu();
				//Tratamiento de fecha
				String fechaAPoner = UtilFormato.mongoTimeToDate(docIte.getString("Time"));

				
				info.setFecha(fechaAPoner);
				info.setCpuLoad(Double.parseDouble(docIte.getString("CPULoad")));
				info.setNumeroCpus(Integer.parseInt(docIte.getString("NumberOfCPUs")));
				resultado.add(info);
			}
		}catch(Throwable t){
			log.log("No es posible devolver info de CPU de ["+nodo+"] debido a ["+t.getMessage()+"]");
		}finally {
			mongoClient.close();
		}
		return resultado;
	}

	public InfoCpu getInfoCpu(Log log, String nodo) {
		MongoClient mongoClient = null;
		InfoCpu resultado = null;
		try {
			String con = UtilMongo.ConMongoLogtek();
			MongoClientURI connectionString = new MongoClientURI(con);
			mongoClient = new MongoClient(connectionString);
			MongoDatabase database = mongoClient.getDatabase("logtek");
			MongoCollection<Document> col = database.getCollection(nodo);
			
			FindIterable<Document> ultimo =   col.find().limit(1);
			for (Document docIte : ultimo) {
				resultado = new InfoCpu();
				String fechaAPoner = UtilFormato.mongoTimeToDate(docIte.getString("Time"));
				resultado.setFecha(fechaAPoner);
				resultado.setCpuLoad(Double.parseDouble(docIte.getString("CPULoad")));
				resultado.setNumeroCpus(Integer.parseInt(docIte.getString("NumberOfCPUs")));
			}
		}catch(Throwable t){
			log.log("No es posible devolver info de CPU de ["+nodo+"] debido a ["+t.getMessage()+"]");
		}finally {
			mongoClient.close();
		}
		return resultado;
	}

	@SuppressWarnings("unchecked")
	public List<InfoDisco> getInfoDiscoEntreFechas(Log log, String nodo, String fechaDesde, String fechaHasta) {
		MongoClient mongoClient = null;
		final List<InfoDisco> resultado = new ArrayList<InfoDisco>();
		try {
			String con = UtilMongo.ConMongoLogtek();
			MongoClientURI connectionString = new MongoClientURI(con);
			mongoClient = new MongoClient(connectionString);
			MongoDatabase database = mongoClient.getDatabase("logtek");
			MongoCollection<Document> col = database.getCollection(nodo);
			
			DateFormat format = new SimpleDateFormat("yyyyMMdd");
			Date fechaDesdeAParsear = format.parse(fechaDesde);
			Date fechaHastaAParsear = format.parse(fechaHasta);
			long fechaDesdeEpoch = fechaDesdeAParsear.getTime() / 1000;
			long fechaHastaEpoch = fechaHastaAParsear.getTime() / 1000;
			long epoch = System.currentTimeMillis()/1000;
			
			//Filta por fecha mayor o igual y menor o igual a la que llega por paramtero
			FindIterable<Document> lista = col.find(Filters.and(Filters.gte("Time", String.valueOf(fechaDesdeEpoch)), Filters.lte("Time",  String.valueOf(fechaHastaEpoch)))).sort(new BasicDBObject("Time", 1));
			
			int tamaLista = 0;
			for (Document docIte : lista) {
				tamaLista++;
				List<Document> detalles = (List<Document>) docIte.get("DiskArray");
				for (Document detalle : detalles) {
					InfoDisco info = new InfoDisco();
					//Tratamiento de fecha
					String fechaAPoner = UtilFormato.mongoTimeToDate(docIte.getString("Time"));

					
					info.setFecha(fechaAPoner);
					info.setMount(detalle.getString("mount"));
					String espacioDispo = detalle.getString("spaceavail");
					String espacioTotal = detalle.getString("spacetotal");
					String spliEsDispo = espacioDispo.split("G")[0];
					String spliEsTot = espacioTotal.split("G")[0];
					info.setEspacioDisponible(Float.parseFloat(spliEsDispo));
					info.setEspacioTotal(Float.parseFloat(spliEsTot)); 
					resultado.add(info);
				}
			}
			String pausa = null;
		}catch(Throwable t){
			log.log("No es posible devolver info de disco de ["+nodo+"] debido a ["+t.getMessage()+"]");
		}finally {
			mongoClient.close();
		}
		return resultado;
	}
	
	@SuppressWarnings({ "unchecked", "resource" })
	public List<LogAgente> getAgenteUsuario(String nodo) {

		MongoClient mongoClient = null;
		List<LogAgente> ret = new ArrayList<LogAgente>();
		
		String con = UtilMongo.ConMongoLogtek();
		MongoClientURI connectionString = new MongoClientURI(con);
		mongoClient = new MongoClient(connectionString);
		MongoDatabase database = mongoClient.getDatabase("logtek");
		MongoCollection<Document> col = database.getCollection("syslog");
		
		System.out.println("1");
		
		List<Document> ue = (List<Document>) col.find().into(new ArrayList<Document>());
		for (Document docIte : ue) {
			//System.out.println("nodeinfo: " + docIte);
			try {
				if(docIte.getString("source").equals(nodo)) {
					//String mensaje = docIte.getString("msg");
										
					LogAgente agente = new LogAgente();
					agente.setFromHost(docIte.getString("fromhost"));
					agente.setFromHostIp(docIte.getString("fromhost-ip"));
					agente.setProgramName(docIte.getString("programname"));
					agente.setSysLogSeverityText(docIte.getString("syslogseverity-text"));
					agente.setTimeReported(docIte.getString("timereported"));
										
					//agente.setIp(mensaje.split("-")[0]);
					//agente.setDate((mensaje.substring(mensaje.indexOf("["), mensaje.indexOf("]")))); 
					ret.add(agente);
				}
			}
			catch(Exception e){
				
			}

		}
		
		mongoClient.close();
		return ret;
	}
	
	@SuppressWarnings({ "unchecked", "resource" })
	public List<LogAgente> getAgenteUsuarioPorFecha(String nodo, String fechaInicio, String fechaFin) {

		MongoClient mongoClient = null;
		List<LogAgente> ret = new ArrayList<LogAgente>();
		
		String con = UtilMongo.ConMongoLogtek();
		MongoClientURI connectionString = new MongoClientURI(con);
		mongoClient = new MongoClient(connectionString);
		MongoDatabase database = mongoClient.getDatabase("logtek");
		MongoCollection<Document> col = database.getCollection("syslog");
		
		List<Document> ue = (List<Document>) col.find().into(new ArrayList<Document>());
		for (Document docIte : ue) {
			try {			
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
				LocalDate dateInicio = LocalDate.parse(fechaInicio, formatter);
				
				LocalDate dateActual = LocalDate.parse(docIte.getString("timereported").split("T")[0], formatter);
				
				LocalDate dateFin = LocalDate.parse(fechaFin, formatter);
				

				System.out.println("cond1: " + dateInicio.isBefore(dateActual) + "cond2: " + dateFin.isAfter(dateActual) + "cond3: " + dateActual.equals(dateInicio) + "cond4: " + dateActual.equals(dateFin));
				if( docIte.getString("source").equals(nodo) && ((dateInicio.isBefore(dateActual) && dateFin.isAfter(dateActual)) || (dateActual.equals(dateInicio) || dateActual.equals(dateFin)))) {
										
					LogAgente agente = new LogAgente();
					agente.setFromHost(docIte.getString("fromhost"));
					agente.setFromHostIp(docIte.getString("fromhost-ip"));
					agente.setProgramName(docIte.getString("programname"));
					agente.setSysLogSeverityText(docIte.getString("syslogseverity-text"));
					agente.setTimeReported(docIte.getString("timereported"));
					agente.setRawMessage(docIte.getString("rawmsg"));					
					
					ret.add(agente);
				}

			}
			catch(Exception e){
				
			}

		}
		System.out.println(ret.size());
		mongoClient.close();
		return ret;
	}

	
}
