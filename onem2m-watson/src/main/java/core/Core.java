/*******************************************************************************
 * Copyright (c) 2017 Sensinov (www.sensinov.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package core;

import http.HttpResponse;
import http.RestHttpClient;
import http.RestHttpServer;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;
import mapper.Mapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.gson.JsonObject;
import com.ibm.iotf.client.api.APIClient;
import com.ibm.iotf.client.api.APIClient.ContentType;

import config.Parameters;

public class Core {
	private final static String PROPERTIES_FILE_NAME = "config.ini";
	public static APIClient watsonClient = null;
	
	public static void main(String[] args) {
		
		Properties props = new Properties();
		try {
			props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME));
		} catch (IOException e1) {
			System.err.println("Not able to read the properties file, exiting..");
			System.exit(1);
		}
		new Parameters(props);
		try {
			watsonClient = new APIClient(props);
		} catch (Exception e) {
			e.printStackTrace();
		}

			try {
				RestHttpServer.start();
			} catch (Exception e) {
				e.printStackTrace();
			}

			HttpResponse httpResponse = new HttpResponse();
			
			String aeRep = Mapper.getAeRep(Parameters.ipeAeName, Parameters.ipeSubNu);
			httpResponse = RestHttpClient.post(Parameters.ipeAeId,null,Parameters.cseURI, aeRep, 2);
	    	
			String subRep = Mapper.getSubRep(Parameters.ipeSubName,"/"+Parameters.cseId+"/"+Parameters.cseName+"/"+Parameters.ipeAeName);
			httpResponse= RestHttpClient.post(Parameters.ipeAeId, null, Parameters.cseURI, subRep, 23);
			discoverAndMap(Parameters.cseURI);
	}
	
	public static void discoverAndMap(String uri){
	
			HttpResponse httpResponse = new HttpResponse();
			httpResponse = RestHttpClient.get(Parameters.ipeAeId, null, uri+"?fu=1&ty=3&lbl="+Parameters.label);			
			if(httpResponse.getStatusCode()==200){
				JSONObject urils= new JSONObject(httpResponse.getBody());
	
				JSONArray urilsArray = urils.getJSONArray("m2m:uril");
		 
				for(int j=0;j<urilsArray.length();j++){
					String subRep2 = Mapper.getSubRep(Parameters.ipeSubName,"/"+Parameters.cseId+"/"+Parameters.cseName+"/"+Parameters.ipeAeName);
					httpResponse= RestHttpClient.post(Parameters.ipeAeId, null,Parameters.csePoA+"/~"+urilsArray.getString(j), subRep2, 23);
					
					httpResponse = RestHttpClient.get(Parameters.ipeAeId, null,Parameters.csePoA+"/~"+urilsArray.getString(j)+"/la");
					System.out.println(httpResponse.getBody());
		
					JSONObject cin = new JSONObject(httpResponse.getBody());
					String content = cin.getJSONObject("m2m:cin").getString("con");
					MustacheFactory mf = new DefaultMustacheFactory();
					for(int i=0;i<Parameters.templates.size();i++){
						System.out.println("Trying to match with template: "+Parameters.templates.get(i));						
						Mustache mustache = mf.compile(Parameters.templates.get(i));
						
						try {
							StringWriter writer = new StringWriter();
							mustache.execute(writer, Mapper.toMap(new JSONObject(content))).flush();
							JSONObject obj =new JSONObject(writer.toString());
							
							if(!obj.get("deviceType").equals("") &&  !obj.get("deviceType").equals("") && !obj.get("deviceType").equals("")){
								System.out.println(Parameters.templates.get(i)+" Matched");						

								System.out.println("DevcieType: "+obj.get("deviceType"));
								System.out.println("DevcieId: "+obj.get("deviceId"));
								System.out.println("Event: "+obj.get("event"));

								JSONObject deviceType = new JSONObject();
								deviceType.put("id", obj.get("deviceType"));
								RestHttpClient.post(Parameters.watsonApiKey, Parameters.watsonToken, Parameters.watsonUri+"device/types", deviceType.toString(),0);
							
								JSONObject deviceId = new JSONObject();
								deviceId.put("deviceId", obj.get("deviceId"));
								RestHttpClient.post(Parameters.watsonApiKey, Parameters.watsonToken, Parameters.watsonUri+"device/types/"+obj.get("deviceType")+"/devices", deviceId.toString(),0);
								
								JsonObject event = new JsonObject();
								event.addProperty("id",  obj.get("deviceType").toString());
								event.addProperty("value",  obj.get("event").toString());
		
								boolean code = false;
								try {
									code = watsonClient.publishApplicationEventforDeviceOverHTTP(obj.get("deviceId").toString(), obj.get("deviceType").toString(), "event", event, ContentType.json);
								} catch (Exception e) {
									e.printStackTrace();
								}
								if(code == true) {
									System.out.println("Event published successfully!");
								} else {
									System.out.println("Failed to publish the event!");
								}
								break;
							}else{
								System.out.println(Parameters.templates.get(i) +" Not matching");						

							}
						} catch (JSONException e) {
							System.out.println(Parameters.templates.get(i) +" Not matching");	
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			System.out.println("***************************************************************");
	}
}

