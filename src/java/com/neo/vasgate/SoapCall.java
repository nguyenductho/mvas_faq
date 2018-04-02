/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neo.vasgate;

/**
 *
 * @author THANG
 */
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SoapCall {

    public static int soapTimeout = 30000;

    public static String getResult(String xml) {
        try {
            Document dom = stringToDom(xml);
            Element root = dom.getDocumentElement();
            NodeList list = root.getChildNodes();
            return list.item(0).getTextContent();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static Document stringToDom(String xml) {
        try {
            ByteArrayInputStream input = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(input);
            return doc;
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String soapCall(String url, String body, String soapAction) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), soapTimeout);
        HttpConnectionParams.setSoTimeout(httpclient.getParams(), soapTimeout);
        String out = "";
        try {
            HttpPost post = new HttpPost(url);
//        	NameValuePair[] data = {
//        	          new NameValuePair("user", "joe"),
//        	          new NameValuePair("password", "bloggs")
//        	        };
//        	post.setRequestBody(data);
            post.setEntity(new StringEntity(body));
            post.setHeader("Content-Type", "text/xml; charset=UTF-8");
            post.setHeader("SOAPAction", soapAction);
            out = IOUtils.toString(httpclient.execute(post).getEntity().getContent(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception: " + e.getMessage();
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return out;
    }

}

