 // static void dfs(int[][] matrix) {
    //     m = matrix.length;
    //     n = matrix[0].length;
    //     boolean[][] vis = new boolean[m][n];

    //     // default start with {0,0}
    //     Deque<Pair<Integer, Integer>> stack = new ArrayDeque<>();
    //     stack.push(new Pair<>(0, 0));
    //     vis[0][0] = true;

    //     while (!stack.isEmpty()) {
    //         var top = stack.pop();
    //         int a = top.first();
    //         int b = top.second();
    //         System.out.println(matrix[a][b]);

    //         for (int[] d : DIRS) {
    //             int x = a + d[0];
    //             int y = b + d[1];
    //             if (isValid(x, y) && !vis[x][y]) {
    //                 vis[x][y] = true;
    //                 stack.push(new Pair<>(x, y));
    //                 // break;
    //             }
    //         }

    //     }
    // }