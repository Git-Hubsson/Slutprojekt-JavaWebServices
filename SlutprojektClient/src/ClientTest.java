import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {
    @Test
    public void testServerOutput() throws IOException {
        // Skapa ett JSONObjekt
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Anton");
        jsonObject.put("age", 29);

        // Skapa en StringWriter och en BufferedWriter
        StringWriter stringWriter = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(stringWriter);

        // Kalla på serverOutput metoden med JSONObjektet och BufferedWriter
        serverOutput(bufferedWriter, jsonObject);

        // Kontrollera att outputen matchar det förväntade resultatet
        assertEquals("{\"name\":\"Anton\",\"age\":29}", stringWriter.toString().trim());
    }
    static void serverOutput(BufferedWriter bWriter, JSONObject jsonObject) throws IOException {
        bWriter.write(String.valueOf(jsonObject));
        bWriter.newLine();
        bWriter.flush();
    }
}
