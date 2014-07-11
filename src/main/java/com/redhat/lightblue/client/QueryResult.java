package com.redhat.lightblue.client;

public interface QueryResult {
    
    LightBlueEntity getNext();
    
    Integer count();
    
    Boolean isPaged();
    
    Boolean hasNextPage();
    
    Boolean hasNext();
    
    LightBlueEntity getByIndex( final Integer index );
    
}