package com.example.bartlomiej_kregielewski_wtorek14

import java.lang.Exception
import java.lang.StringBuilder

class Expression: IMathBeing {

    private val subExpressions: MutableList<IMathBeing>

    // Default visibility modifier is public
    constructor(expr: IMathBeing) {
        subExpressions = mutableListOf()
        subExpressions.add(expr)
    }

    constructor(expr: String) {
        subExpressions = generateFromString(expr)
    }

    private fun generateSignsList(): List<String> {
        val list = mutableListOf<String>()
        list.add(MainActivity.context!!.getString(R.string.buttonAddition))
        list.add(MainActivity.context!!.getString(R.string.buttonSubtraction))
        list.add(MainActivity.context!!.getString(R.string.buttonMultiplication))
        list.add(MainActivity.context!!.getString(R.string.buttonDivision))
        list.add(MainActivity.context!!.getString(R.string.buttonRoot))
        list.add(MainActivity.context!!.getString(R.string.buttonPercent))

        return list.toList()
    }

    // One big mess, but working mess
    // It should be refactored if there was more time
    // For example percent handling should be done differently, instead of OperationWithPercent there can be class that inherits from Number and then in Operation.evaluate() it's handled differently
    private fun generateFromString(expr: String): MutableList<IMathBeing> {
        val tmpExpressionList = mutableListOf<IMathBeing>()
        val operationSigns = generateSignsList()
        val tmpString = StringBuilder()
        for (c in expr) {
            val cToString = c.toString()
            if (operationSigns.contains(cToString) && tmpString.isNotEmpty()) {
                tmpExpressionList.add(Number(tmpString.toString().toDouble()))
                tmpExpressionList.add(Operator(cToString))
                tmpString.clear()
            } else if (operationSigns.contains(cToString)) {
                val last = tmpExpressionList.last()
                if (last is Operator && last.operationType == OperationType.Percent) {
                    tmpExpressionList.add(Operator(cToString))
                } else {
                    throw Exception("That operator needs two arguments")
                }
            } else if (cToString.equals(MainActivity.context!!.getString(R.string.buttonSubtraction))) {
                // Done to enable usage of negative numbers (eg. 6--6 => 12, 6+-6 => 0)
                tmpString.append(cToString)
            } else {
                tmpString.append(cToString)
            }
        }
        if (tmpString.isNotEmpty()) {
            tmpExpressionList.add(Number(tmpString.toString().toDouble()))
            tmpString.clear()
        }

        while (tmpExpressionList.any { x: IMathBeing -> x is Operator }) {
            val highestOperator = tmpExpressionList.filterIsInstance<Operator>().maxByOrNull { x: Operator -> x.priority!! }
            val index = tmpExpressionList.indexOf(highestOperator)
            var newExpr: IMathBeing// = Number(0.0)

            /*if (highestOperator?.operationType == OperationType.Percent) {
                var operation = tmpExpressionList[index-1]
                if (operation is Operation) {
                    newExpr = OperationWithPercent(operation)
                    tmpExpressionList.removeAt(index-1)
                    tmpExpressionList.removeAt(index-1)
                }
            } else {
                var a = tmpExpressionList[index-1]
                var b = tmpExpressionList[index+1]
                newExpr = Operation(a,b,highestOperator!!)

                tmpExpressionList.removeAt(index-1)
                tmpExpressionList.removeAt(index-1)
                tmpExpressionList.removeAt(index-1)
            }*/
            val a = tmpExpressionList[index-1]
            val b = tmpExpressionList[index+1]
            newExpr = Operation(a,b,highestOperator!!)

            if (tmpExpressionList.size > index +2) {
                val nextExpr = tmpExpressionList[index+2]
                if (nextExpr is Operator && nextExpr.operationType == OperationType.Percent) {
                    newExpr = OperationWithPercent(newExpr)
                    tmpExpressionList.removeAt(index+2)
                }
            }

            tmpExpressionList.removeAt(index-1)
            tmpExpressionList.removeAt(index-1)
            tmpExpressionList.removeAt(index-1)

            tmpExpressionList.add(index-1, newExpr)
        }

        return tmpExpressionList
    }

    override fun evaluate(): Double {
        var result = 0.0
        subExpressions.forEach {expr -> result += expr.evaluate()}
        return result
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        subExpressions.forEach {
            stringBuilder.append(it.toString())
        }
        return stringBuilder.toString()
    }

}