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
import java.util.Base64;
import java.util.ArrayList;

public class ComputerDetails {
    String hostnameOrIp;
    String userName;
    String password;
    String computerName;
    ArrayList<String>allData=new ArrayList<>();
    public ComputerDetails(String hostnameOrIp,String userName,String password,String computerName){
        this.hostnameOrIp=hostnameOrIp;
        this.userName=userName;
        this.password=password;
        this.computerName=computerName;
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
            //String relevance="(names of it,IDs of it,agent types of it,cpus of it,values of results (it,bes property \"RAM\"),operating systems of it,values of results(it,bes property \"DNS Name\"),ip addresses of it,values of results (it,bes property \"IPV6 Address\"),last report times of it,values of results (it,bes property \"Device Type\"), values of results (it,bes property \"Relay\")) of bes computers whose (name of it = \""+computerName+"\")"
                    //.replace(" ","%20");
            String relevance="(names%20of%20it,IDs%20of%20it,agent%20types%20of%20it,cpus%20of%20it,values%20of%20results%20(it,bes%20property%20%22RAM%22),operating%20systems%20of%20it,values%20of%20results(it,bes%20property%20%22DNS%20Name%22),ip%20addresses%20of%20it,values%20of%20results%20(it,bes%20property%20%22IPV6%20Address%22),last%20report%20times%20of%20it,values%20of%20results%20(it,bes%20property%20%22Device%20Type%22),%20values%20of%20results%20(it,bes%20property%20%22Relay%22))%20of%20bes%20computers%20whose%20(name%20of%20it%20=%20%22"+computerName+"%22)";
            //System.out.println(relevance);

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
                    String data=detailList.get(i)[0].split(",")[0]+"&"+detailList.get(i)[0].split(",")[1]+"&"+detailList.get(i)[0].split(",")[2]+"&"+detailList.get(i)[0].split(",")[3]+"&"+detailList.get(i)[0].split(",")[4]+"&"+detailList.get(i)[0].split(",")[5]+"&"+detailList.get(i)[0].split(",")[6]+
                            "&"+detailList.get(i)[0].split(",")[7]+"&"+detailList.get(i)[0].split(",")[8]+"&"+detailList.get(i)[0].split(",")[9]+
                            "&"+detailList.get(i)[0].split(",")[10]+"&"+detailList.get(i)[0].split(",")[11]+
                            "&"+detailList.get(i)[0].split(",")[12];
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