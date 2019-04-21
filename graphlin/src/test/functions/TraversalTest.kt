package functions

import org.junit.Test
import graph.AdjacencyMatrixGraph
import graph.Edge
import kotlin.test.assertEquals
import kotlin.test.assertNull


class TraversalTest {

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
            Edge(n3, n2),
            Edge(n7, n6),
            Edge(n6, n7),
            Edge(n4, n0),
            Edge(n4, n5))

    private val g = AdjacencyMatrixGraph(nodes, edges = edges)

    @Test
    fun depthFirstSearch() {

        val predecessor: Array<String?> = Array(nodes.size) { null }
        val discovery = IntArray(nodes.size)
        val finish = IntArray(nodes.size)

        depthFirstSearch(g, discovery, finish, predecessor)

        for(i in 0 until nodes.size){
            print((predecessor[i] ?: "none") + " <-- " + g.nodes[i])
            println(" discovery: " + discovery[i] + ",  finish: " + finish[i])
        }

        assertNull(predecessor[0])
        assertEquals(n0, predecessor[1])
    }

    @Test
    fun breadthFirstSearch() {
        val predecessor: Array<String?> = Array(nodes.size) { null }
        val depth = Array<Int?>(nodes.size){null}
        breadthFirstSearch(g, n0, depth, predecessor)

        for(i in 0 until nodes.size){
            print((predecessor[i] ?: "none") + " <-- " + g.nodes[i])
            println(" depth: " + depth[i])
        }
    }

}