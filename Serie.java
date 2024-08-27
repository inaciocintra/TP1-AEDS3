import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

class Serie {
private int id; 
private String name;
private String language;
private Date first_air_date;
private ArrayList<String> companies;
private SimpleDateFormat formatodedata = new SimpleDateFormat("dd/MM/yyyy");


public Serie(){
    this.id =-1;
    this.name = "unnamed";
    this.language = "xx";
    this.first_air_date = null;
    this.companies = new ArrayList<String>();

}

public Serie(int id, String name, String language, Date first_air_date, ArrayList<String> companies){
    this.id = id;
    this.name = name;
    this.language = language;
    this.first_air_date = first_air_date;
    this.companies = companies;
}

//----- get and set (id) -----
public int getId(){
    return id;
} 
public void setId(int id){
    this.id = id;
}


//----- get and set (name) -----
public String getName(){
    return name;
} 
public void setName(String name){
    this.name = name;
}


//----- get and set (language) -----
public String getLanguage (){
    return language;
} 
public void setLanguage(String language){
    this.language = language;
}


//----- get and set (date) -----
public String getDate(){
    return formatodedata.format(first_air_date);
} 

public void setDate(String first_air_datee){
    try{
        Date dataformatada = formatodedata.parse(first_air_datee);
        this.first_air_date = dataformatada; 
    } 
    catch
    (ParseException e) {
        System.out.println("Erro ao converter a data: " + e.getMessage());
    }
    
}


//----- get and set (companies) ----- 
public ArrayList<String> getCompanies(){
    return companies;
} 
public void setCompanies(String companies){
    this.companies.add(companies);
}

//metodo read
public void readCsv(String line){

//splita a linha 
String data[] = line.split(";");
if(data.length>=5){
    try{
        int idconverted = Integer.parseInt(data[0]);
        setId(idconverted);
    }
    catch (NumberFormatException e) {
        System.out.println("Error when converting: " + e.getMessage());
    }
}
setName(data[1]);
setLanguage(data[2]);
setDate(data[3]);
setCompanies(data[4]);
setCompanies(data[5]);
setCompanies(data[6]);



}
public String toString(){
    return "\n ID: " +id+  "\n Name: " +name+ "\n Language: " +language+ "\n Date: " +first_air_date+ "\n Companies: " +companies;
}

//escrita
public byte[] toByteArray() throws IOException{
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeInt(id);
    dos.writeUTF(name);
    dos.writeUTF(language);
    dos.writeLong(first_air_date.getTime());

    // Escrever o ArrayList de Strings (companias)
    dos.writeInt(companies.size());  // Primeiro, o tamanho da lista
    for(String company : companies) {
        dos.writeUTF(company);       // Escreve cada String da lista
    }

    dos.close();
    return baos.toByteArray();


}

}