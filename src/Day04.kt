class ScratchCard(scratchCardString: String) {

    private val cardString: String = scratchCardString
    private val scratchNumbers: Set<Int>
    private val winningNumbers: Set<Int>


    private fun extractScratchNumbers(): Set<Int> {
        val scratchNumbersString: String = this.cardString
            .split(':')[1]
            .split('|')[0]
            .trim()

        return scratchNumbersString.extractNumbersToSet()
    }

    private fun extractWinningNumbers(): Set<Int> {
        val winningNumbersString: String = this.cardString
            .split(':')[1]
            .split('|')[1]
            .trim()

        return winningNumbersString.extractNumbersToSet()
    }

    public fun getPoints(): Int {
        var points = 0
        for (scratchNumber in scratchNumbers) {
            if (scratchNumber in winningNumbers) {
                points += if (points == 0) 1 else points
            }
        }
        return points
    }

    init {
        scratchNumbers = extractScratchNumbers()
        winningNumbers = extractWinningNumbers()
    }

    companion object {
        fun String.extractNumbersToSet(): Set<Int> {
            val numbersList = this
                .removeMultipleWhitespaces()
                .split(' ')
            return numbersList.map { it -> it.toInt() }.toSet()
        }

        private fun String.removeMultipleWhitespaces(): String = this.replace("\\s{2,}".toRegex(), " ")
    }

}

fun main() {
    fun part1(input: List<String>): Int {

        var totalPoints = 0

        for (scratchCardString in input) {
            val scratchCard = ScratchCard(scratchCardString)
            totalPoints += scratchCard.getPoints()
        }

        return totalPoints
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)

    val input = readInput("Day04")
    part1(input).println()
    //part2(testInput).println()
}
