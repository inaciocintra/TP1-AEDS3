import java.io.FileOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
         SimpleDateFormat formatodedata = new SimpleDateFormat("dd/MM/yyyy");

         FileOutputStream arq; 
         DataOutputStream dos;
         byte[] ba; 

        
        
         try {
            Date date;
            date = formatodedata.parse("15/07/2016");    
            ArrayList<String> companies = new ArrayList<>();
            companies.add("Netflix");
            companies.add("Lionsgate");
            companies.add("Paramount");
            Serie serie = new Serie(99, "Stranger things", "En", date, companies);

            arq = new FileOutputStream("dados/series.db");
            dos = new DataOutputStream(arq);
            ba = serie.toByteArray();
            arq.write(ba.length);
            arq.write(ba);
            System.out.println(serie);
            } catch (ParseException  | IOException e) {
               System.out.println("Error");
            }  // Exemplo de data
            
                
            
           
    
    
}
}
