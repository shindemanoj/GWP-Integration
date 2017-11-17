package GWP;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class GWPSampleInfo extends GWPConnector{
	
	/**
	 * Name: main
	 * Description: Call getSampleData method with required parameters
	 * @param args
	 */
	public static void main(String [] args){
		GWPSampleInfo connector = new GWPSampleInfo();
		boolean result = connector.getSample("VENOUS", "PID-2348", "2017-11-17 15:59:00", "gemtestpc1");
		if(result){
			System.out.println("Got it");
		}
	}
	
	/**
	 * Name: getSample
	 * Description: Login to GWP Server and check if sample exists for matching parameters
	 * @param sampleType
	 * @param patientId
	 * @param sampleAnalyzedTime
	 * @param analyzerName
	 * @return true or false
	 */
	public boolean getSample(String sampleType, String patientId, String sampleAnalyzedTime, String analyzerName) {
		// Initialize variables
		String baseUrl = GWP_IP + "api/samples?sampleNumber=*&operatorId=*&clinician=*&orderNumber=*&limit=50&offset=0";
		System.out.println("\nLogin - Send Http POST request");
		
		try {
			// Call login method
			CloseableHttpClient client = login();
			
			String analyzerId = getAnalyzerId(client, analyzerName);
			System.out.println("Analyzer ID:" + analyzerId);
			
			// Generate timestamp using input parameter
			Date timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sampleAnalyzedTime);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	
			// Create url to get sample data with given arguments
			String sampleURL = baseUrl + "&patientId="+patientId+"&sourceCode=" + sampleType.toUpperCase() + "&dateFrom="+
					dateFormat.format(new Date(System.currentTimeMillis()-3600*1000*2))+"&dateTo="+dateFormat.format(new Date()) 
					+"&timestamp="+dateFormat.format(timestamp) + "&analyzerId=" + analyzerId;
			
			// Send GET request to get samples
			HttpGet request = new HttpGet(sampleURL);
	
			HttpResponse samples = client.execute(request);
	
			System.out.println("\nSending 'GET' request to URL : " + sampleURL);
			System.out.println("Response Code : " +
					samples.getStatusLine().getStatusCode());
			
			JSONArray sampleArray  = new JSONArray(EntityUtils.toString(samples.getEntity()));
			for (int i = 0; i < sampleArray.length(); i++) {
				JSONObject sample = sampleArray.getJSONObject(i);
				if(sample.get("analyzerName").equals(analyzerName + "-1") && sample.get("sampleSourceName").toString().equalsIgnoreCase(sampleType) && 
						sample.getString("patientId").equals(patientId)){
					return true;
				}
			}
			System.out.println(sampleArray);
			
			logout(client);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return false;
	}
	
	/**
	 * Name: getSample
	 * Description: Login to GWP Server and check if sample exists for matching parameters
	 * @param sampleType
	 * @param patientId
	 * @param sampleAnalyzedTime
	 * @param analyzerName
	 * @return true or false
	 */
	public boolean getSampleByOperatorId(String sampleType, String patientId, String sampleAnalyzedTime, String analyzerName) {
		// Initialize variables
		String baseUrl = GWP_IP + "api/samples?sampleNumber=*&operatorId=*&clinician=*&orderNumber=*&limit=50&offset=0";
		System.out.println("\nLogin - Send Http POST request");
		
		try {
			// Call login method
			CloseableHttpClient client = login();
			
			String analyzerId = getAnalyzerId(client, analyzerName);
			System.out.println("Analyzer ID:" + analyzerId);
			
			// Generate timestamp using input parameter
			Date timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sampleAnalyzedTime);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	
			// Create url to get sample data with given arguments
			String sampleURL = baseUrl + "&patientId="+patientId+"&sourceCode=" + sampleType.toUpperCase() + "&dateFrom="+
					dateFormat.format(new Date(System.currentTimeMillis()-3600*1000*2))+"&dateTo="+dateFormat.format(new Date()) 
					+"&timestamp="+dateFormat.format(timestamp) + "&analyzerId=" + analyzerId;
			
			// Send GET request to get samples
			HttpGet request = new HttpGet(sampleURL);
	
			HttpResponse samples = client.execute(request);
	
			System.out.println("\nSending 'GET' request to URL : " + sampleURL);
			System.out.println("Response Code : " +
					samples.getStatusLine().getStatusCode());
			
			JSONArray sampleArray  = new JSONArray(EntityUtils.toString(samples.getEntity()));
			for (int i = 0; i < sampleArray.length(); i++) {
				JSONObject sample = sampleArray.getJSONObject(i);
				if(sample.get("analyzerName").equals(analyzerName + "-1") && sample.get("sampleSourceName").toString().equalsIgnoreCase(sampleType) && 
						sample.getString("patientId").equals(patientId)){
					return true;
				}
			}
			System.out.println(sampleArray);
			
			logout(client);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return false;
	}
}
