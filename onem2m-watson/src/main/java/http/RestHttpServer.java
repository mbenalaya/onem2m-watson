/*******************************************************************************
 * Copyright (c) 2017 Sensinov (www.sensinov.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import mapper.Mapper;

import org.json.JSONException;
import org.json.JSONObject;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.gson.JsonObject;
import com.ibm.iotf.client.api.APIClient.ContentType;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import config.Parameters;
import core.Core;

public class RestHttpServer {
	
    public static void start() throws Exception {		
    	System.out.println("Start server..");
    	HttpServer server = HttpServer.create(new InetSocketAddress(Parameters.ipePort), 0);
    	server.createContext(Parameters.ipeContext, new MyHandler());
    	server.setExecutor(Executors.newCachedThreadPool()); 
    	server.start(); 
    	System.out.println("Server started.");
    }
	
    static class MyHandler implements HttpHandler {
    
        public void handle(HttpExchange httpExchange) throws IOException {
        	httpExchange.getResponseHeaders().set("Connection", "close");

        	System.out.println("Event Recieved!");
        	InputStream in = httpExchange.getRequestBody();
        	String body = convertStreamToString(in);

            System.out.println(body);
            
	            JSONObject sgn = new JSONObject(body);
	            if(sgn.getJSONObject("m2m:sgn").has("m2m:vrq") || sgn.getJSONObject("m2m:sgn").has("vrq") ){
			        System.out.println("Confirm subscription");
	            }else {
					String sur = sgn.getJSONObject("m2m:sgn").getString("sur");
			        JSONObject rep = sgn.getJSONObject("m2m:sgn").getJSONObject("nev").getJSONObject("rep");
	            	System.out.println("ty: "+rep.getInt("ty"));
			        if(rep.getInt("ty")==4){
			        	String content = rep.getString("con");
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

							
								
									JsonObject event = new JsonObject();
									event.addProperty("id",  obj.get("deviceType").toString());
									event.addProperty("value",  obj.get("event").toString());
									
									boolean code = false;
									try {
										code = Core.watsonClient.publishApplicationEventforDeviceOverHTTP(obj.get("deviceId").toString(), obj.get("deviceType").toString(), "blink", event, ContentType.json);
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
								System.out.println(Parameters.templates.get(i)+" Not matching");						
	
							}
							} catch (JSONException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
			        }else if(rep.getInt("ty")==2){
						System.out.println("Wait 10s before AE containers discovery");

			        	try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						String aeName = rep.getString("rn");
			        	Core.discoverAndMap(Parameters.cseURI+"/"+aeName);

			        }
	            }
	     
	            final byte[] out = "".getBytes("UTF-8");
	            
                httpExchange.sendResponseHeaders(200, out.length);
 
                OutputStream os = httpExchange.getResponseBody();
                os.write(out);
                os.close();
		}
    }
    
	public static String convertStreamToString(InputStream is) {
			String body = "";
	        int i;
	        char c;
	        try {                 
	            while ((i = is.read()) != -1) {
	                c = (char) i;
	                body = (String) (body + c);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return body; 
	}
}
