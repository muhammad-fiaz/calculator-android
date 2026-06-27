package dev.fiaz.calculator.domain.engine

import kotlin.math.abs
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

class CalculatorExpressionEvaluator {
    fun evaluate(expression: String, isDegreeMode: Boolean = true): EvaluationResult {
        val normalized = expression.replace("×", "*").replace("÷", "/").replace("−", "-")
        if (normalized.isBlank()) return EvaluationResult.Empty

        return runCatching {
            val tokens = tokenize(normalized)
            val rpn = toRpn(tokens)
            val value = evaluateRpn(rpn, isDegreeMode)
            if (!value.isFinite() || value.isNaN()) {
                throw IllegalArgumentException("Invalid expression")
            }
            EvaluationResult.Success(format(value))
        }.getOrElse { throwable ->
            when (throwable) {
                is ArithmeticException -> EvaluationResult.Error(CalculationError.DivideByZero)
                else -> EvaluationResult.Error(CalculationError.InvalidExpression)
            }
        }
    }

    private fun tokenize(input: String): List<Token> {
        val tokens = mutableListOf<Token>()
        var index = 0
        while (index < input.length) {
            val current = input[index]
            when {
                current.isWhitespace() -> index++
                current.isDigit() || current == '.' -> {
                    val start = index
                    while (index < input.length && (input[index].isDigit() || input[index] == '.')) index++
                    tokens += Token.Number(input.substring(start, index).toDouble())
                }
                current == 'π' -> {
                    tokens += Token.Number(Math.PI)
                    index++
                }
                current == 'e' -> {
                    tokens += Token.Number(Math.E)
                    index++
                }
                current.isLetter() -> {
                    val start = index
                    while (index < input.length && input[index].isLetter()) index++
                    tokens += Token.Function(input.substring(start, index).lowercase())
                }
                current == '√' -> {
                    tokens += Token.Function("sqrt")
                    index++
                }
                current == '(' -> {
                    tokens += Token.LeftParen
                    index++
                }
                current == ')' -> {
                    tokens += Token.RightParen
                    index++
                }
                current in charArrayOf('+', '-', '*', '/', '^', '%', '!') -> {
                    tokens += Token.Operator(current.toString())
                    index++
                }
                else -> throw IllegalArgumentException("Unsupported character")
            }
        }
        return tokens
    }

    private fun toRpn(tokens: List<Token>): List<Token> {
        val output = mutableListOf<Token>()
        val operators = ArrayDeque<Token>()
        var previous: Token? = null

        tokens.forEach { token ->
            when (token) {
                is Token.Number -> output += token
                is Token.Function -> operators.addLast(token)
                is Token.Operator -> {
                    val normalized = token.normalizeUnary(previous)
                    while (operators.isNotEmpty() && operators.last().shouldPopBefore(normalized)) {
                        output += operators.removeLast()
                    }
                    operators.addLast(normalized)
                    previous = normalized
                    return@forEach
                }
                Token.LeftParen -> operators.addLast(token)
                Token.RightParen -> {
                    while (operators.isNotEmpty() && operators.last() != Token.LeftParen) {
                        output += operators.removeLast()
                    }
                    if (operators.isEmpty()) throw IllegalArgumentException("Mismatched parentheses")
                    operators.removeLast()
                    if (operators.lastOrNull() is Token.Function) {
                        output += operators.removeLast()
                    }
                }
            }
            previous = token
        }

        while (operators.isNotEmpty()) {
            val token = operators.removeLast()
            if (token == Token.LeftParen || token == Token.RightParen) throw IllegalArgumentException("Mismatched parentheses")
            output += token
        }
        return output
    }

    private fun evaluateRpn(tokens: List<Token>, isDegreeMode: Boolean): Double {
        val stack = ArrayDeque<Double>()
        tokens.forEach { token ->
            when (token) {
                is Token.Number -> stack.addLast(token.value)
                is Token.Operator -> {
                    when (token.symbol) {
                        "u-" -> stack.addLast(-stack.removeLast())
                        "+" -> stack.addLast(stack.removeLast() + stack.removeLast())
                        "-" -> {
                            val right = stack.removeLast()
                            val left = stack.removeLast()
                            stack.addLast(left - right)
                        }
                        "*" -> stack.addLast(stack.removeLast() * stack.removeLast())
                        "/" -> {
                            val right = stack.removeLast()
                            if (abs(right) < 1e-12) throw ArithmeticException("Divide by zero")
                            val left = stack.removeLast()
                            stack.addLast(left / right)
                        }
                        "^" -> {
                            val right = stack.removeLast()
                            val left = stack.removeLast()
                            stack.addLast(left.pow(right))
                        }
                        "%" -> stack.addLast(stack.removeLast() / 100.0)
                        "!" -> {
                            val value = stack.removeLast()
                            stack.addLast(factorial(value))
                        }
                        else -> throw IllegalArgumentException("Unknown operator")
                    }
                }
                is Token.Function -> {
                    val argument = stack.removeLast()
                    stack.addLast(
                        when (token.name) {
                            "sin" -> if (isDegreeMode) sin(Math.toRadians(argument)) else sin(argument)
                            "cos" -> if (isDegreeMode) cos(Math.toRadians(argument)) else cos(argument)
                            "tan" -> if (isDegreeMode) tan(Math.toRadians(argument)) else tan(argument)
                            "asin" -> if (isDegreeMode) Math.toDegrees(asin(argument)) else asin(argument)
                            "atan" -> if (isDegreeMode) Math.toDegrees(atan(argument)) else atan(argument)
                            "sqrt", "√" -> sqrt(argument)
                            "log" -> log10(argument)
                            "ln" -> ln(argument)
                            "exp" -> exp(argument)
                            "abs" -> abs(argument)
                            "floor" -> floor(argument)
                            "ceil" -> ceil(argument)
                            else -> throw IllegalArgumentException("Unknown function")
                        }
                    )
                }
                else -> throw IllegalArgumentException("Invalid token")
            }
        }
        return stack.singleOrNull() ?: throw IllegalArgumentException("Invalid expression")
    }

    private fun factorial(value: Double): Double {
        if (value < 0 || value % 1 != 0.0) throw IllegalArgumentException("Factorial requires integer")
        var result = 1.0
        var current = value.toLong()
        while (current > 1) {
            result *= current.toDouble()
            current--
        }
        return result
    }

    private fun format(value: Double): String {
        val rounded = kotlin.math.round(value * 1_000_000_000.0) / 1_000_000_000.0
        return if (rounded % 1 == 0.0) rounded.toLong().toString() else rounded.toString()
    }

    private fun Token.Operator.normalizeUnary(previous: Token?): Token.Operator {
        return if (symbol == "-" && (previous == null || previous is Token.Operator || previous == Token.LeftParen || previous is Token.Function)) {
            Token.Operator("u-")
        } else {
            this
        }
    }

    private fun Token.shouldPopBefore(next: Token): Boolean {
        if (this == Token.LeftParen) return false
        val left = precedence()
        val right = next.precedence()
        return if (this is Token.Function) true else when {
            left > right -> true
            left < right -> false
            else -> next.isLeftAssociative()
        }
    }

    private fun Token.precedence(): Int = when (this) {
        is Token.Function -> 5
        is Token.Operator -> when (symbol) {
            "!" -> 6
            "%" -> 6
            "u-" -> 5
            "^" -> 4
            "*", "/" -> 3
            "+", "-" -> 2
            else -> 0
        }
        else -> 0
    }

    private fun Token.isLeftAssociative(): Boolean = when (this) {
        is Token.Operator -> symbol != "^" && symbol != "u-"
        else -> true
    }
}

sealed class Token {
    data class Number(val value: Double) : Token()
    data class Operator(val symbol: String) : Token()
    data class Function(val name: String) : Token()
    data object LeftParen : Token()
    data object RightParen : Token()
}

sealed class EvaluationResult {
    data object Empty : EvaluationResult()
    data class Success(val value: String) : EvaluationResult()
    data class Error(val error: CalculationError) : EvaluationResult()
}

enum class CalculationError {
    InvalidExpression,
    DivideByZero
}