package se.krois.aoc2021.day1

fun main() {
    val input = mutableListOf<Int>()
    while (true) {
        val n = readLine()?.toIntOrNull()
        if (n == null) {
            break
        }
        input.add(n)
    }

    println("Part 1: ${input.zipWithNext { a, b -> a < b }.count { it }}")

    println("Part 2: ${input.windowed(3) { it.sum() }.zipWithNext { a, b -> a < b }.count { it }}")
}
