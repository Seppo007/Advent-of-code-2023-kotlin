fun main() {
    fun parseLineToIntList(line: String): MutableList<List<Int>> = mutableListOf(line.split(" ").map { it.toInt() })

    fun generateNextLine(intList: List<Int>): MutableList<Int> {
        val newList = mutableListOf<Int>()
        intList.zipWithNext().forEach {
            newList.add(it.second - it.first)
        }
        return newList
    }

    fun part1(input: List<String>): Int {

        var extrapolatedNext = 0
        val numberList = mutableListOf<MutableList<List<Int>>>()
        for (line in input) {
            numberList.add(parseLineToIntList(line))
        }

        for ((listIndex, _) in numberList.withIndex()) {
            while (true) {
                val numList = numberList.toList()[listIndex]
                val lastEntry = numList[numList.size - 1]
                if (lastEntry.zipWithNext()
                        .any { (a, b) -> b - a != lastEntry[1] - lastEntry[0] || (a != 0 && b != 0) }
                ) {
                    numberList.find { it == numList }?.add(generateNextLine(lastEntry))
                } else {
                    break;
                }
            }

            val numList = numberList.toList()[listIndex]
            for (sublistIndex in numList.size - 1 downTo 0) {
                extrapolatedNext += numList[sublistIndex].last()
            }
        }

        return extrapolatedNext
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114)

    val input = readInput("Day09")
    part1(input).println()
    part2(testInput).println()
}
