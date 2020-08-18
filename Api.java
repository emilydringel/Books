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
          GoogleBook myBook = new GoogleBook("zyTCAlFPjgYC");
          System.out.println("Book: " + myBook.getName());
          double percent = myBook.getFemalePercent();
          if(percent==-1){
            System.out.println("No information for this book. Sorry.");
          }else{
            System.out.println("Percentage of Names Mentioned that are Female: "+Double.toString(percent*100).substring(0,4)+"%");
            System.out.println("Male names mentioned: "+myBook.getMaleCount()+ "; Female names mentioned: "+myBook.getFemaleCount());
          }
        }catch(Exception E){
          System.out.println("Internal Error: " + E);
        }
    }

}
