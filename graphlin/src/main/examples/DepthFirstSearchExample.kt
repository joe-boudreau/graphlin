package examples

import functions.depthFirstSearch
import graph.AdjacencyMatrixGraph

private val people = generateSocialNetworkOfSize(100, 50)

private val graph = AdjacencyMatrixGraph(people) { p -> p.friends}

fun main(){

    val nodes = graph.nodes
    val edges = graph.edges

    displayGraph(nodes, edges, "Graph") { p -> p.name}

    val predecessor: Array<Person?> = Array(nodes.size) { null }

    depthFirstSearch(graph, predecessor = predecessor)

    val dfsEdges = convertPredecessorToEdgeList(graph, predecessor)

    displayGraph(nodes, dfsEdges, "DFS Graph") { p -> p.name}
}