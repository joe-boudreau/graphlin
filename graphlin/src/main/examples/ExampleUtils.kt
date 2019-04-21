package examples

import graph.Edge
import graph.Graph
import org.graphstream.graph.Node
import org.graphstream.graph.implementations.SingleGraph

fun <T>convertPredecessorToEdgeList(graph: Graph<T>, predecessor: Array<T?>): List<Edge<T>> {

    val dfsEdges: MutableList<Edge<T>> = mutableListOf()
    var pred: T?
    for(index in predecessor.indices){
        pred = predecessor[index]
        if(pred != null){
            dfsEdges.add(Edge(pred, graph.nodes[index]))
        }
    }
    return dfsEdges.toList()
}


fun <T> displayGraph(nodes: List<T>, edges: List<Edge<T>>, title: String, root: T? = null, labelMaker: (n: T) -> String = { n: T -> n.toString()}) {
    val graph = SingleGraph(title)

    nodes.forEach { n -> graph.addNode<Node>(n.toString()).addAttribute("ui.label", labelMaker(n))}
    edges.forEach { e -> graph.addEdge<org.graphstream.graph.Edge>(e.toString(), e.u.toString(), e.v.toString(), true) }

    if(root != null){
        graph.getNode<Node>(root.toString()).addAttribute("ui.style", "fill-color: rgb(0,100,255);")
    }
    graph.display()
}