package com.redhat.lightblue.client;

public class AbstractQueryResult implements QueryResult {

    public LightBlueEntity getNext(){
        
        return null;
    }
    
    public Integer count(){
        
        return 0;
    }
    
    public Boolean isPaged(){
        
        return false;
    }
    
    public Boolean hasNextPage(){
        
        return false;
    }
    
    public Boolean hasNext(){
        
        return false;
    }
    
    public LightBlueEntity getByIndex( final Integer index ){
        
        return null;
    }
    
}
