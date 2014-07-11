package com.redhat.lightblue.client;

import java.util.List;
import java.util.ArrayList;

public class By {
    private List<SearchCriteria> criteria;

    public By () {
        criteria = new ArrayList<SearchCriteria>();
    }

    public void addCriteria(SearchCriteria searchCriteria) {
    	criteria.add(searchCriteria);
    }
    
    public List<SearchCriteria> getAllCriteria() {
    	return criteria;
    }
}
