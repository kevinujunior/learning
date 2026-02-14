
public class DSU {
    int[] rank, parent;
    int n;

    // Constructor
    public DSU(int n) {
        rank = new int[n];
        parent = new int[n];
        this.n = n;
        for (int i = 0; i < n; i++) {
            // Initially, all elements are in
            // their own set.
            parent[i] = i;
        }
    }

    // Returns representative of x's set
    int find(int x) {

        // Path Compression -> flatten the tree by making every node directly point to root
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    // Unites the set that includes x and the set
    // that includes x
    void union(int x, int y) {
        // Find representatives of two sets
        int xRoot = find(x), yRoot = find(y);

        // Elements are in the same set, no need
        // to unite anything.
        if (xRoot == yRoot)
            return;

        // If x's rank is less than y's rank
        if (rank[xRoot] < rank[yRoot])

            // Then move x under y so that depth
            // of tree remains less
            parent[xRoot] = yRoot;

        // Else if y's rank is less than x's rank
        else if (rank[yRoot] < rank[xRoot])

            // Then move y under x so that depth of
            // tree remains less
            parent[yRoot] = xRoot;

        else // if ranks are the same
        {
            // Then move x under y (doesn't matter
            // which one goes where)
            parent[xRoot] = yRoot;

            // And increment the result tree's
            // rank by 1
            rank[yRoot] = rank[yRoot] + 1;
        }
    }

    public static void main(String[] args) {

    }
}
