
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int x) {
        val = x;
    }
}


public class W4_T2_P4 {

    static TreeNode getMirror(TreeNode root){
        if(root==null) return root;

        
        TreeNode temp = root.left;
        root.left = root.right;
        root.right = temp;

        getMirror(root.left);
        getMirror(root.right);
        
        return root;
        
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
        // System.out.println("Tree 1 is a BST (Bounds Method): " + isValidBST(root1)); // true
        System.out.println("Tree 1 is a BST (Inorder Method): " + getMirror(root1)); // true

        
    }
}
