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

public class URLtoXML{
  static final String kuser = "testuser";
  static final String kpass = "testpassword";;

  public URLtoXML(){
  }

  static class RHAuthenticator extends Authenticator {
      public PasswordAuthentication getPasswordAuthentication() {
          return (new PasswordAuthentication(kuser, kpass.toCharArray()));
      }
  }

  //turn throws into catches at some point
  public String urlToXML(String urlString) throws MalformedURLException,IOException{
      Authenticator.setDefault(new RHAuthenticator());
      URL url = new URL(urlString);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      InputStream is = con.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      String str;
      String xmlString = "";
      while((str = reader.readLine()) != null) {
          xmlString+=str;
      }
      reader.close();
      return xmlString;
  }

}
