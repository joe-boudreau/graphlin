package graph

import koma.extensions.get
import koma.extensions.mapIndexed
import koma.extensions.set
import koma.matrix.Matrix
import kotlin.streams.toList

/**
 * Graph implementation backed by an adjacency matrix.
 *
 * Primary constructor accepts a list of nodes and list of edges. Secondary constructor accepts a list of nodes and an
 * adjacency list generator function which is used to determine the adjacency of each node in the given list.
 *
 * Can be directed or undirected. Defaults to undirected if not specified
 */
class AdjacencyMatrixGraph<T>(nodes: List<T>, directed: Boolean = false, edges: List<Edge<T>>) : Graph<T>(nodes, edges) {

    constructor(nodes: List<T>, directed: Boolean = false, adjacencyGenerator: (node: T) -> List<T>) :
            this(nodes, directed, nodes.stream()
                             .flatMap{ node: T -> (adjacencyGenerator(node).stream()
                                                                           .map{v: T -> Edge(node, v) })}
                             .toList())

    private val adjMatrix: Matrix<Int>
    private val indexMap: Map<T, Int>

    init{
        indexMap = HashMap()
        nodes.forEachIndexed { i, n -> indexMap.put(n, i)}

        adjMatrix = Matrix.invoke(nodes.size, nodes.size) { _, _ -> 0}

        edges.forEach { e ->  adjMatrix[nodeIndex(e.u),nodeIndex(e.v)] = 1
                              if(!directed){
                                  adjMatrix[nodeIndex(e.v),nodeIndex(e.u)] = 1
                              }}
    }

    override fun adjacency(node: T): List<T> {
        val rowIndex = nodeIndex(node)
        return adjMatrix.getRow(rowIndex)
                        .mapIndexed { _, c, e -> if (e == 1) c else rowIndex  }
                        .filterCols { c -> c[0] != rowIndex }
                        .mapColsToList { c -> nodes[c[0]] }
    }

    override fun adjacency(node: Int): List<Int> {
        return adjMatrix.getRow(node)
                .mapIndexed { _, c, e -> if (e == 1) c else node  }
                .filterCols { c -> c[0] != node }
                .mapColsToList { c -> c[0] }
    }

    override fun edgeExist(from: T, to: T) = adjMatrix[nodeIndex(from), nodeIndex(to)] == 1

    override fun nodeIndex(n: T): Int{
        return indexMap.getValue(n)
    }
}