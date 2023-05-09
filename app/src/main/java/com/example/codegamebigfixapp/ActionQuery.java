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

public class ActionQuery{
    String hostnameOrIp;
    String userName;
    String password;
    ArrayList<String>allData=new ArrayList<>();
    public ActionQuery(String hostnameOrIp,String userName,String password){
        this.hostnameOrIp=hostnameOrIp;
        this.userName=userName;
        this.password=password;
    }
    public ArrayList<String> getData(){
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

            //relevance and connection
            String relevance="(ids%20of%20it,names%20of%20it,states%20of%20it,statuses%20of%20results%20of%20it)%20of%20bes%20actions";

            String connectionURL="https://"+this.hostnameOrIp+":52311/api/query?relevance="+relevance;
            String credential=this.userName+":"+this.password;

            //Connection Timeout Setting
            int timeoutConnection = 1000;
            URL url= new URL(connectionURL);
            HttpURLConnection bigfx= (HttpURLConnection) url.openConnection();

            //Set Timeout
            bigfx.setConnectTimeout(timeoutConnection);
            String encoding = Base64.getEncoder().encodeToString(credential.getBytes("UTF-8"));
            bigfx.setRequestProperty  ("Authorization", "Basic " + encoding);
            int responseCode=bigfx.getResponseCode();

            //Checking response code
            if (responseCode==200){
                BufferedReader br = new BufferedReader(new InputStreamReader(bigfx.getInputStream(),"UTF-8"));
                String line="";
                StringBuffer allLines=new StringBuffer();
                while((line=br.readLine())!=null){
                    while(!line.contains("</Tuple>") && line.contains("Answer")&& !line.trim().isEmpty()){
                        String m[]=line.split(">");
                        line=m[1].split("<")[0];
                        allLines.append(line+",");
                    }
                    if (line.contains("</Tuple>")){
                        allLines.append("\n");
                    }
                }
                //Property Array
                String arr[]=allLines.toString().split("\n");
                ArrayList<String[]>detailList=new ArrayList<>();
                for (String prop:allLines.toString().split("\n")){
                    String[]dd={prop};
                    detailList.add(dd);
                }
                for (int i=0;i< arr.length;i++){
                    String data=detailList.get(i)[0].split(",")[0]+"&"+detailList.get(i)[0].split(",")[1]+"&"+detailList.get(i)[0].split(",")[2]+
                            "&"+detailList.get(i)[0].split(",")[3];
                    allData.add(data);
                }
                br.close();
                bigfx.disconnect();
            }else{
                throw new Exception("Username/Password Incorrect. Please try again");
            }
        }catch (SocketTimeoutException ex){
            allData.add(ex.toString());
        }
        catch (Exception ex){
            allData.add(ex.toString());
        }finally{
            return (allData);
        }
    }
}