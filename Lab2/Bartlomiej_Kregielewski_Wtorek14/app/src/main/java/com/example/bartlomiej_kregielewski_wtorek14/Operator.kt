package com.example.bartlomiej_kregielewski_wtorek14

import java.lang.Exception

class Operator : IMathBeing {

    var operationType: OperationType
        private set

    var priority: Int? = null
        private set

    private fun calcPriority(operationType: OperationType): Int {
        when (operationType) {
            OperationType.Percent -> return 0
            OperationType.Addition -> return 1
            OperationType.Subtraction -> return 1
            OperationType.Multiplication -> return 2
            OperationType.Division -> return 2
            OperationType.Root -> return 3
        }
    }

    constructor(operationType: OperationType) {
        this.operationType = operationType
        priority = calcPriority(operationType)
    }

    constructor(s: String) {
        when (s) {
            MainActivity.context!!.getString(R.string.buttonAddition) -> {
                operationType = OperationType.Addition
            }
            MainActivity.context!!.getString(R.string.buttonSubtraction) -> {
                operationType = OperationType.Subtraction
            }
            MainActivity.context!!.getString(R.string.buttonMultiplication) -> {
                operationType = OperationType.Multiplication
            }
            MainActivity.context!!.getString(R.string.buttonDivision) -> {
                operationType = OperationType.Division
            }
            MainActivity.context!!.getString(R.string.buttonRoot) -> {
                operationType = OperationType.Root
            }
            MainActivity.context!!.getString(R.string.buttonPercent) -> {
                operationType = OperationType.Percent
            }
            else -> {
                throw Exception("Invalid operator")
            }
        }
        priority = calcPriority(operationType)
    }

    override fun evaluate(): Double {
        throw Exception("Cannot evaluate operator")
    }

    override fun toString(): String {
        return when (operationType) {
            OperationType.Addition -> MainActivity.context!!.getString(R.string.buttonAddition)
            OperationType.Subtraction -> MainActivity.context!!.getString(R.string.buttonSubtraction)
            OperationType.Multiplication -> MainActivity.context!!.getString(R.string.buttonMultiplication)
            OperationType.Division -> MainActivity.context!!.getString(R.string.buttonDivision)
            OperationType.Root -> MainActivity.context!!.getString(R.string.buttonRoot)
            OperationType.Percent -> MainActivity.context!!.getString(R.string.buttonPercent)
        }
        throw Exception("Empty operator")
    }
}