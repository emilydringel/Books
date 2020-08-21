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
import java.util.Scanner;
import java.util.HashSet;

class Library{

  public HashSet<GoogleBook> myLib;

  private URLtoXML transformer;

  public Library(String intitle, String inauthor, URLtoXML myurltoxml) throws MalformedURLException, IOException{
    myLib = new HashSet<>();
    transformer = myurltoxml;
    String response = getXML(intitle, inauthor, transformer);
    parseXML(response);
  }

  private String getXML(String intitle, String inauthor, URLtoXML transformer) throws MalformedURLException, IOException{
    String url = "https://www.googleapis.com/books/v1/volumes?q=";
    if(!inauthor.equals("")){
      inauthor = inauthor.replaceAll(" ","%20");
      url+="+inauthor:"+inauthor;
    }
    if(!intitle.equals("")){
      intitle = intitle.replaceAll(" ","%20");
      url+="+intitle:"+intitle;
    }
    url+="&projection=lite&printType=books";
    String myXml = transformer.urlToXML(url);
    return myXml;
  }

  private void parseXML(String response) throws MalformedURLException, IOException{
    String current = "";
    boolean first = true;
    for(int i=0; i<response.length(); i++){
      if(response.substring(i, i+10).equals("totalItems")){
        i=i+13;
        if(response.charAt(i)=='0'){
          return;
        }
        break;
      }
    }
    //System.out.println(response);
    for(int i=0; i<response.length(); i++){
      if(i<response.length()-22){
        //System.out.println(response.substring(i, i+22));
        if(response.substring(i, i+22).equals("\"kind\": \"books#volume\"")){
          if(first){
            first = false;
          }else{
            GoogleBook myBook = new GoogleBook(current, transformer);
            myLib.add(myBook);
          }
          current = Character.toString(response.charAt(i));
        }else{
          current += Character.toString(response.charAt(i));
        }
      }else{
        current += Character.toString(response.charAt(i));
      }
    }
    GoogleBook myBook = new GoogleBook(current, transformer);
    myLib.add(myBook);
  }

}
