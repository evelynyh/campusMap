package graph;

import java.util.*;

/**
 * <b>Graph</b> is an unsorted mutable network of nodes and edges (which connect 2 nodes).
 *
 * <p>Edges are one-way, meaning an edge from B to A is different from A to B.
 *
 * <p>Edges are considered different if they also have a different label. For example:
 * an edge from A to B with the label "cookie" is different from an edge from A to B
 * with the label "square" is different from an edge from B to A with the label "square."
 *
 * <p> There are no duplicate edges or nodes present in the graph.
 *
 * @param <E> datlabel/weight type in edges
 * @param <N> data types to be stored in nodes
 *
 */


public class Graph<N, E> {

    /**
     * <b>Edge</b> is an immutable edge between nodes in the graph.
     *
     * @param <E> datlabel/weight type in edges
     * @param <N> data types to be stored in nodes
     */

    public static class Edge<N, E> {


        // Abstraction Function:
        // this represents an edge with a "label" going "from" node "to" node
        // "label" of the edge.
        // "from" node is a present node in the graph. This is outgoing from "from."
        // "to" node is a present node in the graph. This is going to "to."
        //
        // Representation Invariant for every Edge e:
        // e != null &&
        // from != null &&
        // to != null &&
        // label != null

        /**
         * Node of where the edge is coming from.
         */
        public final N from;

        /**
         * Node of where the edge is going to.
         */
        public final N to;

        /**
         * Edge label.
         */
        final E label;


        /**
         * Constructs a new edge between nodes with a label.
         *
         * @param from starting of our edge.
         * @param to end of our edge.
         * @param label of our edge.
         * @spec.effects Constructs a new edge with a label.
         */
        public Edge(N from, N to, E label) {
            this.from = from;
            this.to = to;
            this.label = label;
            checkRep();
        }


        /**
         * Throws an exception if the representation invariant is violated.
         */
        private void checkRep() {
            assert (from != null) : "from is null";
            assert (to != null) : "to is null";
            assert (label != null) : "label is null";
        }


        /**
         * Shows whether the edges are equal to each other.
         * edges are equal when their to, from nodes and label are equal
         *
         * @spec.requires other to not be null
         * @param other the edge we are comparing to.
         * @return whether the edges are equal to each other.
         */
        @Override
        public boolean equals(Object other) {
            checkRep();
            if (other instanceof Edge) {
                return hashCode() == (other).hashCode();
            }
            checkRep();
            return false;
        }


        /**
         * Gives a hash code value.
         *
         * @return a hash code value.
         */
        @Override
        public int hashCode() {
            checkRep();
            return(to.hashCode() + 3 * from.hashCode() + 5 * label.hashCode());
        }

        /**
         * Gets label.
         *
         * @return label from the edge.
         */
        public E getLabel() {
            checkRep();
            return label;
        }


        /**
         * Turns edge into string representation.
         * to:label
         *
         * @return string representation of the edge.
         */
        @Override
        public String toString() {
            checkRep();
            return to + ":" + label;
        }
    }

// Abstraction Function:
// Graph, graph, represents a network of various nodes and
// Edges:
// edges with a "label" going "from" node "to" node
// If these nodes are not present in the graph, there is
// no edge created.
//
// from node maps to a set of outgoing Edges.
//
// Representation Invariant for every Graph g:
// g != null &&
// size >= 0 &&
// nodes in graph != null &&
// Edges in g do not contain null values (including label, to, from) &&
// Edges must not refer to nodes not contained in the graph &&
// There are no duplicate Edges (same to, from, and label) &&
// There are no duplicate nodes &&
// nodes map to outgoing edges (i.e. edges "from" node is equal to the graph node key)

    /**
     * Graph structure.
     */
    private final Map<N, Set<Edge<N, E>>> graph;

    /**
     * For checkRep.
     */
    private final boolean check = false;


    /**
     * Constructs a new Graph.
     *
     * @spec.effects Constructs a new Graph that is empty.
     */
    public Graph() {
        graph = new HashMap<>();
        checkRep();
    }


    /**
     * Throws an exception if the representation invariant is violated.
     */
    private void checkRep() {
        assert (this.graph != null);
        assert (this.graph.size() > -1);
        if (check) {
            for(N node : graph.keySet()) {
                assert (node != null) : "null node";
                for(Edge<N, E> e : graph.get(node)) {
                    assert (e != null) : "null Edge";
                    assert (e.to != null && e.from != null && e.label != null) : "null Edge values";
                    assert (graph.containsKey(e.to) &&
                            graph.containsKey(e.from)) : "nodes referenced in edges are not in graph";
                    // sets do not contain duplicates
                    // maps do not contain duplicate keys
                    assert (e.from.equals(node)) : "edge stored in node is not the outgoing edge of that node";
                }
            }
        }
    }


    /**
     * Gets the amount of nodes in the graph.
     *
     * @return the amount of nodes in the graph.
     */
    public int size() {
        checkRep();
        return graph.size();
    }

    /**
     * Finds out if the given edge is present in the graph.
     *
     * @spec.requires edge to not be null
     * @param edge we are looking for.
     * @return whether the edge is in the graph.
     */
    public boolean containsEdge(Edge<N, E> edge) {
        checkRep();
        if (graph.containsKey(edge.from)) {
            return graph.get(edge.from).contains(edge);
        }
        checkRep();
        return false;
    }


    /**
     * Finds out if the given node is present in the graph.
     *
     * @spec.requires node to not be null
     * @param node we are looking for.
     * @return whether the node is in the graph.
     */
    public boolean containsNode(N node) {
        checkRep();
        return graph.containsKey(node);
    }



    /**
     * Adds given node to the graph. Does not add if node exists.
     *
     * @spec.requires node to not be null.
     * @param node we are adding.
     * @spec.modifies this
     * @spec.effects graph has one additional node.
     */
    public void addNode(N node) {
        checkRep();
        if (!graph.containsKey(node)) {
            graph.put(node, new HashSet<>());
        }
        checkRep();
    }


    /**
     * Adds given edge to a node. Does not add if edge exists.
     * Does not add edge if edge contains a node that isn't present.
     *
     * @spec.requires edge to not be null.
     * @param edge we are adding in the graph.
     * @spec.modifies this
     * @spec.effects graph has one additional edge.
     */
    public void addEdge(Edge<N, E> edge) {
        checkRep();
        if (graph.containsKey(edge.from) && graph.containsKey(edge.to)) {
            graph.get(edge.from).add(edge);
        }
        checkRep();
    }


    /**
     * Gets all the edges from a given node.
     *
     * @spec.requires node to not be null.
     * @param node we are getting edge from.
     * @return Collection of all edges.
     */
    public Set<Edge<N, E>> getNeighbors(N node) {
        checkRep();
        return Collections.unmodifiableSet(graph.get(node));
    }


    /**
     * Gets all the node from the graph.
     *
     * @return Collection of all nodes.
     */
    public Collection<N> getNodes() {
        checkRep();
        return Collections.unmodifiableSet(graph.keySet());
    }


    /**
     * turns graph into a string representation.
     *
     * <p> nodes are shown with all of their edges. e.g.:
     * {node1=[edge1, edge2], node2=[edge1], ... nodeN=[edge1, ... edgeN]}
     *
     * @spec.requires graph to not be null.
     * @return string representation of graph.
     */
    @Override
    public String toString() {
        checkRep();
        return graph.toString();
    }


    /**
     * turns edges from a given node into a string representation.
     *
     * <p> edges shown listed out:
     * [edge1, edge2, edge3 ...]
     *
     * @spec.requires node to not be null.
     * @param node we want edges from.
     * @return string representation of all edges in a node.
     */
    public String edgesToString(N node) {
        checkRep();
        return graph.get(node).toString();
    }


    /**
     * Asks whether the graph contains nodes.
     *
     * @return whether the graph contains nodes.
     */
    public boolean isEmpty() {
        checkRep();
        return (graph.size() == 0);
    }


    /**
     * Clears graph.
     * @spec.effects clears graph of all edges and nodes.
     */
    public void clear() {
        checkRep();
        graph.clear();
    }
}
