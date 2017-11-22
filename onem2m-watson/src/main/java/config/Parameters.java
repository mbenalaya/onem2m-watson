/*******************************************************************************
 * Copyright (c) 2017 Sensinov (www.sensinov.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package config;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class Parameters {
	public static String watsonApiKey;
	public static String watsonToken;
	public static String watsonUri;
	
	public static String ipeAeId;
	public static String ipeAeName;
	public static String ipeSubName;
	public static String ipeProtocol;
	public static String ipeIp;
	public static int ipePort;
	public static String ipeContext;
	
	public static String cseId;
	public static String cseName;
	public static String cseProtocol;
	public static String cseIp;
	public static int csePort;
	public static String cseContext;
	
	public static String label;
	public static List<String> templates;
	
	// Calculated
	public static String ipeSubNu;
	public static String csePoA;
	public static String cseURI;

	public Parameters(Properties props){
				
		watsonApiKey = props.getProperty("API-Key");
		watsonToken = props.getProperty("Authentication-Token");
		watsonUri = "https://"+props.getProperty("Organization-ID")+".internetofthings.ibmcloud.com/api/v0002/";
		
		ipeAeId = props.getProperty("IpeAeId");
		ipeAeName = props.getProperty("IpeAeName");
		ipeSubName = props.getProperty("IpeSubName");
		ipeProtocol = props.getProperty("IpeProtocol");
		ipeIp = props.getProperty("IpeIp");
		ipePort = Integer.parseInt(props.getProperty("IpePort"));
		ipeContext = props.getProperty("IpeContext");
		ipeSubNu = ipeProtocol+"://"+ipeIp+":"+ipePort+ipeContext;
		
		cseId = props.getProperty("CseId");
		cseName = props.getProperty("CseName");
		cseProtocol = props.getProperty("CseProtocol");
		cseIp = props.getProperty("CseIp");
		csePort = Integer.parseInt(props.getProperty("CsePort"));
		cseContext = props.getProperty("CseContext");
		
		csePoA=ipeProtocol+"://"+cseIp+":"+csePort+cseContext;
		cseURI=csePoA+"~/"+cseId+"/"+cseName;
		
		label = props.getProperty("label");
		templates = new LinkedList<String>();
		
	    String value;
	    for(int i = 0; (value = props.getProperty("template." + i)) != null; i++) {
	    	templates.add("templates/"+value);
	    }
	}
	
	
}