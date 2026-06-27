package dev.fiaz.calculator

import dev.fiaz.calculator.domain.engine.CalculationError
import dev.fiaz.calculator.domain.engine.CalculatorExpressionEvaluator
import dev.fiaz.calculator.domain.engine.EvaluationResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CalculatorExpressionEvaluatorTest {
    private val evaluator = CalculatorExpressionEvaluator()

    @Test
    fun evaluates_operator_precedence_correctly() {
        assertSuccess("2+3*4", "14")
        assertSuccess("(2+3)*4", "20")
        assertSuccess("2^(1+2)", "8")
        assertSuccess("2^3^2", "512")
        assertSuccess("2^-2", "0.25")
        assertSuccess("100/10/2", "5")
        assertSuccess("-(2+3)*4", "-20")
        assertSuccess("5%+1", "1.05")
        assertSuccess("50%*2", "1")
    }

    @Test
    fun evaluates_scientific_functions_correctly() {
        assertSuccess("sin(30)", "0.5")
        assertSuccess("asin(0.5)", "30")
        assertSuccess("atan(1)", "45")
        assertSuccess("√(9)+1", "4")
        assertSuccess("ln(exp(1))", "1")
        assertSuccess("log(1000)", "3")
        assertSuccess("abs(-3)", "3")
        assertSuccess("floor(2.9)", "2")
        assertSuccess("ceil(2.1)", "3")
        assertSuccess("5!", "120")
        assertSuccess("10%", "0.1")
        assertSuccess("abs(-12.5)", "12.5")
    }

    @Test
    fun returns_divide_by_zero_error() {
        val result = evaluator.evaluate("10/0")
        assertTrue(result is EvaluationResult.Error)
        assertEquals(CalculationError.DivideByZero, (result as EvaluationResult.Error).error)
    }

    @Test
    fun returns_invalid_expression_error() {
        val result = evaluator.evaluate("(2+3")
        assertTrue(result is EvaluationResult.Error)
        assertEquals(CalculationError.InvalidExpression, (result as EvaluationResult.Error).error)
    }

    @Test
    fun returns_invalid_expression_for_domain_and_malformed_input() {
        val malformed = evaluator.evaluate("2++3")
        assertTrue(malformed is EvaluationResult.Error)
        assertEquals(CalculationError.InvalidExpression, (malformed as EvaluationResult.Error).error)

        val domain = evaluator.evaluate("sqrt(-1)")
        assertTrue(domain is EvaluationResult.Error)
        assertEquals(CalculationError.InvalidExpression, (domain as EvaluationResult.Error).error)

        val factorial = evaluator.evaluate("1.5!")
        assertTrue(factorial is EvaluationResult.Error)
        assertEquals(CalculationError.InvalidExpression, (factorial as EvaluationResult.Error).error)
    }

    private fun assertSuccess(expression: String, expected: String) {
        val result = evaluator.evaluate(expression)
        assertTrue("Expected success for expression: $expression", result is EvaluationResult.Success)
        assertEquals(expected, (result as EvaluationResult.Success).value)
    }
}
