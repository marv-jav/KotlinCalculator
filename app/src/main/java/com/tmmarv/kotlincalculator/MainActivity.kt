package com.tmmarv.kotlincalculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    var result: EditText? = null
    var newNumber: EditText? = null
    var displayOperation: TextView? = null

    var operand1: Double? = null
    var pendingOperation: String = "="

    private val STATE_PENDING_OPERATION = "PendingOperation"
    private val STATE_OPERAND = "Operand1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        result = findViewById(R.id.result)
        newNumber = findViewById(R.id.newNumber)
        displayOperation = findViewById(R.id.operation)

        val button0: Button = findViewById(R.id.button0)
        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)
        val button4: Button = findViewById(R.id.button4)
        val button5: Button = findViewById(R.id.button5)
        val button6: Button = findViewById(R.id.button6)
        val button7: Button = findViewById(R.id.button7)
        val button8: Button = findViewById(R.id.button8)
        val button9: Button = findViewById(R.id.button9)
        val buttonDot: Button = findViewById(R.id.buttonDot)

        var buttonEquals: Button = findViewById(R.id.buttonEquals)
        var buttonAdd: Button = findViewById(R.id.buttonAdd)
        var buttonSubtract: Button = findViewById(R.id.buttonMinus)
        var buttonMultiply: Button = findViewById(R.id.buttonMultiply)
        var buttonDivide: Button = findViewById(R.id.buttonDivide)
        var buttonClear: Button = findViewById(R.id.buttonClear)
        var buttonNegate: Button = findViewById(R.id.buttonNeg)

        val listener: View.OnClickListener = View.OnClickListener { view ->
            val b: Button = view as Button
            newNumber?.append(b.text.toString())
        }

        val buttons = arrayOf(button0, button1, button2, button3, button4, button5, button6, button7, button8, button9, buttonDot)
        for (button in buttons) {
            button.setOnClickListener(listener)
        }

        val opListener = View.OnClickListener { view ->
            val b: Button = view as Button
            val op = b.text.toString()
            val value = newNumber?.text.toString()
            try {
                val doubleValue= value.toDouble()
                performOperation(doubleValue, op)
            }catch (e: java.lang.NumberFormatException) {
                newNumber?.setText("")
            }
            pendingOperation = op
            displayOperation?.text = pendingOperation
        }

        val buttonOp = arrayOf(buttonAdd, buttonSubtract, buttonEquals, buttonMultiply, buttonDivide)
        for (button in buttonOp) {
            button.setOnClickListener(opListener)
        }

        buttonNegate.setOnClickListener {
            val value = newNumber?.text.toString()
            if (value.isEmpty()) {
                newNumber?.setText("-")
            }else{
                try {
                    var doubleValue = value.toDouble()
                    doubleValue *= -1;
                    newNumber?.setText(doubleValue.toString())
                }catch (e: NumberFormatException){
                    newNumber?.setText("")
                }
            }
        }

        buttonClear.setOnClickListener{
            var value = newNumber?.text.toString()
            if (pendingOperation != null){
                displayOperation?.text = ""
                operand1 = null
                result?.setText("")
                if (value.isNotEmpty()){
                    newNumber?.setText("")
                }
            }
        }
    }

    private fun performOperation(value: Double, operation: String) {
        if (null == operand1) {
            operand1 = value
        }else{
            if (pendingOperation == "=") {
                pendingOperation = operation
            }
            when(pendingOperation) {
                "=" -> operand1 = value
                "/" -> {
                    if (value == 0.0){
                        operand1 = 0.0
                    }else{
                        operand1 = operand1!! / value
                    }
                }
                "*" -> operand1 = operand1!! * value
                "-" -> operand1 = operand1!! - value
                "+" -> operand1 = operand1!! + value
            }
        }
        result?.setText(operand1.toString())
        newNumber?.setText("")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(STATE_PENDING_OPERATION, pendingOperation)
        if (operand1 != null){
            outState.putDouble(STATE_OPERAND, operand1!!)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION).toString()
        operand1 =savedInstanceState.getDouble(STATE_OPERAND)
        displayOperation?.text = pendingOperation
    }
}