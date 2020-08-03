package com.csci448.cmak.cmak_A1


import androidx.appcompat.app.AppCompatActivity
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.ViewModelProvider
import com.csci448.cmak.cmak_A1.R
import kotlinx.android.synthetic.main.activity_quiz.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



private const val LOG_TAG = "448.QuizActivity"
private const val KEY_INDEX = "index"

class QuizActivity : AppCompatActivity() {

    private lateinit var scoreTextView: TextView
    private lateinit var questionTextView: TextView
    private lateinit var mcLayout: LinearLayout
    private lateinit var saLayout: LinearLayout
    private lateinit var tfLayout: LinearLayout
    private lateinit var mcRadio : RadioGroup
    private lateinit var radioBut1 : RadioButton
    private lateinit var radioBut2 : RadioButton
    private lateinit var radioBut3 : RadioButton
    private lateinit var radioBut4 : RadioButton
    private lateinit var saEditText : EditText

    private lateinit var quizViewModel: QuizViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "onCreate() called")
        setContentView(R.layout.activity_quiz)
        val factory = QuizViewModelFactory()
        quizViewModel = ViewModelProvider(this@QuizActivity, factory).get(QuizViewModel::class.java)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.setCurrentQuestionIndex(currentIndex)

        mcLayout = findViewById(R.id.mc_layout)
        saLayout = findViewById(R.id.short_answer_layout)
        tfLayout = findViewById(R.id.tf_layout)

        val trueButton: Button = findViewById(R.id.true_button)
        val falseButton: Button = findViewById(R.id.false_button)
        val prevButton: Button = findViewById(R.id.previous_button)
        val nextButton: Button = findViewById(R.id.next_button)
        val mcSubmitButton : Button = findViewById(R.id.mc_submit)
        val saSubmitButton : Button = findViewById(R.id.sa_submit)
        scoreTextView = findViewById(R.id.score_text_view)
        questionTextView = findViewById(R.id.question_text_view)
        mcRadio = findViewById(R.id.mc_radiogroup)
        radioBut1 = findViewById(R.id.radio1)
        radioBut2 = findViewById(R.id.radio2)
        radioBut3 = findViewById(R.id.radio3)
        radioBut4 = findViewById(R.id.radio4)
        saEditText = findViewById(R.id.short_answer)



        trueButton.setOnClickListener{ checkAnswer("true")}
        falseButton.setOnClickListener{checkAnswer("false")}
        saSubmitButton.setOnClickListener{ checkAnswer(saEditText.text.toString())}
        mcSubmitButton.setOnClickListener {
            if (quizViewModel.checkQuestionType() == QuestionType.MC) {
                val chosen_mc: String = when (mcRadio.checkedRadioButtonId) {
                    R.id.radio1 -> quizViewModel.getOption(0)
                    R.id.radio2 -> quizViewModel.getOption(1)
                    R.id.radio3 -> quizViewModel.getOption(2)
                    R.id.radio4 -> quizViewModel.getOption(3)
                    else -> ""
                }

                if (chosen_mc.isNotEmpty() && chosen_mc != "") {
                    checkAnswer(chosen_mc)
                }
            }
        }
        prevButton.setOnClickListener{moveToQuestion(-1)}
        nextButton.setOnClickListener{moveToQuestion(1)}
        updateQuestion()
    }

    override fun onStart() {
        super.onStart()
        Log.d(LOG_TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(LOG_TAG, "onResume() called")
    }

    override fun onPause() {
        Log.d(LOG_TAG, "onPause() called")
        super.onPause()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(LOG_TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.getCurrentQuestionIndex())


    }
    override fun onStop() {
        Log.d(LOG_TAG, "onStop() called")
        super.onStop()
    }


    override fun onDestroy() {
        Log.d(LOG_TAG, "onDestroy() called")
        super.onDestroy()
    }

    private fun updateQuestion() {
        Log.d(LOG_TAG, "Updating Question")
        setCurrentScoreText()
        //Set all question types to invisible
        mcLayout.visibility = View.GONE
        saLayout.visibility = View.GONE
        tfLayout.visibility = View.GONE
        mc_submit.visibility = View.GONE
        sa_submit.visibility = View.GONE
        questionTextView.text = resources.getString(quizViewModel.currentQuestionTextId)
        if(quizViewModel.checkQuestionType() == QuestionType.TF) {
            Log.d(LOG_TAG, "Show Question TF")
            tfLayout.visibility = View.VISIBLE
        } else if(quizViewModel.checkQuestionType() == QuestionType.MC){
            Log.d(LOG_TAG, "Show Question MC")
            radioBut1.text = quizViewModel.getOption(0)
            radioBut2.text = quizViewModel.getOption(1)
            radioBut3.text = quizViewModel.getOption(2)
            radioBut4.text = quizViewModel.getOption(3)
            mcLayout.visibility = View.VISIBLE
            mc_submit.visibility = View.VISIBLE
        } else if(quizViewModel.checkQuestionType() == QuestionType.SA){
            Log.d(LOG_TAG, "Show Question SA")
            saLayout.visibility = View.VISIBLE
            sa_submit.visibility = View.VISIBLE
        }

    }

    private fun setCurrentScoreText() {
        scoreTextView.text = quizViewModel.currentScore.toString()
    }


    private fun checkAnswer(userAnswer: String) {
        if (!quizViewModel.checkAnswered()) {
            quizViewModel.hasAnswered()
            var isRight: Boolean = quizViewModel.isAnswerCorrect(userAnswer)

            if (isRight) {
                Toast.makeText(baseContext, R.string.correct_toast, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(baseContext, R.string.incorrect_toast, Toast.LENGTH_SHORT).show()
            }
        } else {
            if(quizViewModel.checkQuestionType() == QuestionType.MC) {
                for (i in 0 until mcRadio.getChildCount()) {
                    mcRadio.getChildAt(i).setEnabled(false)
                }
            } else if (quizViewModel.checkQuestionType() == QuestionType.SA) {
                saEditText.setEnabled(false)
            }
        }
    }

    fun moveToQuestion(direction: Int) {
        if(direction > 0) {
            quizViewModel.moveToNextQuestion()
        }

        else if(direction < 0) {
            quizViewModel.moveToPreviousQuestion()
        }
        updateQuestion()
    }

    fun getCurrentAnswer() = quizViewModel.getCurrentAnswer()
}
