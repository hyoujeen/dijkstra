import java.util.*;

/**
 * Implements the Graph interface
 * for a graph constituted by nodes
 * and edges.
 *
 * @author You Jeen Ha
 * @version CSC 212, 3 May 2018
 */
public class GraphImplementation<N,E>  implements Graph<N,E>{

	/** A hashset of nodes. */
  	Collection<Node<N,E>> nodeSet = new HashSet<Graph.Node<N,E>>();
  	/** A hashset of edges. */
  	Collection<Edge<N,E>> edgeSet = new HashSet<Graph.Edge<N,E>>();
  	/** A hashset of outgoing edges. */
  	private Set<Edge<N,E>> outEdges = new HashSet<Graph.Edge<N,E>>();
  	/** A hashset of incoming edges. */
  	private Set<Edge<N,E>> inEdges = new HashSet<Graph.Edge<N,E>>();
	
	/** Number of nodes */
	private int numNodes = 0;

   /**
	*  Gets a new set of all of the nodes in the graph.  Changes made to the
	*  returned set will not be reflected in the graph, though changes made to
	*  particular nodes in the set may be.
	*
	*  @return a new set of the nodes in the graph
	*/
	public Set<Node<N,E>> getNodes() {
		return new HashSet<Node<N,E>>(nodeSet);
	}

   /**
	*  Gets a new set of all of the edges in the graph.  Changes made to the
	*  returned set will not be reflected in the graph, though changes made to
	*  particular edges in the set may be.
	*
	*  @return a new set of the edges in the graph
	*/
	public Set<Edge<N,E>> getEdges() {
		return new HashSet<Edge<N,E>>(edgeSet);
	}

   /**
	*  Finds a particular edge given its tail and head.
	*
	*  @param tail  the tail ("from" node) of the edge to be found
	*  @param head  the head ("to" node) of the edge to be found
	*  @return      the edge, or null if there is no such edge
	*/
	public Edge<N,E> findEdge(Node<N,E> tail, Node<N,E> head) {
		for (Edge<N,E> edge : edgeSet) {
			if (edge.getTail().equals(tail) && edge.getHead().equals(head)) {
				return edge;
			}
		}
		return null;
	}

   /**
	*  Short-cut to get the number of nodes in the graph.  This is
	*  equivalent to, though not necessarily implemented as,
	*  getNodes().size().
	*
	*  @return the number of nodes in the graph
	*/
	public int numNodes() {
		return nodeSet.size();
	}

   /**
	*  Short-cut to get the number of edges in the graph.  This is
	*  equivalent to, though not necessarily implemented as,
	*  getEdges().size().
	*
	*  @return the number of edges in the graph
	*/
	public int numEdges() {
		return edgeSet.size();
	}

   /**
	*  Adds a node to the graph.  The new node will have degree 0.
	*
	*  @param data  the data to be associated with the node
	*  @return      the new node
	*/
	public Node<N,E> addNode(N data) {
		GraphNode newNode = new GraphNode(data);
		numNodes += 1;
		nodeSet.add(newNode);
		return newNode;
	}

   /**
	*  Adds an edge to the graph, given the tail and head nodes.
	*
	*  @param tail  the tail ("from" node) of the edge to be added
	*  @param head  the head ("to" node) of the edge to be added
	*  @return the new edge
	*/
	public Edge<N,E> addEdge(E data, Node<N,E> tail, Node<N,E> head) {
		GraphEdge newEdge = new GraphEdge(data, tail, head);
		if (!edgeSet.contains(newEdge)) {
			edgeSet.add(newEdge);
		}
		return newEdge;
	}

   /**
	*  Removes a node and all its incident edges from the graph.
	*
	*  @param node  the node to be removed
	*  @throws Error  if the node does not belong to this graph
	*/
	public void removeNode(Node<N,E> node) { 
		try {
			// Remove node
			numNodes -= 1;
			getNodes().remove((Object) node);
			// Remove incidence edges
			for (Iterator<Edge<N,E>> outEdgeItr = node.getOutgoingEdges().iterator(); outEdgeItr.hasNext(); ) {
				Edge<N,E> edge = outEdgeItr.next();
				outEdgeItr.remove(); 
			}
			for (Iterator<Edge<N,E>> inEdgeItr = node.getIncomingEdges().iterator(); inEdgeItr.hasNext(); ) {
				Edge<N,E> edge = inEdgeItr.next();
				inEdgeItr.remove(); 
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new Error("The supplied node is not an edge of this graph.");
		}
		
	}

   /**
	*  Removes an edge from the graph.
	*
	*  @param edge  the edge to be removed
	*  @throws Error  if the edge does not belong to this graph
	*/
	public void removeEdge(Edge<N,E> edge) { 
		if (edgeSet.contains(edge)) {
			edgeSet.remove(edge);
		}
		else {
			throw new Error("The supplied edge is not an edge of this graph.");
		}
	}

   /** 
	*  Removes an edge from the graph given its tail and head nodes.
	*
	*  @param tail  the tail ("from" node) of the edge to be removed
	*  @param to    the head ("to" node) of the edge to be removed
	*  @throws Error  if either the tail or head nodes do not belong to this graph
	*/
	public void removeEdge(Node<N,E> tail, Node<N,E> head) {  
		for (Iterator<Edge<N,E>> edgeItr = edgeSet.iterator(); edgeItr.hasNext(); ) {
			Edge<N,E> edge = edgeItr.next();
			if (edge.getTail().equals(tail) && edge.getHead().equals(head)) { 
				edgeItr.remove();
			}
		}
		if (!nodeSet.contains(tail) || !nodeSet.contains(head)) {
			throw new Error("The supplied edge is not an edge of this graph.");
		}
	}

   /**
	*  Returns the set of nodes in the graph that are not in group.  This is
	*  equivalent to getNodeSet().removeAll(group), but may be implemented
	*  differently.
	*
	*  @param group  a set of nodes
	*  @return       all of the nodes of the graph not present in group
	*/
	public Set<Node<N,E>> otherNodes(Set<Node<N,E>> group) {
		Set<Node<N,E>> otherNodeSet = new HashSet<Node<N,E>>();
		for (Node<N,E> node : nodeSet) {
			if (!group.contains(node)) {
				otherNodeSet.add(node);
			}
		}
		return otherNodeSet;
	}

   /**
	*  Returns the set of nodes including all and only those nodes that
	*  are one end or the other of the given set of edges.
	*
	*  @param edges  a set of edges
	*  @return       a set of nodes that those edges are incident to
	*/
	public Set<Node<N,E>> endpoints(Set<Edge<N,E>> edges) {
		Set<Node<N,E>> endpoints = new HashSet<Node<N,E>>(); 
		for (Edge<N,E> edge : edges) {
			endpoints.add(edge.getTail());
			endpoints.add(edge.getHead());
		}
		return endpoints;
	}

   /**
	*  Performs a breadth-first traversal of a graph starting from the given
	*  node.  As each node or edge is processed the appropriate method in the
	*  processor is invoked.  The traversal will continue until all of the nodes
	*  have been traversed or the processor returns true, whichever happens
	*  first.  The idea is that if, for instance, we are doing a search we can
	*  stop as soon as we find the desired node by returning true from the
	*  processor, at which point the traversal will also return true.
	*
	*  @param start      the starting node for the traversal
	*  @param processor  the processing object to be applied to each node/edge
	*  @return           true if the processor ever returns true, false otherwise
	*  @throws Error if the starting node is not a node of this graph
	*/
	public boolean breadthFirstTraversal(Node<N,E> start, Processor<N,E> processor) {
		Queue<Node<N,E>> queue = new LinkedList<Node<N,E>>();
		Set<Node<N,E>> visited = new HashSet<Node<N,E>>();
		queue.add(start);
		while (!queue.isEmpty()) {
			Node<N,E> node = queue.remove();
			if (visited.contains(node)) {
				continue;
			}
			visited.add(node);
			processor.preProcessNode(node);
			Set<Edge<N,E>> edgeSet = node.getOutgoingEdges();
			for (Edge<N,E> edge : edgeSet) {
				if (processor.processEdge(edge)) {
					return true;
				}
				processor.processEdge(edge);
				Node<N,E> head = edge.getHead();
				queue.add(head);
			}
		}
		if (!nodeSet.contains(start)) {
			throw new Error("The supplied starting node is not a node of this graph.");
		}
		return false;
	}

   /** 
	* Helper for recursive execution of 
	* depth-first traversal of graph.
	*
	* @param start Node where traversal starts
	* @param processor Node processor
	* @param visited Set of visited nodes
	*/
  	private boolean dftHelper(Node start, Processor processor, Set<Node<N,E>> visited) {
		if (start == null) { return false; }
		if (visited.contains(start)) { return false; }
		visited.add(start);
		if (processor.preProcessNode(start)) {
			return true;
		}
		Set<Edge<N,E>> edgeSet = start.getOutgoingEdges();
		for (Edge<N,E> edge : edgeSet) {
			if (processor.processEdge(edge)) {
				return true;
			}
			if (dftHelper(edge.getHead(), processor, visited)) {
				return true;
			}
		}
		if (processor.postProcessNode(start)) {
			return true;
		}
		return false;
	}

   /**
	*  Performs a depth-first traversal of a graph starting from the given node.
	*  As each node or edge is processed the appropriate method in the processor
	*  is invoked.  The traversal will continue until all of the nodes have been
	*  traversed or the processor returns true, whichever happens first.  The
	*  idea is that if, for instance, we are doing a search we can stop as soon
	*  as we find the desired node by returning true from the processor, at which
	*  point the traversal will also return true.
	*
	*  @param start      the starting node for the traversal
	*  @param processor  the processing class to be applied to each node/edge
	*  @return           true if the processor ever returns true, false otherwise
	*  @throws Error if the starting node is not a node of this graph
	*/
	public boolean depthFirstTraversal(Node start, Processor processor) {
		Set<Node<N,E>> visited = new HashSet<Node<N,E>>();
		return dftHelper(start, processor, visited);
	}

   /**
	*  Returns a string representation of the graph.
	*
	*  @return  a string representation of the graph
	*/
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Graph");
		for (Node<N,E> node : nodeSet) {
	 		builder.append("\n  Node: ");
	  		builder.append(node.getData().toString());
	  		for (Edge<N,E> edge : edgeSet) { 
	    		if (edge != null) {
	      			builder.append("\n    To: ").append(edge.getHead().getData()).append(", ").append(edge.getData());
	    		}
	  		}
		}
		return builder.toString();
	}


   /**
	*  Verifies the internal consistency of the graph structure.
	*
	*  @return  true if consistent, false otherwise
	*/
	public boolean validateGraph() {
		for (Edge<N,E> edge : edgeSet) {
			if (edge == null) { return true; }
			if (!edgeSet.contains(edge)) { return false; }
		}
		return true;
	}

   /**
	*  Represents a node in a graph.
	*
	*  @param <N>  the type of the data to be associated with a node
	*  @param <E>  the type of the data to be associated with an edge
	*/
	private class GraphNode implements Graph.Node<N,E> {
  	
	  	/** Data associated with this node */
	  	public N data;

	   /** 
	  	* Constructor. Create a node with given data.
	  	*
	  	* @param data The data associated with this node
	  	*
	  	*/
	  	public GraphNode(N data) {
	  		this.data = data;
	  	}

	   /**
	  	* Return the data associated with this node.
	  	*
	  	* @return The data associated with this node.
	  	*/
	    public N getData() {
	    	return data;
	    }

	   /**
	    *  Return a new set of edges leaving this node, i.e., the set of all edges
	    *  whose tail is this node.  Changes to the returned set will not be
	    *  reflected in the graph, but changes to individual edges in the set may
	    *  be.
	    *
	    *  @return the set of edges leaving this node
	    */
	    public Set<Edge<N,E>> getOutgoingEdges() {
	    	for (Edge<N,E> edge : edgeSet) {
	    		if (edge.getTail() == this) {
	    			outEdges.add(edge);
	    		}
	    	}
	    	return outEdges;
	    }

	   /**
	    *  Return a new set of edges entering this node, i.e., the set of all edges
	    *  whose head is this node.  Changes to the returned set will not be
	    *  reflected in the graph, but changes to individual edges in the set may
	    *  be.
	    *
	    *  @return the set of edges entering this node
	    */
	    public Set<Edge<N,E>> getIncomingEdges() {
	    	for (Edge<N,E> edge : edgeSet) {
	    		if (edge.getHead() == this) {
	    			inEdges.add(edge);
	    		}
	    	}
	    	return inEdges;
	    }

	    /**
	     *  Is this node equal to that node, (i.e., are the contents equal
	     *  to each other)?
	     *
	     *  @param that  the node to compare to this one
	     *  @return true if the data associated with this is equal to the data
	     *               associated with that
	     */
	    public boolean equals(Node<N,E> that) {
	    	return this.getData().equals(that.getData()); 
	    }

	   /**
	    * Is this node equal to that object, (i.e., are the contents
	    * equal to each other)?
	    *
        *  @param that  the object to compare to this one
	    *  @return true if that is a Node, and the data associated with
	    *          this is equal to the data associated with that
	    */
	    @Override
	    public boolean equals(Object that) {
	    	if (that == this) {
	    		return true;
	    	}
	    	if (that == null) {
	    		return false;
	    	}
	    	if (!(that instanceof Node)) {
	    		return false;
	    	}
	    	Node<N,E> other = (Node<N,E>) that;
	    	return this.getData().equals(other.getData());
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
	    	int hashCode = data.hashCode();
	    	return hashCode;
	    }
   } // end of GraphNode nested class


   /**
	*  Represents an edge in the graph.
	*
	*  @param <N>  the type of the data associated with a node
	*  @param <E>  the type of the data associated with an edge
	*/
	private class GraphEdge implements Graph.Edge<N,E> {
	  	/** Data associated with this edge */
	  	public E data;

	  	/** Tail of this edge */
	  	public Node<N,E> tail;

	  	/** Head of this edge */
	  	public Node<N,E> head;

	   /**
	    * Constructor.
	  	* 
	  	* @param data The data associated with this edge
	  	* @param tail The tail node of this edge
	  	* @param head The head node of this edge
	  	*/
	  	public GraphEdge(E data, Node<N,E> tail, Node<N,E> head) {
	  		this.data = data;
	  		this.tail = tail;
	  		this.head = head;
	  	}

	   /**
	    *  Return the data associated with this edge.
	    *
	    *  @return the data associated with this edge
	    */
	    public E getData() {
	    	return data;
	    }

	   /**
	    *  Return the tail ("from" node) of this edge.
	    *
	    *  @return the tail
	    */
	    public Node<N,E> getTail() {
	    	return tail;
	    }

	   /**
	    *  Return the head ("to" node) of this edge.
	    *
	    *  @return the head
	    */
	    public Node<N,E> getHead() {
	    	return head;
	    }

	   /**
	    *  Is this edge equal to that edge, (i.e., are the head, tail, and contents
	    *  equal to each other)?
	    *
	    *  @param that  the edge to compare to this one
	    *  @return true if the head, tail, and data associated with this
	    *          are equal to the head, tail, and data associated with
	    *          that
	    */
	    @Override
	    public boolean equals(Edge<N,E> that) {
	    	boolean isEqual = this.data.equals(that.getData())
	        && this.head.equals(that.getHead())
	        && this.tail.equals(that.getTail());
	    	return isEqual;
	    }

	   /**
	    * Is this edge equal to that object, (i.e., are the head, tail, and
	    * contents equal to each other)?
	    *
	    *  @param that  the object to compare to this one
	    *  @return true if that is an Edge, and the two edges are equal
	    *  as determined by .equals(Node<N,E> that)
	    */
	    @Override
	    public boolean equals(Object that) {
	    	if (that == this) {
	    		return true;
	    	}
	    	if (that == null) {
	    		return false;
	    	}
	    	if (!(that instanceof Edge)) {
	    		return false;
	    	}
	    	Edge<N,E> other = (Edge<N,E>) that;
	    	return this.getTail().equals(other.getTail()) && this.getHead().equals(other.getHead());
	    }

	   /**
	    *  Returns a hash code for this edge.  This must be defined such that if
	    *  two edges are equal (as determined by equals) then their hash codes are
	    *  the same.
	    *
	    *  @return the hash code computed for this object
	    */
	    @Override
	    public int hashCode() {
	    	final int hashCode = data.hashCode() * head.hashCode() * tail.hashCode();
	    	return hashCode;
	    }
	} // end of GraphEdge nested class

} // end of GraphImplementation class
  
  