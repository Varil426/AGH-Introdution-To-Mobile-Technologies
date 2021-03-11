package com.example.bartlomiej_kregielewski_wtorek14

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import android.widget.TextView
import java.lang.Exception

// Bartlomiej Kregielewski
// Tasks done:
// 1. All done
// 2. All done: (Implemented own logic)
// 3. All done: (Error is shown if operation is invalid)
// 4. All done: (see MainActivity.inputText.set())
// 5. All done: (works as long as it's still one symbol)
// 6. All done: (TextView ellipsized, singleLine)
// 7. All done: (?) - it's calculated every input change, equals (=) resets input field, but leaves output
// 8. All done: Root - done (logic: A ROOT B => A-th root of B, example: 3 ROOT 8 => 2), percent - done (logic: A OPERATOR B% => A OPERATOR B/100*A, example: 100 + 10% - 10% = 110 - 10% = 99)
// 9. All done: Root has the highest priority, percent lowest. It's doesn't exclude task 7 if we use two displays ;)

// Tested on:
// 3 ROOT 8 + 100% => 4
// 18 + 2 * 4 / 2 => 22
// 4 * 2.5 => 10
// 2.5 ROOT 8 => 2.297...
// .5 ROOT 2 => 4
// and many more...

class MainActivity : AppCompatActivity() {

    // Yes, I know that this is a bad practice
    public companion object {
        public var context: Context? = null
    }

    private var buttons: MutableList<Button> = mutableListOf()

    private var inputText: String
        get() {
            val calculatorInputTextView = findViewById<TextView>(R.id.calculatorInput)
            return calculatorInputTextView.text.toString()
        }
        set(value) {
            val calculatorInputTextView = findViewById<TextView>(R.id.calculatorInput)
            calculatorInputTextView.text = value
            evaluateInput()
        }

    private var outputText: String
        get() {
            val calculatorOutputTextView = findViewById<TextView>(R.id.calculatorOutput)
            return calculatorOutputTextView.text.toString()
        }
        set(value) {
            val calculatorOutputTextView = findViewById<TextView>(R.id.calculatorOutput)
            calculatorOutputTextView.text = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this

        val rootView = findViewById<TableLayout>(R.id.rootTableLayout)
        val rootViewDescendants = rootView.getAllChildren()
        for (descendant in rootViewDescendants) {
            if (descendant is Button) {
                buttons.add(descendant)
            }
        }

        for (button in buttons) {
            button.setOnClickListener { view -> buttonClicked(view) }
        }
    }

    private fun buttonClicked(view: View) {
        if (view is Button) {
            if (view.text.equals(getString(R.string.buttonClear))) {
                inputText = ""
            } else if (view.text.equals(getString(R.string.buttonEquals))) {
                val calculatorInputTextView = findViewById<TextView>(R.id.calculatorInput)
                calculatorInputTextView.text = ""
            }
            else {
                inputText += view.text
            }
        }
    }

    private fun evaluateInput() {
        try {
            val expr = Expression(inputText)
            val result = expr.evaluate()
            outputText = result.toString()
        } catch (e: Exception) {
            outputText = "Error"
        }
    }

    /**
     * Extension method to get all descendants of view
     */
    private fun View.getAllChildren(): List<View> {
        val result = ArrayList<View>()
        if (this !is ViewGroup) {
            result.add(this)
        } else {
            for (index in 0 until this.childCount) {
                val child = this.getChildAt(index)
                result.addAll(child.getAllChildren())
            }
        }
        return result
    }
}