package gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class SKPredicter {
	public SKPredicter() {
		
	}
	
	public LinkedList<Prediction> predict(String context, String curWord) throws IOException {
		System.out.println(context);
		System.out.println(curWord);
		
		System.out.println("http://172.16.17.207:9005/predict?json=" + "{\"context\":\"" + context + "\",\"lm-code\" : [\"en_FRY\", \"en_GB\"], \"currentword\":\"" + curWord + "\"}");
		
		URL api = new URL("http://172.16.17.207:9005/predict?json=" + URLEncoder.encode("{\"context\":\"" + context + "\",\"lm-code\" : [\"en_FRY\", \"en_GB\"], \"currentword\":\"" + curWord + "\"}"));
        URLConnection yc = api.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        
        String inputLine = in.readLine();
    	Object obj = JSONValue.parse(inputLine);
    	JSONObject array=(JSONObject) obj;
    	
    	JSONArray predictions = (JSONArray) array.get("predictions");
    	
    	LinkedList<Prediction> predArray = new LinkedList<Prediction>();
    	
    	for(int i = 0; i < predictions.size(); i++) {
    		JSONObject prediction = (JSONObject) predictions.get(i);
    		predArray.add(new Prediction((String) prediction.get("term"), (Double) prediction.get("prob")));
//    		System.out.println("Prediction: " + prediction.get("term") + " " + prediction.get("prob"));
    	}
        
        in.close();
        
        Collections.sort(predArray);
        
//        System.out.println((predArray));
        
        return predArray;
	}
	
	public static void main(String[] args) {
		SKPredicter p = new SKPredicter();
		try {
			LinkedList<Prediction> predictions = p.predict("", "");
			System.out.println("I hate that " + predictions.get(0).getTerm());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
