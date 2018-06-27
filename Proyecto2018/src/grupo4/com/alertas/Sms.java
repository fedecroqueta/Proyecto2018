package grupo4.com.alertas;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.util.EntityUtils;


@SuppressWarnings({ "deprecation", "unused" })
public class Sms {
	
	private String host;
	
	public static boolean enviarSms(String numero, String cuerpoSms) throws Exception {
		boolean enviado = false;
		String host 	= "localhost";
		Sms t = new Sms(host);
		
		String url = "http://"+host+":8080/TwilioSms/rest/endpointSms/sms/"+numero+"/"+cuerpoSms;
		t.procesarPOST(null, url);
		
		return enviado;

	}
	public Sms(String host) {
		this.host 		= host;

	}
	
	public void procesarPOST(String token, String url) throws Exception {
		try {
			HttpClient client = getClient();
			HttpPost post = new HttpPost(url);
			post.setHeader("Content-Type", "application/json");
			if (token != null) {
				post.setHeader("SecurityToken", token);
			}

			HttpResponse httpResponse = client.execute(post);
			String content = EntityUtils.toString(httpResponse.getEntity());
			System.out.println("-------------------------------------------------------------"); 
			System.out.println(content);
			System.out.println("-------------------------------------------------------------"); 

		} catch (Exception e) {
			throw new Exception("Error consuming service.", e);
		}
	}
	
	@SuppressWarnings("unused")
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

}
