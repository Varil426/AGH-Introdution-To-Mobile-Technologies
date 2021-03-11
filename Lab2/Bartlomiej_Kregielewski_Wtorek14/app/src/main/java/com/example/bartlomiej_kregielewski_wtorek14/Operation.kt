package com.example.bartlomiej_kregielewski_wtorek14

import java.lang.Exception
import java.lang.StringBuilder
import kotlin.math.pow

open class Operation(internal val a: IMathBeing, internal val b: IMathBeing, internal val operator: Operator) : IMathBeing {

    override fun evaluate(): Double {
        val operationType = operator.operationType
        return when (operationType) {
            OperationType.Addition -> a.evaluate()+b.evaluate()
            OperationType.Subtraction -> a.evaluate()-b.evaluate()
            OperationType.Multiplication -> a.evaluate()*b.evaluate()
            OperationType.Division -> a.evaluate()/b.evaluate()
            OperationType.Root -> {
                // a ROOT b means a-th root of b
                val power = 1/a.evaluate()
                b.evaluate().pow(power)
            }
            else -> throw Exception("Invalid operation")
        }
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append(a.evaluate())
        stringBuilder.append(operator.toString())
        stringBuilder.append(b.evaluate())
        return stringBuilder.toString()
    }

}