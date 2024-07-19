package pl.mkan;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class CavesApp {
    static int n, k;
    static List<Edge>[] graph;
    static List<Integer> path;
    static boolean[] visited;
    static int minDifficulty;
    static boolean foundZeroDifficultyPath;

    @SuppressWarnings("unchecked")
    public static void solve(BufferedReader br, PrintWriter pw) throws IOException {
        String[] firstLine = br.readLine().split(" ");
        n = Integer.parseInt(firstLine[0]);
        k = Integer.parseInt(firstLine[1]);

        graph = (List<Edge>[]) new ArrayList[n + 1];
        for (int i = 0; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int i = 0; i < (3 * n) / 2; i++) {
            String[] line = br.readLine().split(" ");
            int startNode = Integer.parseInt(line[0]);
            int endNode = Integer.parseInt(line[1]);
            int difficulty = Integer.parseInt(line[2]);
            graph[startNode].add(new Edge(endNode, difficulty));
            graph[endNode].add(new Edge(startNode, difficulty));
        }

        for (int i = 1; i <= n; i++) {
            graph[i].sort(Comparator.comparingInt(edge -> edge.cost));
        }

        visited = new boolean[n + 1];
        path = new ArrayList<>();
        minDifficulty = Integer.MAX_VALUE;
        List<Integer> currentPath = new ArrayList<>();
        dfs(1, 0, currentPath);

        if (path.isEmpty()) {
            pw.println("No valid path");
        } else {
            for (int i = 0; i < path.size(); i++) {
                if (i > 0) pw.print(" ");
                pw.print(path.get(i));
            }
        }
    }

    private static void dfs(int node, int difficulty, List<Integer> currentPath) {
        if (foundZeroDifficultyPath) {
            return;
        }
        visited[node] = true;
        currentPath.add(node);

        if (currentPath.size() == n) {
            log.info("Checked path {} with difficulty {}", currentPath, difficulty);
            if (difficulty < minDifficulty) {
                minDifficulty = difficulty;
                path = new ArrayList<>(currentPath);
                if (difficulty == 0) {
                    foundZeroDifficultyPath = true;
                    log.info("Found zero difficulty path, no need to check more paths");
                }
            }
        } else {
            for (Edge edge : graph[node]) {
                if (!visited[edge.to]) {
                    dfs(edge.to, difficulty + edge.cost, currentPath);
                }
            }
        }

        visited[node] = false;
        currentPath.remove(currentPath.size() - 1);
    }

    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader("CAV.IN"));
             PrintWriter pw = new PrintWriter(new FileWriter("CAV.OUT"))) {
            solve(br, pw);
        } catch (IOException e) {
            log.error("File reading/writing error", e);
        }
    }

    static class Edge {
        int to;
        int cost;

        Edge(int to, int cost) {
            this.to = to;
            this.cost = cost;
        }
    }
}