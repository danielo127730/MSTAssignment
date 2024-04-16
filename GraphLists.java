// Simple weighted graph representation 
// Uses an Adjacency Linked Lists, suitable for sparse graphs

import java.io.*;

class Heap
{
    private int[] a;	   // heap array
    private int[] hPos;	   // hPos[h[k]] == k
    private int[] dist;    // dist[v] = priority of v

    private int N;         // heap size
   
    // The heap constructor gets passed from the Graph:
    //    1. maximum heap size
    //    2. reference to the dist[] array
    //    3. reference to the hPos[] array
    public Heap(int maxSize, int[] _dist, int[] _hPos) 
    {
        N = 0;
        a = new int[maxSize + 1];
        dist = _dist;
        hPos = _hPos;
    }


    public boolean isEmpty() 
    {
        return N == 0;
    }


    public void siftUp( int k) 
    {
        int v = a[k];

        // code yourself
        // must use hPos[] and dist[] arrays
    }


    public void siftDown( int k) 
    {
        int v, j;
       
        v = a[k];  
        
        // code yourself 
        // must use hPos[] and dist[] arrays
    }


    public void insert( int x) 
    {
        a[++N] = x;
        siftUp( N);
    }


    public int remove() 
    {   
        int v = a[1];
        hPos[v] = 0; // v is no longer in heap
        a[N+1] = 0;  // put null node into empty spot
        
        a[1] = a[N--];
        siftDown(1);
        
        return v;
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
    private char toChar(int u)
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
        int v, u;
        int wgt, wgt_sum = 0;
        int[]  dist, parent, hPos;
        Node t;

        //code here
        
        //dist[s] = 0;
        
        //Heap h =  new Heap(V, dist, hPos);
        //h.insert(s);
        
        //while ( ...)  
        //{
            // most of alg here
            
       // }
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

    public void SPT_Dijkstra(int s)
    {

    }

}

public class GraphLists {
    public static void main(String[] args) throws IOException
    {
        int s = 12;
        String fname = "wGraph1.txt";               

        Graph g = new Graph(fname);
        //q.display();
       // g.display();
        //g.Prim(1);
       g.DFS(s);
       g.BFS(s);
       //g.MST_Prim(s);   
       //g.SPT_Dijkstra(s);               
    }
}
