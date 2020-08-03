package com.csci448.cmak.cmak_A1
import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.ViewModel
import com.csci448.cmak.cmak_A1.Question
import com.csci448.cmak.cmak_A1.R

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {

    private val questionBank: MutableList<Question> = mutableListOf()
    private var score = 0


    /*
    Resources.getSystem().getString(R.string.Fivechoice1),
            Resources.getSystem().getString(R.string.Fivechoice2),
            Resources.getSystem().getString(R.string.Fivechoice3),
            Resources.getSystem().getString(R.string.Fivechoice4))))
     */
    init {
        questionBank.add(Question(R.string.question1, QuestionType.TF, "false", mutableListOf()))
        questionBank.add(Question(R.string.question4, QuestionType.TF, "false", mutableListOf()))
        questionBank.add(Question(R.string.question3, QuestionType.TF,"false", mutableListOf()))
        questionBank.add(Question(R.string.question2, QuestionType.TF, "true", mutableListOf()))
        questionBank.add(Question(R.string.question6, QuestionType.SA, "Denver", mutableListOf()))
        questionBank.add(Question(R.string.question5, QuestionType.MC, "Alaska", mutableListOf(
            "Antarctica",
            "Alaska",
            "Africa",
            "Asia")))

        Log.d(TAG, "ViewModel instance about to be initialized")

    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel instance about to be destroyed")
    }

    private var currentQuestionIndex = 0


    private val currentQuestion: Question
        get() = questionBank[currentQuestionIndex]

    val currentQuestionTextId: Int
        get() = currentQuestion.textResId

    val currentScore: Int
        get() = score

    fun isAnswerCorrect(answerChoice: String): Boolean {
        if(currentQuestion.isAnswerTrue(answerChoice)) {
            score++
            return true
        }
        return false
    }

    fun moveToNextQuestion() {
        if(currentQuestionIndex == questionBank.size - 1) {
            currentQuestionIndex = 0
        } else {
            currentQuestionIndex++
        }
    }

    fun moveToPreviousQuestion() {
        if(currentQuestionIndex == 0) {
            currentQuestionIndex = questionBank.size - 1
        } else {
            currentQuestionIndex--
        }
    }

    fun getCurrentQuestionIndex(): Int {
        return currentQuestionIndex
    }

    fun setCurrentQuestionIndex(index: Int) {
        currentQuestionIndex = index
    }

    fun getCurrentAnswer(): Any {
        return questionBank[currentQuestionIndex].solution
    }

    fun hasAnswered(){
        currentQuestion.answered = true
    }

    fun checkAnswered(): Boolean {
        return currentQuestion.answered
    }

    fun checkQuestionType() : QuestionType {
        return currentQuestion.type
    }

    fun getOption(index : Int) : String {
        return currentQuestion.solutionBank[index]
    }

//    fun getSolutionList() : MutableList<Int> {
//        return currentQuestion.solutionBank
//    }

}