package examples

import functions.djikstra
import graph.Edge
import graph.AdjacencyMatrixGraph
import koma.matrix.Matrix
import kotlin.random.Random

private val people = generateSocialNetworkOfSize(70, 30)

private val graph = AdjacencyMatrixGraph(people) { p -> p.friends }

fun main(){
    val nodes: List<Person> = graph.nodes
    val edges: List<Edge<Person>> = graph.edges

    val rootPerson = nodes[0]

    displayGraph(nodes, edges, "Graph", rootPerson) { p -> p.name}

    val predecessor: Array<Person?> = Array(nodes.size) { null }

    val random = Random.Default
    val weights = Matrix.invoke(nodes.size, nodes.size) { _, _ -> random.nextInt(10)+1}

    val distance = IntArray(graph.nodes.size)
    djikstra(graph, rootPerson, weights = weights, predecessor = predecessor, distance = distance)

    val dfsEdges = convertPredecessorToEdgeList(graph, predecessor)

    displayGraph(nodes, dfsEdges, "BFS Graph", rootPerson){ p -> p.name + ", d="+distance[graph.nodeIndex(p)]}
}