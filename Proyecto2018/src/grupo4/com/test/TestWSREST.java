package grupo4.com.test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.util.EntityUtils;

@SuppressWarnings({ "unused", "deprecation" })
public class TestWSREST {

	private String host;
	private static String usuario = "fede" ;
	private static String password = "fede";
	
	public static void main(String[] args) {
		try {
			String url 		= "";
			String host 	= "localhost";

			TestWSREST t = new TestWSREST(host, usuario, password);
			String token = t.login();
			
			url = "https://"+host+":8443/Proyecto2018/rest/infra/listarNodos";
			t.procesarGET(token, url);
			
			url = "https://"+host+":8443/Proyecto2018/rest/info/free/node3";
			t.procesarGET(token, url);
			
			url = "https://"+host+":8443/Proyecto2018/rest/info/infoDisco/node3";
			t.procesarGET(token, url);
			
			url = "https://"+host+":8443/Proyecto2018/rest/info/cabezal/node3";
			t.procesarGET(token, url);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public TestWSREST(String host, String usuario, String password) {
		this.host 		= host;
		TestWSREST.usuario 	= usuario;
		TestWSREST.password 	= password;
	}
	
	public void procesarGET(String token, String url) throws Exception {
		try {
			HttpClient client = getClient();
			HttpGet get = new HttpGet(url);
			get.setHeader("Content-Type", "application/json");
			if (token != null) {
				get.setHeader("SecurityToken", token);
			}

			HttpResponse httpResponse = client.execute(get);
			String content = EntityUtils.toString(httpResponse.getEntity());
			System.out.println("-------------------------------------------------------------"); 
			System.out.println(content);
			System.out.println("-------------------------------------------------------------"); 

		} catch (Exception e) {
			throw new Exception("Error consuming service.", e);
		}
	}
	
	public String login() throws Exception {
		String url = "https://"+host+":8443/Proyecto2018/rest/seguridad/token";

		try {
			HttpClient client = createHttpClient();
			String authData = usuario + ":" + password;
			String encoded = new sun.misc.BASE64Encoder().encode(authData.getBytes());
			
			SSLContext sslContext = SSLContext.getInstance("TLS");      
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			InputStream is = new FileInputStream("C:\\restProy.cert");
			InputStream caInput = new BufferedInputStream(is);
			Certificate ca = null;
			try {
			    ca = cf.generateCertificate(caInput);
			} finally {
			    caInput.close();
			}
			String keyStoreType = KeyStore.getDefaultType();
			KeyStore keyStore = KeyStore.getInstance(keyStoreType);
			keyStore.load(null, null);
			keyStore.setCertificateEntry("ca", ca);
			client.getConnectionManager();
			
			HttpGet get = new HttpGet(url);
			get.setHeader("Content-Type", "application/json");
			get.setHeader("Authorization", "Basic " + encoded);

			HttpResponse httpResponse = client.execute(get);
			Header [] hs = httpResponse.getHeaders("SecurityToken");
			if (hs.length == 1) {
				Header h = hs[0];
				String token = h.getValue();
				System.out.println("-------------- Resultado Login  -----------------------------"); 
				System.out.println("--> Estas loggeado como ["+usuario+"]"); 
				System.out.println("--> JWT :["+token+"]");
				System.out.println("-------------- Estas dentro!  -------------------------"); 
				return token;
			}
			System.out.println("--> No se pudo obtener token");

			return null;
		} catch (Exception e) {
			throw new Exception("Error consuming service.", e);
		}
	}
	
	private static HttpClient createHttpClient() throws NoSuchAlgorithmException, KeyManagementException {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
	
			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}
	
			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };
		SSLContext context = SSLContext.getInstance("TLS");
		context.init(null, trustAllCerts, null);
		SSLSocketFactory sf = new SSLSocketFactory(context, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		schemeRegistry.register(new Scheme("https", 443, sf));
		SingleClientConnManager cm = new SingleClientConnManager(schemeRegistry);
		return new DefaultHttpClient(cm);
	}

	private HttpClient getClient() throws Exception {
		HttpClient client = createHttpClient();
		SSLContext sslContext = SSLContext.getInstance("TLS");      
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		InputStream is = new FileInputStream("C:\\restProy.cert");
		InputStream caInput = new BufferedInputStream(is);
		Certificate ca = null;
		
		try {
		    ca = cf.generateCertificate(caInput);
		} finally {
		    caInput.close();
		}
		
		String keyStoreType = KeyStore.getDefaultType();
		KeyStore keyStore = KeyStore.getInstance(keyStoreType);
		keyStore.load(null, null);
		keyStore.setCertificateEntry("ca", ca);
		client.getConnectionManager();
		return client;

	}
	
}
