package com.redhat.lightblue.client.enums;

public enum RequestType {
	
	METADATA(ServiceType.METADATA.getValue()),  
	DATA_INSERT(ServiceType.DATA.getValue()),
	DATA_SAVE(ServiceType.DATA.getValue() + "save/"), 
	DATA_UPDATE(ServiceType.DATA.getValue() + "update/"), 
	DATA_DELETE(ServiceType.DATA.getValue() + "delete/"), 
	DATA_FIND(ServiceType.DATA.getValue() + "find/");

  private String value;
  private RequestType(String value) {
     this.value = value;
  }
  public String getValue() {
     return value;
  }
	
}
