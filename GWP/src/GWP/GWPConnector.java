package GWP;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

@SuppressWarnings("deprecation")
public abstract class GWPConnector {
	protected static String GWP_IP;
	protected static String LOGIN_URL;
	protected static String LOGOUT_URL;
	protected static String GET_SAMPLE_URL;
	protected static String USERNAME;
	protected static String ENCODED_PASSWORD;
	protected static String GET_DATE_TIME_URl;
	protected static String GET_SAMPLE_COUNT_URL;
	
	protected GWPConnector() {
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream("config.properties");

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			System.out.println(prop.getProperty("gwp_ip"));
			System.out.println(prop.getProperty("username"));
			System.out.println(prop.getProperty("encoded_pwd"));
			
			GWP_IP = "https://" + prop.getProperty("gwp_ip") + "/";
			LOGIN_URL = GWP_IP + "api/login";
			LOGOUT_URL = GWP_IP + "api/logout";
			GET_SAMPLE_URL = GWP_IP + "api/samples?sampleNumber=*&operatorId=*&clinician=*&orderNumber=*&limit=50&offset=0";
			USERNAME = prop.getProperty("username");
			ENCODED_PASSWORD = prop.getProperty("encoded_pwd");
			GET_DATE_TIME_URl = GWP_IP + "api/global/datetime";
			GET_SAMPLE_COUNT_URL = GWP_IP + "api/samples/count?sampleNumber=*&operatorId=*&clinician=*&orderNumber=*&limit=50&offset=0";
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Name: login
	 * Description: Method to login GWP Client
	 * @param client Instance of CloseableHttpClient
	 * @throws Exception in case of any thing wrong
	 */
	protected CloseableHttpClient login() throws Exception{
		// Create new object of HttpClients with SSL certificate to access Https urls
		CloseableHttpClient client = null;
			client = HttpClients.custom()
			        .setSSLSocketFactory(new SSLConnectionSocketFactory(SSLContexts.custom()
			                .loadTrustMaterial(null, new TrustSelfSignedStrategy())
			                .build())).build();
		
		// Create httpPost object to send post request
		String loginURL = GWP_IP + "api/login";
		HttpPost httpPost = new HttpPost(loginURL);
        
        // add header and login credentials to httpPost request
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", USERNAME));
        params.add(new BasicNameValuePair("password", ENCODED_PASSWORD));
        httpPost.setEntity(new UrlEncodedFormEntity(params));
        
        // Execute Post request and get response
        CloseableHttpResponse response = client.execute(httpPost);
        System.out.println("\nSending 'POST' request to URL : " + loginURL);
		System.out.println("Post parameters : " + httpPost.getEntity());
		System.out.println("\nResponse Code : " + response.getStatusLine().getStatusCode());

		String result = EntityUtils.toString(response.getEntity());
		System.out.println(result);
		return client;
	}
	
	protected String getAnalyzerId(CloseableHttpClient client, String analyzerName) throws Exception{
		
		String analyzerId = "";
		String getAnalyzerInfoUrl = GWP_IP + "api/analyzers";
		HttpGet request = new HttpGet(getAnalyzerInfoUrl);

		HttpResponse response = client.execute(request);

		System.out.println("\nSending 'GET' request to URL : " + getAnalyzerInfoUrl);
		System.out.println("Response Code : " +
				response.getStatusLine().getStatusCode());
		
		JSONArray analyzers = new JSONArray(EntityUtils.toString(response.getEntity()));
		for (int i = 0; i < analyzers.length(); i++) {
			JSONObject analyzer = analyzers.getJSONObject(i);
			if(analyzer.get("name").equals(analyzerName + "-1")){
				return analyzer.getString("id");
			}			
		}
		return analyzerId;
	}
	
	/**
	 * Name: Logout
	 * Description: Logout from GWP Client Session
	 * @param client
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	protected void logout(CloseableHttpClient client) throws Exception{
		
		// Send GET request to get samples
		String logoutUrl = GWP_IP + "api/logout";
		HttpGet request = new HttpGet(logoutUrl);

		HttpResponse response = client.execute(request);

		System.out.println("\nSending 'GET' request to URL : " + logoutUrl);
		System.out.println("Response Code : " +
				response.getStatusLine().getStatusCode());
		
		client.close();
	}
}
