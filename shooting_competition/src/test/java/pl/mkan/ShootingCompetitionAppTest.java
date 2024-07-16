package pl.mkan;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class ShootingCompetitionAppTest {

    @Test
    void testAccordingToExample() throws IOException {
        String input = """
                2
                4 4
                2 4
                3 4
                1 3
                1 4
                5 5
                1 5
                2 4
                3 4
                2 4
                2 3
                """;

        String expectedOutput = "2 3 1 4\nNO\n";

        BufferedReader br = new BufferedReader(new StringReader(input));
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        ShootingCompetitionApp.solve(br, pw);
        pw.flush();

        assertEquals(expectedOutput, sw.toString());
    }
}