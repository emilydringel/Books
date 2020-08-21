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

public class Api {

    public static final void main(String[] args){
        try{
          Scanner scan = new Scanner(System.in);
          URLtoXML transformer = new URLtoXML();
          System.out.println("Do you have an author?");
          String author = scan.nextLine();
          System.out.println("Do you have a title?");
          String title = scan.nextLine();
          Library myLibrary = new Library(title, author, transformer);
          for(GoogleBook myBook: myLibrary.myLib){
            System.out.println("Book: " + myBook.getName());
            System.out.println("Authors: " + myBook.getAuthors());
            double percent = myBook.getFemalePercent();
            if(percent==-1){
              System.out.println("No information for this book. Sorry.");
            }else{
              System.out.println("Description: "+myBook.getDescription());
              String percentString = Double.toString(percent*100);
              if(percentString.length()>=4){
                percentString = percentString.substring(0,4);
              }
              System.out.println("Percentage of Names Mentioned that are Female: " + percentString +"%");
              System.out.println("Male names mentioned: " + myBook.getMaleCount() + "; Female names mentioned: "+myBook.getFemaleCount());
            }
          }
        }catch(Exception E){
          System.out.println("Internal Error: " + E);
        }
    }

}
