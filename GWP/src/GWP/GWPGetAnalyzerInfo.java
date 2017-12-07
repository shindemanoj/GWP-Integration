package GWP;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class GWPGetAnalyzerInfo extends GWPConnector {
	/**
	 * Name: main
	 * Description: Call checkAnalyzerConnected
	 * @param args
	 */
	public static void main(String [] args){
		GWPGetAnalyzerInfo connector = new GWPGetAnalyzerInfo();
		boolean result = connector.verifyAnalyteStatus("gemtestpc8", "tbili", "APV/Amp. Due");
		if(result){
			System.out.println("Got it");
		}
	}
	
	/**
	 * Name: verifyAnalyzerConnected
	 * Description: Verify if Analyzer is connected in GWP
	 * @param analyzerName
	 * @return true or false
	 */
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
	
	/**
	 * Name: verifyAnalytesWithoutCartridge
	 * Description: Verify Analytes are not present without Cartridge in Analyzer
	 * @param analyzerName
	 * @return true or false
	 */
	public boolean verifyAnalytesWithoutCartridge(String analyzerName) {
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
			JSONArray analytes = analyzerInfo.getJSONArray("analytes");
			if(analytes.length() == 0){
				result = true;
			}
			
			logout(client);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Name: verifyAnalytesWithCartridge
	 * Description: Verify all analytes present in Analyzer with Cartridge
	 * @param analyzerName
	 * @param analytesList
	 * @return
	 */
	public boolean verifyAnalytesWithCartridge(String analyzerName, List<String> analytesList) {
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
			JSONArray analytes = analyzerInfo.getJSONArray("analytes");
			boolean allAnalytesPresent = true;
			for (int i = 0; i < analytes.length(); i++) {
				JSONObject analyteInfo = analytes.getJSONObject(i);
				String analyteName = analyteInfo.getString("name").replaceAll("\\<.*?\\>", "");
				System.out.println(analyteName);
				if(!analytesList.contains(analyteName)){
					allAnalytesPresent = false;
				}
			}
			result = allAnalytesPresent;
			
			logout(client);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Name: verifyStatusWithCartridge
	 * Description: Verify Analyzer Status without Cartridge
	 * @param analyzerName
	 * @return true or false
	 */
	public boolean verifyStatusWithCartridge(String analyzerName) {
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
			String status = analyzerInfo.getJSONObject("DashboardAnalyzerVO").getString("uiState");
			if(status.equals("WARMING_UP")){
				result = true;
			}
			
			logout(client);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Name: verifyTestsAndDaysWithCartridge
	 * Description: Verify Tests and Days with Cartridge of Analyzer
	 * @param analyzerName
	 * @param tests
	 * @param days
	 * @return true or false
	 */
	public boolean verifyTestsAndDaysWithCartridge(String analyzerName, int tests, int days) {
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
			JSONObject analyzerCartridgeInfo = analyzerInfo.getJSONObject("DashboardAnalyzerVO").getJSONObject("CatridgeStatusVO");
			if(analyzerCartridgeInfo.getString("tests").equals(String.valueOf(tests)) && 
					analyzerCartridgeInfo.getString("time").equals(String.valueOf(days))){
				result =true;
			}
			
			logout(client);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Name: verifyCVPDueStatus
	 * Description: Verify CVP Due Status of Analyzer
	 * @param analyzerName
	 * @return
	 */
	public boolean verifyCVPDueStatus(String analyzerName) {
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
			String status = analyzerInfo.getJSONObject("DashboardAnalyzerVO").getString("uiState");
			if(status.equals("CVP_DUE")){
				result = true;
			}
			
			logout(client);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Name: verifyAnalyzerStatusBar
	 * Description: Verify Analyzer's Status Bar
	 * @param analyzerName
	 * @param instrumentStatus
	 * @return
	 */
	public boolean verifyAnalyzerStatusBar(String analyzerName, String instrumentStatus) {
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
			String status = analyzerInfo.getJSONObject("DashboardAnalyzerVO").getString("uiState");
			if(status.equalsIgnoreCase(instrumentStatus.replaceAll(" ", "_"))){
				result = true;
			}
			
			logout(client);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Name: verifyAnalyteStatus
	 * Description: Verify Status of Anlayte
	 * @param analyzerName
	 * @param analyte
	 * @param status
	 * @return
	 */
	public boolean verifyAnalyteStatus(String analyzerName, String analyte, String status) {
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
			JSONArray analytes = analyzerInfo.getJSONArray("analytes");
			for (int i = 0; i < analytes.length(); i++) {
				JSONObject analyteInfo = analytes.getJSONObject(i);
				String analyteName = analyteInfo.getString("name").replaceAll("\\<.*?\\>", "");
				System.out.println(analyteName);
				if(analyte.equalsIgnoreCase(analyteName)){
					if(analyteInfo.getString("state").equalsIgnoreCase(status.replaceAll(" ", "_")) 
							|| analyteInfo.getString("stateText").equalsIgnoreCase(status)){
						result = true;
					}
				}
			}
			
			logout(client);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
