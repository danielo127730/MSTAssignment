// Simple weighted graph representation 
// Uses an Adjacency Linked Lists, suitable for sparse graphs

import java.io.*;

class Heap
{
    private int[] a;	   // heap array
    

    private int N;         // heap size
   
    // The heap constructor gets passed from the Graph:
    //    1. maximum heap size
    //    2. reference to the dist[] array
    //    3. reference to the hPos[] array
    public Heap(int maxSize) 
    {
        N = 0;
        a = new int[maxSize + 1];
    }

    public boolean contains(int x) {
        for (int i = 1; i <= N; i++) {
            if (a[i] == x) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() 
    {
        return N == 0;
    }


    public void siftUp( int k,int[] dist) 
    {
        display();
        int v = a[k];
        /* 
        System.out.println("k =" + k);
        System.out.println("v =" + toChar(v));
        System.out.println("a[k] = " + a[k]);
        System.out.println("dist[v" + "(" + toChar(v) + ")" + "] = " + dist[v]);
        System.out.println("k/2 =" + k/2);
        */
        if(getLength(a) > 1){
        while(dist[v] < dist[a[k/2]] && k > 1){
            System.out.println("k = " + k);
            System.out.println("a[k] = " + a[k]);
            a[k] = a[k/2];
            k = k/2;
        }
        }  
        a[k] = v;
        
    }

    public int getLength(int a[]){
        int count = 0;
        for(int i = 0;i < a.length; i++){
            if(a[i] != 0){
                count++;
            }
        }
        return count;
    }
    public void siftDown( int k,int[] dist) 
    {
        int v, j;
        
        v = a[k];  
        j = 2*k;
            
        while(j < N){
            if(j < N && dist[a[j+1]] < dist[a[j]]){
                j++;
            }
            if(v <= a[j]){
                break;
            }
            a[k] = a[j];
            k = j;
            j = 2*k;
        }
        a[k] = v;
    }


    public void insert( int x,int[] dist) 
    {
        System.out.println("\nBefore insert a.len =" + getLength(a));
        a[++N] = x;
        siftUp( N,dist);
    }


    public int remove(int[] dist) 
    {   
        int v = a[1];
        a[N+1] = 0;  // put null node into empty spot
        
        a[1] = a[N--];
        siftDown(1,dist);
        
        return v;
    }

    public void display(){
        for(int i =1;i < getLength(a);i++){
            System.out.print(a[i] + "-> ");
        }
        System.out.println();
    }
    public char toChar(int u)
    {  
        return (char)(u + 64);
    }

}

class Node {
    public int vert;
    public int wgt;
    public Node next;
}

class qNode{
    public int val;
    public qNode next;
}
class graphEdge{
    public int v1;
    public int v2;
    public int wgt;
}
class Queue{
    private qNode head = null;
    private qNode tail = null;

    public void add(int num){

        //Empty List
        if(head == null){
            qNode node = new qNode();
            node.val = num;
            node.next = null;
            head = node;
            tail = node;
        }
        //Non-empty List
        else{
            qNode current = tail;

            qNode node = new qNode();
            node.val = num;
            node.next = null;

            current.next = node;

            tail = node;
        }
    }
    public int remove(){
        //Empty List
        if(head == null){
            return -1;
        }
        else{
            qNode current = head.next;
            int num = head.val;
            head = current;
            return num;
        }
    }

    public void display(){
        qNode current = head;
        while(current.next != null){
            System.out.print(current.val + " -> ");
            current = current.next;
        }
        System.out.print(current.val + "\n");
    }

    public boolean isEmpty(){
        return head == null;
    }
    
}


class Graph {
    
    
    // V = number of vertices
    // E = number of edges
    // adj[] is the adjacency lists array
    private int V, E;
    private Node[] adj;
    private Node z;
    private int[] mst_pa;

    //private int[] dist;
    // used for traversing graph
    private boolean[] visited;

    private int id;
    
    
    // default constructor
    public Graph(String graphFile)  throws IOException
    {
        int u, v;
        int e, wgt;
        Node t;

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

    public void DFS(int v)
    {
        System.out.print("DFS:\n");

        visited = new boolean[V+1];

        DFS_Trav(v);

    }

    private void DFS_Trav(int v){

        visited[v] = true;
        

        System.out.print(toChar(v) + " ");

        Node node = adj[v];
        while(node != z){
            if(!visited[node.vert]){
                DFS_Trav(node.vert);
            }
            node = node.next;
        }
    }


    public void BFS(int v)
    {
        visited = new boolean[V+1];

        visited[v] = true;
        Queue q = new Queue();
        System.out.println("\nBFS:");
        System.out.print(toChar(v) + " ");
        q.add(v);
        while(!q.isEmpty()){
            int vert = q.remove();
            Node node = adj[vert];
            while(node != z){
                if(!visited[node.vert]){
                    visited[node.vert] = true;
                    System.out.print(toChar(node.vert) + " ");
                    q.add(node.vert);
                }
                node = node.next;
            }
        }
    }

	public void MST_Prim(int s)
    {
        int[] dist = new int[V+1]; // Array to store distances from source
        int[] parent = new int[V+1]; // Array to store parent vertices in MST
        int[] hPos = new int[V+1]; // Array to store positions of vertices in the heap

        // Initialize distances with infinity and parent vertices as -1
        for (int i = 0; i <= V; i++) {
            dist[i] = Integer.MAX_VALUE;
            parent[i] = -1;
        }

        dist[s] = 0; // Distance to source vertex

        Heap h = new Heap(V); // Initialize heap with maximum size V
        h.insert(s, dist); // Insert the source vertex into the heap

        int wgt_sum = 0; // Total weight of MST

        while (!h.isEmpty()) {
            int u = h.remove(dist); // Extract the vertex with minimum distance from the heap
            wgt_sum += dist[u]; // Add the distance to the total weight of MST

            // Iterate through adjacent vertices of u
            Node node = adj[u];
            while (node != z) {
                int v = node.vert;
                int weight = node.wgt;

                // If v is not yet in MST and the weight of edge u-v is less than the current distance to v
                if (h.contains(v) && weight < dist[v]) {
                    dist[v] = weight; // Update the distance to v
                    parent[v] = u; // Update parent of v
                    h.siftUp(hPos[v], dist); // Update the position of v in the heap
                }
                node = node.next;
            }
        }

        System.out.print("\n\nWeight of MST = " + wgt_sum + "\n");

    }
    public void Prim(int v)
    {
        boolean[] visited = new boolean[V+1];
        int[] mst = new int[V];
        mst_pa = new int[V+1];
        mst[0] = v;
        int totalWgt = 0;
        graphEdge[] mst_edges = new graphEdge[V+1];
        int c = 1;
        visited[v] = true;
        while(!allNodesVisited(visited)){
            graphEdge[] edges = getEdgesFromVerts(mst);
            graphEdge lowest = findLowestEdge(edges);
            System.out.println("Edge with smallest weight: " + toChar(lowest.v1) + "->" +toChar(lowest.v2) + "(" + lowest.wgt + ")" );
            totalWgt += lowest.wgt;
            mst[c] = lowest.v2;
            mst_edges[c-1] = lowest;
            c++;
            
            System.out.println("Adding " + toChar(lowest.v2) + " to MST");
            mst_pa[lowest.v2] = lowest.v1;
            visited[lowest.v2] = true;
        }
        showMST();
        
        graphEdge[] edges = getEdgesFromVerts(mst,true);
        
        Graph mst_graph = new Graph(mst_edges,V);
        System.out.println("Displaying MST as adjacency list:");
        mst_graph.display();
        System.out.print("\n\nWeight of MST = " + totalWgt + "\n");
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

    private graphEdge[] getEdgesFromVerts(int[] verts,boolean includeVisited)
    {
        
        graphEdge[] edges = new graphEdge[V*4];
        int c = 0;
        
        for(int i = 0;i<verts.length;i++){
            if(verts[i] == 0){
                break;
            }
            Node node = adj[verts[i]];
            while(node != z){
                graphEdge edge = new graphEdge();
                edge.v1 = verts[i];
                edge.v2 = node.vert;
                edge.wgt = node.wgt;
                edges[c] = edge;
                c++;
                
                node = node.next;
            }
        }
        return edges;
    }

    public void showMST()
    {
            System.out.print("\n\nMinimum Spanning tree parent array is:\n");
            System.out.print("Child    Parent\n");
            for(int v = 1; v <= V; ++v)
            {
                if(mst_pa[v] == 0){
                    System.out.println("  " + toChar(v) + "  ->  Starting Vertex");
                }
                else{
                    System.out.println("  " + toChar(v) + "  ->  " + toChar(mst_pa[v]));
                }
                
            }
                
            System.out.println("");
    }

    public int[] SPT_Dijkstra(int s)
    {
        int[] dist = new int[V+1];
        boolean visited[] = new boolean[V+1];

        for(int i =0; i< V+1;i++){
            dist[i] = Integer.MAX_VALUE;
        }
        Heap heap = new Heap(V);
        dist[s] = 0;
        heap.insert(s, dist);
        while(!heap.isEmpty()){
            //System.out.println("Heap not empty");
            int current = heap.remove(dist);
            System.out.println("New current = " + toChar(current));
            if(visited[current]){
                continue;
            }
            
            visited[current] = true;
            
            int[] neighbors = getNeighbours(current);
            
            for(int i = 0; i < heap.getLength(neighbors); i++){
                System.out.println(toChar(current) + " has neighbor " + toChar(neighbors[i]));
            }
            for(int i = 0; i < heap.getLength(neighbors); i++){
                int tempDist = dist[current] +  getWeight(current, neighbors[i]);
                System.err.println(toChar(current) + "->" + toChar(neighbors[i]) + " weight = " + getWeight(current, neighbors[i]));
                System.out.println("prev dist =" + dist[neighbors[i]]);
                System.out.println("tempDist = " + tempDist);
                
                if(tempDist < dist[neighbors[i]]){
                    dist[neighbors[i]] = tempDist;
                    System.out.println("new dist for " + toChar(neighbors[i]) + " =" + dist[neighbors[i]]);
                    
                    System.out.println("Inserting " + toChar(neighbors[i]) + " into heap");
                    heap.insert(neighbors[i], dist);
                }
            }
        }
        return dist;
    }

    private int[] getNeighbours(int v){
        System.out.println("Getting neighbours of vert:" + v + toChar(v));
        int[] neighbors = new int[V];
        Node curr;
        curr = adj[v];
        int i = 0;
        while(curr != z){
            neighbors[i] = curr.vert;
            curr = curr.next;
            i++;
        }
        return neighbors;
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

public class GraphLists {
    public static void main(String[] args) throws IOException
    {
        int s = 1;
        String fname = "wGraph1.txt";               

        Graph g = new Graph(fname);

        g.DFS(s);
        g.BFS(s);

        g.MST_Prim(s);

        // Comment out the loop that calculates SPTs
        /*int[] dist = g.SPT_Dijkstra(s);
        for(int i = 2;i<14;i++){
            dist = g.SPT_Dijkstra(i);
            System.err.println("------------------");
        }
        System.out.println("\nDijkstra -> Distances from vert(" + g.toChar(s) + ")");
        for(int i =1;i<dist.length;i++){
            System.out.println(g.toChar(i)+ " : " + dist[i]);
        }*/
    }
}
