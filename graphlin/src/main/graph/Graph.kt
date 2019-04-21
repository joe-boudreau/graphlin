package graph

/**
 * An immutable graph
 */
abstract class Graph<T>(val nodes: List<T>, val edges: List<Edge<T>>) {

    /**
     * Returns the neighbours of a given node as a list of nodes
     */
    abstract fun adjacency(node: T): List<T>

    /**
     * Returns the neighbours of a given node, referenced by its index. Returns a list of indices of neighbouring nodes
     */
    abstract fun adjacency(node: Int): List<Int>

    /**
     * Returns true if an edge exists between the two given nodes. False otherwise
     */
    abstract fun edgeExist(from: T, to: T): Boolean

    /**
     * Returns the internal index of the given node. This is backed by a hashmap so it is an O(1) operation
     */
    abstract fun nodeIndex(node: T): Int


}

/**
 * Default Edge implementation. Represents an edge between node u and node v.
 * If the edge is to be interpreted as directed, the direction is from u ---> v
 */
data class Edge<T>(val u: T, val v: T)
