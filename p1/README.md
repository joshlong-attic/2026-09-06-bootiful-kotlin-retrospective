# Basics

## `val`/`var`

show how variables can and can't be assigned to values

## convenient literals

* show how `1` is an `Int`
* show how `1.0` is a `Double`
* show how `true` is a `Boolean`
* show how `null` is a `Nothing`
* show how `1..2` is a `Range<Int>`
* show how `"the sum is ${1 + 1}"` is a `String`
* show how `listOf(1,2,3)` is a `List<Int>`

## functions, putting the fun back in functions

* show java-like version where the method is written long hand
* show how the result expression can be assigned to the return value eg, `fun add(a:Int, b: Int) = a + b`.

## basic class and interface OOP

* define a class with a method

```
abstract class BaseRunner {
    protected fun log(message: String) {
        println(message)
    }
}
```

* create an implementation

```
class MyApplicationRunner : ApplicationRunner, BaseRunner() {
    override fun run(args: ApplicationArguments) {
        log("Hello World")
    }
}
```

* anonymous inner classes

```
val runner  = object : ApplicationRunner {
  override fun run(args: ApplicationArguments) {
    
    }
}
```

## lambdas
* show how lambdas can lign with an interface:
```
val myHandler: ApplicationRunner = { println("Hello World!") }
```

* show how lambdas can be used for structural lambdas

```
var myHandleAnonymous = { str: String -> str.length }
println (myHandleAnonymous("Hello World!"))
```

* show how lambdas can be used to replace method references

```
val printer = { x: Int -> println(x) }
list.forEach(printer)
```

* show how lambdas can be blocks at the end of parameter lists
```
val list = listOf(1,2,3)
list.forEach { x-> println(x) }
```

* show how lambdas have an invisible param called `it`
```
val list = listOf(1,2,3)
list.forEach { println(it) }
```



## outline

* intro to Kotlin (Josh)
    * `val`/`var`
    * expression oriented (if / else , switched etc)
    * structural lambdas
    * extension functions
    * no primitives
    * string interpolation
    * effect oriented `.apply` / `.let` etc
    * inheritance
    * data classes
    * operator overloads
    * blocks from lambda params
    * unified interfaces (Array<Int> vs int[])
* bootiful Kotlin 101 (Josh)
* Kotlin and beyond (James)
