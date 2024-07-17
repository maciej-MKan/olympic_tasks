package pl.mkan;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CavesApp {
    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader("CAV.IN"));
             PrintWriter pw = new PrintWriter(new FileWriter("CAV.OUT"))) {
            solve(br, pw);
            log.info("Files access pass");
        } catch (IOException e) {
            log.error("I/O Error: ", e);
        }
    }

    public static void solve(BufferedReader br, PrintWriter pw) throws IOException {
        String[] firstLine = br.readLine().split(" ");
        int n = Integer.parseInt(firstLine[0]);
        int k = Integer.parseInt(firstLine[1]);

        List<List<Edge>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < (3 * n) / 2; i++) {
            String[] line = br.readLine().split(" ");
            int a = Integer.parseInt(line[0]);
            int b = Integer.parseInt(line[1]);
            int c = Integer.parseInt(line[2]);
            graph.get(a).add(new Edge(b, c));
            graph.get(b).add(new Edge(a, c));
        }

        List<Integer> path = new ArrayList<>();
        boolean[] visited = new boolean[n + 1];
        path.add(1); // Start from the entrance
        visited[1] = true;

        PathResult result = new PathResult();
        dfs(1, graph, visited, path, 0, result, k);

        if (result.found) {
            for (int i = 0; i < result.bestPath.size(); i++) {
                if (i > 0) pw.print(" ");
                pw.print(result.bestPath.get(i));
            }
        } else {
            pw.print("NO");
        }
    }

    private static void dfs(int node, List<List<Edge>> graph, boolean[] visited, List<Integer> path, int difficultCount, PathResult result, int k) {
        if (path.size() == graph.size() - 1) {
            if (node == 1 && (result.bestPath.isEmpty() || difficultCount < result.minDifficult)) {
                result.bestPath = new ArrayList<>(path);
                result.minDifficult = difficultCount;
                result.found = true;
            }
            return;
        }

        for (Edge edge : graph.get(node)) {
            if (!visited[edge.to] && (path.size() < k || edge.to > k)) {
                visited[edge.to] = true;
                path.add(edge.to);
                dfs(edge.to, graph, visited, path, difficultCount + edge.cost, result, k);
                path.remove(path.size() - 1);
                visited[edge.to] = false;
            }
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

    static class PathResult {
        List<Integer> bestPath = new ArrayList<>();
        int minDifficult = Integer.MAX_VALUE;
        boolean found = false;
    }
}