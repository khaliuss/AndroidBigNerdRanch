package com.example.geoquiz


import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

//Git Challenges branch
//First challenge show toast notification on top
//Second challenge add listener to TextView
//Third challenge add back button
//Fourth challenge change button to imageButton
//Fifth challenge preventing repeat answers
//sixth challenge graded quiz

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var cheatButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView

    private val quizViewModel:QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }



    private var answered = 0
    private var correct = 0



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"OnCreate() called")
        setContentView(R.layout.activity_main)
        val currentIndex = savedInstanceState?.getInt(KEY_INDEX,0) ?: 0
        quizViewModel.currentIndex = currentIndex

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        cheatButton = findViewById(R.id.cheat_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)



        trueButton.setOnClickListener {

            if(!quizViewModel.isAnswered){
                checkAnswer(true)
            }

            isAnswered(quizViewModel.isAnswered)


        }

        falseButton.setOnClickListener {
            if(!quizViewModel.isAnswered){
                checkAnswer(false)
            }

            isAnswered(quizViewModel.isAnswered)


        }

        cheatButton.setOnClickListener {
            val intent = CheatActivity.newIntent(this,quizViewModel.currentQuestionAnswer)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            isAnswered(quizViewModel.isAnswered)
            updateQuestion()
        }
        prevButton.setOnClickListener {
            quizViewModel.moveToBack()
            isAnswered(quizViewModel.isAnswered)
            updateQuestion()
        }

        questionTextView.setOnClickListener{
            quizViewModel.moveToNext()
            updateQuestion()
        }

        updateQuestion()
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK){
            return
        }

        if (requestCode == REQUEST_CODE_CHEAT){
            quizViewModel.isCheated = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN,false) ?: false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG,"onSaveInstantState")
        outState.putInt(KEY_INDEX,quizViewModel.currentIndex)
    }

    private fun updateQuestion(){
        if (answered == quizViewModel.questionBankSize){
            val percent = correct/quizViewModel.questionBankSize.toDouble() * 100
            val end = Toast.makeText(this,"Right percent: ${percent.toInt()}% $correct",Toast.LENGTH_LONG)
                end.setGravity(Gravity.TOP,0,0)
                end.show()

        }else{
            val questionTextResId = quizViewModel.currentQuestionText
            questionTextView.setText(questionTextResId)
        }

    }

    private fun checkAnswer(userAnswer:Boolean){
        ++answered
        quizViewModel.isAnswered=true
        val correctAnswer = quizViewModel.currentQuestionAnswer
        var messageResId = 0

         if (quizViewModel.isCheated){
              messageResId = R.string.judgment_toast
         }else if (userAnswer == correctAnswer){
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

            falseButton.setBackgroundColor(getColor(R.color.prim))
            trueButton.setBackgroundColor(getColor(R.color.prim))
        }
    }


}