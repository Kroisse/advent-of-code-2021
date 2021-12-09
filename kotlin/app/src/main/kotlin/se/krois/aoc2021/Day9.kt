package se.krois.aoc2021.day9

const val WIDTH = 100

data class Direction(val x: Int, val y: Int)

val DIRECTIONS = listOf(Direction(0, 1), Direction(1, 0), Direction(0, -1), Direction(-1, 0))

fun solve() {
    val guard = IntArray(WIDTH + 2) { Int.MAX_VALUE }
    val heightMap = mutableListOf(guard)

    while (true) {
        val line = readLine() ?: break
        val row = guard.clone()
        for ((i, ch) in line.withIndex()) {
            row[i + 1] = ch.digitToInt()
        }
        heightMap.add(row)
    }

    heightMap.add(guard)

    println(part2(heightMap))
}

fun lowPoints(heightMap: List<IntArray>): List<Pair<Int, Int>> {
    val lowPoints = mutableListOf<Pair<Int, Int>>()
    for (y in 1 until heightMap.size - 1) {
        for (x in 1 until heightMap[y].size - 1) {
            val height = heightMap[y][x]
            if (DIRECTIONS.all { (dx, dy) -> heightMap[y + dy][x + dx] > height }) {
                lowPoints.add(Pair(x, y))
            }
        }
    }
    return lowPoints
}

fun part1(heightMap: List<IntArray>): Int =
        lowPoints(heightMap).sumOf { (x, y) -> 1 + heightMap[y][x] }

fun basinSize(heightMap: List<IntArray>, pos: Pair<Int, Int>): Int {
    val queue = mutableListOf(pos)
    val visited = mutableSetOf(pos)
    while (queue.isNotEmpty()) {
        val (x, y) = queue.removeAt(0)
        for ((dx, dy) in DIRECTIONS) {
            val newX = x + dx
            val newY = y + dy
            if (heightMap[newY][newX] < 9) {
                val newPos = Pair(newX, newY)
                if (visited.add(newPos)) {
                    queue.add(newPos)
                }
            }
        }
    }
    return visited.size
}

fun part2(heightMap: List<IntArray>): Int =
        lowPoints(heightMap)
                .map { basinSize(heightMap, it) }
                .sortedDescending()
                .take(3)
                .reduce(Int::times)
