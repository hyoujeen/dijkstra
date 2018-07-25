import java.util.List;
import java.util.Set;

/**
 *  A graph, similar in idea to the List interface of the Collections
 *  framework.  A graph is a conceptually a pair <N,E> where N is a
 *  set of nodes and E is a set of edges, where each edge is an
 *  ordered pair <n1,n2> where both n1 and n2 are members of N.  
 *  
 *  @author John Ridgway
 *  @version CSC 212, April 19, 2018
 *
 *  @param <N>  the type of the data to be associated with a node
 *  @param <E>  the type of the data to be associated with an edge
 */
public interface Graph<N,E> {
  /**
   *  Get a new set of all of the nodes in the graph.  Changes made to the
   *  returned set will not be reflected in the graph, though changes made to
   *  particular nodes in the set may be.
   *
   *  @return a new set of the nodes in the graph
   */
  public Set<Node<N,E>> getNodes();

  /**
   *  Get a new set of all of the edges in the graph.  Changes made to the
   *  returned set will not be reflected in the graph, though changes made to
   *  particular edges in the set may be.
   *
   *  @return a new set of the edges in the graph
   */
  public Set<Edge<N,E>> getEdges();

  /**
   *  Find a particular edge given its tail and head.
   *
   *  @param tail  the tail ("from" node) of the edge to be found
   *  @param head  the head ("to" node) of the edge to be found
   *  @return      the edge, or null if there is no such edge
   */
  public Edge<N,E> findEdge(Node<N,E> tail, Node<N,E> head);

  /**
   *  Short-cut to get the number of nodes in the graph.  This is
   *  equivalent to, though not necessarily implemented as,
   *  getNodes().size().
   *
   *  @return the number of nodes in the graph
   */
  public int numNodes();
  
  /**
   *  Short-cut to get the number of edges in the graph.  This is
   *  equivalent to, though not necessarily implemented as,
   *  getEdges().size().
   *
   *  @return the number of edges in the graph
   */
  public int numEdges();

  /**
   *  Adds a node to the graph.  The new node will have degree 0.
   *
   *  @param data  the data to be associated with the node
   *  @return      the new node
   */
  public Node<N,E> addNode(N data);

  /**
   *  Adds an edge to the graph, given the tail and head nodes.
   *
   *  @param tail  the tail ("from" node) of the edge to be added
   *  @param head  the head ("to" node) of the edge to be added
   *  @return the new edge
   */
  public Edge<N,E> addEdge(E data, Node<N,E> tail, Node<N,E> head);

  /**
   *  Removes a node and all its incident edges from the graph.
   *
   *  @param node  the node to be removed
   *  @throws Error  if the node does not belong to this graph
   */
  public void removeNode(Node<N,E> node);

  /**
   *  Removes an edge from the graph.
   *
   *  @param edge  the edge to be removed
   *  @throws Error  if the edge does not belong to this graph
   */
  public void removeEdge(Edge<N,E> edge);

  /** 
   *  Removes an edge from the graph given its tail and head nodes.
   *
   *  @param tail  the tail ("from" node) of the edge to be removed
   *  @param to    the head ("to" node) of the edge to be removed
   *  @throws Error  if either the tail or head nodes do not belong to this graph
   */
  public void removeEdge(Node<N,E> tail, Node<N,E> head);

  /**
   *  Returns the set of nodes in the graph that are not in group.  This is
   *  equivalent to getNodeSet().removeAll(group), but may be implemented
   *  differently.
   *
   *  @param group  a set of nodes
   *  @return       all of the nodes of the graph not present in group
   */
  public Set<Node<N,E>> otherNodes(Set<Node<N,E>> group);

  /**
   *  Returns the set of nodes including all and only those nodes that
   *  are one end or the other of the given set of edges.
   *
   *  @param edges  a set of edges
   *  @return       a set of nodes that those edges are incident to
   */
  public Set<Node<N,E>> endpoints(Set<Edge<N,E>> edges);

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
  public boolean breadthFirstTraversal(Node<N,E> start, Processor<N,E> processor);

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
  public boolean depthFirstTraversal(Node<N,E> start, Processor<N,E> processor);

  /**
   *  Returns a string representation of the graph.
   *
   *  @return  a string representation of the graph
   */
  public String toString();

  /**
   *  Verifies the internal consistency of the graph structure.
   *
   *  @return  true if consistent, false otherwise
   */
  public boolean validateGraph();

  /**
   *  Represents a node in a graph.
   *
   *  @param <N>  the type of the data to be associated with a node
   *  @param <E>  the type of the data to be associated with an edge
   */
  public interface Node<N,E> {
    /**
     *  Return the data associated with this node.
     *
     *  @return the data associated with this node
     */
    public N getData();

    /**
     *  Return a new set of edges leaving this node, i.e., the set of all edges
     *  whose tail is this node.  Changes to the returned set will not be
     *  reflected in the graph, but changes to individual edges in the set may
     *  be.
     *
     *  @return the set of edges leaving this node
     */
    public Set<Edge<N,E>> getOutgoingEdges();

    /**
     *  Return a new set of edges entering this node, i.e., the set of all edges
     *  whose head is this node.  Changes to the returned set will not be
     *  reflected in the graph, but changes to individual edges in the set may
     *  be.
     *
     *  @return the set of edges entering this node
     */
    public Set<Edge<N,E>> getIncomingEdges();

    /**
     *  Is this node equal to that node, (i.e., are the contents equal
     *  to each other)?
     *
     *  @param that  the node to compare to this one
     *  @return true if the data associated with this is equal to the data
     *               associated with that
     */
    public boolean equals(Node<N,E> that);

    /**
     * Is this node equal to that object, (i.e., are the contents
     * equal to each other)?
     *
     *  @param that  the object to compare to this one
     *  @return true if that is a Node, and the data associated with
     *          this is equal to the data associated with that
     */
    public boolean equals(Object that);

    /**
     *  Returns a hash code for this node.  This must be defined such
     *  that if two nodes are equal (as determined by equals) then
     *  their hash codes are the same.
     *
     *  @return the hash code computed for this object
     */
    public int hashCode();
  }

  /**
   *  Represents an edge in the graph.
   *
   *  @param <N>  the type of the data to be associated with a node
   *  @param <E>  the type of the data to be associated with an edge
   */
  public interface Edge<N,E> {
    /**
     *  Return the data associated with this edge.
     *
     *  @return the data associated with this edge
     */
    public E getData();

    /**
     *  Return the tail ("from" node) of this edge.
     *
     *  @return the tail
     */
    public Node<N,E> getTail();

    /**
     *  Return the head ("to" node) of this edge.
     *
     *  @return the head
     */
    public Node<N,E> getHead();


    /**
     *  Is this edge equal to that edge, (i.e., are the head, tail, and contents
     *  equal to each other)?
     *
     *  @param that  the edge to compare to this one
     *  @return true if the head, tail, and data associated with this
     *          are equal to the head, tail, and data associated with
     *          that
     */
    public boolean equals(Edge<N,E> that);

    /**
     * Is this edge equal to that object, (i.e., are the head, tail, and
     * contents equal to each other)?
     *
     *  @param that  the object to compare to this one
     *  @return true if that is an Edge, and the two edges are equal
     *  as determined by .equals(Node<N,E> that)
     */
    public boolean equals(Object that);

    /**
     *  Returns a hash code for this edge.  This must be defined such that if
     *  two edges are equal (as determined by equals) then their hash codes are
     *  the same.
     *
     *  @return the hash code computed for this object
     */
    public int hashCode();
  }

  /**
   *  A processor for graph traversals.  There are three methods that may be
   *  called by the traversals.  preProcessNode will always be called prior to
   *  processing the children of a node.  postProcessNode will only be called by
   *  depth-first searches and will be called after processing all of the
   *  children of the node.  processEdge will always be called to process an
   *  edge.  If any of the methods returns true the traversal will stop at that
   *  point and return true, regardless of whether all of the nodes have been
   *  traversed.
   *
   *  @param <N>  the type of data stored at each node
   *  @param <E>  the type of data stored with each edge
   */
  public interface Processor<N,E> {
    /**
     *  Called before processing the children of each node.
     *
     *  @param node  the node to be processed
     *  @return true if the traversal should be stopped immediately and true
     *          returned, false otherwise
     */
    boolean preProcessNode(Node<N,E> node);

    /**
     *  Called after processing the children of each node.
     *
     *  @param node  the node to be processed
     *  @return true if the traversal should be stopped immediately and true
     *          returned, false otherwise
     */
    boolean postProcessNode(Node<N,E> node);

    /**
     *  Called to process each edge.
     *
     *  @param edge  the edge to be processed
     *  @return true if the traversal should be stopped immediately and true
     *          returned, false otherwise
     */
    boolean processEdge(Edge<N,E> edge);
  }
}
