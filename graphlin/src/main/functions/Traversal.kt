package functions

import graph.Graph
import java.util.concurrent.ArrayBlockingQueue
import kotlin.test.assertEquals

const val WHITE = 0
const val GREY = 1
const val BLACK = 2

fun <T>depthFirstSearch(g: Graph<T>, discovery: IntArray = IntArray(g.nodes.size),
                        finish: IntArray = IntArray(g.nodes.size),
                        predecessor: Array<T?>){

    val color = IntArray(g.nodes.size)
    initDFS(g.nodes.size, color, discovery, finish, predecessor)

    var time = 0

    fun visit(i: Int) {
        time++
        discovery[i] = time
        color[i] = GREY
        for (node in g.adjacency(g.nodes[i])){
            val j = g.nodeIndex(node)
            if(color[j] == WHITE){
                predecessor[j] = g.nodes[i]
                visit(j)
            }
        }
        color[i] = BLACK
        time++
        finish[i] = time
    }


    for(node in g.nodes){
        val i = g.nodeIndex(node)
        if(color[i] == WHITE){
            visit(i)
        }
    }
}

private fun <T> initDFS(numNodes: Int, color: IntArray, discovery: IntArray, finish: IntArray, predecessor: Array<T?>) {
    assertEquals(color.size, numNodes)
    assertEquals(discovery.size, numNodes)
    assertEquals(finish.size, numNodes)
    assertEquals(predecessor.size, numNodes)

    for (n in 0 until numNodes) {
        color[n] = WHITE
        discovery[n] = 0
        finish[n] = 0
        predecessor[n] = null
    }
}


fun <T>breadthFirstSearch(g: Graph<T>, root: T, depth: Array<Int?> = Array(g.nodes.size){null},
                          predecessor: Array<T?>){

    val color = IntArray(g.nodes.size)
    var nI: Int
    for (n in g.nodes.subtract(arrayListOf(root))){
        nI = g.nodeIndex(n)
        color[nI] = WHITE
        depth[nI] = null
        predecessor[nI] = null
    }

    val s = g.nodeIndex(root)
    color[s] = GREY
    depth[s] = 0
    predecessor[s] = null
    val q = ArrayBlockingQueue<T>(g.nodes.size)
    q.put(root)
    var u: T
    while (q.isNotEmpty()){
        u = q.remove()
        val uI = g.nodeIndex(u)

        for (v in g.adjacency(u)){
            val vI = g.nodeIndex(v)
            if (color[vI] == WHITE){
                color[vI] = GREY
                depth[vI] = depth[uI]?.plus(1)
                predecessor[vI] = u
                q.put(v)
            }
        }
        color[uI] = BLACK
    }


}


