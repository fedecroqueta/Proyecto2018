package grupo4.com.core.modelJSON;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static java.util.Arrays.asList;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@SuppressWarnings("unused")
public final class LogNodoJSON {
	
	public ObjectId id;
	public String time;
	public String serverName;
	public String distro;
	public String uptime;
	public String ipAdd;
	public String publicIP;
	public int cantidadCpu;
	public float cpuLoad;
	public long totalMemoria;
	public long memoriaEnUso;
	public long memoriaLibre;
	
	
	/**
	 * Consutructor con todos los campos
	 * @param id
	 * @param time
	 * @param serverName
	 * @param distro
	 * @param uptime
	 * @param ipAdd
	 * @param publicIP
	 * @param cantidadCpu
	 * @param cpuLoad
	 * @param totalMemoria
	 * @param memoriaEnUso
	 * @param memoriaLibre
	 */
	public LogNodoJSON(ObjectId id, String time, String serverName, String distro, String uptime, String ipAdd,
			String publicIP, int cantidadCpu, float cpuLoad, long totalMemoria, long memoriaEnUso, long memoriaLibre) {
		super();
		this.id = id;
		this.time = time;
		this.serverName = serverName;
		this.distro = distro;
		this.uptime = uptime;
		this.ipAdd = ipAdd;
		this.publicIP = publicIP;
		this.cantidadCpu = cantidadCpu;
		this.cpuLoad = cpuLoad;
		this.totalMemoria = totalMemoria;
		this.memoriaEnUso = memoriaEnUso;
		this.memoriaLibre = memoriaLibre;
	}
	
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getDistro() {
		return distro;
	}
	public void setDistro(String distro) {
		this.distro = distro;
	}
	public String getUptime() {
		return uptime;
	}
	public void setUptime(String uptime) {
		this.uptime = uptime;
	}
	public String getIpAdd() {
		return ipAdd;
	}
	public void setIpAdd(String ipAdd) {
		this.ipAdd = ipAdd;
	}
	public String getPublicIP() {
		return publicIP;
	}
	public void setPublicIP(String publicIP) {
		this.publicIP = publicIP;
	}
	public int getCantidadCpu() {
		return cantidadCpu;
	}
	public void setCantidadCpu(int cantidadCpu) {
		this.cantidadCpu = cantidadCpu;
	}
	public float getCpuLoad() {
		return cpuLoad;
	}
	public void setCpuLoad(float cpuLoad) {
		this.cpuLoad = cpuLoad;
	}
	public long getTotalMemoria() {
		return totalMemoria;
	}
	public void setTotalMemoria(long totalMemoria) {
		this.totalMemoria = totalMemoria;
	}
	public long getMemoriaEnUso() {
		return memoriaEnUso;
	}
	public void setMemoriaEnUso(long memoriaEnUso) {
		this.memoriaEnUso = memoriaEnUso;
	}
	public long getMemoriaLibre() {
		return memoriaLibre;
	}
	public void setMemoriaLibre(long memoriaLibre) {
		this.memoriaLibre = memoriaLibre;
	}
	
	

}
