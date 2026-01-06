
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int x) {
        val = x;
    }
}


public class W4_T2_P3 {

    private static TreeNode prev;

    static boolean isValid(TreeNode root){
        if(root==null) return true;
        
        if (!isValid(root.left)) return false;

    
        if(prev!=null && prev.val>=root.val) return false;
        
        prev = root;

        return isValid(root.right);
    }

    static boolean isvalidBSTInorder(TreeNode root){
        prev = null;
        return isValid(root);    
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
        System.out.println("Tree 1 is a BST (Inorder Method): " + isvalidBSTInorder(root1)); // true


        // Invalid BST: (5 is in left subtree of 4, but is > 4)
        //       4
        //      / \
        //     2   5
        //    / \
        //   1   5
        TreeNode root2 = new TreeNode(4);
        root2.left = new TreeNode(2);
        root2.right = new TreeNode(5);
        root2.left.left = new TreeNode(1);
        root2.left.right = new TreeNode(5); // Invalid element
        // System.out.println("Tree 2 is a BST (Bounds Method): " + checker.isValidBST(root2)); // false
        System.out.println("Tree 2 is a BST (Inorder Method): " + isvalidBSTInorder(root2)); // false
        
    }
}
