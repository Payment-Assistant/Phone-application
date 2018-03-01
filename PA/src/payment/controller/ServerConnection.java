package payment.controller;

import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class ServerConnection {

    public HttpURLConnection con = null;
    public BufferedReader in = null;
    public OutputStreamWriter wr = null;
    public void connectToServer(){
        String url = "http://82.202.246.186:8080/web/UserController";
        try{
            con = (HttpURLConnection)new URL(url).openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
        }
        catch(Exception e){
            System.out.println("No connection to server");
        }
    }
}
