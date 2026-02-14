import java.util.*;

public class BFS {
    static int m,n;
    public record Pair<A, B>(A first, B second) {};

    static int[][] DIRS = {
        {0, 1},   // right
        {1, 0},   // down
        {0, -1},  // left
        {-1, 0}   // up
    };

    static boolean isValid(int i, int j){
        return i>=0 && i<m && j>=0 && j<n;
    }

    static void bfs(int[][] matrix){
        m = matrix.length;
        n = matrix[0].length;
        boolean[][] vis = new boolean[m][n];

        //default start with {0,0}
        Deque<Pair<Integer,Integer>> q = new ArrayDeque<>();
        q.offerLast(new Pair<>(0,0));
        vis[0][0] = true;
        
        while(!q.isEmpty()){
            int k = q.size();
            while(k-- > 0){
                var front = q.pollFirst();
                int a = front.first();
                int b = front.second();
                
                System.out.println(matrix[a][b]);
                
                for(int[] d: DIRS){
                    int x = a + d[0];
                    int y = b + d[1];
                    if(isValid(x, y) && !vis[x][y]){
                        vis[x][y] = true;
                        q.offerLast(new Pair<>(x,y));
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        int[][] mat = new int[][]{
            {1,2,3},
            {4,5,6},
            {7,8,9}
        };

        System.out.println(Arrays.deepToString(mat));

        bfs(mat);
    }
    
}
