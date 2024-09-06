import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.FileInputStream;
import java.io.DataInputStream;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        
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
        System.out.println("-Para carregar o arquivo digite 1");
        System.out.println("-Para ler do arquivo digite 2");
        System.out.println("-Para atualizar um registro do arquivo digite 3");
        System.out.println("-Para excluir um registro digite 4");
        System.out.println("-Para criar um registro digite 5");
        System.out.println("-Para ordenar um registro digite 6");
        System.out.println("-Para sair digite 7 ou qualquer outro numero maior");
        operacao = entrada.nextInt();
        // boolean condiçao ve se ja foi criado o arquivo sequencial das series
        boolean condicao = false;

        while (operacao >= 1 && operacao <= 6) {

// ----- CARREGA REGISTRO -----
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

                        // dis.available indica quantos bytes ainda restam para serem lidos no arquivo ou stream
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
                    // caso os registros das series ainda nao tenha sido criado, ele escreve no arquivo sequencial todas elas (faz o carregamento de td) e depois le o id desejado
                    condicao = true;
                    try {
                        String linha;
                        filereader = new FileReader(nomearquivo);
                        bufferedreader = new BufferedReader(filereader);
                        bufferedreader.readLine();

                        // le as linhas do csv até o fim/null
                        while ((linha = bufferedreader.readLine()) != null) {

                            Serie serie = new Serie();
                            //passa a linha para o metodo ler que ira setar os atributos de cada serie na Serie.java
                            
                            serie.ler(linha);
                            listadeseries.add(serie);

                            arq = new FileOutputStream("dados/series.db", true);
                            dos = new DataOutputStream(arq);

                            //converte cada objeto de serie criada para byte array
                            ba = serie.toByteArray();
                            //escreve a lapide (o true significa que o registro depois dela ainda está válido/existe, ou seja nao foi excluido) no arquivo sequencial
                            dos.writeBoolean(true); // lapide
                            //escreve o tamanho do registro da serie no aqrquivo sequencial
                            dos.writeInt(ba.length);
                            //escreve o registro inteiro da serie no aqrquivo sequencial
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

                        // dis.available indica quantos bytes ainda restam para serem lidos no arquivo ou stream
                        while (dis.available() > 0) {
                            boolean lapide = dis.readBoolean(); // le a lápide, que indica se o registro está ativo
                            int tamanhoRegistro = dis.readInt(); //le o tamanho do registro em bytes
                            ba = new byte[tamanhoRegistro];
                            dis.readFully(ba); // lê exatamente o número de bytes especificado pelo tamanho do array ba e os armazena nesse array

                            if (lapide) { // Se o registro está ativo (não foi excluído)
                                Serie serie = new Serie();
                                serie.fromByteArray(ba); //reconstrói o objeto Serie a partir do byte array

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

            else if (operacao == 3) {
                int idserie;
                System.out.println("Qual id você deseja atualizar: ");
                idserie = entrada.nextInt();
                if (condicao) {

        try {
        RandomAccessFile arqq = new RandomAccessFile("dados/series.db", "rw");

        boolean encontrou = false;

        while (arqq.getFilePointer() < arqq.length()) {
            long posicaoInicio = arqq.getFilePointer(); // Marca a posição do início do registro

            boolean lapide = arqq.readBoolean(); // Lê a lápide
            int tamanhoRegistro = arqq.readInt(); // Lê o tamanho do registro em bytes
            ba = new byte[tamanhoRegistro];
            arqq.readFully(ba); // Lê o registro completo

            if (lapide) { // Se o registro está ativo
                Serie serie = new Serie();
                serie.fromByteArray(ba);

                if (serie.getId() == idserie) {
                    encontrou = true;

                    // Solicita os novos atributos da série
                    System.out.println("Digite os novos atributos da série:");
                    System.out.print("ID: ");
                    int novoId = entrada.nextInt();
                    entrada.nextLine(); // Consumir a nova linha

                    System.out.print("Nome: ");
                    String novoNome = entrada.nextLine();

                    System.out.print("Idioma: ");
                    String novoIdioma = entrada.nextLine();

                    System.out.print("Data de estreia (dd/MM/yyyy): ");
                    String novaDataStr = entrada.nextLine();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date novaData = sdf.parse(novaDataStr);

                    System.out.print("Empresas (separadas por vírgula): ");
                    String empresasStr = entrada.nextLine();
                    ArrayList<String> novasEmpresas = new ArrayList<>(Arrays.asList(empresasStr.split(",")));

                    // Cria uma nova série com os valores informados pelo usuário
                    Serie novaSerie = new Serie(novoId, novoNome, novoIdioma, novaData, novasEmpresas);
                    byte[] novoBa = novaSerie.toByteArray();

                    if (novoBa.length <= tamanhoRegistro) {
                        // Se o novo registro for menor ou igual ao existente, sobrescreve no local
                        arqq.seek(posicaoInicio); // Retorna para o início do registro
                        arqq.writeBoolean(true); // Mantém a lápide ativa
                        arqq.writeInt(tamanhoRegistro); // Mantém o tamanho original do registro
                        arqq.write(novoBa); // Escreve o novo registro

                        // Preenche bytes restantes, se o novo registro for menor que o original
                        if (novoBa.length < tamanhoRegistro) {
                            byte[] bytesSobrantes = new byte[tamanhoRegistro - novoBa.length];
                            arqq.write(bytesSobrantes);
                        }
                    } else {
                        // Se o novo registro for maior, marca o existente como excluído
                        arqq.seek(posicaoInicio); // Volta ao início do registro
                        arqq.writeBoolean(false); // Marca a lápide como excluída
                        
                        // Move para o final do arquivo para adicionar o novo registro
                        arqq.seek(arqq.length());
                        arqq.writeBoolean(true); // Novo registro com lápide ativa
                        arqq.writeInt(novoBa.length); // Tamanho do novo registro
                        arqq.write(novoBa); // Escreve os novos dados
                    }

                    System.out.println("Série com ID " + idserie + " atualizada com sucesso.");
                    break; // Sai do loop após a atualização
                }
            }
        }

        if (!encontrou) {
            System.out.println("Série com ID " + idserie + " não encontrada para atualização.");
        }

        arqq.close();

    } catch (IOException | ParseException e) {
        e.printStackTrace();
    }
                }
                else{
                    // caso os registros das series ainda nao tenha sido criado, ele escreve no arquivo sequencial todas elas (faz o carregamento de td) e depois atualiza o id desejado
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
                        RandomAccessFile arqq = new RandomAccessFile("dados/series.db", "rw");
                
                        boolean encontrou = false;
                
                        while (arqq.getFilePointer() < arqq.length()) {
                            long posicaoInicio = arqq.getFilePointer(); // Marca a posição do início do registro
                
                            boolean lapide = arqq.readBoolean(); // Lê a lápide
                            int tamanhoRegistro = arqq.readInt(); // Lê o tamanho do registro em bytes
                            ba = new byte[tamanhoRegistro];
                            arqq.readFully(ba); // Lê o registro completo
                
                            if (lapide) { // Se o registro está ativo
                                Serie serie = new Serie();
                                serie.fromByteArray(ba);
                
                                if (serie.getId() == idserie) {
                                    encontrou = true;
                
                                    // Solicita os novos atributos da série
                                    System.out.println("Digite os novos atributos da série:");
                                    System.out.print("ID: ");
                                    int novoId = entrada.nextInt();
                                    entrada.nextLine(); // Consumir a nova linha
                
                                    System.out.print("Nome: ");
                                    String novoNome = entrada.nextLine();
                
                                    System.out.print("Idioma: ");
                                    String novoIdioma = entrada.nextLine();
                
                                    System.out.print("Data de estreia (dd/MM/yyyy): ");
                                    String novaDataStr = entrada.nextLine();
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                    Date novaData = sdf.parse(novaDataStr);
                
                                    System.out.print("Empresas (separadas por vírgula): ");
                                    String empresasStr = entrada.nextLine();
                                    ArrayList<String> novasEmpresas = new ArrayList<>(Arrays.asList(empresasStr.split(",")));
                
                                    // Cria uma nova série com os valores informados pelo usuário
                                    Serie novaSerie = new Serie(novoId, novoNome, novoIdioma, novaData, novasEmpresas);
                                    byte[] novoBa = novaSerie.toByteArray();
                
                                    if (novoBa.length <= tamanhoRegistro) {
                                        // Se o novo registro for menor ou igual ao existente, sobrescreve no local
                                        arqq.seek(posicaoInicio); // Retorna para o início do registro
                                        arqq.writeBoolean(true); // Mantém a lápide ativa
                                        arqq.writeInt(tamanhoRegistro); // Mantém o tamanho original do registro
                                        arqq.write(novoBa); // Escreve o novo registro
                
                                        // Preenche bytes restantes, se o novo registro for menor que o original
                                        if (novoBa.length < tamanhoRegistro) {
                                            byte[] bytesSobrantes = new byte[tamanhoRegistro - novoBa.length];
                                            arqq.write(bytesSobrantes);
                                        }
                                    } else {
                                        // Se o novo registro for maior, marca o existente como excluído
                                        arqq.seek(posicaoInicio); // Volta ao início do registro
                                        arqq.writeBoolean(false); // Marca a lápide como excluída
                                        
                                        // Move para o final do arquivo para adicionar o novo registro
                                        arqq.seek(arqq.length());
                                        arqq.writeBoolean(true); // Novo registro com lápide ativa
                                        arqq.writeInt(novoBa.length); // Tamanho do novo registro
                                        arqq.write(novoBa); // Escreve os novos dados
                                    }
                
                                    System.out.println("Série com ID " + idserie + " atualizada com sucesso.");
                                    break; // Sai do loop após a atualização
                                }
                            }
                        }
                
                        if (!encontrou) {
                            System.out.println("Série com ID " + idserie + " não encontrada para atualização.");
                        }
                
                        arqq.close();
                
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }
                }

            }

// ----- DELETE -----
             else if(operacao ==4){
                int idserie;
                System.out.println("Qual id deseja excluir: ");
                idserie = entrada.nextInt();
                if (condicao && idserie >= 1 && idserie <= 1094) { 
                    try {
                        RandomAccessFile arqq = new RandomAccessFile("dados/series.db", "rw");
                
                        boolean encontrou = false;
                
                        while (arqq.getFilePointer() < arqq.length()) {
                            long posicaoInicio = arqq.getFilePointer(); // Posição inicial do registro
                
                            boolean lapide = arqq.readBoolean(); // Lê a lápide
                            int tamanhoRegistro = arqq.readInt(); // Lê o tamanho do registro em bytes
                            ba = new byte[tamanhoRegistro];
                            arqq.readFully(ba); // Lê o registro completo
                
                            if (lapide) { // Se o registro está ativo
                                Serie serie = new Serie();
                                serie.fromByteArray(ba);
                
                                if (serie.getId() == idserie) {
                                    encontrou = true;
                
                                    // Marca o registro como excluído, escrevendo a lápide como `false`
                                    arqq.seek(posicaoInicio); // Volta para a posição inicial do registro
                                    arqq.writeBoolean(false); // Marca como excluído
                                    System.out.println("Série com ID " + idserie + " foi marcada como excluída.");
                                    break; // Sai do loop após marcar o registro como excluído
                                }
                            }
                        }
                
                        if (!encontrou) {
                            System.out.println("Série com ID " + idserie + " não encontrada para exclusão.");
                        }
                
                        arqq.close();
                
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                else{
                    // caso os registros das series ainda nao tenha sido criado, ele escreve no arquivo sequencial todas elas (faz o carregamento de td) e depois exclui o id desejado
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
                        RandomAccessFile arqq = new RandomAccessFile("dados/series.db", "rw");
                
                        boolean encontrou = false;
                
                        while (arqq.getFilePointer() < arqq.length()) {
                            long posicaoInicio = arqq.getFilePointer(); // Posição inicial do registro
                
                            boolean lapide = arqq.readBoolean(); // Lê a lápide
                            int tamanhoRegistro = arqq.readInt(); // Lê o tamanho do registro em bytes
                            ba = new byte[tamanhoRegistro];
                            arqq.readFully(ba); // Lê o registro completo
                
                            if (lapide) { // Se o registro está ativo
                                Serie serie = new Serie();
                                serie.fromByteArray(ba);
                
                                if (serie.getId() == idserie) {
                                    encontrou = true;
                
                                    // Marca o registro como excluído, escrevendo a lápide como `false`
                                    arqq.seek(posicaoInicio); // Volta para a posição inicial do registro
                                    arqq.writeBoolean(false); // Marca como excluído
                                    System.out.println("Série com ID " + idserie + " foi marcada como excluída.");
                                    break; // Sai do loop após marcar o registro como excluído
                                }
                            }
                        }
                
                        if (!encontrou) {
                            System.out.println("Série com ID " + idserie + " não encontrada para exclusão.");
                        }
                
                        arqq.close();
                
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    
                }

             }
// ----- CREATE -----
             else if (operacao ==5){
                if(condicao){

                    try {
                        // Solicitar os dados do usuário para o novo registro
                        System.out.println("Digite os atributos da nova série:");
                        System.out.print("ID: ");
                        int novoId = entrada.nextInt();
                        entrada.nextLine(); // Consumir a nova linha
                
                        System.out.print("Nome: ");
                        String novoNome = entrada.nextLine();
                
                        System.out.print("Idioma: ");
                        String novoIdioma = entrada.nextLine();
                
                        System.out.print("Data de estreia (dd/MM/yyyy): ");
                        String novaDataStr = entrada.nextLine();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date novaData = sdf.parse(novaDataStr);
                
                        System.out.print("Empresas (separadas por vírgula): ");
                        String empresasStr = entrada.nextLine();
                        ArrayList<String> novasEmpresas = new ArrayList<>(Arrays.asList(empresasStr.split(",")));
                
                        // Criar uma nova instância de Serie com os dados fornecidos pelo usuário
                        Serie novaSerie = new Serie(novoId, novoNome, novoIdioma, novaData, novasEmpresas);
                        byte[] novoBa = novaSerie.toByteArray();
                
                        // Abrir o arquivo em modo de escrita/append
                        RandomAccessFile arqq = new RandomAccessFile("dados/series.db", "rw");
                        arqq.seek(arqq.length()); // Move para o final do arquivo
                
                        // Escrever a lápide, tamanho do registro e os dados da série no final do arquivo
                        arqq.writeBoolean(true); // Lápide ativa
                        arqq.writeInt(novoBa.length); // Tamanho do registro
                        arqq.write(novoBa); // Escreve os dados da série
                
                        System.out.println("Nova série criada com sucesso no final do arquivo.");
                        arqq.close();
                
                    } catch (IOException | ParseException e) {
                        System.out.println("Erro ao criar novo registro: " + e.getMessage());
                    }
                }
                 
                else{
                    // caso os registros das series ainda nao tenha sido criado, ele escreve no arquivo sequencial todas elas (faz o carregamento de td) e depois permite a criaçao de novos registros
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
                        // Solicitar os dados do usuário para o novo registro
                        System.out.println("Digite os atributos da nova série:");
                        System.out.print("ID: ");
                        int novoId = entrada.nextInt();
                        entrada.nextLine(); // Consumir a nova linha
                
                        System.out.print("Nome: ");
                        String novoNome = entrada.nextLine();
                
                        System.out.print("Idioma: ");
                        String novoIdioma = entrada.nextLine();
                
                        System.out.print("Data de estreia (dd/MM/yyyy): ");
                        String novaDataStr = entrada.nextLine();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date novaData = sdf.parse(novaDataStr);
                
                        System.out.print("Empresas (separadas por vírgula): ");
                        String empresasStr = entrada.nextLine();
                        ArrayList<String> novasEmpresas = new ArrayList<>(Arrays.asList(empresasStr.split(",")));
                
                        // Criar uma nova instância de Serie com os dados fornecidos pelo usuário
                        Serie novaSerie = new Serie(novoId, novoNome, novoIdioma, novaData, novasEmpresas);
                        byte[] novoBa = novaSerie.toByteArray();
                
                        // Abrir o arquivo em modo de escrita/append
                        RandomAccessFile arqq = new RandomAccessFile("dados/series.db", "rw");
                        arqq.seek(arqq.length()); // Move para o final do arquivo
                
                        // Escrever a lápide, tamanho do registro e os dados da série no final do arquivo
                        arqq.writeBoolean(true); // Lápide ativa
                        arqq.writeInt(novoBa.length); // Tamanho do registro
                        arqq.write(novoBa); // Escreve os dados da série
                
                        System.out.println("Nova série criada com sucesso no final do arquivo.");
                        arqq.close();
                
                    } catch (IOException | ParseException e) {
                        System.out.println("Erro ao criar novo registro: " + e.getMessage());
                    }


                } 
             }
             else if(operacao == 6){
                try {
                    // 1. Distribui os registros do arquivo original em arquivos temporários
                    int tamanhoMemoria = 100;
                    distribuirSeriesParaArquivos("dados/series.db", tamanhoMemoria);
    
                    // 2. Faz a intercalação dos arquivos temporários gerados
                    String nomeArquivoOrdenado = "dados/series_ordenadas.db";

                    int numeroDeArquivos = contarArquivosTemporarios(); // Quantos arquivos temporários foram gerados na fase de distribuição
                    intercalarArquivos(numeroDeArquivos, nomeArquivoOrdenado);
    
                    System.out.println("Arquivo ordenado com sucesso!");
                } catch (IOException e) {
                    System.out.println("Erro ao ordenar o arquivo: " + e.getMessage());
                }
            } else {
                operacao = 7;
            } 

            System.out.println("\nQual operação deseja fazer?");
            System.out.println("-Para carregar o arquivo digite 1");
            System.out.println("-Para ler do arquivo digite 2");
            System.out.println("-Para atualizar um registro do arquivo digite 3");
            System.out.println("-Para excluir um registro digite 4");
            System.out.println("-Para criar um registro digite 5"); 
            System.out.println("-Para ordenar um registro digite 6"); 
            System.out.println("-Para sair digite 7 ou qualquer outro numero maior");
            operacao = entrada.nextInt();

        }
        entrada.close();
    }
    public static int contarArquivosTemporarios() {
        int contador = 0;
        while (true) {
            File arquivo = new File("bloco" + contador + ".tmp");
            if (arquivo.exists()) {
                contador++;
            } else {
                break;
            }
        }
        return contador;
    }

    public static void distribuirSeriesParaArquivos(String nomeArquivoEntrada, int tamanhoMemoria) throws IOException {
        RandomAccessFile entrada = new RandomAccessFile(nomeArquivoEntrada, "r");
        int contadorArquivo = 0;
    
        while (entrada.getFilePointer() < entrada.length()) {
            ArrayList<Serie> listaSeries = new ArrayList<>();
            
            // Carrega os registros na memória até o limite especificado
            for (int i = 0; i < tamanhoMemoria && entrada.getFilePointer() < entrada.length(); i++) {
                boolean lapide = entrada.readBoolean();
                int tamanhoRegistro = entrada.readInt();
                byte[] ba = new byte[tamanhoRegistro];
                entrada.readFully(ba);
    
                if (lapide) {
                    Serie serie = new Serie();
                    serie.fromByteArray(ba);
                    listaSeries.add(serie);
                }
            }
    
            // Ordena o bloco de séries carregadas na memória por ID
            listaSeries.sort(Comparator.comparingInt(Serie::getId));
    
            // Grava o bloco ordenado em um novo arquivo temporário
            String nomeArquivoTemporario = "bloco" + contadorArquivo + ".tmp";
            RandomAccessFile tempFile = new RandomAccessFile(nomeArquivoTemporario, "rw");
    
            for (Serie serie : listaSeries) {
                byte[] ba = serie.toByteArray();
                tempFile.writeBoolean(true); // Lápide
                tempFile.writeInt(ba.length); // Tamanho do registro
                tempFile.write(ba); // Registro da série
            }
    
            tempFile.close();
            contadorArquivo++;
        }
    
        entrada.close();
    }

    public static void intercalarArquivos(int numeroDeArquivos, String arquivoSaida) throws IOException {
    // Array de RandomAccessFiles para cada arquivo temporário
    RandomAccessFile[] arquivos = new RandomAccessFile[numeroDeArquivos];
    for (int i = 0; i < numeroDeArquivos; i++) {
        arquivos[i] = new RandomAccessFile("bloco" + i + ".tmp", "r");
    }

    // Arquivo de saída final
    RandomAccessFile saida = new RandomAccessFile(arquivoSaida, "rw");

    // Array para armazenar o último registro lido de cada arquivo
    Serie[] seriesCorrentes = new Serie[numeroDeArquivos];
    boolean[] lapides = new boolean[numeroDeArquivos];
    byte[][] buffers = new byte[numeroDeArquivos][];

    // Carrega o primeiro registro de cada arquivo temporário
    for (int i = 0; i < numeroDeArquivos; i++) {
        if (arquivos[i].getFilePointer() < arquivos[i].length()) {
            lapides[i] = arquivos[i].readBoolean();
            int tamanhoRegistro = arquivos[i].readInt();
            buffers[i] = new byte[tamanhoRegistro];
            arquivos[i].readFully(buffers[i]);

            if (lapides[i]) {
                seriesCorrentes[i] = new Serie();
                seriesCorrentes[i].fromByteArray(buffers[i]);
            }
        }
    }

    // Enquanto houver registros em qualquer arquivo temporário
    while (true) {
        int indiceMenor = -1;
        Serie menorSerie = null;

        // Encontra o menor registro entre os arquivos
        for (int i = 0; i < numeroDeArquivos; i++) {
            if (seriesCorrentes[i] != null && (menorSerie == null || seriesCorrentes[i].getId() < menorSerie.getId())) {
                menorSerie = seriesCorrentes[i];
                indiceMenor = i;
            }
        }

        // Se não houver mais registros para intercalar, termina
        if (indiceMenor == -1) {
            break;
        }

        // Escreve o menor registro no arquivo de saída
        byte[] ba = menorSerie.toByteArray();
        saida.writeBoolean(true); // Lápide
        saida.writeInt(ba.length); // Tamanho do registro
        saida.write(ba); // Registro da série

        // Carrega o próximo registro do arquivo que tinha o menor
        if (arquivos[indiceMenor].getFilePointer() < arquivos[indiceMenor].length()) {
            lapides[indiceMenor] = arquivos[indiceMenor].readBoolean();
            int tamanhoRegistro = arquivos[indiceMenor].readInt();
            buffers[indiceMenor] = new byte[tamanhoRegistro];
            arquivos[indiceMenor].readFully(buffers[indiceMenor]);

            if (lapides[indiceMenor]) {
                seriesCorrentes[indiceMenor] = new Serie();
                seriesCorrentes[indiceMenor].fromByteArray(buffers[indiceMenor]);
            } else {
                seriesCorrentes[indiceMenor] = null;
            }
        } else {
            seriesCorrentes[indiceMenor] = null;
        }
    }

    // Fecha os arquivos temporários e o arquivo de saída
    for (RandomAccessFile arquivo : arquivos) {
        arquivo.close();
    }
    saida.close();
}
}
