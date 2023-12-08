class SeedManager(input: List<String>) {

    private var input: List<String> = mutableListOf()
    private var seedList: List<Long> = mutableListOf()

    private fun initializeCorrespondingMapFromXToY(x: String, y: String, map: MutableMap<Long, Long>) {
        val xToYStartIndex = input.indexOf(x) + 1
        val xToYEndIndex = if (y != "") input.indexOf(y) - 2 else input.size - 1

        for (i in xToYStartIndex..xToYEndIndex) {
            val mappingString = input[i].split(' ')
            val mapTo = mappingString[0].toLong()
            val begin = mappingString[1].toLong()
            val range = mappingString[2].toLong()
            for (step in 0..range - 1) {
                if (map.get(begin + step) == null) map.put(begin + step, mapTo + step)
            }
        }
    }

    private fun extractSeeds(): List<Long> = input[0].split("seeds: ")[1].split(' ').map { it -> it.toLong() }

    public fun getSeeds(): List<Long> = seedList

    public fun getLocationFor(seed: Long): Long {
        val soilForSeed = seedToSoilMap.getOrDefault(seed, seed)
        val fertilizerForSoil = soilToFertilizerMap.getOrDefault(soilForSeed, soilForSeed)
        val waterForFertilizer = fertilizerToWaterMap.getOrDefault(fertilizerForSoil, fertilizerForSoil)
        val lightForWater = waterToLightMap.getOrDefault(waterForFertilizer, waterForFertilizer)
        val tempForLight = lightToTemperatureMap.getOrDefault(lightForWater, lightForWater)
        val humidityForTemp = temperatureToHumidityMap.getOrDefault(tempForLight, tempForLight)
        return humidityToLocationMap.getOrDefault(humidityForTemp, humidityForTemp)
    }

    init {
        this.input = input
        seedList = extractSeeds()
        initializeCorrespondingMapFromXToY("seed-to-soil map:", "soil-to-fertilizer map:", seedToSoilMap)
        initializeCorrespondingMapFromXToY("soil-to-fertilizer map:", "fertilizer-to-water map:", soilToFertilizerMap)
        initializeCorrespondingMapFromXToY("fertilizer-to-water map:", "water-to-light map:", fertilizerToWaterMap)
        initializeCorrespondingMapFromXToY("water-to-light map:", "light-to-temperature map:", waterToLightMap)
        initializeCorrespondingMapFromXToY(
            "light-to-temperature map:",
            "temperature-to-humidity map:",
            lightToTemperatureMap
        )
        initializeCorrespondingMapFromXToY(
            "temperature-to-humidity map:",
            "humidity-to-location map:",
            temperatureToHumidityMap
        )
        initializeCorrespondingMapFromXToY("humidity-to-location map:", "", humidityToLocationMap)
        println("initialized all mappings")
    }

    companion object {
        var seedToSoilMap: MutableMap<Long, Long> = mutableMapOf()
        var soilToFertilizerMap: MutableMap<Long, Long> = mutableMapOf()
        var fertilizerToWaterMap: MutableMap<Long, Long> = mutableMapOf()
        var waterToLightMap: MutableMap<Long, Long> = mutableMapOf()
        var lightToTemperatureMap: MutableMap<Long, Long> = mutableMapOf()
        var temperatureToHumidityMap: MutableMap<Long, Long> = mutableMapOf()
        var humidityToLocationMap: MutableMap<Long, Long> = mutableMapOf()
    }

}

fun main() {
    fun part1(input: List<String>): Int {

        val seedManager = SeedManager(input)
        val seeds = seedManager.getSeeds()
        var locations = mutableSetOf<Long>()

        println("seeds are $seeds")

        for (seed in seeds) {
            locations.add(seedManager.getLocationFor(seed))
        }

        return locations.min().toInt()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35)

    val input = readInput("Day05")
    part1(input).println()
    // part2(input).println()
}
