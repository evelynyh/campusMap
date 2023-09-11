package graph.junitTests;

import graph.Graph;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;


/**
 * This class contains a set of test cases that can be used to test the implementation of the Graph
 * class.
 */
public class GraphTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

    public Graph<String, String> smallGraph() {
        Graph<String, String> g = new Graph<>();
        g.addNode(("node1"));
        g.addNode(("node2"));
        return g;
    }

    public Graph<Integer, String> intGraph() {
        Graph<Integer, String> g = new Graph<>();
        g.addNode((1));
        g.addNode((2));
        return g;
    }




    @Test
    public void testIsEmptyThenClear() {
        Graph<String, String> g = smallGraph();
        assertFalse(g.isEmpty());
        g.clear();
        assertTrue(g.isEmpty());
    }


    @Test
    public void testIsEmptyInt() {
        Graph<Integer, String> g = intGraph();
        assertFalse(g.isEmpty());
        g.clear();
        assertTrue(g.isEmpty());
    }

    @Test
    public void testIsEmptySmall() {
        Graph<String,String> g = new Graph<>();
        g.addEdge(new Graph.Edge<>(("node1"), ("node2"), ""));
        assertTrue(g.isEmpty());
        g= smallGraph();
        assertFalse(g.isEmpty());
        g.clear();
        assertTrue(g.isEmpty());
    }

    @Test
    public void testSizeSimple() {
        Graph<String,String> g = new Graph<>();
        g.addEdge(new Graph.Edge<>(("node1"), ("node2"),""));
        assertEquals(0, g.size());
        g.addNode(("node1"));
        g.addNode(("node2"));
        g.addNode(("node2"));
        assertEquals(2, g.size());
    }

    @Test
    public void testSizeInt() {
        Graph<Integer,String> g = new Graph<>();
        g.addEdge(new Graph.Edge<>((1), (2),""));
        assertEquals(0, g.size());
        g.addNode((2));
        g.addNode((1));
        g.addNode((2));
        assertEquals(2, g.size());
    }

    @Test
    public void testSizeSmall() {
        Graph<String, String> g = smallGraph();
        g.addNode(("node3"));
        g.addNode(("node4"));
        g.addEdge(new Graph.Edge<>(("node1"), ("node2"), ""));
        g.addNode(("node2"));
        assertEquals(4, g.size());
        g.clear();
        assertEquals(0, g.size());
        g.addNode(("node3"));
        g.addNode(("node3"));
        assertEquals(1, g.size());
    }

    @Test
    public void EdgeToStringSimple() {
        Graph<String, String> g = smallGraph();
        g.addEdge(new Graph.Edge<>(("node1"), ("node2"), "greetings"));
        assertEquals("{node2=[], node1=[node2:greetings]}", g.toString());
        String testNode = "";
        for (String node: g.getNodes()) {
            if (g.getNeighbors(node).size() > 0) {
                testNode = node;
            }
        }

        Graph.Edge<String, String>  test = null;
        for (Graph.Edge<String, String> e: g.getNeighbors(testNode)) {
            test = e;
        }
        assertEquals("[" + test.toString() + "]", g.edgesToString(testNode));
        //compares edge toString method with graph representation
    }


    @Test
    public void EdgeToStringInt() {
        Graph<Integer,String> g = intGraph();
        g.addEdge(new Graph.Edge<>((1), (2), "greetings"));
        assertEquals("{1=[2:greetings], 2=[]}", g.toString());
        int testNode = 0;
        for (int node: g.getNodes()) {
            if (g.getNeighbors(node).size() > 0) {
                testNode = node;
            }
        }

        Graph.Edge<Integer, String>  test = null;
        for (Graph.Edge<Integer, String>  e: g.getNeighbors(testNode)) {
            test = e;
        }
        assertEquals("[" + test + "]", g.edgesToString(testNode));
        //compares edge toString method with graph representation
    }

    @Test
    public void EdgesGetLabel() {
        Graph<String,String> g = smallGraph();
        g.addEdge(new Graph.Edge<>(("node1"), ("node2"), "greetings"));

        for (String node: g.getNodes()) {
            for (Graph.Edge<String, String>  e : g.getNeighbors(node)) {
                assertEquals("greetings", e.getLabel());
            }
        }
    }

    @Test
    public void EdgeEquals() {
        Graph<String, String> g = smallGraph();
        g.addEdge(new Graph.Edge<>(("node1"), ("node2"), ""));

        for (String node: g.getNodes()) {
            for (Graph.Edge<String, String>  e : g.getNeighbors(node)) {
                assertTrue(e.equals(new Graph.Edge<>(("node1"), ("node2"), "")));
                assertTrue((new Graph.Edge<>(("node1"), ("node2"), "")).equals(e));
                assertFalse(e.equals(new Graph.Edge<>(("node1"), ("node2"), "hello")));  //different label
                assertFalse((new Graph.Edge<>(("node1"), ("node2"), "hello")).equals(e));  //different label
                assertFalse(e.equals(new Graph.Edge<>(("node2"), ("node1"), "")));  //switched parameters
            }
        }
    }


    @Test
    public void IntEquals() {
        Graph<Integer, String> g = intGraph();
        g.addEdge(new Graph.Edge<>((1), (2), ""));

        for (int node: g.getNodes()) {
            for (Graph.Edge<Integer, String>  e : g.getNeighbors(node)) {
                assertTrue(e.equals(new Graph.Edge<>((1), (2), "")));
                assertTrue((new Graph.Edge<>((1), (2), "")).equals(e));
                assertFalse(e.equals(new Graph.Edge<>((1), (2), "hello")));  //different label
                assertFalse((new Graph.Edge<>((1), (2), "hello")).equals(e));  //different label
                assertFalse(e.equals(new Graph.Edge<>((2), (1), "")));  //switched parameters
            }
        }
    }

    @Test
    public void ContainsNodeSimple() {
        Graph<String, String> g = smallGraph();
        g.addNode(("node3"));
        g.addNode(("node3"));


        for (int i = 1; i < 4; i++) {
            assertTrue(g.containsNode(("node"+ i)));
        }
        assertFalse(g.containsNode(("node4")));
    }

    @Test
    public void ContainsNodeInt() {
        Graph<Integer, String> g = intGraph();
        g.addNode((3));
        g.addNode((4));


        for (int i = 1; i < 4; i++) {
            assertTrue(g.containsNode((1+ i)));
        }
        assertFalse(g.containsNode((5)));
    }

    @Test
    public void ContainsNodeEmptyGraph() {
        Graph<String, String> g = new Graph<>();
        assertFalse(g.containsNode(("node4")));
    }

    @Test
    public void ContainsEdgeSmall() {
        Graph<String, String> g = smallGraph();
        g.addNode(("node3"));

        g.addEdge(new Graph.Edge<>(("node1"),("node1"), ""));
        g.addEdge(new Graph.Edge<>(("node1"),("node2"), "edge label"));

        g.addEdge(new Graph.Edge<>(("node2"),("node1"), ""));
        g.addEdge(new Graph.Edge<>(("node2"),("node2"), "edge label"));

        g.addEdge(new Graph.Edge<>(("node3"),("node1"), ""));
        g.addEdge(new Graph.Edge<>(("node3"),("node2"), "edge label"));


        for (int i = 1; i < 4; i++) {
            assertTrue(g.containsEdge(new Graph.Edge<>(("node"+i),("node1"), "")));
            assertTrue(g.containsEdge(new Graph.Edge<>(("node"+i),("node2"), "edge label")));
        }
        assertFalse(g.containsEdge(new Graph.Edge<>(("node2"),("node3"), "edge label")));
        //switched to/from nodes

        assertFalse(g.containsEdge(new Graph.Edge<>(("node4"),("node2"), "edge label")));
        //changed from only

        assertFalse(g.containsEdge(new Graph.Edge<>(("node3"),("node3"), "edge label")));
        //changed to only

        assertFalse(g.containsEdge(new Graph.Edge<>(("node3"),("node1"), "not a label")));
        //changed label only
    }

    @Test
    public void ContainsEdgeEmptyGraph() {
        Graph<String, String> g = new Graph<>();
        assertFalse(g.containsEdge(new Graph.Edge<>(("node3"),("node1"), "not a label")));
    }

    @Test
    public void ContainsEdgeEmptyCharGraph() {
        Graph<Character, Integer> g = new Graph<>();
        assertFalse(g.containsEdge(new Graph.Edge<>(('w'),('a'), 3)));
    }
}
