/*******************************************************************************
 * Copyright (c) 2017 Sensinov (www.sensinov.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Mapper {
	public static String getAeRep(String name, String uri){
		JSONArray poa = new JSONArray();
		poa.put(uri);
		
		JSONObject obj = new JSONObject();
		obj.put("rn", name);
		obj.put("api", "company.com.watson-ipe");
		obj.put("rr", "true");
		obj.put("poa", poa);
		
		JSONObject ae = new JSONObject();
		ae.put("m2m:ae", obj);

		return ae.toString();
	}
	
	public static String getSubRep(String name, String uri){
		JSONArray nu = new JSONArray();
		nu.put(uri);
		
		JSONObject obj = new JSONObject();
		obj.put("rn", name);
		obj.put("nu", nu);
		obj.put("nct", 2);
		
		JSONObject sub = new JSONObject();
		sub.put("m2m:sub", obj);

		return sub.toString();
	}
	
	 public static Map<String, Object> toMap(JSONObject object) throws JSONException{
	      Map<String, Object> map = new HashMap<String, Object>();
	      Iterator<?> keys = object.keys();
	      while (keys.hasNext()){
	          String key = (String) keys.next();
	          map.put(key, fromJson(object.get(key)));
	      }
	      return map;
	  }

	  public static List<Object> toList(JSONArray array) throws JSONException{
	      List<Object> list = new ArrayList<Object>();
	      for (int i = 0; i < array.length(); i++){
	          list.add(fromJson(array.get(i)));
	      }
	      return list;
	  }

	  private static Object fromJson(Object json) throws JSONException{
	      if (json instanceof JSONObject){
	          return toMap((JSONObject) json);
	      } else if (json instanceof JSONArray){
	          return toList((JSONArray) json);
	      } else{
	          return json;
	      }
	  }
}
