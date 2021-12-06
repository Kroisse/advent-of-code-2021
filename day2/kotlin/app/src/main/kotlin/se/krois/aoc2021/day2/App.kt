package se.krois.aoc2021.day2

fun main() {
    var x = 0
    var y = 0
    var aim = 0

    while (true) {
        val input = readLine() ?: break

        val (cmd, amount) = input.split(" ")
        val n = amount.toInt()

        when (cmd) {
            "forward" -> {
                x += n
                y += (aim * n)
            }
            "down" -> aim += n
            "up" -> aim -= n
        }
    }
    println(x * y)
}
