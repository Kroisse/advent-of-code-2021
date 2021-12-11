package se.krois.aoc2021.day10

fun solve() {
    var syntaxErrorScore = 0
    var completionScores = mutableListOf<Long>()
    while (true) {
        val input = readLine()?.trim() ?: break
        when (val result = check(input)) {
            is Corrupted -> syntaxErrorScore += result.score
            is Completion -> completionScores.add(result.score)
        }
    }
    completionScores.sort()
    println("syntax error score: $syntaxErrorScore")
    println("completion score: ${completionScores[completionScores.size / 2]}")
}

sealed class CheckResult

class Corrupted(val score: Int) : CheckResult()

class Completion(val score: Long) : CheckResult()

fun check(line: String): CheckResult {
    val stack = mutableListOf<Char>()
    for (c in line) {
        when (c) {
            '(', '[', '{', '<' -> stack.add(c)
            ')', ']', '}', '>' -> {
                val top = stack.removeLastOrNull()
                when (Pair(top, c)) {
                    Pair('(', ')'), Pair('[', ']'), Pair('{', '}'), Pair('<', '>') -> continue
                }
                when (c) {
                    ')' -> return Corrupted(3)
                    ']' -> return Corrupted(57)
                    '}' -> return Corrupted(1197)
                    '>' -> return Corrupted(25137)
                }
            }
        }
    }

    var score = 0L
    for (c in stack.reversed()) {
        score *= 5
        when (c) {
            '(' -> score += 1
            '[' -> score += 2
            '{' -> score += 3
            '<' -> score += 4
        }
    }
    return Completion(score)
}
