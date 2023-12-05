class ScratchCard(id: Int, scratchCardString: String) {

    private val cardId: Int = id
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

    private fun getWonCards(): Int {
        var wonCards = 0
        for (scratchNumber in scratchNumbers) {
            if (scratchNumber in winningNumbers) {
                wonCards += 1
            }
        }
        return wonCards
    }

    private fun updateOwnAndSuccessorCardAmountsWith(wonCards: Int) {
        for (j in 1..cardAmounts.getOrDefault(cardId, 1)) {
            for (i in 1..wonCards) {
                val currentAmount = cardAmounts[cardId + i]
                cardAmounts[cardId + i] = if (currentAmount != null) currentAmount + 1 else 0
            }
        }
    }

    init {
        scratchNumbers = extractScratchNumbers()
        winningNumbers = extractWinningNumbers()
        updateOwnAndSuccessorCardAmountsWith(getWonCards())
    }

    companion object {
        private val cardAmounts: MutableMap<Int, Int> = mutableMapOf()

        public fun initializeOriginalCardAmounts(cardCount: Int) {
            for (i in 1..cardCount) {
                cardAmounts[i] = 1
            }
        }

        fun getTotalCards(): Int = cardAmounts.values.sum()

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

        for ((index, scratchCardString) in input.withIndex()) {
            val scratchCard = ScratchCard(index + 1, scratchCardString)
            totalPoints += scratchCard.getPoints()
        }

        return totalPoints
    }

    fun part2(input: List<String>): Int {
        /*
        1. create map with cardId -> amount beginning with cardId = 2 and initialize all amounts with 0
        2. Parse first card from input for each winning number n increase amount of card n in map +1
        3. iterate over all card keys
            4. iterate over its amounts
            5. for m points increase +1 for m following card keys amounts
         */

        ScratchCard.initializeOriginalCardAmounts(input.size)
        for ((index, scratchCardString) in input.withIndex()) {
            ScratchCard(index + 1, scratchCardString)
        }

        return ScratchCard.getTotalCards()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
