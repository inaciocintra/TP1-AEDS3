import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.DataInputStream;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        // SimpleDateFormat formatodedata = new SimpleDateFormat("dd/MM/yyyy");
        Scanner entrada = new Scanner(System.in);
        int operacao;
        FileOutputStream arq;
        DataOutputStream dos;
        FileInputStream arq2;
        DataInputStream dis;

        byte[] ba;

        ArrayList<Serie> listadeseries = new ArrayList<>();
        FileReader filereader;
        BufferedReader bufferedreader;
        String nomearquivo = "tvs.csv/tvs.csv";

        System.out.println("Qual operação deseja fazer?");
        System.out.println("-Para escrever no arquivo digite 1");
        System.out.println("-Para ler do arquivo digite 2");
        System.out.println("-Para atualizar um registro do arquivo digite 3");
        System.out.println("-Para excluir um registro digite 4");
        System.out.println("-Para sair digite qualquer outro numero");
        operacao = entrada.nextInt();
        // boolean condiçao ve se ja foi criado o arquivo sequencial das series
        boolean condicao = false;
        
        while (operacao >= 1 && operacao <= 4) {

// ----- CREATE -----
            if (operacao == 1) {
                condicao = true;

                try {
                    String linha;
                    filereader = new FileReader(nomearquivo);
                    bufferedreader = new BufferedReader(filereader);
                    bufferedreader.readLine();
                    // le as linhas do csv até o fim/null
                    while ((linha = bufferedreader.readLine()) != null) {

                        Serie serie = new Serie();
                        // passa a linha para o metodo ler que ira setar os atributos de cada serie na Serie.java
                        serie.ler(linha);
                        listadeseries.add(serie);

                        arq = new FileOutputStream("dados/series.db", true);
                        dos = new DataOutputStream(arq);

                        // converte cada objeto de serie criada para byte array
                        ba = serie.toByteArray();
                        // escreve a lapide (o true significa que o registro depois dela ainda está
                        // válido/existe, ou seja nao foi excluido) no arquivo sequencial
                        dos.writeBoolean(true); // lapide
                        // escreve o tamanho do registro da serie no aqrquivo sequencial
                        dos.writeInt(ba.length);
                        // escreve o registro inteiro da serie no aqrquivo sequencial
                        dos.write(ba);

                    }
                    filereader.close();
                    bufferedreader.close();
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }

// ----- READ -----
            else if (operacao == 2) {
                int idserie;
                System.out.println("Digite o id da serie que você quer buscar/ler: ");
                idserie = entrada.nextInt();
                // se o arquivo sequencial ja tiver sido criado e o id
                if (condicao && idserie >= 1 && idserie <= 1094) {
                    try {

                        arq2 = new FileInputStream("dados/series.db");
                        dis = new DataInputStream(arq2);

                        boolean encontrou = false;

                        // dis.available indica quantos bytes ainda restam para serem lidos no arquivo
                        // ou stream
                        while (dis.available() > 0) {
                            boolean lapide = dis.readBoolean(); // Lê a lápide, que indica se o registro está ativo
                            int tamanhoRegistro = dis.readInt(); // Lê o tamanho do registro em bytes
                            ba = new byte[tamanhoRegistro];
                            dis.readFully(ba); // lê exatamente o número de bytes especificado pelo tamanho do array ba
                                               // e os armazena nesse array

                            if (lapide) { // Se o registro está ativo (não foi excluído)
                                Serie serie = new Serie();
                                serie.fromByteArray(ba); // Reconstrói o objeto `Serie` a partir do byte array

                                if (serie.getId() == idserie) { // Verifica se é o ID procurado
                                    encontrou = true;
                                    System.out.println("Série encontrada:");
                                    System.out.println("ID: " + serie.getId());
                                    System.out.println("Nome: " + serie.getName());
                                    System.out.println("Idioma: " + serie.getLanguage());
                                    System.out.println("Data de estreia: " + serie.getDate());
                                    System.out.println("Empresas: " + String.join(", ", serie.getCompanies()));
                                    break; // Sai do loop após encontrar a série
                                }
                            }
                        }

                        if (!encontrou) {
                            System.out.println("Série com ID " + idserie + " não encontrada.");
                        }

                        dis.close();
                    } catch (IOException e) {
                        System.out.println("Erro ao ler o arquivo: " + e.getMessage());
                    }

                }

                else {
                    // caso os registros das series ainda nao tenha sido criado, ele escreve no
                    // arquivo sequencial todas elas (faz o create) e depois le o id desejado
                    condicao = true;
                    try {
                        String linha;
                        filereader = new FileReader(nomearquivo);
                        bufferedreader = new BufferedReader(filereader);
                        bufferedreader.readLine();

                        // le as linhas do csv até o fim/null
                        while ((linha = bufferedreader.readLine()) != null) {

                            Serie serie = new Serie();
                            // passa a linha para o metodo ler que ira setar os atributos de cada serie na Serie.java
                            serie.ler(linha);
                            listadeseries.add(serie);

                            arq = new FileOutputStream("dados/series.db", true);
                            dos = new DataOutputStream(arq);

                            // converte cada objeto de serie criada para byte array
                            ba = serie.toByteArray();
                            // escreve a lapide (o true significa que o registro depois dela ainda está
                            // válido/existe, ou seja nao foi excluido) no arquivo sequencial
                            dos.writeBoolean(true); // lapide
                            // escreve o tamanho do registro da serie no aqrquivo sequencial
                            dos.writeInt(ba.length);
                            // escreve o registro inteiro da serie no aqrquivo sequencial
                            dos.write(ba);

                        }
                        filereader.close();
                        bufferedreader.close();
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                    try {

                        arq2 = new FileInputStream("dados/series.db");
                        dis = new DataInputStream(arq2);

                        boolean encontrou = false;

                        // dis.available indica quantos bytes ainda restam para serem lidos no arquivo
                        // ou stream
                        while (dis.available() > 0) {
                            boolean lapide = dis.readBoolean(); // Lê a lápide, que indica se o registro está ativo
                            int tamanhoRegistro = dis.readInt(); // Lê o tamanho do registro em bytes
                            ba = new byte[tamanhoRegistro];
                            dis.readFully(ba); // lê exatamente o número de bytes especificado pelo tamanho do array ba
                                               // e os armazena nesse array

                            if (lapide) { // Se o registro está ativo (não foi excluído)
                                Serie serie = new Serie();
                                serie.fromByteArray(ba); // Reconstrói o objeto `Serie` a partir do byte array

                                if (serie.getId() == idserie) { // Verifica se é o ID procurado
                                    encontrou = true;
                                    System.out.println("Série encontrada:");
                                    System.out.println("ID: " + serie.getId());
                                    System.out.println("Nome: " + serie.getName());
                                    System.out.println("Idioma: " + serie.getLanguage());
                                    System.out.println("Data de estreia: " + serie.getDate());
                                    System.out.println("Empresas: " + String.join(", ", serie.getCompanies()));
                                    break; // Sai do loop após encontrar a série
                                }
                            }
                        }

                        if (!encontrou) {
                            System.out.println("Série com ID " + idserie + " não encontrada.");
                        }

                        dis.close();
                    } catch (IOException e) {
                        System.out.println("Erro ao ler o arquivo: " + e.getMessage());
                    }

                }

            }
            // ----- UPDATE -----
            // else if(operacao ==3){

            // }
            // else if(operacao ==4){

            // }
            // else{}

            System.out.println("Qual operação deseja fazer?");
            System.out.println("-Para escrever no arquivo digite 1");
            System.out.println("-Para ler do arquivo digite 2");
            System.out.println("-Para atualizar um registro do arquivo digite 3");
            System.out.println("-Para excluir um registro digite 4");
            System.out.println("-Para sair digite qualquer outro numero");
            operacao = entrada.nextInt();

        }
        entrada.close();
    }
}
