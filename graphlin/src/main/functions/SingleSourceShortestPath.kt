package functions

import graph.Graph
import koma.extensions.get
import koma.matrix.Matrix

fun <T>djikstra(g: Graph<T>, root: T, weights: Matrix<Int>, predecessor: Array<T?>, distance: IntArray = IntArray(g.nodes.size)){

    initializeSingleSource(g, root, distance, predecessor)
    val Q = mutableListOf<Int>() //TODO: Implement better data structure for extracting min and decrease key operations
    Q.addAll(g.nodes.indices)

    var u: Int
    while(Q.isNotEmpty()){
        u = Q.minBy { distance[it] }!!
        Q.remove(u)
        for (v in g.adjacency(u)){
            relax(g, u, v, distance, weights, predecessor)
        }
    }
}

fun <T>bellmanFord(g: Graph<T>, root: T, weights: Matrix<Int>, predecessor: Array<T?>, distance: IntArray = IntArray(g.nodes.size)): Boolean{
    initializeSingleSource(g, root, distance, predecessor)
    for(i in 1 until g.nodes.size){
        for(edge in g.edges){
            relax(g, g.nodeIndex(edge.u), g.nodeIndex(edge.v), distance, weights, predecessor)
        }
    }
    for(edge in g.edges){
        val vI = g.nodeIndex(edge.v)
        val uI = g.nodeIndex(edge.u)
        if(distance[vI] > distance[uI] + weights[uI, vI]){
            return false //Negative weight cycle exists
        }
    }
    return true
}


private fun <T>initializeSingleSource(g: Graph<T>, root: T, distance: IntArray, predecessor: Array<T?>){
    for(v in g.nodes){
        distance[g.nodeIndex(v)] = Int.MAX_VALUE
        predecessor[g.nodeIndex(v)] = null
    }
    distance[g.nodeIndex(root)] = 0
}

private fun <T>relax(g: Graph<T>, u: Int, v: Int, distance: IntArray, weights: Matrix<Int>, predecessor: Array<T?>){
    if(distance[v] > distance[u] + weights[u, v]){
        distance[v] = distance[u] + weights[u,v]
        predecessor[v] = g.nodes[u]
    }
}