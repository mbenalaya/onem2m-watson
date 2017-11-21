/*******************************************************************************
 * Copyright (c) 2017 Sensinov (www.sensinov.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package http;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class RestHttpClient {
	
	public static HttpResponse get(String username, String password, String uri) {
		System.out.println("HTTP GET "+uri);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet= new HttpGet(uri);
		httpGet.addHeader("Authorization",basicEncode(username,password));
		httpGet.addHeader("X-M2M-Origin",username);
		httpGet.addHeader("Accept","application/json");
	
		HttpResponse httpResponse = new HttpResponse();
		
		try {
			CloseableHttpResponse closeableHttpResponse = httpclient.execute(httpGet);
			try{
			httpResponse.setStatusCode(closeableHttpResponse.getStatusLine().getStatusCode());
			httpResponse.setBody(EntityUtils.toString(closeableHttpResponse.getEntity()));
			
			System.out.println(httpResponse.toString()+"\n");
			}finally{
				closeableHttpResponse.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return httpResponse;	
	}
	
	public static HttpResponse put(String username, String password, String uri, String body) {
		System.out.println("HTTP PUT "+uri);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPut httpPut= new HttpPut(uri);
		
		httpPut.addHeader("Authorization",basicEncode(username,password));
		httpPut.addHeader("X-M2M-Origin",username);
		httpPut.addHeader("Content-Type","application/json");
		httpPut.addHeader("Accept","application/json");

		HttpResponse httpResponse = new HttpResponse();
		try {
			CloseableHttpResponse closeableHttpResponse =null;
			try {
				httpPut.setEntity(new StringEntity(body));
				closeableHttpResponse= httpclient.execute(httpPut);
				httpResponse.setStatusCode(closeableHttpResponse.getStatusLine().getStatusCode());
				httpResponse.setBody(EntityUtils.toString(closeableHttpResponse.getEntity()));
				
				System.out.println(httpResponse.toString()+"\n");
			}finally{
				httpPut.releaseConnection();
				closeableHttpResponse.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return httpResponse ;	
	}
	
	public static HttpResponse post(String username, String password, String uri, String body, int ty) {
		System.out.println("HTTP POST "+uri);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(uri);
		
		httpPost.addHeader("Authorization",basicEncode(username,password));
		httpPost.addHeader("X-M2M-Origin",username);
		httpPost.addHeader("Accept","application/json");
		
		String ext = "";
		if(ty!=0){
			ext=";ty="+ty;
		}
		httpPost.addHeader("Content-Type","application/json"+ext);
				
		HttpResponse httpResponse = new HttpResponse();
		try {
			CloseableHttpResponse closeableHttpResponse=null;
			try{
				httpPost.setEntity(new StringEntity(body));
				closeableHttpResponse = httpclient.execute(httpPost);
	
				httpResponse.setStatusCode(closeableHttpResponse.getStatusLine().getStatusCode());
				httpResponse.setBody(EntityUtils.toString(closeableHttpResponse.getEntity()));
				
				System.out.println(httpResponse.toString()+"\n");
			}finally{
				closeableHttpResponse.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return httpResponse ;	
	}
	
	public static HttpResponse delete(String username, String password, String uri) {
		System.out.println("HTTP DELETE "+uri);

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpDelete httpDelete = new HttpDelete(uri);
		
		httpDelete.addHeader("Authorization",basicEncode(username,password));
		httpDelete.addHeader("X-M2M-Origin",username);
		httpDelete.addHeader("Accept","application/json");

		HttpResponse httpResponse = new HttpResponse();
		try {
			CloseableHttpResponse closeableHttpResponse = httpclient.execute(httpDelete);
			try {
				httpResponse.setStatusCode(closeableHttpResponse.getStatusLine().getStatusCode());
				httpResponse.setBody(EntityUtils.toString(closeableHttpResponse.getEntity()));
				
				System.out.println(httpResponse.toString()+"\n");
			}finally{
				httpDelete.releaseConnection();
				closeableHttpResponse.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return httpResponse ;	
	}	
	
	
	static String basicEncode(String username, String password){
		String basic=null;
		try {
			basic = "Basic "+Base64.getEncoder().encodeToString((username+":"+password).getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return basic;
	}
}
