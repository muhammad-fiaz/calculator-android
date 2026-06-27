package dev.fiaz.calculator.domain.model

enum class UnitCategory {
    LENGTH, WEIGHT, TEMPERATURE, AREA, VOLUME, TIME, SPEED, ENERGY, PRESSURE, DATA
}

data class UnitType(
    val name: String,
    val factor: Double, // Factor to convert to a base unit (e.g., meters for length)
    val offset: Double = 0.0, // Used for temperature (e.g., Celsius to Kelvin)
    val symbol: String
)
