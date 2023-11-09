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

        // Initialize sockets and streams
        Socket socket = null;
        InputStreamReader inputSR = null;
        OutputStreamWriter outputSW = null;
        BufferedReader bReader = null;
        BufferedWriter bWriter = null;

        try {
            // Allow the user to enter URL and port
            System.out.println("To connect to the server handling personal data, please enter \"localhost\" as URL and port \"1337\".\n");
            System.out.println("Enter URL: ");
            Scanner scanner = new Scanner(System.in);
            String url = scanner.nextLine();
            System.out.println("Enter port: ");
            int port = scanner.nextInt();
            scanner.nextLine();

            // Connect to the server
            socket = new Socket(url, port);
            System.out.println("Connected to the server");
            System.out.println("");

            // Set up input and output stream
            inputSR = new InputStreamReader(socket.getInputStream());
            outputSW = new OutputStreamWriter(socket.getOutputStream());
            bReader = new BufferedReader(inputSR);
            bWriter = new BufferedWriter(outputSW);

            while (true) {
                // Ask the user what action to perform
                System.out.println("1. Type \"GET\" to retrieve information from the system");
                System.out.println("2. Type \"POST\" to send new information about a person to the system");
                System.out.println("3. Type \"QUIT\" to exit");
                String menuChoice = scanner.nextLine().toUpperCase();

                // Ask which data the user wants to send
                if (menuChoice.equals("POST")) {
                    JSONObject request = new JSONObject();
                    request.put("ContentType", "application/json");
                    request.put("HTTPMethod", "POST");
                    JSONObject person = new JSONObject();
                    System.out.println("Name: ");
                    String name = scanner.nextLine();
                    person.put("name", name);
                    System.out.println("Age: ");
                    String age = scanner.nextLine();
                    person.put("age", age);
                    request.put("PersonData", person);
                    serverOutput(bWriter, request);
                    // Print the response from the server
                    System.out.println(bReader.readLine());
                }

                // Ask which data the user wants to retrieve
                if (menuChoice.equals("GET")) {
                    System.out.println("1. Type \"all\" to show all data");
                    System.out.println("2. Enter the name of the person you want to retrieve information about");
                    String parameter = scanner.nextLine();
                    JSONObject request = new JSONObject();
                    request.put("ContentType", "application/json");
                    request.put("HTTPMethod", "GET");
                    request.put("URLParameter", parameter);
                    serverOutput(bWriter, request);
                    // Print the response from the server
                    JSONObject response = (JSONObject) new JSONParser().parse(bReader.readLine());
                    JSONObject data = (JSONObject) response.get("Body");
                    System.out.println(data);
                }

                // Exit the program
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
            // Close streams and socket when the program ends
            try {
                if (socket != null) socket.close();
                if (inputSR != null) inputSR.close();
                if (outputSW != null) outputSW.close();
                if (bWriter != null) bWriter.close();
                if (bReader != null) bReader.close();
            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println("Client Shutting Down");
        }
    }

    // Send a request to the server
    static void serverOutput (BufferedWriter bWriter, JSONObject JSONObject) throws IOException {
        bWriter.write(JSONObject.toJSONString());
        bWriter.newLine();
        bWriter.flush();
    }
}