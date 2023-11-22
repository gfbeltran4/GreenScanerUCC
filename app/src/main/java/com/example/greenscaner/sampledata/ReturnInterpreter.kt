package com.example.greenscaner.sampledata

interface ReturnInterpreter {
    fun classify(confidence:FloatArray,maxConfidence:Int)
}