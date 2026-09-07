package com.example.basics

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class BasicsApplication

fun main(args: Array<String>) {

    //	runApplication<BasicsApplication>(*args)

    val myHandler: ApplicationRunner = { println("Hello World!") }

    var myHandleAnonymous = { str: String -> str.length }
    println (myHandleAnonymous("Hello World!"))

    var list = listOf(1,2,3,4,5)
    val printer = { x: Int -> println(x) }
    list.forEach(printer)
    val runner = object : ApplicationRunner {
        override fun run(args: ApplicationArguments) {
            TODO("Not yet implemented")
        }
    }
}

abstract class BaseRunner {
    protected fun log(message: String) {
        println(message)
    }
}

class MyApplicationRunner : ApplicationRunner, BaseRunner() {

    override fun run(args: ApplicationArguments) {
        TODO("Not yet implemented")
    }
}


fun add(a: Int, b: Int): Int {
    return a + b
}

fun buildMessage(networkResult: NetworkResult): String {
    return when (networkResult) {
        is NetworkResult.Failure -> "Failure: ${networkResult.error.message}"
        is NetworkResult.Success -> "Success: ${networkResult.data}"
        NetworkResult.Loading -> "Loading..."
    }
}

sealed class NetworkResult {
    data class Success(val data: String) : NetworkResult()
    data class Failure(val error: Throwable) : NetworkResult()
    object Loading : NetworkResult()
}