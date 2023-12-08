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

class SeedManager(input: List<String>) {

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

    public fun getSeeds(): List<Long> = seedList

    public fun getLocationFor(seed: Long) =
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
        seedList = extractSeeds()

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

        println("initialized all mappings")
    }

    companion object {
        val seedToSoilMappingsList: MutableList<Mapping> = mutableListOf()
        val soilToFertilizerMappingsList: MutableList<Mapping> = mutableListOf()
        val fertilizerToWaterMappingsList: MutableList<Mapping> = mutableListOf()
        val waterToLightMappingsList: MutableList<Mapping> = mutableListOf()
        val lightToTempMappingsList: MutableList<Mapping> = mutableListOf()
        val tempToHumidityMappingsList: MutableList<Mapping> = mutableListOf()
        val humidityToLocationMappingsList: MutableList<Mapping> = mutableListOf()
    }

}

fun main() {
    fun part1(input: List<String>): Long {

        val seedManager = SeedManager(input)
        val seeds = seedManager.getSeeds()
        val locations = mutableSetOf<Long>()

        println("seeds are $seeds")

        for (seed in seeds) {
            locations.add(seedManager.getLocationFor(seed))
        }

        println("locations are $locations")
        return locations.min()
    }

    fun part2(input: List<String>): Long {
        return input.size.toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    // check(part1(testInput) == 35)

    val input = readInput("Day05")
    part1(input).println()
    // part2(input).prLongln()
}
