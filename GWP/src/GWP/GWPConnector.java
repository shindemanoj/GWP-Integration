package GWP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	public static final String LOGIN_URL = "https://192.168.237.241/api/login";
	public static final String LOGOUT_URL = "https://192.168.237.241/api/logout";
	public static final String GET_SAMPLE_URL = "https://192.168.237.241/api/samples?sampleNumber=*&operatorId=*&clinician=*&orderNumber=*&limit=50&offset=0";
	public static final String USERNAME = "supervisor";
	public static final String ENCODED_PASSWORD = "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92";
	public static final String GET_DATE_TIME_URl = "https://192.168.237.241/api/global/datetime";
	public static final String GET_SAMPLE_COUNT_URL = "https://192.168.237.241/api/samples/count?sampleNumber=*&operatorId=*&clinician=*&orderNumber=*&limit=50&offset=0";
	public static final String GWP_IP = "https://192.168.237.241/";
	
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
		System.out.println("\nGet Sample Data - Send Http GET request");
		
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
	public void logout(CloseableHttpClient client) throws Exception{
		
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
