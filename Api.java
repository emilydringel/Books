import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.io.IOException;

public class Api {

    public static final void main(String[] args){
        try{
          GoogleBook skippingChristmas = new GoogleBook("zyTCAlFPjgYC");
          System.out.println("femPct: "+skippingChristmas.getFemPct());
        }catch(Exception E){
          System.out.println(E);
        }
    }

}
