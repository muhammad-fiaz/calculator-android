package dev.fiaz.calculator.domain.repository

import dev.fiaz.calculator.domain.model.UnitCategory
import dev.fiaz.calculator.domain.model.UnitType

interface UnitConverterRepository {
    fun getCategories(): List<UnitCategory>
    fun getUnitsForCategory(category: UnitCategory): List<UnitType>
    fun convert(value: Double, from: UnitType, to: UnitType): Double
}
