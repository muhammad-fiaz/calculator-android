package dev.fiaz.calculator

import dev.fiaz.calculator.data.repository.UnitConverterRepositoryImpl
import dev.fiaz.calculator.domain.model.UnitCategory
import org.junit.Assert.assertEquals
import org.junit.Test

class UnitConverterRepositoryImplTest {
    private val repository = UnitConverterRepositoryImpl()

    @Test
    fun gets_all_categories_correctly() {
        val categories = repository.getCategories()
        assertEquals(10, categories.size)
        assertEquals(UnitCategory.LENGTH, categories[0])
    }

    @Test
    fun gets_units_for_category_correctly() {
        val lengthUnits = repository.getUnitsForCategory(UnitCategory.LENGTH)
        assertEquals(8, lengthUnits.size)
        assertEquals("Meter", lengthUnits.find { it.symbol == "m" }?.name)
    }

    @Test
    fun converts_length_correctly() {
        val lengthUnits = repository.getUnitsForCategory(UnitCategory.LENGTH)
        val meter = lengthUnits.first { it.symbol == "m" }
        val kilometer = lengthUnits.first { it.symbol == "km" }
        val mm = lengthUnits.first { it.symbol == "mm" }

        // 1.5 km to m
        val meters = repository.convert(1.5, kilometer, meter)
        assertEquals(1500.0, meters, 0.001)

        // 5000 mm to m
        val meters2 = repository.convert(5000.0, mm, meter)
        assertEquals(5.0, meters2, 0.001)
    }

    @Test
    fun converts_temperature_correctly() {
        val tempUnits = repository.getUnitsForCategory(UnitCategory.TEMPERATURE)
        val celsius = tempUnits.first { it.symbol == "°C" }
        val fahrenheit = tempUnits.first { it.symbol == "°F" }
        val kelvin = tempUnits.first { it.symbol == "K" }

        // 0 °C to °F
        val f = repository.convert(0.0, celsius, fahrenheit)
        assertEquals(32.0, f, 0.001)

        // 100 °C to °F
        val f2 = repository.convert(100.0, celsius, fahrenheit)
        assertEquals(212.0, f2, 0.001)

        // 0 K to °C
        val c = repository.convert(0.0, kelvin, celsius)
        assertEquals(-273.15, c, 0.001)
    }
}
