package com.redhat.lightblue.client.util;

import com.redhat.lightblue.client.AbstractLightblueClient;
import com.redhat.lightblue.client.Client;

/**
 * Created by mcirioli on 7/11/14.
 */
public class Demo {

    public final static void main(String[] args) throws Exception {
        Client client = new Client();
        System.out.println("Response: " +  client.getMetadata());
    }

}