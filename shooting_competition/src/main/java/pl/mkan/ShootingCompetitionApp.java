package pl.mkan;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class ShootingCompetitionApp {
    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader("SHO.IN"));
             PrintWriter pw = new PrintWriter(new FileWriter("SHO.OUT"))) {
            solve(br, pw);
            log.info("Files access pass");
        } catch (IOException e) {
            log.error("I/O Error: ", e);
        }
    }

    public static void solve(BufferedReader br, PrintWriter pw) throws IOException {
        int numBlocks = Integer.parseInt(br.readLine().trim());

        for (int b = 0; b < numBlocks; b++) {
            String[] dimensions = br.readLine().trim().split(" ");
            int r = Integer.parseInt(dimensions[0]);
            int c = Integer.parseInt(dimensions[1]);

            int[][] whitePositions = new int[c][2];
            for (int i = 0; i < c; i++) {
                String[] positions = br.readLine().trim().split(" ");
                whitePositions[i][0] = Integer.parseInt(positions[0]) - 1;
                whitePositions[i][1] = Integer.parseInt(positions[1]) - 1;
            }

            int[] result = findCorrectSeries(r, c, whitePositions);
            if (result == null) {
                pw.println("NO");
            } else {
                for (int i = 0; i < result.length; i++) {
                    if (i > 0) pw.print(" ");
                    pw.print(result[i] + 1);
                }
                pw.println();
            }
        }
    }

    private static int[] findCorrectSeries(int r, int c, int[][] whitePositions) {
        int[] result = new int[c];
        boolean[] rowUsed = new boolean[r];
        return backtrack(whitePositions, result, rowUsed, 0) ? result : null;
    }

    private static boolean backtrack(int[][] whitePositions, int[] result, boolean[] rowUsed, int col) {
        if (col == whitePositions.length) {
            return true;
        }

        for (int i = 0; i < 2; i++) {
            int row = whitePositions[col][i];
            if (!rowUsed[row]) {
                rowUsed[row] = true;
                result[col] = row;
                if (backtrack(whitePositions, result, rowUsed, col + 1)) {
                    return true;
                }
                rowUsed[row] = false;
            }
        }
        return false;
    }
}