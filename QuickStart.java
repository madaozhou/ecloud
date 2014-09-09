/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
//package org.apache.http.examples.client;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.json.*;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.ByteArrayEntity;

public class QuickStart {
    public static final String URL =
        "http://115.29.204.44:8080/ecloud/v1/dev/ctl/getver/";

    public static void main(String[] args) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(URL);
            //List <NameValuePair> nvps = new ArrayList <NameValuePair>();
            //nvps.add(new BasicNameValuePair("auth_token", "test"));
            //nvps.add(new BasicNameValuePair("device", "/dev/ctl/demo01/gw01"));
            //httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            JsonObject jobj = Json.createObjectBuilder()
                .add("auth_token", "test")
                .add("device", "/dev/ctl/demo01/gw01")
                .build();
            httpPost.setEntity(new ByteArrayEntity(
                        jobj.toString().getBytes("UTF8")));
            CloseableHttpResponse response1 = httpclient.execute(httpPost);
            try {
                System.out.println(response1.getStatusLine());
                HttpEntity entity1 = response1.getEntity();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        entity1.getContent()));
                System.out.println("=============================");
                System.out.println("Contents of post entity");
                System.out.println("=============================");
                String lines;
                while ((lines = reader.readLine()) != null) {
                    System.out.println(lines);
                }
                reader.close();
                System.out.println("=============================");
                System.out.println("Contents of post entity ends");
                System.out.println("=============================");

                // do something useful with the response body
                // and ensure it is fully consumed
                EntityUtils.consume(entity1);
            } finally {
                response1.close();
            }

/*
 *            HttpPost httpPost = new HttpPost("http://targethost/login");
 *            List <NameValuePair> nvps = new ArrayList <NameValuePair>();
 *            nvps.add(new BasicNameValuePair("username", "vip"));
 *            nvps.add(new BasicNameValuePair("password", "secret"));
 *            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
 *            CloseableHttpResponse response2 = httpclient.execute(httpPost);
 *
 *            try {
 *                System.out.println(response2.getStatusLine());
 *                HttpEntity entity2 = response2.getEntity();
 *                // do something useful with the response body
 *                // and ensure it is fully consumed
 *                EntityUtils.consume(entity2);
 *            } finally {
 *                response2.close();
 *            }
 */
        } finally {
            httpclient.close();
        }
    }

}
