import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {

        // Create JSON objects for 4 existing persons
        int id = 0;
        JSONObject data = new JSONObject();
        data.put("ID: " + id++, createPerson("Åke", 78));
        data.put("ID: " + id++, createPerson("Ulla", 80));
        data.put("ID: " + id++, createPerson("John", 10));
        data.put("ID: " + id++, createPerson("Rodriguez", 22));

        // Initialize server socket, input/output streams, and buffered reader/writer
        ServerSocket serverSocket;
        Socket clientSocket;
        BufferedReader bReader;

        InputStreamReader inputSR;
        OutputStreamWriter outputSW;
        BufferedWriter bWriter;

        // Start the server
        try {
            serverSocket = new ServerSocket(1337);
            System.out.println("Server is now running");
        } catch (IOException e) {
            System.out.println(e);
            return;
        }

        try {
            // Wait for a client to connect and print client information
            clientSocket = serverSocket.accept();
            System.out.println("A client has connected. " + clientSocket);

            // Create input/output streams and buffered reader/writer
            inputSR = new InputStreamReader(clientSocket.getInputStream());
            outputSW = new OutputStreamWriter(clientSocket.getOutputStream());

            bReader = new BufferedReader(inputSR);
            bWriter = new BufferedWriter(outputSW);

            while (true) {
                // Listen for a request, then parse the request and save it as a JSON object
                JSONObject request = (JSONObject) new JSONParser().parse(bReader.readLine());
                // Retrieve variables from the request object
                System.out.println("Received request: " + request);
                String httpMethod = request.get("HTTPMethod").toString();
                String contentType = request.get("ContentType").toString();


                // Add a new person to the JSON object data
                if (httpMethod.equals("POST") && contentType.equalsIgnoreCase("application/json")) {
                    JSONObject personData = (JSONObject) request.get("PersonData");
                    saveData(personData, data, id, bWriter);
                    id++;
                }

                // Retrieve person info
                if (httpMethod.equals("GET") && contentType.equalsIgnoreCase("application/json")) {
                    String parameter = request.get("URLParameter").toString();

                    if (parameter.equalsIgnoreCase("all")) {
                        clientOutput(bWriter, data);
                        // Loop through the data object and send person info to the client that matches the search
                    } else {
                        for (int i = 0; i < data.size(); i++) {
                            JSONObject persons = (JSONObject) data.get("ID: " + i);
                            String name = (String) persons.get("name");
                            if (parameter.equalsIgnoreCase(name)) {
                                JSONObject person = new JSONObject();
                                person.put("ID: " + i, persons);
                                clientOutput(bWriter, person);
                                break;
                            } else if (i == data.size() - 1 && !parameter.equalsIgnoreCase(name)) {
                                clientOutput(bWriter, "Person not found in the register");
                            }
                        }
                    }
                }

                // Terminate if the client requests it
                if (httpMethod.equalsIgnoreCase("quit")) {
                    clientOutput(bWriter, "Server is shutting down");
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
            System.out.println("Server Shutting Down");
        }
    }

    // Add received information to the data file
    static void saveData(JSONObject personData, JSONObject data, int id, BufferedWriter bWriter) throws IOException, ParseException {
        data.put("ID: " + id, personData);
        clientOutput(bWriter, "The following information has been saved: " + "\"ID: " + id + "\"" + personData);
    }

    // Create a new person. Primarily used to shorten the code when the server creates people for the data file
    static JSONObject createPerson(String name, int age) {
        JSONObject person = new JSONObject();
        person.put("name", name);
        person.put("age", age);
        return person;
    }

    // Send information to the client
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