package se.krois.aoc2021.day12

typealias Graph = Map<String, Set<String>>

fun solve() {
    val graph = read()
    val paths = findPaths(graph, "start", setOf(), false)
    println("Paths: $paths")
}

fun read(): Graph {
    val graph = mutableMapOf<String, MutableSet<String>>()

    while (true) {
        val line = readLine() ?: break
        val (a, b) = line.trim().split("-")
        graph.getOrPut(a, ::mutableSetOf).add(b)
        graph.getOrPut(b, ::mutableSetOf).add(a)
    }
    return graph
}

fun findPaths(
        graph: Graph,
        node: String,
        visited: Set<String>,
        smallCaveVisited: Boolean,
): Int {
    if (node == "end") {
        return 1
    }
    if (visited.contains(node)) {
        if (smallCaveVisited || node == "start") {
            return 0
        }
        return graph[node]?.sumOf { findPaths(graph, it, visited, true) } ?: 0
    }
    val isSmallCave = node.first().isLowerCase()
    val nextVisited = if (isSmallCave) visited + node else visited
    return graph[node]?.sumOf { findPaths(graph, it, nextVisited, smallCaveVisited) } ?: 0
}
