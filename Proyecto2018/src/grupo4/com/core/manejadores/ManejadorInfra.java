package grupo4.com.core.manejadores;

import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import com.mongodb.client.model.Filters;

import grupo4.com.core.modelJEE.InfoCabezalNodo;
import grupo4.com.core.modelJEE.InfoDisco;
import grupo4.com.util.Log;
import  grupo4.com.util.UtilMongo;

@SuppressWarnings("unused")
public class ManejadorInfra {

	public List<String> getColeccionesAsignadas(Log log){
		List<String> colecciones = null;
		MongoClient mongoClient = null;
		try {
			colecciones = new ArrayList<String>();
			String con = UtilMongo.ConMongoLogtek();
			MongoClientURI connectionString = new MongoClientURI(con);
			mongoClient = new MongoClient(connectionString);
			MongoDatabase database = mongoClient.getDatabase("logtek");
			MongoIterable <String> nombres = database.listCollectionNames();
            for (String nombreColeccion: nombres) {
            	if(nombreColeccion != null && !nombreColeccion.equals("system.indexes")) {
            		colecciones.add(nombreColeccion);
            	}
            }
		}catch(Throwable t){
			log.log("No es posible devolver coleciones debido a ["+t.getMessage()+"]");
		}finally {
			mongoClient.close();
		}
		return colecciones;
	}
}
