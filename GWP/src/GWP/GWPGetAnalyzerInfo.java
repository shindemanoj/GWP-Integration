package GWP;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class GWPGetAnalyzerInfo extends GWPConnector {
	/**
	 * Name: main
	 * Description: Call checkAnalyzerConnected
	 * @param args
	 */
	public static void main(String [] args){
		GWPGetAnalyzerInfo connector = new GWPGetAnalyzerInfo();
		boolean result = connector.verifyAnalyzerConnected("gemtestpc1");
		if(result){
			System.out.println("Got it");
		}
	}
	
	public boolean verifyAnalyzerConnected(String analyzerName) {
		boolean result = false;
		String getAnalyzerStatusUrl = GWP_IP + "api/analyzers/";
		
		try {
			// Call login method
			CloseableHttpClient client = login();
			
			String analyzerId = getAnalyzerId(client, analyzerName);
			System.out.println("Analyzer ID:" + analyzerId);
			getAnalyzerStatusUrl = getAnalyzerStatusUrl + analyzerId + "/status";
			
			// Send GET request to get analyzer status
			HttpGet request = new HttpGet(getAnalyzerStatusUrl);
			HttpResponse response = client.execute(request);
	
			System.out.println("\nSending 'GET' request to URL : " + getAnalyzerStatusUrl);
			System.out.println("Response Code : " +
					response.getStatusLine().getStatusCode());
			
			JSONObject analyzerInfo = new JSONObject(EntityUtils.toString(response.getEntity()));
			System.out.println(analyzerInfo);
			result = analyzerInfo.getJSONObject("DashboardAnalyzerVO").getBoolean("connected");
			
			logout(client);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
