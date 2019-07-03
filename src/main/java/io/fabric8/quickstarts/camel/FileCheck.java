package io.fabric8.quickstarts.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.apache.camel.Exchange;
import java.util.*; 

@Component
public class FileCheck {

  HashSet<String> h = new HashSet<String>(); 

  public boolean check(Exchange exchange) {
    System.out.println("checking: ");
    System.out.println("We just downloaded: " + exchange.getIn().getHeader("CamelFileName"));

    if (h.contains(exchange.getIn().getHeader("CamelFileName"))) {
    	System.out.println("Already processed file:: " + exchange.getIn().getHeader("CamelFileName")); 
    	return false;
    }

 // Iterator<String> i = h.iterator(); 
 //    while (i.hasNext()) {
 //    	System.out.println("Already processed files:: " + i.next()); 

 //        if (i.next() == exchange.getIn().getHeader("CamelFileName")) {
 //        	return false;
 //        }
 //    }
    
    System.out.println("Adding file:: " + exchange.getIn().getHeader("CamelFileName")); 
    h.add((String) exchange.getIn().getHeader("CamelFileName"));
    return true;
  }
} 
