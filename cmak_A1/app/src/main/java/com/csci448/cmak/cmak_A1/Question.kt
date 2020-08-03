package com.csci448.cmak.cmak_A1

open class Question(question: Int, questionType: QuestionType, sol: String, solOptions : MutableList<String> = mutableListOf()) {
    val textResId : Int = question
    val type : QuestionType = questionType
    var answered : Boolean = false
    val solution : String = sol
    val solutionBank : MutableList<String> = solOptions

    fun isAnswerTrue(ans : String) : Boolean{
        if(solution.toLowerCase() == ans.toLowerCase()) {
            return true
        }
        return false
    }
}