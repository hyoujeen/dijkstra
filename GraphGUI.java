import java.awt.*;
import java.awt.event.*;
import javax.swing.*;        
import java.io.*;

/**
 *  Class that runs a maze display/solution GUI.
 *
 *  @author Nicholas R. Howe, John Ridgway, You Jeen Ha
 *  @version CSC 212, 3 May 2018
 */
public class GraphGUI {
  /** Holds the graph GUI component */
  private GraphComponent graphComponent; 

  /** Holds the graph to solve */
  private ComplexGraph<PlacedData<Integer>,Integer> graph;

  /** The window */
  private JFrame frame;

  /** The node last selected by the user. */
  private Graph.Node<PlacedData<Integer>, Integer> chosenNode;

  /**
   *  Constructor that builds a completely empty graph.
   */
  public GraphGUI() {
    this.graph = new ComplexGraph<PlacedData<Integer>, Integer>();
    initializeGraph();
    this.graphComponent = new GraphComponent(this.graph);
  }

  /**
   *  Create and show the GUI.
   */
  public void createAndShowGUI() {
    // Create and set up the window.
    frame = new JFrame("Graph Application");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Add components
    createComponents(frame.getContentPane());

    // Display the window.
    frame.pack();
    frame.setVisible(true);
  }

  /**
   *  Create the components and add them to the frame.
   *
   *  @param pane the frame to which to add them
   */
  public void createComponents(Container pane) {
    pane.add(this.graphComponent);
    
   	// Mouse functionality
    MyMouseListener ml = new MyMouseListener();
    this.graphComponent.addMouseListener(ml);
    this.graphComponent.addMouseMotionListener(ml);
    
    // Overall window layout
    JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout());
    

    // Text display for shortest distances calculated by Dijkstra's Algorithm
    JTextArea dijkstraOutput = new JTextArea(10, 30);
    pane.add(dijkstraOutput, BorderLayout.NORTH);

    // Reset button
    JButton resetButton = new JButton("Reset");
    resetButton.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e) {
        	// Returns graph to originally painted state
          	for (Graph.Node<PlacedData<Integer>, Integer> node : graph.getNodes()) { 
            	node.getData().setColor(Color.cyan);
          	}
          	graphComponent.repaint();
          	// Returns text area to blank slate
          	dijkstraOutput.setText(" ");
        }
    });
    panel.add(resetButton);
    
    // Depth-first traversal button
    JButton dftButton = new JButton("DFT");
    dftButton.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e) {
          new Thread(new Runnable() {
              public void run() {
                if (chosenNode != null) {
                  new GraphImplementation().depthFirstTraversal(chosenNode, new NodeProcessor());
                }
              }
            }).start();
        }
    });
    panel.add(dftButton);
    
    // Breadth-first traversal button
    JButton bftButton = new JButton("BFT");
    bftButton.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e) {
          new Thread(new Runnable() {
              public void run() {
                if (chosenNode != null) {
                  new GraphImplementation().breadthFirstTraversal(chosenNode, new NodeProcessor());
                }
              }
            }).start();
        }
    });
    panel.add(bftButton);

    // Dijkstra Algorithm button that executes the algorithm
    JButton dijkstraButton = new JButton("Find shortest distance.");
    dijkstraButton.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
        	if (chosenNode != null) {
          		Dijkstra dijkstraAlgorithm = new Dijkstra();
          		dijkstraAlgorithm.executeDijkstra(graph, chosenNode);
          		// Display shortest distances in GraphApplication window
				dijkstraOutput.setText(" From chosen node: " + chosenNode.toString() + "\n" + "\n" + dijkstraAlgorithm.printDijkstra(chosenNode));
        	}
      	}
    });
    panel.add(dijkstraButton);

    // Adds overall window layout to window
    pane.add(panel, BorderLayout.SOUTH);
  }

  /**
   *  Execute the application.
   */
  public void execute() {
    // Schedule a job for the event-dispatching thread:
    // creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          createAndShowGUI();
        }
      });
  }

  /**
   *  The obligatory main method for the application.  With no
   *  arguments the application will read the graph from the standard
   *  input; with one argument (a file name) it will read the graph
   *  from the named file.
   *
   *  @param args  the command-line arguments
   */
  public static void main(String[] args) throws IOException {
    GraphGUI graphSolver;
    graphSolver = new GraphGUI();
    graphSolver.execute();
  }

  /**
   *  A mouse listener to handle click and drag actions on nodes.
   */
  private class MyMouseListener extends MouseAdapter {
    /** How far off the center of the node was the click? */
    private int deltaX;
    private int deltaY;

   /**
    * Event handler for mouse drag.
    * @param e Mouse drag event 
    */
    public void mouseDragged(MouseEvent e) {
      System.out.println("MouseDragged");
      if (chosenNode != null) {
        chosenNode.getData().setX(e.getX() + deltaX);
        chosenNode.getData().setY(e.getY() + deltaY);
      }
      graphComponent.repaint();
    }

   /**
    * Event handler for mouse click.
    * @param e Mouse click/press event
    */
    public void mousePressed(MouseEvent e) {
      System.out.println("MousePressed");
      for (Graph.Node<PlacedData<Integer>, Integer> node : graph.getNodes()) {
        double nodeX = node.getData().getX();
        double nodeY = node.getData().getY();
        double mouseX = e.getX();
        double mouseY = e.getY();
        if (Math.sqrt((nodeX-mouseX)*(nodeX-mouseX)
                      +(nodeY-mouseY)*(nodeY-mouseY))
            <= graphComponent.NODE_RADIUS) {
          chosenNode = node;
        }
      }
      if (chosenNode != null) {
        deltaX = chosenNode.getData().getX() - e.getX();
        deltaY = chosenNode.getData().getY() - e.getY();
      }
    }
  } // end of nested class MyMouseListener
 
 /**
  * Creates a graph, adding nodes and edges.
  */
  private void initializeGraph() {
    Graph.Node<PlacedData<Integer>, Integer> node1 = graph.addNode(new PlacedData<Integer>(1, 50, 50));
    Graph.Node<PlacedData<Integer>, Integer> node2 = graph.addNode(new PlacedData<Integer>(2, 150, 50));
    Graph.Node<PlacedData<Integer>, Integer> node3 = graph.addNode(new PlacedData<Integer>(3, 150, 150));
    Graph.Node<PlacedData<Integer>, Integer> node4 = graph.addNode(new PlacedData<Integer>(4, 50, 150));
    Graph.Node<PlacedData<Integer>, Integer> node5 = graph.addNode(new PlacedData<Integer>(5, 250, 50));
    Graph.Node<PlacedData<Integer>, Integer> node6 = graph.addNode(new PlacedData<Integer>(6, 250, 100));
    Graph.Node<PlacedData<Integer>, Integer> node7 = graph.addNode(new PlacedData<Integer>(7, 250, 150));
    Graph.Edge<PlacedData<Integer>, Integer> edge1 = graph.addEdge(1, node1, node2);
    Graph.Edge<PlacedData<Integer>, Integer> edge2 = graph.addEdge(2, node1, node3);
    Graph.Edge<PlacedData<Integer>, Integer> edge3 = graph.addEdge(3, node2, node4);
    Graph.Edge<PlacedData<Integer>, Integer> edge4 = graph.addEdge(4, node2, node5);
    Graph.Edge<PlacedData<Integer>, Integer> edge5 = graph.addEdge(5, node3, node6);
    Graph.Edge<PlacedData<Integer>, Integer> edge6 = graph.addEdge(6, node3, node7);
    Graph.Edge<PlacedData<Integer>, Integer> edge7 = graph.addEdge(7, node3, node1);
    Graph.Edge<PlacedData<Integer>, Integer> edge8 = graph.addEdge(8, node2, node6);
    // Graph.Edge<PlacedData<Integer>, Integer> edge9 = graph.addEdge(9, node4, node3);
    // Graph.Edge<PlacedData<Integer>, Integer> edge10 = graph.addEdge(10, node6, node7);
    // Graph.Edge<PlacedData<Integer>, Integer> edge11 = graph.addEdge(11, node3, node2);
    System.out.println(graph.validateGraph());
    System.out.println(graph.toString());
  }

 /**
  * Implements Processor interface within Graph interface,
  * for processing nodes in Depth-First and Breadth-First traversals
  * of the graph.
  */
  private class NodeProcessor implements Graph.Processor<PlacedData<Integer>, Integer> {
   /**
    * Processes edge.
    *
    * @param edge Edge to be processed
    * @return false Does nothing 
    */
    public boolean processEdge(Graph.Edge<PlacedData<Integer>, Integer> edge) {
      // do nothing
      return false;
    }
  
   /**
    * Preprocesses node and indicates preprocessing
    * process by painting the nodes in the GUI application.
    *
    * @param node Node to be processed
    * @return false
    */
    public boolean preProcessNode(Graph.Node<PlacedData<Integer>, Integer> node) {
      System.out.println("Node preprocessed.");
      node.getData().setColor(java.awt.Color.red);
      graphComponent.repaint();
      try {
        Thread.sleep(1000);
      } catch (Exception e) {
      }
      return false;
    }

   /**
    * Postprocesses node.
    *
    * @param node Node to be processed
    * @return false Does nothing
    */
    public boolean postProcessNode(Graph.Node<PlacedData<Integer>, Integer> node) {
      // do nothing
      return false;
    }
  } // end of nested class NodeProcessor

} // end of class GraphGUI 
