package se.krois.aoc2021.day11

import org.jetbrains.kotlinx.multik.api.*
import org.jetbrains.kotlinx.multik.ndarray.data.*
import org.jetbrains.kotlinx.multik.ndarray.operations.*

fun solve() {
    val grid = read()

    var flashes = 0
    for (i in 1..100) {
        flashes += step(grid)
    }
    println("Octopus flashes: $flashes")

    var count = 100
    while (grid[1..grid.shape[0] - 1, 1..grid.shape[1] - 1].any { it != 0 }) {
        step(grid)
        count++
    }
    println("Octopus synchronizes after $count steps")
}

fun read(): D2Array<Int> {
    val firstLine = readLine()?.trim() ?: throw IllegalArgumentException("No input")
    val grid = mk.d2array(firstLine.length + 2, firstLine.length + 2) { -1 }
    read_row(grid, 1, firstLine)
    for (row in 2..grid.shape[0] - 1) {
        val line = readLine()?.trim() ?: break
        read_row(grid, row, line)
    }
    return grid
}

fun read_row(grid: D2Array<Int>, row: Int, line: String) {
    for ((col, ch) in line.withIndex()) {
        grid[row, col + 1] = ch.digitToIntOrNull() ?: 0
    }
}

fun step(grid: D2Array<Int>): Int {
    val flashes = mutableListOf<Pair<Int, Int>>()
    var flashesTail = 0

    for (row in 1 until (grid.shape[0] - 1)) {
        for (col in 1 until (grid.shape[1] - 1)) {
            if (grid[row, col] == -1) continue
            ++grid[row, col]
            if (grid[row, col] >= 10) {
                flashes.add(Pair(row, col))
            }
        }
    }

    while (flashesTail < flashes.size) {
        val range = flashesTail until flashes.size
        flashesTail = flashes.size

        for (i in range) {
            val (row, col) = flashes[i]

            for (r in row - 1..row + 1) {
                for (c in col - 1..col + 1) {
                    if (grid[r, c] == -1) continue
                    ++grid[r, c]
                    if (grid[r, c] == 10) {
                        flashes.add(Pair(r, c))
                    }
                }
            }
        }
    }

    for ((r, c) in flashes) {
        grid[r, c] = 0
    }

    return flashes.size
}
