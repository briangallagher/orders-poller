/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.fabric8.quickstarts.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.apache.camel.language.bean.BeanLanguage;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.component.jackson.JacksonDataFormat;
import io.fabric8.quickstarts.camel.Order;

@Component
public class SampleAutowiredAmqpRoute extends RouteBuilder {

    @Autowired JmsConnectionFactory amqpConnectionFactory;
    @Bean
    public org.apache.camel.component.amqp.AMQPComponent amqpConnection() {
        org.apache.camel.component.amqp.AMQPComponent amqp = new org.apache.camel.component.amqp.AMQPComponent();
        amqp.setConnectionFactory(amqpConnectionFactory);
        return amqp;
    }

    @Override
    public void configure() throws Exception {
        // original
        // from("file:src/main/data?noop=true")
        //     .to("amqp:queue:NEWSCIENCEQUEUE");

        JacksonDataFormat jsonDataFormat = new JacksonDataFormat(Order.class);

        System.out.println("Reading 2....");

        from("ftp://epiz_23875282@ftpupload.net/htdocs/orders?password=dlqaAJwNoz&passiveMode=true")
        .log("after ftp")
        .filter().method("fileCheck", "check")
        .log("Processing ${id} "  + header("CamelFileName"))
        .log("body:: ${body}")
        .unmarshal(jsonDataFormat)
        .marshal().json(JsonLibrary.Jackson)
        // .setHeader("Content-Type", "application/json")
        .setHeader("Content-Type").simple("application/json;charset=UTF-8")
        .to("amqp:queue:orders")
        .log("Route complete");
    }

    String[] files = {};
    public boolean processed(org.apache.camel.builder.ValueBuilder fname) {
        String filename = fname.toString();
        System.out.println("filename: " + filename);
        files[files.length] = filename;
        for (int i = 0; i < files.length; i++) {
             System.out.println(files[i] + " ");
        }
        return true;
    }

}

