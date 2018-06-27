package grupo4.com.core.manejadores;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import grupo4.com.core.modelJEE.InfoARevisarNodos;
import grupo4.com.core.modelJEE.InfoDisco;
import grupo4.com.util.Log;
import grupo4.com.util.UtilMongo;

public class ManejadorAlertas {
	
	/**
	 * Retorna espacio libre en disco de un nodo
	 * @param log
	 * @param nodo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public InfoARevisarNodos getInfoARevisar (Log log, String nodo) {
		InfoARevisarNodos resultado = null;
		MongoClient mongoClient = null;
		try {
			String con = UtilMongo.ConMongoLogtek();
			MongoClientURI connectionString = new MongoClientURI(con);
			mongoClient = new MongoClient(connectionString);
			MongoDatabase database = mongoClient.getDatabase("logtek");
			MongoCollection<Document> col = database.getCollection(nodo);
			
			FindIterable<Document> ue =   col.find().sort(new Document("_id", -1)).limit(1);
			resultado = new InfoARevisarNodos();
			
			for (Document ultima_entrada : ue) {
				//Agarro la memoria RAM libre y seteo en el resultado
				double freeM = Double.parseDouble(ultima_entrada.getString("FreeMemory"));
				double enMB = freeM/1024;
				resultado.setFreeMemory(enMB);
				
				//Agarro el cpu y lo seteo en el resultado
				double cpuL = Double.parseDouble(ultima_entrada.getString("CPULoad"));
				resultado.setCpuLoad(cpuL);
				
				//Agarro el espacio disponible en disco y lo seteo en resultado
				List<Document> detalles = (List<Document>) ultima_entrada.get("DiskArray");
				for (Document detalle : detalles) {
					String discoLibre = detalle.getString("spaceavail");
					String[] partes = discoLibre.split("G");
					resultado.setFreeDisk( Double.parseDouble(partes[0]));
				}
			}
		}catch(Throwable t) {
			log.log("No es posible devolver info de nodos a revisar debido a ["+t.getMessage()+"]");
		}finally {
			mongoClient.close();
		}
		return resultado;
	}

}
