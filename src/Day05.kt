class Mapping(mappingString: String) {

    private var mappingString: String = ""
    var targetBase: Long = -1
    var from: Long = -1
    var step: Long = -1
    var end: Long = -1

    private fun extractMappingFromMappingString() {
        val mappingValues = mappingString.split(" ")
        targetBase = mappingValues[0].toLong()
        from = mappingValues[1].toLong()
        step = mappingValues[2].toLong()
        end = from + step
    }

    init {
        this.mappingString = mappingString
        extractMappingFromMappingString()
    }

    override fun toString(): String {
        return "$targetBase $from $step"
    }

}

class SeedManager(input: List<String>, seedList: List<Long> = mutableListOf() ) {

    private var input: List<String> = mutableListOf()
    private var seedList: List<Long> = mutableListOf()

    private fun initializeXToYMappingFor(x: String, y: String, list: MutableList<Mapping>) {
        val xToYStartIndex = input.indexOf(x) + 1
        val xToYEndIndex = if (y != "") input.indexOf(y) - 2 else input.size - 1

        for (mappingEntryIndex in xToYStartIndex..xToYEndIndex) {
            list.add(Mapping(input[mappingEntryIndex]))
        }
    }

    private fun getMappingResultFromListFor(toMap: Long, list: MutableList<Mapping>): Long {
        var mappingResult = toMap
        for (mapEntry in list) {
            if (toMap >= mapEntry.from && toMap <= mapEntry.end) {
                mappingResult = mapEntry.targetBase + (toMap - mapEntry.from)
                break
            }
        }
        return mappingResult
    }

    private fun extractSeeds(): List<Long> = input[0].split("seeds: ")[1].split(' ').map { it -> it.toLong() }

    fun getSeeds(): List<Long> = seedList

    fun getLocationFor(seed: Long) =
        getMappingResultFromListFor(
            getMappingResultFromListFor(
                getMappingResultFromListFor(
                    getMappingResultFromListFor(
                        getMappingResultFromListFor(
                            getMappingResultFromListFor(
                                getMappingResultFromListFor(
                                    seed,
                                    seedToSoilMappingsList
                                ),
                                soilToFertilizerMappingsList
                            ), fertilizerToWaterMappingsList
                        ), waterToLightMappingsList
                    ), lightToTempMappingsList
                ), tempToHumidityMappingsList
            ), humidityToLocationMappingsList
        )

    init {
        this.input = input
        if(seedList.isNotEmpty()) {
            this.seedList = seedList
        } else {
            this.seedList = extractSeeds()
        }

        initializeXToYMappingFor(
            "seed-to-soil map:", "soil-to-fertilizer map:",
            seedToSoilMappingsList
        )
        initializeXToYMappingFor(
            "soil-to-fertilizer map:", "fertilizer-to-water map:",
            soilToFertilizerMappingsList
        )
        initializeXToYMappingFor(
            "fertilizer-to-water map:", "water-to-light map:",
            fertilizerToWaterMappingsList
        )
        initializeXToYMappingFor(
            "water-to-light map:", "light-to-temperature map:",
            waterToLightMappingsList
        )
        initializeXToYMappingFor(
            "light-to-temperature map:", "temperature-to-humidity map:",
            lightToTempMappingsList
        )
        initializeXToYMappingFor(
            "temperature-to-humidity map:", "humidity-to-location map:",
            tempToHumidityMappingsList
        )
        initializeXToYMappingFor(
            "humidity-to-location map:", "",
            humidityToLocationMappingsList
        )
    }

    companion object {
        val seedToSoilMappingsList: MutableList<Mapping> = mutableListOf()
        val soilToFertilizerMappingsList: MutableList<Mapping> = mutableListOf()
        val fertilizerToWaterMappingsList: MutableList<Mapping> = mutableListOf()
        val waterToLightMappingsList: MutableList<Mapping> = mutableListOf()
        val lightToTempMappingsList: MutableList<Mapping> = mutableListOf()
        val tempToHumidityMappingsList: MutableList<Mapping> = mutableListOf()
        val humidityToLocationMappingsList: MutableList<Mapping> = mutableListOf()

        fun generateSeedListFromSeedsWithRanges(seedRangeString: String): List<Long> {
            println("Got seedRangeString $seedRangeString")
            val seedRangeEntries = seedRangeString.split(" ")

            val seedEntries: MutableList<Long> = mutableListOf()
            val rangeEntries: MutableList<Long> = mutableListOf()

            for(i in 0..seedRangeEntries.size - 1) {
                if(i%2 == 0) {
                    seedEntries.add(seedRangeEntries[i].toLong())
                } else {
                    rangeEntries.add(seedRangeEntries[i].toLong())
                }
            }

            println("seeds: $seedEntries")
            println("ranges: $rangeEntries")

            val seedList: MutableList<Long> = mutableListOf()
            /*for(seedEntryIndex in 0..seedEntries.size - 1){
                for(i in 0..rangeEntries[seedEntryIndex]) {
                    seedList.add(seedEntries[seedEntryIndex] + i)
                }
            }*/

            for(i in 0..rangeEntries[1]) {
                seedList.add(seedEntries[1] + i)
            }

            println("seed list completed")
            return seedList
        }
    }

}

fun main() {
    fun part1(input: List<String>): Long {

        val seedManager = SeedManager(input)
        val seeds = seedManager.getSeeds()
        val locations = mutableSetOf<Long>()

        for (seed in seeds) {
            locations.add(seedManager.getLocationFor(seed))
        }

        return locations.min()
    }

    fun part2(input: List<String>): Long {

        val seedsRangeString = input[0].split(": ")[1]

        val seedManager = SeedManager(input, SeedManager.generateSeedListFromSeedsWithRanges(seedsRangeString))
        val seeds = seedManager.getSeeds()
        val locations = mutableSetOf<Long>()

        // Do the min calculation per seed + range pair and in the end get the lowest of them (else creating all those possible seeds is too much for the heap)
        for (seed in seeds) {
            locations.add(seedManager.getLocationFor(seed))
        }

        return locations.min()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    // check(part2(testInput) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
