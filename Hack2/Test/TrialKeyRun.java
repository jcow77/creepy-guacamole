import io.indico.Indico;
import io.indico.api.results.*;
import java.util.*;
import io.indico.api.text.*;
import java.io.*;
import org.apache.http.client.*;
import org.apache.http.HttpEntity.*;
import org.apache.commons.io.*;
import com.google.gson.*;
import twitter4j.*;
import java.nio.charset.*;
   
public class TrialKeyRun
{
   public static final int numstates = 50;
  // static CharsetEncoder asciiEncoder = Charset.forName("US-ASCII").newEncoder(); 
   public static void main (String []args) throws Exception
   {
      Indico indico = new Indico("8f118404900586284fdece0ae651010c");
      System.setOut(new PrintStream(new FileOutputStream("OUTPUT.txt")));
      String[] array = new String[50];
      Scanner governors = new Scanner(new File("list.txt"));
      for(int x = 0; x < 50; x++){
         array[x] = governors.nextLine();
      }
      
      Twitter twitter = TwitterFactory.getSingleton();
      
      for(String handle : array) {
         double lib = 0;
         double con = 0;
         double green = 0;
         double tar = 0;
         
         Paging page = new Paging(1,40);  // Accesses last 200 tweets, I think this is the limit
         int p = 1;
         List<Status> statuses = new ArrayList<Status>();
         while (p < 2) // Can only access 20 per page I think
         {
            p++;
            page.setPage(p);
            statuses.addAll(twitter.getUserTimeline(handle,page));  //get the tweets
         }
         List<String> examples = new ArrayList<String>();
         for (Status status : statuses)
         {
            if(status.getText().contains("í") || status.getText().contains("é") || status.getText().contains("ó") || status.getText().contains(" ") || status.getText().contains("½")) {
                break;
            }
            IndicoResult prediction = indico.political.predict(status.getText());
            Map<PoliticalClass, Double> e = prediction.getPolitical();
            
            lib += e.get(PoliticalClass.Liberal);
            con += e.get(PoliticalClass.Conservative);
            green += e.get(PoliticalClass.Green);
            tar += e.get(PoliticalClass.Libertarian);
         }
         double winner = Math.max(Math.max(lib, con), Math.max(green, tar));
         if (winner ==lib) {
            System.out.println("Liberal");
         }
         if (winner == con) {
            System.out.println("Conservative");
         }
         if (winner == green) {
            System.out.println("Green");
         }
         if (winner == tar) {
             System.out.println("Libertarian");
         }
      }
   }
}