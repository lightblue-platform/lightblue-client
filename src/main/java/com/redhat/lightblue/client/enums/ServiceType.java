package com.redhat.lightblue.client.enums;

public enum ServiceType {

	METADATA("rest/metadata/"),  
	DATA("rest/data/");
	
  private String value;
  private ServiceType(String value) {
     this.value = value;
  }
  public String getValue() {
     return value;
  }
	
}
