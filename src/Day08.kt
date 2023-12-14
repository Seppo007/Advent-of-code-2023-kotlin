import java.util.Dictionary

class Navigation(input: String) {
    val left: String
    val right: String

    private fun parseFrom(input: String): List<String> {
        val leftRightRegex = """\w{3}, \w{3}""".toRegex()
        val leftRight = leftRightRegex.find(input)!!.value.split(", ")

        return leftRight
    }

    init {
        val (left, right) = parseFrom(input)
        this.left = left
        this.right = right
    }
}

fun createPathDictionary(input: List<String>): Map<String, Navigation> {
    val pathDictionary = mutableMapOf<String, Navigation>()
    for (pathEntry in input) {
        val pathKey = pathEntry.split(" =")[0]
        pathDictionary[pathKey] = Navigation(pathEntry)
    }
    return pathDictionary.toMap()
}

fun printDictionary(dictionary: Map<String, Navigation>) {
    println("dictionary:")
    for(entry in dictionary) {
        val value = dictionary[entry.key]!!
        println("${entry.key} -> (${value.left}, ${value.right})")
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val directionList = input[0]
        val pathDictionary = createPathDictionary(input.subList(2, input.size))

        var cur = "AAA"
        var steps = 0
        do {
            for(direction in directionList) {
                if(direction == 'L') {
                    cur = pathDictionary[cur]!!.left
                } else {
                    cur = pathDictionary[cur]!!.right
                }
                steps += 1
            }
        } while (cur != "ZZZ")

        return steps
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 6)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
