package se.krois.aoc2021.day13

fun solve() {
    var dots = mutableSetOf<Pair<Int, Int>>()
    while (true) {
        val pos = readLine()?.trim()?.split(",")?.map(String::toIntOrNull) ?: break
        if (pos.size < 2) break
        val (x, y) = pos
        if (x == null || y == null) break
        dots.add(Pair(x, y))
    }

    val prefix = "fold along "
    while (true) {
        val inst = readLine()?.trim() ?: break
        if (inst.startsWith(prefix).not()) break
        val (axis, n) = inst.slice(prefix.length until inst.length).split("=")
        val k = n.toIntOrNull() ?: break
        println("$axis $k")
        when (axis) {
            "x" -> {
                dots =
                        dots
                                .map { (x, y) -> if (x > k) Pair(2 * k - x, y) else Pair(x, y) }
                                .toMutableSet()
            }
            "y" -> {
                dots =
                        dots
                                .map { (x, y) -> if (y > k) Pair(x, 2 * k - y) else Pair(x, y) }
                                .toMutableSet()
            }
            else -> break
        }
        println("${dots.size}")
    }

    var cx = 0
    var cy = 0
    val comparator =
            compareBy { p: Pair<Int, Int> -> p.second }.thenBy { p: Pair<Int, Int> -> p.first }
    for ((x, y) in dots.sortedWith(comparator)) {
        while (cy < y) {
            ++cy
            cx = 0
            println()
        }
        while (cx < x) {
            ++cx
            print(' ')
        }
        print('*')
        ++cx
    }
}
