import java.util.*;

public class DFS {

    static int m, n;

    public record Pair<A, B>(A first, B second) {
    };

    // static int[][] DIRS = {
    //         { 0, 1 }, // right
    //         { 1, 0 }, // down
    //         { 0, -1 }, // left
    //         { -1, 0 } // up
    // };

    // read aesthetically in a matrix 
    //Right, left, up, down
    static int[][] DIRS = {
        {1, 0}, {-1, 0}, {0, 1}, {0, -1}
    };

    static boolean isValid(int i, int j) {
        return i >= 0 && i < m && j >= 0 && j < n;
    }


    // use recursive dfs more intutive
    static void dfs(int[][] matrix,int startx, int starty, boolean[][] vis) {
    

        // default start with {0,0}
        Deque<Pair<Integer, Integer>> stack = new ArrayDeque<>();
        stack.push(new Pair<>(0, 0));
        vis[0][0] = true;

        while (!stack.isEmpty()) {
            var top = stack.pop();
            int a = top.first();
            int b = top.second();
            System.out.println(matrix[a][b]);

            for (int[] d : DIRS) {
                int x = a + d[0];
                int y = b + d[1];
                if (isValid(x, y) && !vis[x][y]) {
                    vis[x][y] = true;
                    stack.push(new Pair<>(x, y));
                    break;
                }
            }

        }
    }

    static void dfsRec(int[][] matrix, int a, int b, boolean[][] vis){
        vis[a][b] = true;
        System.out.println(matrix[a][b]);
        for (int[] d : DIRS) {
            int x = a + d[0];
            int y = b + d[1];
            if (isValid(x, y) && !vis[x][y]) {
                dfsRec(matrix,x,y,vis);
            }
        }
    }

 
  

    public static void main(String[] args) {
        // int[][] mat = new int[][] {
        //         { 1, 2, 3, 4 },
        //         { 5, 6, 7, 8 },
        //         { 9, 10, 11, 12 },
        //         { 13, 14, 15, 16 }
        // };

        int[][] mat = new int[][] {
                { 1, 2, 3, 4 },
                { 5, 6, 7, 8 },
                { 9, 10, 11, 12 },
                { 13, 14, 15, 16 }
        };

        System.out.println(Arrays.deepToString(mat));
        m = mat.length;
        n = mat[0].length;
        boolean[][] vis = new boolean[m][n];
        // dfs(mat,0,0,vis);
        dfsRec(mat,0,0,vis);
    }

}
