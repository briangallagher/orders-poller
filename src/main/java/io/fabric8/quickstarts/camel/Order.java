package io.fabric8.quickstarts.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.apache.camel.Exchange;
import java.util.*; 

// @Component
public class Order {

    private String orderitem;
    private String quantity;

    // public Order(String orderitem, String quantity) {
    //     this.orderitem = orderitem;
    //     this.quantity = quantity;
    // }
    
    // public Order() {
    // }

    public String getOrderitem() {
        return this.orderitem;
    }

    public void setOrderitem(String item) {
        this.orderitem = item;
    }    

    public String getQuantity() {
        return this.quantity;
    }

    public void setQuantity(String item) {
        this.quantity = item;
    }    

}
