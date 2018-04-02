/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neo.vasgate;

import java.util.Date;

/**
 *
 * @author THANG
 */
public class VasgateUtil {
    
    public static String url = "http://10.54.9.60:8088/vsgapi/services/vsgapi?wsdl";
    public static String username = "testconnect";
    public static String password = "testconnect";
    
    public static String callVasgate (String cmd){
       String systemcode = "?";
       if (cmd.startsWith("KT.DV_DETAIL_NEW")) systemcode = "994";
       String body = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://ws.wsapi.neo\">"
                + "<soapenv:Header/>"
                + "<soapenv:Body>"
                	+ "<ser:doAction>"
                		+ "<ser:systemcode>" + systemcode + "</ser:systemcode>"
                		+ "<ser:channel>" + "vasgate" + "</ser:channel>"
                		+ "<ser:usercommand>" + cmd + "</ser:usercommand>"
                		+ "<ser:username>" + username + "</ser:username>"
                		+ "<ser:password>" + password + "</ser:password>"
                	+ "</ser:doAction>"
                + "</soapenv:Body>"
                + "</soapenv:Envelope>";
        String soapAction = "http://ws.wsapi.neo/doAction";
        String xmlresult = SoapCall.soapCall(url, body, soapAction);
        String result = xmlresult;
        if (xmlresult != null && xmlresult.length() > 0 && !xmlresult.startsWith("Exception")) {
            result = SoapCall.getResult(xmlresult);
        }
        System.out.println(new Date() + " VASGATE CMD: " + cmd + " result: " + result);
        return result;
    }
    
    
    
}
