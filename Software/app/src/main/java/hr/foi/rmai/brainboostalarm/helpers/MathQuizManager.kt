package hr.foi.rmai.brainboostalarm.helpers

class MathQuizManager {
    fun generateAdditionQuestion(): Pair<String, Int> {

        val num1 = (1..100).random()
        val num2 = (1..100).random()
        val resultAddition = num1 + num2
        val questionAddition = "$num1 + $num2"

        return Pair(questionAddition, resultAddition)
    }

    fun generateSubtractionQuestion(): Pair<String, Int> {

        val num1 = (1..100).random()
        val num2 = (1..num1).random()
        val resultSubtraction = num1 - num2
        val questionSubtraction = "$num1 - $num2"

        return Pair(questionSubtraction, resultSubtraction)
    }

    fun generateMultiplicationQuestion(): Pair<String, Int> {
        val num1 = (1..15).random()
        val num2 = (1..15).random()
        val resultMultiplication = num1 * num2
        val questionMultiplication = "$num1 * $num2"

        return Pair(questionMultiplication, resultMultiplication)
    }

    fun generateDividingQuestion(): Pair<String, Int> {
        val num1 = generateNonPrimeNumbers(1, 100)
        val num2 = generateDivisibleNumber(num1)
        val resultDividing = num1 / num2
        val questionDividing = "$num1 / $num2"

        return Pair(questionDividing, resultDividing)
    }

    fun generateEquation(): Pair<String, Int> {
        val num1 = (30..50).random()
        val num2 = (1..30).random()
        val x = (1..30).random()
        val resultEquation = num1 + num2 - x
        val questionEquation = "$resultEquation=$num1+$num2-x"

        return Pair(questionEquation, x)
    }

    fun generateNonPrimeNumbers(min: Int, max: Int): Int {
        val nonPrimes = (min..max).filterNot { isPrime(it) }
        return if (nonPrimes.isNotEmpty()) nonPrimes.random() else min
    }

    fun isPrime(n: Int): Boolean {
        if (n <= 1) return false

        for (i in 2 until n) {
            if (n % i == 0) return false
        }
        return true
    }

    fun generateDivisibleNumber(num1: Int): Int {
        var num: Int
        do {
            num = (1..num1).random()
        } while (num1 % num != 0)
        return num
    }

    fun generateAnswerChoices(solution: Int): List<Int> {
        val choices = mutableListOf(solution)
        while (choices.size < 4) {
            val offset = (1..2).random() * if ((1..2).random() == 1) -1 else 1
            val choice = solution + offset
            if (choice != solution && choice !in choices && choice > 0) choices.add(choice)
        }
        choices.shuffle()
        return choices
    }
}