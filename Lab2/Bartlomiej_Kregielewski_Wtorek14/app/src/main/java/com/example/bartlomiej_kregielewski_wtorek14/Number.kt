package com.example.bartlomiej_kregielewski_wtorek14

class Number(private val value: Double) : IMathBeing {

    override fun toString(): String {
        return value.toString()
    }

    override fun evaluate(): Double {
        return value
    }
}