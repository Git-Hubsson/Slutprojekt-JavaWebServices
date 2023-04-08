import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {

        //Skapa JSON-objekt för 4 befintliga personer
        int id = 0;
        JSONObject data = new JSONObject();
        data.put("ID: " + id++, createPerson("Åke", 78));
        data.put("ID: " + id++, createPerson("Ulla", 80));
        data.put("ID: " + id++, createPerson("John", 10));
        data.put("ID: " + id++, createPerson("Rodriguez", 22));

        //Initiera serversocket, input/output streams och buffered reader/writer
        ServerSocket serverSocket;
        Socket clientSocket;
        BufferedReader bReader;

        InputStreamReader inputSR;
        OutputStreamWriter outputSW;
        BufferedWriter bWriter;

        //Starta servern
        try {
            serverSocket = new ServerSocket(1337);
            System.out.println("Servern är nu igång");
        } catch (IOException e) {
            System.out.println(e);
            return;
        }

        try {
            //Vänta på att en klient ska ansluta och skriv ut klientens information
            clientSocket = serverSocket.accept();
            System.out.println("En klient har anslutit. " + clientSocket);

            //Skapa input/output streams och buffered reader/writer
            inputSR = new InputStreamReader(clientSocket.getInputStream());
            outputSW = new OutputStreamWriter(clientSocket.getOutputStream());

            bReader = new BufferedReader(inputSR);
            bWriter = new BufferedWriter(outputSW);

            while (true) {
                //Lyssna efter request, sedan parsa requesten och spara den som ett JSON-objekt
                JSONObject request = (JSONObject) new JSONParser().parse(bReader.readLine());
                //Hämta variabler från request-objektet
                System.out.println("Tog emot request: " + request);
                String httpMethod = request.get("HTTPMethod").toString();
                String contentType = request.get("ContentType").toString();


                //Lägg till en ny person i JSON-objektet data
                if (httpMethod.equals("POST") && contentType.equalsIgnoreCase("application/json")) {
                    JSONObject personData = (JSONObject) request.get("PersonData");
                    saveData(personData, data, id, bWriter);
                    id++;
                }

                //Hämta personinfo
                if (httpMethod.equals("GET") && contentType.equalsIgnoreCase("application/json")) {
                    String parameter = request.get("URLParameter").toString();

                    if (parameter.equalsIgnoreCase("allt")) {
                        clientOutput(bWriter, data);
                        //Loopa igenom data-objektet och skicka personinfo till klienten som matchar sökningen
                    } else {
                        for (int i = 0; i < data.size(); i++) {
                            JSONObject persons = (JSONObject) data.get("ID: " + i);
                            String namn = (String) persons.get("name");
                            if (parameter.equalsIgnoreCase(namn)) {
                                JSONObject person = new JSONObject();
                                person.put("ID: " + i, persons);
                                clientOutput(bWriter, person);
                                break;
                            } else if (i == data.size() - 1 && !parameter.equalsIgnoreCase(namn)) {
                                clientOutput(bWriter, "Personen finns inte i registret");
                            }
                        }
                    }
                }

                //Avslutar om klienten begär det
                if (httpMethod.equalsIgnoreCase("quit")) {
                    clientOutput(bWriter, "Servern avslutas");
                    break;
                }
            }

            clientSocket.close();
            inputSR.close();
            outputSW.close();
            bReader.close();
            bWriter.close();

        } catch (IOException e) {
            System.out.println(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("Server Avslutas");
        }
    }

    //Lägg till emottagen information i data-filen
    static void saveData(JSONObject personData, JSONObject data, int id, BufferedWriter bWriter) throws IOException, ParseException {
        data.put("ID: " + id, personData);
        clientOutput(bWriter, "Följande information har sparats: " + "\"ID: " + id + "\"" + personData);
    }

    //Skapa ny person. Används primärt för att korta ner koden när servern skapar personer till data-filen
    static JSONObject createPerson(String name, int age) {
        JSONObject person = new JSONObject();
        person.put("name", name);
        person.put("age", age);
        return person;
    }

    //Skicka information till klienten
    static void clientOutput(BufferedWriter bWriter, JSONObject JSONObject) throws IOException {
        JSONObject response = new JSONObject();
        response.put("httpStatusCode", 200);
        response.put("ContentType", "application/json");
        response.put("Body", JSONObject);
        bWriter.write(response.toJSONString());
        bWriter.newLine();
        bWriter.flush();
    }

    static void clientOutput(BufferedWriter bWriter, String string) throws IOException {
        bWriter.write(string);
        bWriter.newLine();
        bWriter.flush();
    }
}