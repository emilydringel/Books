import java.util.*;
import java.net.MalformedURLException;
import java.io.IOException;

public class GoogleBook{

  //create in constructor
  private String googleID;
  private String volumeXML;
  //add to in parse
  private String description;
  private String categories;
  //add to in parse abstracts
  private HashMap<String,Integer> names;
  //add to in parse names
  private int females;
  private int males;
  private double femPct;

  URLtoXML transformer;

  public GoogleBook(String id){
    try{
      googleID = id;
      transformer = new URLtoXML();
      volumeXML = idToXML();
      findDescriptionAndCategories();
      getNames();
      parseNames();
    }catch(Exception E){
      System.out.println("An error occurred");
      System.out.println(E);
    }
  }

  public double getFemPct(){
    return femPct;
  }

  private String idToXML() throws MalformedURLException, IOException{
    String url = "https://www.googleapis.com/books/v1/volumes/";
    url += googleID;
    return transformer.urlToXML(url);
  }

  private void findDescriptionAndCategories(){
    int counter = 0;
    while(counter<volumeXML.length()){
      if(counter+11>volumeXML.length()){
        break;
      }
      if(volumeXML.substring(counter, counter+10).equals("categories")){
        counter = counter+13;
        categories = "";
        while(!volumeXML.substring(counter, counter + 11).equals("averageRati")){
          categories += Character.toString(volumeXML.charAt(counter));
          counter++;
        }
        break;
      }else if(volumeXML.substring(counter, counter+11).equals("description")){
        counter = counter+14;
        description = "";
        while(!volumeXML.substring(counter, counter + 11).equals("industryIde")){
          description += Character.toString(volumeXML.charAt(counter));
          counter++;
        }
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
      femPct = females/(females+males);
    }else{
      femPct = -1;
    }
  }
}
