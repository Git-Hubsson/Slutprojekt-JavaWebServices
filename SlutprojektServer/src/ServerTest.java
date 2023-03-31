import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    @Test
    public void testCreatePerson() {
        JSONObject expectedPerson = new JSONObject();
        expectedPerson.put("name", "John");
        expectedPerson.put("age", 35);

        JSONObject actualPerson = Server.createPerson("John", 35);

        assertEquals(expectedPerson, actualPerson);
    }
    @Test
    public void testSaveData() throws IOException, ParseException {
        JSONObject personData = new JSONObject();
        personData.put("name", "Anton");
        personData.put("age", 29);
        JSONObject data = new JSONObject();
        int id = 1;
        StringWriter writer = new StringWriter();
        BufferedWriter bWriter = new BufferedWriter(writer);

        Server.saveData(personData, data, id, bWriter);

        String expected = "Följande information har sparats: {\"name\":\"Anton\",\"age\":29}";
        assertEquals(expected, writer.toString().trim());
        assertEquals(1, data.size());
        assertEquals(personData, data.get("ID: 1"));
    }
}