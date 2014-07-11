package com.redhat.lightblue.client.json;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LightBlueJsonHelperImpl implements LightBlueJsonHelper {
	public Map<String,Object> getJsonMap(String json) {
		
		HashMap<String,Object> result = null;
		JsonFactory factory = new JsonFactory(); 
	    ObjectMapper mapper = new ObjectMapper(factory); 
	    TypeReference<HashMap<String,Object>> typeRef 
	            = new TypeReference<HashMap<String,Object>>() {};

	    try {
			result = mapper.readValue(json, typeRef);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    return result;
	    
	}
}
