package dev.fiaz.calculator.data.repository

import dev.fiaz.calculator.domain.model.UnitCategory
import dev.fiaz.calculator.domain.model.UnitType
import dev.fiaz.calculator.domain.repository.UnitConverterRepository
import javax.inject.Inject

class UnitConverterRepositoryImpl @Inject constructor() : UnitConverterRepository {

    override fun getCategories(): List<UnitCategory> = UnitCategory.entries

    override fun getUnitsForCategory(category: UnitCategory): List<UnitType> = when (category) {
        UnitCategory.LENGTH -> listOf(
            UnitType("Millimeter", 0.001, symbol = "mm"),
            UnitType("Centimeter", 0.01, symbol = "cm"),
            UnitType("Meter", 1.0, symbol = "m"),
            UnitType("Kilometer", 1000.0, symbol = "km"),
            UnitType("Inch", 0.0254, symbol = "in"),
            UnitType("Foot", 0.3048, symbol = "ft"),
            UnitType("Yard", 0.9144, symbol = "yd"),
            UnitType("Mile", 1609.344, symbol = "mi")
        )
        UnitCategory.WEIGHT -> listOf(
            UnitType("Milligram", 0.001, symbol = "mg"),
            UnitType("Gram", 1.0, symbol = "g"),
            UnitType("Kilogram", 1000.0, symbol = "kg"),
            UnitType("Ounce", 28.3495, symbol = "oz"),
            UnitType("Pound", 453.592, symbol = "lb")
        )
        UnitCategory.TEMPERATURE -> listOf(
            UnitType("Celsius", 1.0, symbol = "°C"),
            UnitType("Fahrenheit", 5.0/9.0, offset = 32.0, symbol = "°F"),
            UnitType("Kelvin", 1.0, offset = 273.15, symbol = "K")
        )
        UnitCategory.AREA -> listOf(
            UnitType("Square Millimeter", 0.000001, symbol = "mm²"),
            UnitType("Square Centimeter", 0.0001, symbol = "cm²"),
            UnitType("Square Meter", 1.0, symbol = "m²"),
            UnitType("Hectare", 10000.0, symbol = "ha"),
            UnitType("Square Kilometer", 1000000.0, symbol = "km²"),
            UnitType("Square Inch", 0.00064516, symbol = "in²"),
            UnitType("Square Foot", 0.092903, symbol = "ft²"),
            UnitType("Acre", 4046.86, symbol = "ac"),
            UnitType("Square Mile", 2589988.11, symbol = "mi²")
        )
        UnitCategory.VOLUME -> listOf(
            UnitType("Milliliter", 0.001, symbol = "ml"),
            UnitType("Liter", 1.0, symbol = "L"),
            UnitType("Cubic Meter", 1000.0, symbol = "m³"),
            UnitType("Teaspoon", 0.00492892, symbol = "tsp"),
            UnitType("Tablespoon", 0.0147868, symbol = "tbsp"),
            UnitType("Fluid Ounce", 0.0295735, symbol = "fl oz"),
            UnitType("Cup", 0.236588, symbol = "cup"),
            UnitType("Pint", 0.473176, symbol = "pt"),
            UnitType("Quart", 0.946353, symbol = "qt"),
            UnitType("Gallon", 3.78541, symbol = "gal")
        )
        UnitCategory.TIME -> listOf(
            UnitType("Millisecond", 0.001, symbol = "ms"),
            UnitType("Second", 1.0, symbol = "s"),
            UnitType("Minute", 60.0, symbol = "min"),
            UnitType("Hour", 3600.0, symbol = "h"),
            UnitType("Day", 86400.0, symbol = "d"),
            UnitType("Week", 604800.0, symbol = "wk"),
            UnitType("Month", 2629746.0, symbol = "mo"),
            UnitType("Year", 31556952.0, symbol = "yr")
        )
        UnitCategory.SPEED -> listOf(
            UnitType("Meter per Second", 1.0, symbol = "m/s"),
            UnitType("Kilometer per Hour", 1.0/3.6, symbol = "km/h"),
            UnitType("Mile per Hour", 0.44704, symbol = "mph"),
            UnitType("Knot", 0.514444, symbol = "kn"),
            UnitType("Mach", 340.3, symbol = "M")
        )
        UnitCategory.DATA -> listOf(
            UnitType("Bit", 1.0/8.0, symbol = "bit"),
            UnitType("Byte", 1.0, symbol = "B"),
            UnitType("Kilobyte", 1024.0, symbol = "KB"),
            UnitType("Megabyte", 1024.0 * 1024.0, symbol = "MB"),
            UnitType("Gigabyte", 1024.0 * 1024.0 * 1024.0, symbol = "GB"),
            UnitType("Terabyte", 1024.0 * 1024.0 * 1024.0 * 1024.0, symbol = "TB"),
            UnitType("Petabyte", 1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0, symbol = "PB")
        )
        UnitCategory.PRESSURE -> listOf(
            UnitType("Pascal", 1.0, symbol = "Pa"),
            UnitType("Bar", 100000.0, symbol = "bar"),
            UnitType("PSI", 6894.76, symbol = "psi"),
            UnitType("Atmosphere", 101325.0, symbol = "atm"),
            UnitType("Torr", 133.322, symbol = "Torr")
        )
        UnitCategory.ENERGY -> listOf(
            UnitType("Joule", 1.0, symbol = "J"),
            UnitType("Kilojoule", 1000.0, symbol = "kJ"),
            UnitType("Calorie", 4.184, symbol = "cal"),
            UnitType("Kilocalorie", 4184.0, symbol = "kcal"),
            UnitType("Watt-hour", 3600.0, symbol = "Wh"),
            UnitType("Kilowatt-hour", 3600000.0, symbol = "kWh"),
            UnitType("Electronvolt", 1.60218e-19, symbol = "eV")
        )
    }

    override fun convert(value: Double, from: UnitType, to: UnitType): Double {
        if (from == to) return value
        
        // Convert to base unit first
        // Note: For temperature, offset is applied BEFORE factor when converting TO base (if Celsius is base)
        // Actually, if we use Celsius as base:
        // F -> C: (F - 32) * 5/9
        // K -> C: K - 273.15
        // C -> C: (C - 0) * 1
        
        val baseValue = (value - from.offset) * from.factor
        
        // Convert from base unit to target
        // C -> F: (C / (5/9)) + 32
        // C -> K: C + 273.15
        return (baseValue / to.factor) + to.offset
    }
}
