import java.util.*;
import java.lang.*;

/** 
 * Implements Dijkstra's Algorithm and can be used in
 * a directed, weighted graph to find the shortest paths
 * from a selected node to other nodes.
 *
 * @author You Jeen Ha
 * @version CSC 212 Final Project, 8 May 2018
 */

public class Dijkstra {

	/** Node for overriding hashmap equals and hashcode methods */
	private Graph.Node<PlacedData<Integer>,Integer> nextNode;
	/** Set of adjacent edges */
	private Set<Graph.Edge<PlacedData<Integer>, Integer>> adjacencies = new HashSet<Graph.Edge<PlacedData<Integer>,Integer>>();
	/** Set of unvisited nodes */
	private Set<Graph.Node<PlacedData<Integer>,Integer>> unvisited = new HashSet<Graph.Node<PlacedData<Integer>,Integer>>();
	/** Set of visited nodes */ 
	private Set<Graph.Node<PlacedData<Integer>,Integer>> visited = new HashSet<Graph.Node<PlacedData<Integer>,Integer>>();
	/* Map of temporary distances between nodes */
	private HashMap<Graph.Node<PlacedData<Integer>,Integer>, Integer> tempDistanceMap = new HashMap<Graph.Node<PlacedData<Integer>,Integer>, Integer>();
	/* Map of actual distances between nodes (primarily to override comparator for PriorityQueue) */
	private HashMap<Graph.Node<PlacedData<Integer>,Integer>, Integer> distanceMap = new HashMap<Graph.Node<PlacedData<Integer>,Integer>, Integer>();
  	/** Map of edge weights */
  	private HashMap<Graph.Edge<PlacedData<Integer>,Integer>, Integer> weightMap = new HashMap<Graph.Edge<PlacedData<Integer>,Integer>, Integer>();

   /**
    * Returns a set of unvisited nodes in the graph.
    *
    * @param graph The graph with which shortest distances will be calculated.
    * @return unvisited The set of unvisited nodes.
    */
	private Set<Graph.Node<PlacedData<Integer>,Integer>> unvisitedNodes(ComplexGraph<PlacedData<Integer>,Integer> graph) {
		for (Graph.Node<PlacedData<Integer>,Integer> node : graph.getNodes()) {
			unvisited.addAll(graph.endpoints(node.getOutgoingEdges()));
		}
		return unvisited;
	}

   /**
    * Returns a set of all outgoing edges of nodes. 
    *
    * @return adjacencies All adjacency edges
    */
	private Set<Graph.Edge<PlacedData<Integer>,Integer>> getAdjacencyEdges() { 
		for (Graph.Node<PlacedData<Integer>,Integer> node : unvisited) {
			adjacencies.addAll(node.getOutgoingEdges());	
		}
		return adjacencies;

	}

   /**
    * Executes the Dijkstra Algorithm on a graph. Invoked in GUI application.
    *
    * @param graph The graph with which shortest distances will be calculated.
    * @param chosenNode The node from which shortest distances to endpoints will be calculated.
    */
	public void executeDijkstra(ComplexGraph<PlacedData<Integer>,Integer> graph, Graph.Node<PlacedData<Integer>, Integer> chosenNode) {
		// Mark all nodes as unvisited
		unvisited = unvisitedNodes(graph);
		// Get all adjacency edges
		adjacencies = getAdjacencyEdges();
		
		// Set temporary and permanent graph node distances 
		initializeGraph(chosenNode);

		// Priority queue with a custom comparator - The head of the queue now has the minimal distance
		PriorityQueue<Graph.Node<PlacedData<Integer>, Integer>> queue = new PriorityQueue<Graph.Node<PlacedData<Integer>, Integer>>(new Comparator<Graph.Node<PlacedData<Integer>,Integer>>() {
		   /**
			* Compares nodes in priority queue according to 
			* their respective distances.
			*
			* @param d1, d2 The nodes to be compared.
			* @return distances
			*/
			@Override
			public int compare(Graph.Node<PlacedData<Integer>,Integer> d1, Graph.Node<PlacedData<Integer>,Integer> d2) {
				int dist1 = distanceMap.get(d1);
				int dist2 = distanceMap.get(d2);
				if (dist1 > dist2) {
					return 1;
				}
				else if (dist1 < dist2) {
					return -1;
				}
				else {
					return 0;
				}
			}
		});
		
		// Fill queue with chosen node
		queue.add(chosenNode);
		// Traverse through unvisited nodes with minimal distances and its neighbors to find shortest distances
		while (!queue.isEmpty()) {
			Graph.Node<PlacedData<Integer>, Integer> minDistNode = queue.poll();
			if (!visited.contains(minDistNode)) { 
				if (visited.contains(minDistNode)) { 
					continue;
				}
				visited.add(minDistNode); // mark node as visited
				Set<Graph.Node<PlacedData<Integer>, Integer>> neighbors = getNeighborNodes(minDistNode);
				for (Graph.Node<PlacedData<Integer>, Integer> neighbor : neighbors) {
					if (unvisited.contains(neighbor)) {
						int distanceToNeighbor = getDistance(minDistNode, neighbor);
						int newDistance = Math.abs(tempDistanceMap.get(minDistNode) + distanceToNeighbor);
						if (newDistance < tempDistanceMap.get(neighbor)) { 
							// Update neighbor's new distance since it is shorter
							tempDistanceMap.put(neighbor, newDistance);
							queue.add(neighbor);
						}
					}
				}	
			}
		}
	}

   /**
    * Initializes distances of each nodes with temporary values
    * of 0 and infinity as well as calculates weight edges and 
    * actual node distances based on nodes' coordinate values
    * and put these calculations in maps.
    *
    * @param chosenNode The node that the user clicks on in the GUI application.
    */
	private void initializeGraph(Graph.Node<PlacedData<Integer>, Integer> chosenNode) {
		// Assign permanent distances/weights for every destination node and outgoing edge 	
		for (Graph.Edge<PlacedData<Integer>,Integer> edge : adjacencies) { 
			Graph.Node<PlacedData<Integer>, Integer> startingNode = edge.getTail();
			Graph.Node<PlacedData<Integer>, Integer> destinationNode = edge.getHead();
			distanceMap.put(destinationNode, (int)Math.hypot(destinationNode.getData().getX() - startingNode.getData().getX(), destinationNode.getData().getY() - startingNode.getData().getY()));
			weightMap.put(edge, (int)Math.hypot(destinationNode.getData().getX() - startingNode.getData().getX(), destinationNode.getData().getY() - startingNode.getData().getY()));
		}

		// Initialize distances from chosen node to each unvisited node as infinity
		for (Graph.Node<PlacedData<Integer>,Integer> node : unvisited) {
			tempDistanceMap.put(node, (int) Double.POSITIVE_INFINITY); 
		}
		// Distance from chosen node to itself is always 0
		if (unvisited.contains(chosenNode)) {
			initializeSource(chosenNode);
		}
	}

   /**
	* Initializes the source for Dijkstra's Algorithm with
	* the chosen node, adding both the chosen node and a 
	* distance of 0 to the temporary distance map and the
	* actual distance map.
	* 
	* @param chosenNode The node selected by the user
	*/
	private void initializeSource(Graph.Node<PlacedData<Integer>,Integer> chosenNode) {
		// Constant distance for chosen node
		tempDistanceMap.put(chosenNode, 0);
		distanceMap.put(chosenNode, 0); 
	}
	
	
   /** 
	* Gets the set of neighboring nodes of current chosen node.
	*
	* @param chosenNode The current node from which neighbors are considered.
	* @return neighborNodes The set of neighboring nodes for the current node.
	*/
	private Set<Graph.Node<PlacedData<Integer>,Integer>> getNeighborNodes(Graph.Node<PlacedData<Integer>,Integer> chosenNode) {
		Set<Graph.Node<PlacedData<Integer>,Integer>> neighborNodes = new HashSet<Graph.Node<PlacedData<Integer>,Integer>>();
		for (Graph.Edge<PlacedData<Integer>,Integer> neighborEdge : chosenNode.getOutgoingEdges()) {
			neighborNodes.add(neighborEdge.getHead());
		}
		return neighborNodes;
	}

   /**
	* Gets distance/weight of edge based on its tail and head.
	* 
	* @param startingNode The node from which the edge is directed.
	* @param destinationNode The node toward which the edge is directed.
	* @throw Error If distance is less than 0, which it should not be.
	*/
	private int getDistance(Graph.Node<PlacedData<Integer>,Integer> startingNode, Graph.Node<PlacedData<Integer>,Integer> destinationNode) {
		for (Graph.Edge<PlacedData<Integer>,Integer> edge : adjacencies) {
			if (edge.getTail().equals(startingNode) && edge.getHead().equals(destinationNode)) {
				 return weightMap.get(edge);
			}
		}
		throw new Error("Distance should be greater than 0.");
	}
	
   /** 
    * Prints shortest paths found by the Dijkstra Algorithm. 
    *
    * @param chosenNode The node chosen by the user in the GUI application.
    * @return output Shortest paths are printed as strings.
    */
	public String printDijkstra(Graph.Node<PlacedData<Integer>, Integer> chosenNode) {
		String key, value;
		StringBuilder output = new StringBuilder();
	 
	 	for (Graph.Node<PlacedData<Integer>,Integer> node : tempDistanceMap.keySet()) {
			key = node.toString();
			value = tempDistanceMap.get(node).toString();
			output.append(" To node " + key + ", distance is: " + value + "\n");
		}
	 	return output.toString();
	}

   /**
    * Is this next node equal to that object?
    *
    *  @param o  the object to compare to this one
    *  @return true if that is nextNode
    */
	@Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Dijkstra)) {
            return false;
        }
        Dijkstra dijkstra = (Dijkstra) o;
        return dijkstra.nextNode.equals(nextNode);
    }

   /**
    *  Returns a hash code for this node.  This must be defined such
    *  that if two nodes are equal (as determined by equals) then
    *  their hash codes are the same.
    *
    *  @return the hash code computed for this object
    */
    @Override
    public int hashCode() {
        return nextNode.hashCode();
    }


} // end of Dijkstra class
