package pl.mkan;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class CavesAppTest {

    @Test
    public void testExample() throws IOException {
        String input = """
                8 5
                1 3 0
                3 2 0
                7 3 1
                7 2 0
                8 7 0
                1 8 0
                6 8 0
                6 4 0
                6 5 1
                5 4 0
                2 4 0
                5 1 0
                """;
        String expectedOutput = "1 5 4 6 8 7 2 3";

        BufferedReader br = new BufferedReader(new StringReader(input));
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        CavesApp.solve(br, pw);
        pw.flush();

        String output = sw.toString().trim();
        assertEquals(expectedOutput, output);
    }
}