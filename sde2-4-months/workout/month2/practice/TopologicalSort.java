import java.util.*;


public class TopologicalSort {

    List<List<Integer>> adj;
    int V ;

    TopologicalSort(int V){
        this.V = V;
        adj = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
    }
    

    void addEdge(int u , int v){
        adj.get(u).add(v);
    }

    void dfs(int v, int[] vis, Deque<Integer> st){
        vis[v] = 1;

        for(int n : adj.get(v)){
            if(vis[n]==0){
                dfs(n,vis,st);
            }
            else if(vis[n]==1){
                throw new IllegalStateException("Cycle detected");
            }
        }

        vis[v] = 2;

        st.push(v);
    }

    void dfsTopoSort(){
        int[] vis  = new int[V];
        Deque<Integer> st = new ArrayDeque<>();
        
        for (int i = 0; i < V; i++) {
            if(vis[i]==0){
                dfs(i,vis,st);
            }   
        }


        List<Integer> order = new ArrayList<>();

        while(!st.isEmpty()){
            Integer num = st.pop();
            order.add(num);
        }

        System.out.println(order);
    
    }

    public static void main(String[] args) {
        int V = 5;

        TopologicalSort tp = new TopologicalSort(V);
        tp.addEdge(0,1);
        tp.addEdge(2,1);
        tp.addEdge(3,2);
        tp.addEdge(4,2);
        tp.dfsTopoSort();

    }

    
}
