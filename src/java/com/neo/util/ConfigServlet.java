/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neo.util;

import com.neo.vasgate.VasgateUtil;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


/**
 *
 * @author THANG
 */
public class ConfigServlet implements ServletContextListener {

    public Properties readProperties(String path) throws IOException {
        Properties prop = new Properties();
        InputStream input = null;
        String file_ = ConfigServlet.class.getClassLoader().getResource("config.properties").getPath();
        try {
            input = new FileInputStream(file_);
            // load a properties file
            prop.load(input);
            System.out.println(new Date() + " Load properties file");
            return prop;
        } catch (IOException ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void contextInitialized(ServletContextEvent sce) {
        // Do your thing during webapp's startup.
        System.out.println("Tomcat start ------------------ Begin Load file config.properties ------------------------");
        Properties myprop;
        try {
            myprop = readProperties("config.properties");
            
           // ReceiveMOGabit.USER_NAME = myprop.getProperty("gapit.username");
            VasgateUtil.username = myprop.getProperty("vasgate.username");
            System.out.println("Vasgate username = "+ VasgateUtil.username);
            VasgateUtil.password = myprop.getProperty("vasgate.password");
            System.out.println("Vasgate password = "+ VasgateUtil.password);
            
           
            System.out.println("----------------------------END LOAD FILE CONFIG.PROPERTIES------------------------------");
        } catch (Exception e1) {
            e1.printStackTrace();
            System.out.println(" ERROR -----------------Khong co file config.properties OR tham so khong dung-------------------------------");
            return;
        }

    }

    public void contextDestroyed(ServletContextEvent sce) {
        // Do your thing during webapp's shutdown.

    }

}
