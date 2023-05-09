package com.example.codegamebigfixapp;

import java.io.*;
import java.net.*;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import java.util.*;

public class ActionStopCall{
    String hostnameOrIp;
    String userName;
    String password;
    String id;
    String response="";
    public ActionStopCall(String hostnameOrIp,String userName,String password,String actionID){
        this.hostnameOrIp=hostnameOrIp;
        this.userName=userName;
        this.password=password;
        this.id=actionID;
    }
    public String getData(){
        try{
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };
            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            String connectionURL="https://"+this.hostnameOrIp+":52311/api/action/"+this.id+"/stop";
            String credential=this.userName+":"+this.password;

            //Connection Timeout Setting
            int timeoutConnection = 1000;
            URL url= new URL(connectionURL);
            HttpURLConnection bigfx= (HttpURLConnection) url.openConnection();
            bigfx.setRequestMethod("POST");

            //Set Timeout
            bigfx.setConnectTimeout(timeoutConnection);
            String encoding = Base64.getEncoder().encodeToString(credential.getBytes("UTF-8"));
            bigfx.setRequestProperty  ("Authorization", "Basic " + encoding);
            int responseCode=bigfx.getResponseCode();

            //Checking response code
            if (responseCode==200){
                BufferedReader br = new BufferedReader(new InputStreamReader(bigfx.getInputStream(),"UTF-8"));
                String line="";
                while((line=br.readLine())!=null){
                    System.out.println(line);
                    response=line;
                }
                br.close();
                bigfx.disconnect();
            }else{
                throw new Exception("Username/Password Incorrect. Please try again");
            }
        }catch (SocketTimeoutException ex){
            //allData.add(ex.toString());
            response=ex.toString();
        }
        catch (Exception ex){
            //allData.add(ex.toString());
            response=ex.toString();
        }finally{
            return (response);
        }
    }
}