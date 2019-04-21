@file:koma.internal.JvmName("NDArray")
@file:koma.internal.JvmMultifileClass

/**
 * THIS FILE IS AUTOGENERATED, DO NOT MODIFY. EDIT THE FILES IN templates/
 * AND RUN ./gradlew :codegen INSTEAD!
 */

package koma.extensions

import koma.internal.default.utils.reduceArrayAxis
import koma.internal.default.utils.argMin${dtypeName}
import koma.internal.default.utils.argMax${dtypeName}
import koma.internal.default.utils.wrapIndex
import koma.ndarray.NDArray
import koma.ndarray.${factoryPrefix}NDArrayFactory
import koma.pow
import koma.matrix.Matrix
import koma.util.IndexIterator

$toMatrix

@koma.internal.JvmName("fill${dtypeName}")
${inline}fun ${genDec} NDArray<${dtype}>.fill(f: (idx: IntArray) -> ${dtype}) = apply {
    for ((nd, linear) in this.iterateIndices())
        this.set${dtypeName}(linear, f(nd))
}

@koma.internal.JvmName("fill${dtypeName}Both")
${inline}fun ${genDec} NDArray<${dtype}>.fillBoth(f: (nd: IntArray, linear: Int) -> ${dtype}) = apply {
    for ((nd, linear) in this.iterateIndices())
        this.set${dtypeName}(linear, f(nd, linear))
}

@koma.internal.JvmName("fill${dtypeName}Linear")
${inline}fun ${genDec} NDArray<${dtype}>.fillLinear(f: (idx: Int) -> ${dtype}) = apply {
    for (idx in 0 until size)
        this.set${dtypeName}(idx, f(idx))
}

@koma.internal.JvmName("create${dtypeName}")
${inline}fun ${genDec} ${factoryPrefix}NDArrayFactory<${dtype}>.create(vararg lengths: Int, filler: (idx: IntArray) -> ${dtype})
    = $extensionCreate

/**
 * Returns a new NDArray with the given shape, populated with the data in this array.
 *
 * @param dims Desired dimensions of the output array.
 *
 * @returns A copy of the elements in this array, shaped to the given number of rows and columns,
 *          such that `this.toList() == this.reshape(*dims).toList()`
 *
 * @throws IllegalArgumentException when the product of all of the given `dims` does not equal [size]
 */
@koma.internal.JvmName("reshape${dtypeName}")
${reifiedInline}fun $reifiedDec NDArray<${dtype}>.reshape(vararg dims: Int): NDArray<${dtype}> {
    if (dims.reduce { a, b -> a * b } != size)
        throw IllegalArgumentException("\$size items cannot be reshaped to \${dims.toList()}")
    var idx = 0
    return ${factoryPattern("*dims")} { _ -> get${dtypeName}(idx++) }
}


/**
 * Takes each element in a NDArray, passes them through f, and puts the output of f into an
 * output NDArray.
 *
 * @param f A function that takes in an element and returns an element
 *
 * @return the new NDArray after each element is mapped through f
 */
@koma.internal.JvmName("map${dtypeName}")
inline fun ${mapDec} NDArray<${dtype}>.map(crossinline f: (${dtype}) -> R)
    = NDArray.createLinear(*shape().toIntArray(), filler={ f(this.get${dtypeName}(it)) } )

/**
 * Takes each element in a NDArray, passes them through f, and puts the output of f into an
 * output NDArray. Index given to f is a linear index, depending on the underlying storage
 * major dimension.
 *
 * @param f A function that takes in an element and returns an element. Function also takes
 *      in the linear index of the element's location.
 *
 * @return the new NDArray after each element is mapped through f
 */
@koma.internal.JvmName("mapIndexed${dtypeName}")
${inline}fun ${genDec} NDArray<${dtype}>.mapIndexed(f: (idx: Int, ele: ${dtype}) -> ${dtype})
$extensionMapIndexed

/**
 * Takes each element in a NDArray and passes them through f.
 *
 * @param f A function that takes in an element
 *
 */
@koma.internal.JvmName("forEach${dtypeName}")
${inline}fun ${genDec} NDArray<${dtype}>.forEach(f: (ele: ${dtype}) -> Unit) {
    // TODO: Change this back to iteration once there are non-boxing iterators
    for (idx in 0 until size)
        f(get${dtypeName}(idx))
}
/**
 * Takes each element in a NDArray and passes them through f. Index given to f is a linear
 * index, depending on the underlying storage major dimension.
 *
 * @param f A function that takes in an element. Function also takes
 *      in the linear index of the element's location.
 *
 */
@koma.internal.JvmName("forEachIndexed${dtypeName}")
${inline}fun $genDec NDArray<${dtype}>.forEachIndexed(f: (idx: Int, ele: ${dtype}) -> Unit) {
    // TODO: Change this back to iteration once there are non-boxing iterators
    for (idx in 0 until size)
        f(idx, get${dtypeName}(idx))
}

/**
 * Takes each element in a NDArray, passes them through f, and puts the output of f into an
 * output NDArray. Index given to f is the full ND index of the element.
 *
 * @param f A function that takes in an element and returns an element. Function also takes
 *      in the ND index of the element's location.
 *
 * @return the new NDArray after each element is mapped through f
 */
@koma.internal.JvmName("mapIndexedN${dtypeName}")
${inline}fun $genDec NDArray<${dtype}>.mapIndexedN(f: (idx: IntArray, ele: ${dtype}) -> ${dtype}): NDArray<${dtype}>
$extensionMapIndexedN

/**
 * Takes each element in a NDArray and passes them through f. Index given to f is the full
 * ND index of the element.
 *
 * @param f A function that takes in an element. Function also takes
 *      in the ND index of the element's location.
 *
 */
@koma.internal.JvmName("forEachIndexedN${dtypeName}")
${inline}fun $genDec NDArray<${dtype}>.forEachIndexedN(f: (idx: IntArray, ele: ${dtype}) -> Unit) {
    for ((nd, linear) in iterateIndices())
        f(nd, get${dtypeName}(linear))
}

/**
 * Converts this NDArray into a one-dimensional ${arrayClass} in row-major order.
 */
${reifiedInline}fun $reifiedDec NDArray<${dtype}>.to${arrayType}Array() = ${arrayClass}(size) { get${dtypeName}(it) }

fun <T> NDArray<T>.getSlice${dtypeName}(vararg indices: Any): NDArray<${dtype}> {
    if (indices.size != shape().size)
        throw IllegalArgumentException("Specified \${indices.size} indices for an array with \${shape().size} dimensions")
    val indexArrays = mutableListOf<IntArray>()
    val outputShape = mutableListOf<Int>()
    val outputDims = mutableListOf<Int>()
    val inputIndex = kotlin.IntArray(indices.size)

    // Convert the inputs to arrays of integer indices.

    for (i in 0 until indices.size) {
        val index = indices[i]
        val size = shape()[i]
        if (index is Int) {
            inputIndex[i] = index
            indexArrays.add(kotlin.intArrayOf(wrapIndex(index, size)))
        }
        else if (index is Iterable<*>) {
            outputDims.add(i)
            indexArrays.add(index.map { wrapIndex(it as Int, size) }.toIntArray())
            outputShape.add(indexArrays.last().size)
        }
        else
            throw IllegalArgumentException("All indices must be Int or Iterable<Int>")
    }
    if (outputShape.size == 0)
        throw IllegalArgumentException("A slice must have at least one dimension")

    // Create the output array.

    val lengths = outputShape.toIntArray()
    val filler = { index: IntArray ->
        for (i in 0 until outputDims.size)
            inputIndex[outputDims[i]] = indexArrays[outputDims[i]][index[i]]
        get${dtypeName}(*inputIndex)
    }
    return $extensionCreate
}

fun <T> NDArray<T>.setSlice${dtypeName}(vararg indices: Any, value: ${dtype}) {
    if (indices.size != shape().size)
        throw IllegalArgumentException("Specified \${indices.size} indices for an array with \${shape().size} dimensions")
    val indexArrays = mutableListOf<IntArray>()

    // Convert the inputs to arrays of integer indices.

    for (i in 0 until indices.size) {
        val index = indices[i]
        val size = shape()[i]
        if (index is Int)
            indexArrays.add(kotlin.intArrayOf(wrapIndex(index, size)))
        else if (index is Iterable<*>)
            indexArrays.add(index.map { wrapIndex(it as Int, size) }.toIntArray())
        else
            throw IllegalArgumentException("All indices must be Int or Iterable<Int>")
    }

    // Set the elements.

    val lengths = IntArray(indices.size, { indexArrays[it].size })
    val element = IntArray(lengths.size)
    for ((nd, linear) in IndexIterator(lengths)) {
        for (i in 0 until element.size)
            element[i] = indexArrays[i][nd[i]]
        set${dtypeName}(*element, v=value)
    }
}

fun <T> NDArray<T>.setSlice${dtypeName}(vararg indices: Any, value: NDArray<${dtype}>) {
    if (indices.size != shape().size)
        throw IllegalArgumentException("Specified \${indices.size} indices for an array with \${shape().size} dimensions")
    val indexArrays = mutableListOf<IntArray>()

    // Convert the inputs to arrays of integer indices.

    for (i in 0 until indices.size) {
        val index = indices[i]
        val size = shape()[i]
        if (index is Int)
            indexArrays.add(kotlin.intArrayOf(wrapIndex(index, size)))
        else if (index is Iterable<*>)
            indexArrays.add(index.map { wrapIndex(it as Int, size) }.toIntArray())
        else
            throw IllegalArgumentException("All indices must be Int or Iterable<Int>")
    }

    // Make sure the shapes match, after eliminating dimensions of size 1.

    val lengths = IntArray(indices.size, { indexArrays[it].size })
    val outputDims = lengths.filter { it != 1 }
    val inputDims = value.shape().filter { it != 1}
    if (!(outputDims.toIntArray() contentEquals inputDims.toIntArray()))
        throw IllegalArgumentException("Cannot assign a value of shape \${inputDims.toList()} to a slice of shape \${outputDims.toList()}")

    // Set the elements.

    val element = IntArray(lengths.size)
    for ((nd, linear) in IndexIterator(lengths)) {
        for (i in 0 until element.size)
            element[i] = indexArrays[i][nd[i]]
        set${dtypeName}(*element, v=value.get${dtypeName}(linear))
    }
}

@koma.internal.JvmName("set${dtypeName}")
operator fun $genDec NDArray<${dtype}>.set(vararg indices: Int, value: NDArray<${dtype}>) {
    val lastIndex = indices.mapIndexed { i, range -> range + value.shape()[i] }
    val outOfBounds = lastIndex.withIndex().any { it.value > shape()[it.index] }
    if (outOfBounds)
        throw IllegalArgumentException("NDArray with shape \${shape()} cannot be " +
                "set at \${indices.toList()} by a \${value.shape()} array " +
                "(out of bounds)")

    val offset = indices.map { it }.toIntArray()
    value.forEachIndexedN { idx, ele ->
        val newIdx = offset.zip(idx).map { it.first + it.second }.toIntArray()
        this.setGeneric(indices=*newIdx, v=ele)
    }
}

/**
 * Find the linear index of the minimum element in this array.
 */
@koma.internal.JvmName("argMin${dtypeName}")
fun $comparableDec NDArray<${dtype}>.argMin(): Int = argMinInternal()

/**
 * Find the linear index of the minimum element along one axis of this array,  returning the result in a new array.
 * If the array contains non-comparable values, this throws an exception.
 * 
 * @param axis      the axis to compute the minimum over
 * @param keepdims  if true, the output array has the same number of dimensions as the original one,
 *                  with [axis] having size 1.  If false, the output array has one fewer dimensions
 *                  than the original one.
 */
@koma.internal.JvmName("argMinAxis${dtypeName}")
fun $genDec NDArray<${dtype}>.argMin(axis: Int, keepdims: Boolean): NDArray<Int> =
    reduceArrayAxis(this, { length: Int, get: (Int) -> ${dtype} -> argMin${dtypeName}(length, get) }, axis, keepdims)

/**
 * Find the value of the minimum element in this array.
 */
@koma.internal.JvmName("min${dtypeName}")
fun $comparableDec NDArray<${dtype}>.min(): ${dtype} = minInternal()

/**
 * Find the minimum element along one axis of this array, returning the result in a new array.
 * If the array contains non-comparable values, this throws an exception.
 *
 * @param axis      the axis to compute the minimum over
 * @param keepdims  if true, the output array has the same number of dimensions as the original one,
 *                  with [axis] having size 1.  If false, the output array has one fewer dimensions
 *                  than the original one.
 */
@koma.internal.JvmName("minAxis${dtypeName}")
inline fun $reifiedDec NDArray<${dtype}>.min(axis: Int, keepdims: Boolean): NDArray<${dtype}> =
    reduceArrayAxis(this, { length: Int, get: (Int) -> ${dtype} -> get(argMin${dtypeName}(length, get)) }, axis, keepdims)

/**
 * Find the linear index of the maximum element in this array.
 */
@koma.internal.JvmName("argMax${dtypeName}")
fun $comparableDec NDArray<${dtype}>.argMax(): Int = argMaxInternal()

/**
 * Find the linear index of the maximum element along one axis of this array, returning the result in a new array.
 * If the array contains non-comparable values, this throws an exception.
 * 
 * @param axis      the axis to compute the maximum over
 * @param keepdims  if true, the output array has the same number of dimensions as the original one,
 *                  with [axis] having size 1.  If false, the output array has one fewer dimensions
 *                  than the original one.
 */
@koma.internal.JvmName("argMaxAxis${dtypeName}")
fun $genDec NDArray<${dtype}>.argMax(axis: Int, keepdims: Boolean): NDArray<Int> =
    reduceArrayAxis(this, { length: Int, get: (Int) -> ${dtype} -> argMax${dtypeName}(length, get) }, axis, keepdims)

/**
 * Find the value of the maximum element in this array.
 */
@koma.internal.JvmName("max${dtypeName}")
fun $comparableDec NDArray<${dtype}>.max(): ${dtype} = maxInternal()

/**
 * Find the maximum element along one axis of this array, returning the result in a new array.
 * If the array contains non-comparable values, this throws an exception.
 *
 * @param axis      the axis to compute the maximum over
 * @param keepdims  if true, the output array has the same number of dimensions as the original one,
 *                  with [axis] having size 1.  If false, the output array has one fewer dimensions
 *                  than the original one.
 */
@koma.internal.JvmName("maxAxis${dtypeName}")
inline fun $reifiedDec NDArray<${dtype}>.max(axis: Int, keepdims: Boolean): NDArray<${dtype}> =
    reduceArrayAxis(this, { length: Int, get: (Int) -> ${dtype} -> get(argMax${dtypeName}(length, get)) }, axis, keepdims)

operator fun $genDec NDArray<${dtype}>.get(vararg indices: Int) = get${dtypeName}(*indices)
operator fun $genDec NDArray<${dtype}>.set(vararg indices: Int, value: ${dtype}) = set${dtypeName}(indices=*indices, v=value)

$operators