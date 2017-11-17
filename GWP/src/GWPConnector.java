import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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

/**
 * 
 * GWPConnector Class for GWP Integration
 * @author mshinde
 *
 */
@SuppressWarnings("deprecation")
public class GWPConnector {	
	public static final String LOGIN_URL = "https://192.168.237.241/api/login";
	public static final String GET_SAMPLE_URL = "https://192.168.237.241/api/samples?sampleNumber=*&operatorId=*&clinician=*&orderNumber=*&limit=50&offset=0";
	public static final String USERNAME = "supervisor";
	public static final String ENCODED_PASSWORD = "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92";
	public static final String GET_DATE_TIME_URl = "https://192.168.237.241/api/global/datetime";
	public static final String GET_SAMPLE_COUNT_URL = "https://192.168.237.241/api/samples/count?sampleNumber=*&operatorId=*&clinician=*&orderNumber=*&limit=50&offset=0";
	
	/**
	 * Name: main
	 * Description: Call getSampleData method with required parameters
	 * @param args
	 */
	public static void main(String [] args){
		GWPConnector.getSampleData("CAPILLARY", "PID-130", "2017-11-15 09:59:00");
	}
	
	/**
	 * Name: getSampleData
	 * Description: Login to GWP Server and get sample data for matching parameters
	 * @param sampleType
	 * @param patientId
	 * @param sampleAnalyzedTime
	 * @return null or sample data array
	 */
	public static String getSampleData(String sampleType, String patientId, String sampleAnalyzedTime) {
		// Initialize variables
		CloseableHttpClient client;
		String result = null;
		
		try {
			// Create new object of HttpClients with SSL certificate to access Https urls
			client = HttpClients.custom()
			        .setSSLSocketFactory(new SSLConnectionSocketFactory(SSLContexts.custom()
			                .loadTrustMaterial(null, new TrustSelfSignedStrategy())
			                .build()
			            )
			        ).build();
		
		System.out.println("\nLogin - Send Http POST request");
		
		// Call login method
		login(client);
		
		// Generate timestamp using input parameter
		Date timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sampleAnalyzedTime);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

		// Create url to get sample data with given arguments
		String sampleURL = GET_SAMPLE_URL + "&patientId="+patientId+"&sourceCode="+sampleType+"&dateFrom="+
				dateFormat.format(new Date(System.currentTimeMillis()-3600*1000*48))+"&dateTo="+dateFormat.format(new Date()) +"&timestamp="+dateFormat.format(timestamp);
		
		if(sampleType.isEmpty()){
			sampleURL = GET_SAMPLE_URL + "&patientId="+patientId+"&sourceCode="+sampleType+"&dateFrom="+
					dateFormat.format(new Date(System.currentTimeMillis()-3600*1000*48))+"&dateTo="+dateFormat.format(new Date()) +"&timestamp="+dateFormat.format(timestamp);
		}
		if(patientId.isEmpty()){
			patientId = "*";
		}
		
		// Send GET request to get samples
		HttpGet request = new HttpGet(sampleURL);

		HttpResponse samples = client.execute(request);

		System.out.println("\nSending 'GET' request to URL : " + sampleURL);
		System.out.println("Response Code : " +
				samples.getStatusLine().getStatusCode());
		
		
		result  = EntityUtils.toString(samples.getEntity());
		
		JSONArray sample = new JSONArray(result);
		System.out.println("\nAnalyzer Name: " + sample.get(0));
		
		client.close();
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * Name: checkSampleExist
	 * Description: Login to GWP Server and check if sample exists for matching parameters
	 * @param sampleType
	 * @param patientId
	 * @param sampleAnalyzedTime
	 * @return true or false
	 */
	public static boolean checkSampleExist(String sampleType, String patientId, String sampleAnalyzedTime) {
		// Initialize variables
		CloseableHttpClient client;
		int count = 0;
		
		try {
			// Create new object of HttpClients with SSL certificate to access Https urls
			client = HttpClients.custom()
			        .setSSLSocketFactory(new SSLConnectionSocketFactory(SSLContexts.custom()
			                .loadTrustMaterial(null, new TrustSelfSignedStrategy())
			                .build()
			            )
			        ).build();
		
		System.out.println("\nLogin - Send Http POST request");
		
		// Call login method
		login(client);
		
		// Generate timestamp using input parameter
		Date timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sampleAnalyzedTime);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

		// Create url to get sample data with given arguments
		String sampleURL = GET_SAMPLE_COUNT_URL + "&patientId="+patientId+"&sourceCode="+sampleType+"&dateFrom="+
				dateFormat.format(new Date(System.currentTimeMillis()-3600*1000*48))+"&dateTo="+dateFormat.format(new Date()) +"&timestamp="+dateFormat.format(timestamp);
		
		if(sampleType.isEmpty()){
			sampleURL = GET_SAMPLE_COUNT_URL + "&patientId="+patientId+"&sourceCode="+sampleType+"&dateFrom="+
					dateFormat.format(new Date(System.currentTimeMillis()-3600*1000*48))+"&dateTo="+dateFormat.format(new Date()) +"&timestamp="+dateFormat.format(timestamp);
		}
		if(patientId.isEmpty()){
			patientId = "*";
		}
		// Send GET request to get samples
		HttpGet request = new HttpGet(sampleURL);

		HttpResponse samples = client.execute(request);

		System.out.println("\nSending 'GET' request to URL : " + sampleURL);
		System.out.println("Response Code : " +
				samples.getStatusLine().getStatusCode());
		
		count  = Integer.parseInt(EntityUtils.toString(samples.getEntity()));
		System.out.println(count);
		client.close();
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return count != 0;
	}
	
	/**
	 * Name: login
	 * Description: Method to login GWP Client
	 * @param client Instance of CloseableHttpClient
	 * @throws Exception in case of any thing wrong
	 */
	public static void login(CloseableHttpClient client) throws Exception {
		System.out.println("\nGet Sample Data - Send Http GET request");
		// Create httpPost object to send post request
		HttpPost httpPost = new HttpPost(LOGIN_URL);
        
        // add header and login credentials to httpPost request
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", USERNAME));
        params.add(new BasicNameValuePair("password", ENCODED_PASSWORD));
        httpPost.setEntity(new UrlEncodedFormEntity(params));
        
        // Execute Post request and get response
        CloseableHttpResponse response = client.execute(httpPost);
        System.out.println("\nSending 'POST' request to URL : " + LOGIN_URL);
		System.out.println("Post parameters : " + httpPost.getEntity());
		System.out.println("\nResponse Code : " +
                                    response.getStatusLine().getStatusCode());

		String result = EntityUtils.toString(response.getEntity());
		System.out.println(result);
	}
}