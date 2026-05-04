fun <T> List<T>.withIndices(): List<Pair<Int, T>> {
    return this.mapIndexed { index, item -> Pair(index, item) }
}