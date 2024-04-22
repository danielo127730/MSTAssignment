import java.io.*;

class graphEdge{
    public int v1;
    public int v2;
    public int wgt;
}

class Graph {
    
    
    // V = number of vertices
    // E = number of edges
    // adj[] is the adjacency lists array
    private int V, E;
    private Node[] adj;
    private Node z;

    private boolean[] visited;
    private int[] rootNodes;

    
    
    // default constructor
    public Graph(String graphFile)  throws IOException
    {
        int u, v;
        int e, wgt;

        FileReader fr = new FileReader(graphFile);
		BufferedReader reader = new BufferedReader(fr);
	           
        String splits = " +";  // multiple whitespace as delimiter
		String line = reader.readLine();        
        String[] parts = line.split(splits);
        System.out.println("Parts[] = " + parts[0] + " " + parts[1]);
        
        V = Integer.parseInt(parts[0]);
        E = Integer.parseInt(parts[1]);
        
        // create sentinel node
        z = new Node(); 
        z.next = z;
        
        // create adjacency lists, initialised to sentinel node z       
        adj = new Node[V+1];        
        for(v = 1; v <= V; ++v)
            adj[v] = z;               
        
       // read the edges
        System.out.println("Reading edges from text file");
        for(e = 1; e <= E; ++e)
        {
            line = reader.readLine();
            parts = line.split(splits);
            u = Integer.parseInt(parts[0]);
            v = Integer.parseInt(parts[1]); 
            wgt = Integer.parseInt(parts[2]);
            
            System.out.println("Edge " + toChar(u) + "--(" + wgt + ")--" + toChar(v));   

           
            //Code to add to adjacency list
            addEdge(u, v, wgt); 
            
        }	       
        System.out.println("\n");
    }

    //Second constructor to create adj graph from list of edges
    private Graph(graphEdge edges[],int numV)
    {
        // create sentinel node
        z = new Node(); 
        z.next = z;
        
        V = numV;
        
        // create adjacency lists, initialised to sentinel node z       
        adj = new Node[V+1];        
        for(int v = 1; v <= V; ++v)
            adj[v] = z; 

        //For each edge
        for(int i = 0;i<edges.length;i++){
            if(edges[i] == null){
                break;
            }
            Node node = new Node();
            //If v1->v2 not in list then add to list
            if(!edgeAlreadyExists(adj,edges[i].v1,edges[i].v2)){
                node = new Node();
                node.vert = edges[i].v2;
                node.wgt = edges[i].wgt;
                node.next = adj[edges[i].v1];

                adj[edges[i].v1] = node;
            }
            //If v2->v1 not in list then add to list
            if(!edgeAlreadyExists(adj,edges[i].v2,edges[i].v1)){
                node = new Node();
                node.vert = edges[i].v1;
                node.wgt = edges[i].wgt;
                node.next = adj[edges[i].v2];

                adj[edges[i].v2] = node;
            }
        }
    }


    private graphEdge[] adjGraphToEdgeArray(){
        graphEdge[] edges = new graphEdge[E];
        /*
        for(int i=0;i<E;i++){
            edges[i].v1 = 0;
            edges[i].v2 = 0;
            edges[i].wgt = 0;
        }
         */
        int count = 0;
        for(int i = 1; i<V+1;i++){
            int v1 = i;
            Node curr = new Node();
            curr = adj[v1];
            while(curr != z){
                int v2 = curr.vert;
                if(!edgeAlreadyExists(edges,v1, v2)){

                    int wgt = curr.wgt;
                    graphEdge edge = new graphEdge();
                    edge.v1 = v1;
                    edge.v2 = v2;
                    edge.wgt = wgt;
                    
                    edges[count] = edge;
                    count++;
                }
                curr = curr.next;
            }
        }
        return edges;
    }
    public Graph kruskal(){
        //Keeps track of which root nodes for each vertex
        rootNodes = new int[V+1];
        visited = new boolean[V+1];
        graphEdge[] edges2 = new graphEdge[E+1];
        int count = 0;
        graphEdge[] edges = adjGraphToEdgeArray();

        while(!allNodesVisited(visited)){
            graphEdge lowest = findLowestEdge(edges);
            edges = removeEdge(edges, lowest);
            if(!createsCycle(rootNodes, lowest)){
                System.out.println("Adding " + toChar(lowest.v1) + " -> " + toChar(lowest.v2));
                edges2[count] = lowest;
                visited[lowest.v1] = true;
                visited[lowest.v2] = true;
                count++;
                //edges = removeEdge(edges, lowest);

                //If neither has root node
                if(rootNodes[lowest.v1] == 0 && rootNodes[lowest.v2] == 0){
                    //System.out.println("Neither have rootNode");
                    //V1 root node is itself
                    rootNodes[lowest.v1] = lowest.v1;
                    //V2 root node is v1
                    rootNodes[lowest.v2] = lowest.v1;
                }
                else{
                    //V1 has no root node
                    if(rootNodes[lowest.v1] == 0){
                        //System.out.println("v2 has rootNode: " + rootNodes[lowest.v2]);
                        rootNodes[lowest.v1] = rootNodes[lowest.v2];
                    }
                    //v2 has no root node
                    else if(rootNodes[lowest.v2] == 0){
                        //System.out.println("v1 has rootNode: " + rootNodes[lowest.v1]);
                        rootNodes[lowest.v2] = rootNodes[lowest.v1];
                    }
                    //Both have root nodes
                    else{
                        //System.out.println("both have rootNode");
                        //System.out.println("v1 has rootNode: " + rootNodes[lowest.v1]);
                        //System.out.println("v2 has rootNode: " + rootNodes[lowest.v2]);
                        //If more nodes have rootNode[v1] then rootNode[v2]: rootNode[v2] = rootNode[v1]
                        int setLenV1 = getSetLength(rootNodes[lowest.v1]);
                        int setLenV2 =  getSetLength(rootNodes[lowest.v2]);
                        
                        //System.out.println("v1 setlen: " + setLenV1 );
                        //System.out.println("v2 setlen: " + setLenV2 );
                        if(setLenV1 > setLenV2){
                            changeRootNodes(rootNodes[lowest.v2],rootNodes[lowest.v1]);
                        }
                        else if(setLenV2 > setLenV1){
                            changeRootNodes(rootNodes[lowest.v1],rootNodes[lowest.v2]);
                        }
                        else if(setLenV1 == setLenV2){
                            //System.out.println("Both have same setLen");
                            changeRootNodes(rootNodes[lowest.v2],rootNodes[lowest.v1]);
                        }
                    }
                }
            }
            else{
                
                System.out.print("Cannot add edge ");
                System.out.println(toChar(lowest.v1) + " -> " + toChar(lowest.v2));
                System.out.println("Creates Cycle");
                
            }
        }
        Graph newMST = new Graph(edges2,V);
        return newMST;
    }

    private void changeRootNodes(int from,int to){
        for(int i = 1; i < rootNodes.length;i++){
            if(rootNodes[i] == from){
                rootNodes[i] = to;
            }
        }
    }
    private int getSetLength(int v){
        int count = 0;
        for(int i = 1; i < rootNodes.length;i++){
            if(rootNodes[i] == v){
                count++;
            }
        }
        return count;
    }

    public void displayKruskal(graphEdge[] edges){
        System.out.println(edges.length);
        for(int i = 0;i < getLength(edges); i++){
            System.out.println("Edge " + toChar(edges[i].v1) + " -> " + toChar(edges[i].v2));
        }
    }
    private graphEdge[] removeEdge(graphEdge[] edges,graphEdge edge){
        int index = -1;  // Initialize to -1 to indicate not found
        graphEdge[] newEdges = new graphEdge[edges.length-1];
        for (int i = 0; i < edges.length; i++) {
            if (edges[i] == edge) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            
            for (int i = index; i < edges.length - 1; i++) {
                edges[i] = edges[i + 1];
            }

            
            for(int i = 0;i < edges.length -1; i++){
                newEdges[i] = edges[i];
            }
        }
        return newEdges;
    }
    private boolean createsCycle(int[] rootNodes,graphEdge edge){
        int v1 = edge.v1;
        int v2 = edge.v2;
        if(rootNodes[v1] != 0 && rootNodes[v2] != 0){
            if(rootNodes[v1] == rootNodes[v2]){
                return true;
            }
        }
        return false;
    }
    private boolean edgeAlreadyExists(graphEdge[] edges,int v1,int v2)
    {
        for(int i = 0;i < E;i++){
            if(edges[i] != null){

            
                if(edges[i].v1 == v1){
                    if(edges[i].v2 == v2){
                        return true;
                    }
                }
                if(edges[i].v1 == v2){
                    if(edges[i].v2 == v1){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    private boolean edgeAlreadyExists(Node[] adj,int v1,int v2)
    {
        Node node = new Node();
        node = adj[v1];
        while(node !=z)
        {
            if(node.vert == v2){
                return true;
            }
            node = node.next;
        }
        return false;
    }

    private int getLength(graphEdge[] edges){
        int count = 0;
        for(int i = 0; i < edges.length; i++){
            if(edges[i] == null){
                break;
            }
            count++;
        }
        return count;
    }
    private void addEdge(int u,int v,int wgt)
    {
        Node node = new Node();
        node.vert = v;
        node.wgt = wgt;
        node.next = adj[u];

        adj[u] = node;

        node = new Node();
        node.vert = u;
        node.wgt = wgt;
        node.next = adj[v];

        adj[v] = node;
    }

    // convert vertex into char for pretty printing
    public char toChar(int u)
    {  
        return (char)(u + 64);
    }
    
    // method to display the graph representation
    public void display() {
        int v;
        Node n;
        
        for(v=1; v<=V; ++v){
            System.out.print("\nadj[" + toChar(v) + "] ->" );
            for(n = adj[v]; n != z; n = n.next) 
                System.out.print(" |" + toChar(n.vert) + " | " + n.wgt + "| ->");    
        }
        System.out.println("");
    } 

    

    private graphEdge findLowestEdge(graphEdge[] edges)
    {
        graphEdge lowest = new graphEdge();
        lowest = edges[0];
        for(int i = 0;i<edges.length;i++){
            if(edges[i] == null){
                break;
            }
            if(edges[i].wgt < lowest.wgt){
                lowest = edges[i];
            }
        }
        return lowest;
    }

    private boolean allNodesVisited(boolean[] visited)
    {
        for(int i = 1;i<visited.length;i++){
            if(!visited[i]){
                return false;
            }
        }
        return true;
    }

    private graphEdge[] getEdgesFromVerts(int[] verts)
    {
        boolean[] visited = new boolean[V+1];
        graphEdge[] edges = new graphEdge[V+1];
        int c = 0;
        for(int i = 0;i<verts.length;i++){
            if(verts[i] == 0){
                break;
            }
            visited[verts[i]] = true;
        }
        for(int i = 0;i<verts.length;i++){
            if(verts[i] == 0){
                break;
            }
            Node node = adj[verts[i]];
            while(node != z){
                if(!visited[node.vert])
                {
                    
                    graphEdge edge = new graphEdge();
                    edge.v1 = verts[i];
                    edge.v2 = node.vert;
                    edge.wgt = node.wgt;
                    edges[c] = edge;
                    c++;
                }
                node = node.next;
            }
        }
        return edges;
    }


    

    

    private int getWeight(int v, int u){
        Node curr = adj[v];
        while(curr != z){
            if(curr.vert == u){
                break;
            }
            curr = curr.next;
        }
        return curr.wgt;
    }

}

public class kruskal {
    public static void main(String[] args)  throws IOException{
        String fname = "wGraph1.txt";       
        Graph g = new Graph(fname);
        Graph MST;
        MST = g.kruskal();
        MST.display();
        //g.displayKruskal(edges);
    }
}
