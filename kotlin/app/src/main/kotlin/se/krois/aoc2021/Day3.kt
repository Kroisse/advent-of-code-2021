package se.krois.aoc2021.day3

const val LENGTH = 12

fun solve() {
    val counts = MutableList(LENGTH) { 0 }
    var n = 0
    while (true) {
        val input = readLine()?.toIntOrNull(2) ?: break
        n += 1

        for (i in 0 until LENGTH) {
            val bit = input and (1 shl i)
            counts[i] += if (bit > 0) 1 else 0
        }
    }

    // Part 1
    val threshold = n / 2
    println("Threshold: $threshold")
    val gamma = counts.mapIndexed { i, count -> if (count > threshold) (1 shl i) else 0 }.sum()
    val epsilon = ((1 shl LENGTH) - 1) xor gamma
    println("Gamma: ${gamma}, Epsilon: $epsilon")
    println(gamma * epsilon)
}
