package examples

import functions.breadthFirstSearch
import graph.AdjacencyMatrixGraph
import graph.Edge

private val people = generateSocialNetworkOfSize(100, 50)

private val graph = AdjacencyMatrixGraph(people) { p -> p.friends}

fun main(){
    val nodes: List<Person> = graph.nodes
    val edges: List<Edge<Person>> = graph.edges

    val rootPerson = nodes[0]

    displayGraph(nodes, edges, "Graph", rootPerson) { p -> p.name}

    val predecessor: Array<Person?> = Array(nodes.size) { null }

    breadthFirstSearch(graph, rootPerson, predecessor = predecessor)

    val dfsEdges = convertPredecessorToEdgeList(graph, predecessor)

    displayGraph(nodes, dfsEdges, "BFS Graph", rootPerson){ p -> p.name}
}