package com.example.geoquiz


import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

//Git Challenges branch
//First challenge show toast notification on top
//Second challenge add listener to TextView
//Third challenge add back button
//Fourth challenge change button to imageButton
//Fifth challenge preventing repeat answers
//sixth challenge graded quiz

class MainActivity : AppCompatActivity() {

    private lateinit var  trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView

    private val questionBank = listOf(
        Question(R.string.question_australia,true,false),
        Question(R.string.question_oceans,true,false),
        Question(R.string.question_mideast,false,false),
        Question(R.string.question_africa,false,false),
        Question(R.string.question_america,true,false),
        Question(R.string.question_asia,true,false))

    private var currentIndex = 0
    private var answered = 0
    private var correct = 0



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)


        trueButton.setOnClickListener {

            if(!questionBank[currentIndex].isAnswered){
                checkAnswer(true)
            }

            isAnswered(questionBank[currentIndex].isAnswered)


        }

        falseButton.setOnClickListener {
            if(!questionBank[currentIndex].isAnswered){
                checkAnswer(false)
            }

            isAnswered(questionBank[currentIndex].isAnswered)


        }

        nextButton.setOnClickListener {
            currentIndex = (currentIndex+1) % questionBank.size
            isAnswered(questionBank[currentIndex].isAnswered)
            updateQuestion()
        }
        prevButton.setOnClickListener {
            currentIndex = if (currentIndex == 0){
                 questionBank.size-1
            }else{
                currentIndex-1
            }
            isAnswered(questionBank[currentIndex].isAnswered)
            updateQuestion()
        }

        questionTextView.setOnClickListener{
            currentIndex = (currentIndex+1) % questionBank.size
            updateQuestion()
        }

        updateQuestion()
    }

    private fun updateQuestion(){
        if (answered == questionBank.size){
            val percent = correct/questionBank.size.toDouble() * 100
            val end = Toast.makeText(this,"Right percent: ${percent.toInt()}% $correct",Toast.LENGTH_LONG)
                end.setGravity(Gravity.TOP,0,0)
                end.show()

        }else{
            val questionTextResId = questionBank[currentIndex].textResId
            questionTextView.setText(questionTextResId)
        }

    }

    private fun checkAnswer(userAnswer:Boolean){
        ++answered
        questionBank[currentIndex].isAnswered=true
        val correctAnswer = questionBank[currentIndex].answer
        var messageResId =0

        if (userAnswer == correctAnswer){
            messageResId = R.string.correct_toast
            correct++
        }else{
            messageResId = R.string.incorrect_toast
        }

        Toast.makeText(this,messageResId,Toast.LENGTH_SHORT).show()

        updateQuestion()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isAnswered(isAnswered:Boolean){
        if (isAnswered){
            falseButton.isClickable = false
            trueButton.isClickable = false

            falseButton.setBackgroundColor(getColor(R.color.grey))
            trueButton.setBackgroundColor(getColor(R.color.grey))
        }else{
            falseButton.isClickable = true
            trueButton.isClickable = true

            falseButton.setBackgroundColor(getColor(com.google.android.material.R.color.design_default_color_primary))
            trueButton.setBackgroundColor(getColor(com.google.android.material.R.color.design_default_color_primary))
        }
    }


}