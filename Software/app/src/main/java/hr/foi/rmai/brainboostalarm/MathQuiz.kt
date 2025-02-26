package hr.foi.rmai.brainboostalarm

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import hr.foi.rmai.brainboostalarm.helpers.MathQuizManager
import kotlinx.coroutines.*
import hr.foi.rmai.brainboostalarm.services.AlarmService

class MathQuiz : AppCompatActivity() {

    private lateinit var mathQuestion: TextView
    private lateinit var firstSolutionText: TextView
    private lateinit var secondSolutionText: TextView
    private lateinit var thirdSolutionText: TextView
    private lateinit var fourthSolutionText: TextView
    private lateinit var firstSolutionCard: CardView
    private lateinit var secondSolutionCard: CardView
    private lateinit var thirdSolutionCard: CardView
    private lateinit var fourthSolutionCard: CardView

    private val mathQuizManager = MathQuizManager()
    private var currentOperation: Operation = Operation.ADDITION
    private var score: Int = 0
    private var currentQuestion: String = ""
    private var currentSolution: Int = 0
    private val operations = Operation.values()
    private var currentOperationIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_math_quiz)

        mathQuestion = findViewById(R.id.tv_math_question)

        firstSolutionCard = findViewById(R.id.cv_first_solution_card)
        firstSolutionText = findViewById(R.id.tv_math_first_solution)

        secondSolutionCard = findViewById(R.id.cv_second_solution_card)
        secondSolutionText = findViewById(R.id.tv_math_second_solution)

        thirdSolutionCard = findViewById(R.id.cv_third_solution_card)
        thirdSolutionText = findViewById(R.id.tv_math_third_solution)

        fourthSolutionCard = findViewById(R.id.cv_fourth_solution_card)
        fourthSolutionText = findViewById(R.id.tv_math_fourth_solution)

        firstSolutionCard.setOnClickListener {
            checkAnswer(firstSolutionText.text.toString().toInt())
        }
        secondSolutionCard.setOnClickListener {
            checkAnswer(secondSolutionText.text.toString().toInt())
        }
        thirdSolutionText.setOnClickListener {
            checkAnswer(thirdSolutionText.text.toString().toInt())
        }
        fourthSolutionText.setOnClickListener {
            checkAnswer(fourthSolutionText.text.toString().toInt())
        }

        generateTask(currentOperation)
    }

    private fun generateTask(operation: Operation) {
        val (question, solution) = when (operation) {
            Operation.ADDITION -> mathQuizManager.generateAdditionQuestion()
            Operation.SUBTRACTION -> mathQuizManager.generateSubtractionQuestion()
            Operation.MULTIPLICATION -> mathQuizManager.generateMultiplicationQuestion()
            Operation.DIVISION -> mathQuizManager.generateDividingQuestion()
            Operation.EQUATION -> mathQuizManager.generateEquation()
        }
        currentQuestion = question
        currentSolution = solution
        mathQuestion.text = question
        val choices = mathQuizManager.generateAnswerChoices(solution)
        firstSolutionText.text = choices[0].toString()
        secondSolutionText.text = choices[1].toString()
        thirdSolutionText.text = choices[2].toString()
        fourthSolutionText.text = choices[3].toString()
    }

    private fun checkAnswer(selectedAnswer: Int) {
        val message: String
        if (selectedAnswer == currentSolution) {
            message = "Correct"
            score++
            if (currentOperationIndex < operations.size - 1) {
                currentOperationIndex++
            }
        }
        else {
            message = "Incorrect"
        }
            showToastMessage(message)


            GlobalScope.launch(Dispatchers.Main) {
                delay(2000) // 4 seconds delay
                generateTask(operations[currentOperationIndex])
            }

        if (score == 5) {
            stopAlarmService()
            finish()
            return
        }
    }

    private fun showToastMessage(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.show()
        GlobalScope.launch(Dispatchers.Main) {
            delay(1000) // 2 seconds delay
            toast.cancel()
        }
    }

    private fun stopAlarmService() {
        val serviceIntent = Intent(this, AlarmService::class.java)
        stopService(serviceIntent)
    }
}

enum class Operation {
    ADDITION,
    SUBTRACTION,
    MULTIPLICATION,
    DIVISION,
    EQUATION
}
