package com.example.bartlomiej_kregielewski_wtorek14

import java.lang.Exception
import kotlin.math.pow

class OperationWithPercent(operation: Operation) : Operation(operation.a, operation.b, operation.operator) {

    init {
        if (operator.operationType == OperationType.Root) {
            throw Exception("Invalid operation")
        }
    }

    override fun evaluate(): Double {
        return when (operator.operationType) {
            OperationType.Addition -> a.evaluate()+(b.evaluate()/100)*a.evaluate()
            OperationType.Subtraction -> a.evaluate()-(b.evaluate()/100)*a.evaluate()
            OperationType.Multiplication -> a.evaluate()*(b.evaluate()/100)*a.evaluate()
            OperationType.Division -> a.evaluate()/(b.evaluate()/100)*a.evaluate()
            else -> throw Exception("Invalid operation")
        }
    }

    override fun toString(): String {
        return super.toString() + "%"
    }

}