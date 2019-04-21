package graph

import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AdjacencyMatrixGraphTest{

    private val n0 = "hi"
    private val n1 = "im"
    private val n2 = "a"
    private val n3 = "graph"
    private val n4 = "of"
    private val n5 = "string"
    private val n6 = "nodes"
    private val n7 = "okay"

    private val nodes = listOf(n0, n1, n2, n3, n4,n5, n6, n7)

    private val edges = listOf(Edge(n0, n1),
            Edge(n0, n3),
            Edge(n0, n6),
            Edge(n2, n5),
            Edge(n3, n2),
            Edge(n7, n6),
            Edge(n6, n7),
            Edge(n4, n0))

    private val directedGraph = AdjacencyMatrixGraph(nodes, true, edges = edges)

    @Test
    fun adjacency() {

        assertEquals(listOf(n1, n3, n6), directedGraph.adjacency(n0))

        assertEquals(listOf(n5), directedGraph.adjacency(n2))
        assertEquals(listOf(n2), directedGraph.adjacency(n3))
        assertEquals(listOf(n0), directedGraph.adjacency(n4))
        assertEquals(listOf(n7), directedGraph.adjacency(n6))
        assertEquals(listOf(n6), directedGraph.adjacency(n7))

        assertEquals(emptyList<String>(), directedGraph.adjacency(n5))
        assertEquals(emptyList<String>(), directedGraph.adjacency(n1))
    }

    @Test
    fun edgeExist(){
        assertTrue { directedGraph.edgeExist(n0, n1) }
        assertFalse { directedGraph.edgeExist(n1, n0) }
        assertTrue { directedGraph.edgeExist(n2, n5) }
        assertTrue { directedGraph.edgeExist(n4, n0) }
        assertFalse { directedGraph.edgeExist(n3, n7) }
    }
}