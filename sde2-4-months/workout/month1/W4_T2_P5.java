
import java.util.*;

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int x) {
        val = x;
    }
}


public class W4_T2_P5 {


    static void dfs(TreeNode root){
        if(root==null) return;
        System.out.println(root.val);
        dfs(root.left);
        dfs(root.right);

    }


    //
    static void bfs(TreeNode root){
        Queue<TreeNode> q = new LinkedList<>();
        List<List<Integer>> result = new ArrayList<>();
        q.offer(root);

        while(!q.isEmpty()){
            int k = q.size();
            List<Integer> currentLevel = new ArrayList<>();
            result.add(new ArrayList<>());
            while(k-- > 0 ){
                var node = q.poll();
                currentLevel.add(node.val);
                if(node.left!=null){
                    q.offer(node.left);
                }
                if(node.right!=null){
                    q.offer(node.right);
                }
            }
            result.add(currentLevel);
        }

        result.forEach(row-> {
            row.forEach(val -> System.out.print(val + " "));
            System.out.println("");
        });



    }
    public static void main(String[] args) {


        

         // Valid BST:
        //       4
        //      / \
        //     2   5
        //    / \
        //   1   3
        TreeNode root1 = new TreeNode(4);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(5);
        root1.left.left = new TreeNode(1);
        root1.left.right = new TreeNode(3);
        System.out.println("Tree 1 DFS ");
        dfs(root1);
        System.out.println("Tree 1 BFS");
        bfs(root1); // true
        
    }
}
