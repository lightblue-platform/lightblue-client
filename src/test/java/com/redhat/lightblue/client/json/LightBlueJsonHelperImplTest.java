package com.redhat.lightblue.client.json;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import com.redhat.lightblue.client.json.LightBlueJsonHelper;
import com.redhat.lightblue.client.json.LightBlueJsonHelperImpl;

public class LightBlueJsonHelperImplTest {
	
	private static final String TEST_TERMS_METADATA_JSON = "{\"entityInfo\":{\"name\":\"terms\",\"enums\":[{\"name\":\"termsType\",\"values\":[\"indemnification\",\"site\",\"T7\",\"purchase\",\"application\",\"subscription\"]},{\"name\":\"termsCategory\",\"values\":[\"customer\",\"indemnification\",\"user\"]},{\"name\":\"status\",\"values\":[\"active\",\"inactive\"]},{\"name\":\"operator\",\"values\":[\"start_with\",\"contains\",\"not_contains\",\"not_equals\",\"equals\",\"end_with\"]}],\"datastore\":{\"datasource\":\"mongodata\",\"collection\":\"terms\",\"backend\":\"mongo\"}},\"schema\":{\"name\":\"terms\",\"version\":{\"value\":\"0.1.1-SNAPSHOT\",\"changelog\":\"Initial version\"},\"status\":{\"value\":\"active\"},\"access\":{\"insert\":[\"admin\"],\"update\":[\"admin\"],\"find\":[\"anyone\"],\"delete\":[\"admin\"]},\"fields\":{\"startDate\":{\"type\":\"date\"},\"termsCategory\":{\"type\":\"string\",\"constraints\":{\"enum\":\"termsCategory\"}},\"status\":{\"type\":\"string\",\"constraints\":{\"enum\":\"status\",\"required\":true}},\"hostname\":{\"type\":\"string\"},\"endDate\":{\"type\":\"date\"},\"termsVerbiage\":{\"type\":\"array\",\"items\":{\"type\":\"object\",\"fields\":{\"startDate\":{\"type\":\"date\"},\"termsVerbiageTranslation\":{\"type\":\"array\",\"items\":{\"type\":\"object\",\"fields\":{\"startDate\":{\"type\":\"date\"},\"body\":{\"type\":\"string\"},\"_id\":{\"type\":\"integer\",\"constraints\":{\"required\":true}},\"status\":{\"type\":\"string\",\"constraints\":{\"enum\":\"status\",\"required\":true}},\"localeCode\":{\"type\":\"string\",\"constraints\":{\"required\":true}},\"endDate\":{\"type\":\"date\"},\"language\":{\"type\":\"string\"},\"published\":{\"type\":\"boolean\"},\"URL\":{\"type\":\"string\"},\"version\":{\"type\":\"string\"}}}},\"status\":{\"type\":\"string\",\"constraints\":{\"enum\":\"status\",\"required\":true}},\"description\":{\"type\":\"string\"},\"name\":{\"type\":\"string\"},\"endDate\":{\"type\":\"date\"},\"version\":{\"type\":\"string\"},\"termsVerbiageTranslation#\":{\"type\":\"integer\",\"access\":{\"find\":[\"anyone\"]}}}}},\"siteCode\":{\"type\":\"string\"},\"productAttribute\":{\"type\":\"array\",\"items\":{\"type\":\"object\",\"fields\":{\"op\":{\"type\":\"string\",\"constraints\":{\"enum\":\"operator\"}},\"value\":{\"type\":\"string\"},\"code\":{\"type\":\"string\"}}}},\"lastUpdateDate\":{\"type\":\"date\",\"constraints\":{\"required\":true}},\"creationDate\":{\"type\":\"date\",\"constraints\":{\"required\":true}},\"createdBy\":{\"type\":\"string\",\"constraints\":{\"required\":true}},\"_id\":{\"type\":\"integer\",\"constraints\":{\"required\":true}},\"lastUpdatedBy\":{\"type\":\"string\",\"constraints\":{\"required\":true}},\"optional\":{\"type\":\"boolean\"},\"termsType\":{\"type\":\"string\",\"constraints\":{\"enum\":\"termsType\",\"required\":true}},\"object_type\":{\"type\":\"string\",\"access\":{\"find\":[\"anyone\"],\"update\":[\"noone\"]},\"constraints\":{\"required\":true,\"minLength\":1}},\"termsVerbiage#\":{\"type\":\"integer\",\"access\":{\"find\":[\"anyone\"]}},\"productAttribute#\":{\"type\":\"integer\",\"access\":{\"find\":[\"anyone\"]}}}}}";
	
	@Test
	public void testParseTermsMetadata() {
		LightBlueJsonHelper helper = new LightBlueJsonHelperImpl();
		Map<String,Object> result = helper.getJsonMap(TEST_TERMS_METADATA_JSON);
		
		assertNotNull(result);
		
		// log for debugging purposes... remove later
		System.out.println(result.toString());
	}

}
