import java.util.*;
import java.net.MalformedURLException;
import java.io.IOException;

public class GoogleBook{

  //INCLUDE AUTHOR AT SOME POINT
  //create in constructor
  private String googleID;
  private String volumeXML;
  //add to in parse
  private String bookName;
  private String authors;
  private String description;
  //add to in parse abstracts
  private HashMap<String,Integer> names;
  //add to in parse names
  private int females = 0;
  private int males = 0;
  private double femPct = -1;

  URLtoXML transformer;

  public GoogleBook(String myXML, URLtoXML myurltoxml) throws MalformedURLException, IOException{
    transformer = myurltoxml;
    volumeXML = myXML;
    parseXML();
    if(description==null){
      femPct = -1;
      return;
    }
    getNames();
    parseNames();
  }

  public String getName(){
    return bookName;
  }

  public String getAuthors(){
    return authors;
  }

  public int getFemaleCount(){
    return females;
  }

  public int getMaleCount(){
    return males;
  }

  public double getFemalePercent(){
    return femPct;
  }

  public String getDescription(){
    return description;
  }

  private void parseXML(){
    int counter = 0;
    while(counter<volumeXML.length()){
      if(counter+11>volumeXML.length()){
        break;
      }if(volumeXML.substring(counter, counter+5).equals("\"id\":")){
        counter = counter+7;
        googleID = "";
        while(!volumeXML.substring(counter, counter + 2).equals("\",")){
          googleID += Character.toString(volumeXML.charAt(counter));
          counter++;
        }
      }else if(volumeXML.substring(counter, counter+6).equals("\"title")){
        counter = counter+10;
        bookName = "";
        while(!volumeXML.substring(counter, counter + 2).equals("\",")){
          bookName += Character.toString(volumeXML.charAt(counter));
          counter++;
        }
      }else if(volumeXML.substring(counter, counter+10).equals("\"authors\":")){
        counter = counter+11;
        String unparsedAuthors = "";
        while(!volumeXML.substring(counter, counter + 9).equals("\"publishe")){
          unparsedAuthors += Character.toString(volumeXML.charAt(counter));
          counter++;
        }
        authors = parseAuthors(unparsedAuthors);
      }else if(volumeXML.substring(counter, counter+10).equals("descriptio")){
        counter = counter+14;
        description = "";
        while(!volumeXML.substring(counter, counter + 10).equals("readingMod")){
          description += Character.toString(volumeXML.charAt(counter));
          counter++;
        }
        break;
      }else{
        counter++;
      }
    }
  }

  private void getNames(){
    names = new HashMap<>();
    String[] words = description.split(" ");
    boolean thisCap = false;
    for(String word: words){
      word = word.replaceAll("\\p{Punct}","");
      if(word.equals("")){continue;}
      thisCap = Character.isUpperCase(word.charAt(0));
      word = word.toLowerCase();
      if(names.containsKey(word)){
        if(thisCap){
          names.put(word, names.get(word)+1);
        }else{
          names.remove(word);
        }
      }else if(thisCap){
        names.put(word, 1);
      }
      genderAWord(word);
    }
  }

  private void parseNames() throws MalformedURLException, IOException{
    String base = "https://api.genderize.io?name=";
    boolean female = false;
    double pct = 0;
    for(String name: names.keySet()){
      base += name;
      String genderizeReturn = transformer.urlToXML(base);
      for(int i=0; i<genderizeReturn.length()-6; i++){
        if(genderizeReturn.substring(i,i+6).equals("gender")){
          i=i+9;
          if(genderizeReturn.substring(i,i+4).equals("male")){
            female = false;
          }else if(genderizeReturn.substring(i,i+6).equals("female")){
            female = true;
          }else{
            break;
          }
        }if(genderizeReturn.substring(i,i+6).equals("probab")){
          i=i+13;
          String percent = genderizeReturn.substring(i,i+4);
          if(percent.charAt(1)==','){
            percent = percent.substring(0,1);
          }else if(percent.charAt(3)==','){
            percent = percent.substring(0,3);
          }
          pct = Double.parseDouble(percent);
        }if(genderizeReturn.substring(i,i+5).equals("count")){
          i=i+7;
          int count = Integer.parseInt(genderizeReturn.substring(i,genderizeReturn.length()-1));
          if(count>1000 && female && pct>.85){
            females += names.get(name);
          }else if(count>1000 && pct>.85){
            males += names.get(name);
          }
        }
      }
      base = "https://api.genderize.io?name=";
    }if(females+males>0){
      femPct = (double) females/(females+males);
    }else{
      femPct = -1;
    }
  }

  private static String parseAuthors(String unparsed){
    String parsed = "";
    boolean inQuotes = false;
    for(int i=0; i<unparsed.length(); i++){
      if(unparsed.charAt(i)=='"'){
        inQuotes = !inQuotes;
        if(!inQuotes){
          parsed += ", ";
        }
      }else if(inQuotes){
        parsed += Character.toString(unparsed.charAt(i));
      }
    }
    return parsed.substring(0, parsed.length()-2);
  }

  private void genderAWord(String word){
    if(word.equals("boy")||word.equals("man")||word.equals("father")||
      word.equals("son")||word.equals("uncle")||word.equals("husband")||
      word.equals("grandfather")||word.equals("he")||word.equals("him")||
      word.equals("his")||word.equals("grandson")){
        males++;
    }else if(word.equals("girl")||word.equals("woman")||word.equals("mother")||
      word.equals("daughter")||word.equals("aunt")||word.equals("wife")||
      word.equals("grandmother")||word.equals("she")||word.equals("her")||
      word.equals("hers")||word.equals("granddaughter")){
        females++;
    }
  }
}
