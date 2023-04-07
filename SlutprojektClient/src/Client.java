import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {

        //Initiera sockets och streams
        Socket socket = null;
        InputStreamReader inputSR = null;
        OutputStreamWriter outputSW = null;
        BufferedReader bReader = null;
        BufferedWriter bWriter = null;

        try {
            //Låt användaren knappa in URL och port
            System.out.println("För att ansluta till servern som hanterar persondata, vänligen ange \"localhost\" som URL och port 1337.\n");
            System.out.println("Ange URL: ");
            Scanner scanner = new Scanner(System.in);
            String url = scanner.nextLine();
            System.out.println("Ange port");
            int port = scanner.nextInt();
            scanner.nextLine();

            //Anslut till servern
            socket = new Socket(url, port);
            System.out.println("Ansluten till servern");
            System.out.println("");

            //Sätt upp input och output stream
            inputSR = new InputStreamReader(socket.getInputStream());
            outputSW = new OutputStreamWriter(socket.getOutputStream());
            bReader = new BufferedReader(inputSR);
            bWriter = new BufferedWriter(outputSW);

            while (true) {
                //Fråga användaren vad som ska utföras
                System.out.println("Skriv GET för att hämta information från systemet");
                System.out.println("Skriv POST för att skicka ny information om en person till systemet");
                System.out.println("Skriv QUIT för att avsluta");
                String menuChoice = scanner.nextLine().toUpperCase();

                //Fråga vilken data användaren vill skicka
                if (menuChoice.equals("POST")) {
                    JSONObject request = new JSONObject();
                    request.put("ContentType", "application/json");
                    request.put("HTTPMethod", "POST");
                    JSONObject person = new JSONObject();
                    System.out.println("Namn: ");
                    String name = scanner.nextLine();
                    person.put("name", name);
                    System.out.println("Ålder: ");
                    String age = scanner.nextLine();
                    person.put("age", age);
                    request.put("PersonData", person);
                    serverOutput(bWriter, request);
                    //Skriver ut svaret från servern
                    System.out.println(bReader.readLine());
                }

                //Fråga vilken data användaren vill hämta
                if (menuChoice.equals("GET")) {
                    System.out.println("Skriv allt för att visa all data");
                    System.out.println("Skriv in namnet på personen du vill hämta information om");
                    String parameter = scanner.nextLine();
                    JSONObject request = new JSONObject();
                    request.put("ContentType", "application/json");
                    request.put("HTTPMethod", "GET");
                    request.put("URLParameter", parameter);
                    serverOutput(bWriter, request);
                    //Skriver ut svaret från servern
                    JSONObject response = (JSONObject) new JSONParser().parse(bReader.readLine());
                    JSONObject data = (JSONObject) response.get("Body");
                    System.out.println(data);
                }

                //Avsluta programmet
                if (menuChoice.equals("QUIT")) {
                    JSONObject request = new JSONObject();
                    request.put("ContentType", "application/json");
                    request.put("HTTPMethod", menuChoice);
                    serverOutput(bWriter, request);
                    System.out.println(bReader.readLine());
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            //Stäng av streams och socket när programmet avslutas
            try {
                if (socket != null) socket.close();
                if (inputSR != null) inputSR.close();
                if (outputSW != null) outputSW.close();
                if (bWriter != null) bWriter.close();
                if (bReader != null) bReader.close();
            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println("Client Avslutas");
        }
    }

    //Skicka en request till servern
    static void serverOutput (BufferedWriter bWriter, JSONObject JSONObject) throws IOException {
        bWriter.write(JSONObject.toJSONString());
        bWriter.newLine();
        bWriter.flush();
    }
}