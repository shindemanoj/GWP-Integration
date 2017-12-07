package GWP;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class GWPGetConfigInfo extends GWPConnector {
	/**
	 * Name: main
	 * Description: Call checkAnalyzerConnected
	 * @param args
	 */
	public static void main(String [] args){
		GWPGetConfigInfo connector = new GWPGetConfigInfo();
		boolean result = connector.verifyOtherMaterialSetup("1503", "GEM System Evaluator 1", "GEM5000");
		if(result){
			System.out.println("Got it");
		}
	}
	
	/**
	 * Name: verifyParametersSetup
	 * Description: Verify if parameter is enabled or not in GWP
	 * @param analyzerName
	 * @param parameter
	 * @param enabled
	 * @return true or false
	 */
	public boolean verifyParametersSetup(String analyzerName, String parameter, boolean enabled) {
		boolean result = false;
		String getParametersSetupUrl = GWP_IP + "api/analyzers/";
		
		try {
			// Call login method
			CloseableHttpClient client = login();
			
			String analyzerId = getAnalyzerId(client, analyzerName);
			System.out.println("Analyzer ID:" + analyzerId);
			getParametersSetupUrl = getParametersSetupUrl + analyzerId + "/config/parameter_setup";
			
			// Send GET request to get analyzer status
			HttpGet request = new HttpGet(getParametersSetupUrl);
			HttpResponse response = client.execute(request);
	
			System.out.println("\nSending 'GET' request to URL : " + getParametersSetupUrl);
			System.out.println("Response Code : " +
					response.getStatusLine().getStatusCode());
			
			JSONArray parametersInfo = new JSONArray(EntityUtils.toString(response.getEntity()));
			for (int i = 0; i < parametersInfo.length(); i++) {
				JSONObject parameterObj = parametersInfo.getJSONObject(i);
				if(parameterObj.getString("parameterNumber").equalsIgnoreCase(parameter.replaceAll(" ", "_").replaceAll("\\p{P}","_"))){
					if(enabled == parameterObj.getBoolean("enable")){
						result = true;
					}
					break;
				}
			}
			
			logout(client);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Name: verifyCorrelationFactors
	 * Description: Verify parameter's slope and offset in GWP
	 * @param analyzerName
	 * @param parameter
	 * @param slope
	 * @param offset
	 * @return true or false
	 */
	public boolean verifyCorrelationFactors(String analyzerName, String parameter, double slope, double offset) {
		boolean result = false;
		String getParametersSetupUrl = GWP_IP + "api/analyzers/";
		
		try {
			// Call login method
			CloseableHttpClient client = login();
			
			String analyzerId = getAnalyzerId(client, analyzerName);
			System.out.println("Analyzer ID:" + analyzerId);
			getParametersSetupUrl = getParametersSetupUrl + analyzerId + "/config/correlation_factors";
			
			// Send GET request to get analyzer status
			HttpGet request = new HttpGet(getParametersSetupUrl);
			HttpResponse response = client.execute(request);
	
			System.out.println("\nSending 'GET' request to URL : " + getParametersSetupUrl);
			System.out.println("Response Code : " +
					response.getStatusLine().getStatusCode());
			
			JSONObject correlationInfo = new JSONObject(EntityUtils.toString(response.getEntity()));
			JSONArray correlationList = correlationInfo.getJSONArray("list");
			for (int i = 0; i < correlationList.length(); i++) {
				JSONObject correlationObj = correlationList.getJSONObject(i);
				if(correlationObj.getString("parameterNumber").equalsIgnoreCase(parameter.replaceAll(" ", "_").replaceAll("\\p{P}","_"))){
					if(correlationObj.getDouble("slope") == slope && correlationObj.getDouble("offset") == offset){
						result = true;
					}
					break;
				}
			}
			
			logout(client);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Name: verifySampleRemovalConfirmation
	 * Description: Verify Sample Removal Confirmation
	 * @param analyzerName
	 * @param confirmSampleRemoval
	 * @param retractProbeTimeout
	 * @return true or false
	 */
	public boolean verifySampleRemovalConfirmation(String analyzerName, boolean confirmSampleRemoval, int retractProbeTimeout) {
		boolean result = false;
		String getParametersSetupUrl = GWP_IP + "api/analyzers/";
		
		try {
			// Call login method
			CloseableHttpClient client = login();
			
			String analyzerId = getAnalyzerId(client, analyzerName);
			System.out.println("Analyzer ID:" + analyzerId);
			getParametersSetupUrl = getParametersSetupUrl + analyzerId + "/config/sample_removal_confirmation";
			
			// Send GET request to get analyzer status
			HttpGet request = new HttpGet(getParametersSetupUrl);
			HttpResponse response = client.execute(request);
	
			System.out.println("\nSending 'GET' request to URL : " + getParametersSetupUrl);
			System.out.println("Response Code : " +
					response.getStatusLine().getStatusCode());
			
			JSONObject sampleRemovalInfo = new JSONObject(EntityUtils.toString(response.getEntity()));
			if(sampleRemovalInfo.getInt("retractProbeTimeout") == retractProbeTimeout 
					&& sampleRemovalInfo.getBoolean("confirmSampleRemoval") == confirmSampleRemoval){
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
	 * Name: verifySoundVolume
	 * Description: Verify Sound Volume in GWP
	 * @param analyzerName
	 * @param touchKeySound
	 * @return true or false
	 */
	public boolean verifySoundVolume(String analyzerName, String touchKeySound) {
		boolean result = false;
		String getAnalyzerConfigUrl = GWP_IP + "api/analyzers/";
		
		try {
			// Call login method
			CloseableHttpClient client = login();
			
			String analyzerId = getAnalyzerId(client, analyzerName);
			System.out.println("Analyzer ID:" + analyzerId);
			getAnalyzerConfigUrl = getAnalyzerConfigUrl + analyzerId + "/config/sound_volume";
			
			// Send GET request to get analyzer status
			HttpGet request = new HttpGet(getAnalyzerConfigUrl);
			HttpResponse response = client.execute(request);
	
			System.out.println("\nSending 'GET' request to URL : " + getAnalyzerConfigUrl);
			System.out.println("Response Code : " +
					response.getStatusLine().getStatusCode());
			
			JSONObject soundVolumeInfo = new JSONObject(EntityUtils.toString(response.getEntity()));
			if(soundVolumeInfo.getString("touchKeySound").equalsIgnoreCase(touchKeySound)){
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
	 * Name: verifyExternalKeyboard
	 * Description: Verify if External Keyboard is enabled in GWP
	 * @param analyzerName
	 * @param useExternalKeyboard
	 * @return
	 */
	public boolean verifyExternalKeyboard(String analyzerName, boolean useExternalKeyboard) {
		boolean result = false;
		String getAnalyzerConfigUrl = GWP_IP + "api/analyzers/";
		
		try {
			// Call login method
			CloseableHttpClient client = login();
			
			String analyzerId = getAnalyzerId(client, analyzerName);
			System.out.println("Analyzer ID:" + analyzerId);
			getAnalyzerConfigUrl = getAnalyzerConfigUrl + analyzerId + "/config/external_keyboard";
			
			// Send GET request to get analyzer status
			HttpGet request = new HttpGet(getAnalyzerConfigUrl);
			HttpResponse response = client.execute(request);
	
			System.out.println("\nSending 'GET' request to URL : " + getAnalyzerConfigUrl);
			System.out.println("Response Code : " +
					response.getStatusLine().getStatusCode());
			
			JSONObject externalKeyboardInfo = new JSONObject(EntityUtils.toString(response.getEntity()));
			if(externalKeyboardInfo.getBoolean("useExternalKeyboard") == useExternalKeyboard){
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
	 * Name: verifyIQMProcessCTime
	 * Description: Verify IQM Process Time in GWP
	 * @param analyzerName
	 * @param time
	 * @return true or false
	 */
	public boolean verifyIQMProcessCTime(String analyzerName, String time) {
		boolean result = false;
		String getAnalyzerConfigUrl = GWP_IP + "api/analyzers/";
		
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
			LocalTime parsedTime = LocalTime.parse(time, formatter);
			// Call login method
			CloseableHttpClient client = login();
			
			String analyzerId = getAnalyzerId(client, analyzerName);
			System.out.println("Analyzer ID:" + analyzerId);
			getAnalyzerConfigUrl = getAnalyzerConfigUrl + analyzerId + "/config/ccal_time";
			
			// Send GET request to get analyzer status
			HttpGet request = new HttpGet(getAnalyzerConfigUrl);
			HttpResponse response = client.execute(request);
	
			System.out.println("\nSending 'GET' request to URL : " + getAnalyzerConfigUrl);
			System.out.println("Response Code : " +
					response.getStatusLine().getStatusCode());
			
			JSONObject iQMProcessCTimeInfo = new JSONObject(EntityUtils.toString(response.getEntity()));
			DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HH:mm:ss+ss:ss");
			System.out.println(parsedTime.format(formatter1));
			System.out.println(iQMProcessCTimeInfo.getString("cCalTime"));
			if(iQMProcessCTimeInfo.getString("cCalTime").equals(parsedTime.format(formatter1))){
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
	 * Name: verifyDefaultClinician
	 * Description: Verify if Default Clinician is enabled in GWP
	 * @param analyzerName
	 * @param defaultClinician
	 * @return true or false
	 */
	public boolean verifyDefaultClinician(String analyzerName, boolean defaultClinician) {
		boolean result = false;
		String getAnalyzerConfigUrl = GWP_IP + "api/analyzers/";
		
		try {
			// Call login method
			CloseableHttpClient client = login();
			
			String analyzerId = getAnalyzerId(client, analyzerName);
			System.out.println("Analyzer ID:" + analyzerId);
			getAnalyzerConfigUrl = getAnalyzerConfigUrl + analyzerId + "/config/default_values";
			
			// Send GET request to get analyzer status
			HttpGet request = new HttpGet(getAnalyzerConfigUrl);
			HttpResponse response = client.execute(request);
	
			System.out.println("\nSending 'GET' request to URL : " + getAnalyzerConfigUrl);
			System.out.println("Response Code : " +
					response.getStatusLine().getStatusCode());
			
			JSONObject defaultValuesInfo = new JSONObject(EntityUtils.toString(response.getEntity()));
			if(defaultValuesInfo.getBoolean("defaultOrderingClinician") == defaultClinician){
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
	 * Name: verifyDefaultPatientID
	 * Description: Verify if Default Patient is enabled in GWP
	 * @param analyzerName
	 * @param defaultPatientID
	 * @return true or false
	 */
	public boolean verifyDefaultPatientID(String analyzerName, boolean defaultPatientID) {
		boolean result = false;
		String getAnalyzerConfigUrl = GWP_IP + "api/analyzers/";
		
		try {
			// Call login method
			CloseableHttpClient client = login();
			
			String analyzerId = getAnalyzerId(client, analyzerName);
			System.out.println("Analyzer ID:" + analyzerId);
			getAnalyzerConfigUrl = getAnalyzerConfigUrl + analyzerId + "/config/default_values";
			
			// Send GET request to get analyzer status
			HttpGet request = new HttpGet(getAnalyzerConfigUrl);
			HttpResponse response = client.execute(request);
	
			System.out.println("\nSending 'GET' request to URL : " + getAnalyzerConfigUrl);
			System.out.println("Response Code : " +
					response.getStatusLine().getStatusCode());
			
			JSONObject defaultValuesInfo = new JSONObject(EntityUtils.toString(response.getEntity()));
			if(defaultValuesInfo.getBoolean("defaultPatient") == defaultPatientID){
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
	 * Name: verifyDefaultOperatorID
	 * Description: Verify if Default Operator is enabled in GWP
	 * @param analyzerName
	 * @param defaultOperatorID
	 * @return true or false
	 */
	public boolean verifyDefaultOperatorID(String analyzerName, boolean defaultOperatorID) {
		boolean result = false;
		String getAnalyzerConfigUrl = GWP_IP + "api/analyzers/";
		
		try {
			// Call login method
			CloseableHttpClient client = login();
			
			String analyzerId = getAnalyzerId(client, analyzerName);
			System.out.println("Analyzer ID:" + analyzerId);
			getAnalyzerConfigUrl = getAnalyzerConfigUrl + analyzerId + "/config/default_values";
			
			// Send GET request to get analyzer status
			HttpGet request = new HttpGet(getAnalyzerConfigUrl);
			HttpResponse response = client.execute(request);
	
			System.out.println("\nSending 'GET' request to URL : " + getAnalyzerConfigUrl);
			System.out.println("Response Code : " +
					response.getStatusLine().getStatusCode());
			
			JSONObject defaultValuesInfo = new JSONObject(EntityUtils.toString(response.getEntity()));
			if(defaultValuesInfo.getBoolean("defaultOperator") == defaultOperatorID){
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
	 * Name: verifyCVPMaterialSetup
	 * Description: Verify CVP Material Setup in GWP using LOT number, desc and model
	 * @param lOTNumber
	 * @param lotDesc
	 * @param model
	 * @return true or false
	 */
	public boolean verifyCVPMaterialSetup(String lOTNumber,  String lotDesc, String model) {
		boolean result = false;
		String getAnalyzerConfigUrl = GWP_IP + "api/qc_lot/CVP";
		
		try {
			// Call login method
			CloseableHttpClient client = login();
			
			// Send GET request to get analyzer status
			HttpGet request = new HttpGet(getAnalyzerConfigUrl);
			HttpResponse response = client.execute(request);
	
			System.out.println("\nSending 'GET' request to URL : " + getAnalyzerConfigUrl);
			System.out.println("Response Code : " +
					response.getStatusLine().getStatusCode());
			
			JSONArray cVPInfoArray = new JSONArray(EntityUtils.toString(response.getEntity()));
			for (int i = 0; i < cVPInfoArray.length(); i++) {
				JSONObject cVPObj = cVPInfoArray.getJSONObject(i);
				if(cVPObj.getString("displayNumber").equalsIgnoreCase(lOTNumber) && cVPObj.getString("displayDescription").equalsIgnoreCase(lotDesc)
						&& cVPObj.getString("analyzerModel").equalsIgnoreCase(model)){
					result = true;
				}
			}
			
			logout(client);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Name: verifyOtherMaterialSetup
	 * Description: Verify Other Material Setup in GWP using LOT number, desc and model
	 * @param lOTNumber
	 * @param lotDesc
	 * @param model
	 * @return true or false
	 */
	public boolean verifyOtherMaterialSetup(String lOTNumber,  String lotDesc, String model) {
		boolean result = false;
		String getAnalyzerConfigUrl = GWP_IP + "api/qc_lot/GEM_EVALUATOR";
		
		try {
			// Call login method
			CloseableHttpClient client = login();
			
			// Send GET request to get analyzer status
			HttpGet request = new HttpGet(getAnalyzerConfigUrl);
			HttpResponse response = client.execute(request);
	
			System.out.println("\nSending 'GET' request to URL : " + getAnalyzerConfigUrl);
			System.out.println("Response Code : " +
					response.getStatusLine().getStatusCode());
			
			JSONArray cVPInfoArray = new JSONArray(EntityUtils.toString(response.getEntity()));
			for (int i = 0; i < cVPInfoArray.length(); i++) {
				JSONObject cVPObj = cVPInfoArray.getJSONObject(i);
				if(cVPObj.getString("displayNumber").equalsIgnoreCase(lOTNumber) && cVPObj.getString("displayDescription").equalsIgnoreCase(lotDesc)
						&& cVPObj.getString("analyzerModel").equalsIgnoreCase(model)){
					result = true;
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
