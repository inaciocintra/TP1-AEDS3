import java.io.FileOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        SimpleDateFormat formatodedata = new SimpleDateFormat("dd/MM/yyyy");

        FileOutputStream arq; 
        DataOutputStream dos;
        byte[] ba; 

        try {
            // Registro 1
            Date date = formatodedata.parse("15/07/2016");    
            ArrayList<String> companies = new ArrayList<>();
            companies.add("Netflix");
            companies.add("Lionsgate");
            companies.add("Paramount");

            Serie serie = new Serie(1, "Stranger Things", "En", date, companies);

            arq = new FileOutputStream("dados/series.db"); 
            dos = new DataOutputStream(arq);
            ba = serie.toByteArray();
            dos.writeBoolean(true);
            dos.writeInt(ba.length);
            dos.write(ba);
            System.out.println(serie);

            // Registro 2
            Date date1 = formatodedata.parse("01/02/2013");    
            ArrayList<String> companies1 = new ArrayList<>();
            companies1.add("Warner");
            companies1.add("Amazon");

            Serie serie1 = new Serie(2, "Breaking Bad", "PT", date1, companies1);

            ba = serie1.toByteArray();
            dos.writeBoolean(true);
            dos.writeInt(ba.length);
            dos.write(ba);
            System.out.println(serie1);

            // Registro 3
            Date date2 = formatodedata.parse("23/05/2019");    
            ArrayList<String> companies2 = new ArrayList<>();
            companies2.add("HBO");
            companies2.add("Sky Atlantic");

            Serie serie2 = new Serie(3, "Chernobyl", "En", date2, companies2);

            ba = serie2.toByteArray();
            dos.writeBoolean(true);
            dos.writeInt(ba.length);
            dos.write(ba);
            System.out.println(serie2);

            dos.close(); // Fecha o DataOutputStream
            arq.close(); // Fecha o FileOutputStream
        } catch (ParseException | IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
