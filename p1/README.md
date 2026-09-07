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
* show how everything's an expreson: `val result = if (true) 1 else 2`
* show how everything's a unified interface: `val list: List<Int> = listOf(1,2,3)`; `val array : Array<Int> = arrayOf(1,2,3)`

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

* data classes
```
data class Person(val name: String, val age: Int)
```

## functions++ 

* extension methods. two things happening in this example: method is added to `String`, and the callback takes place in the context of `String`
```
fun String.apply(block: String.() -> Unit): String  {
    block()
    return this
}
```

* operator overloading 
```
data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }
}
val p1 = Point(1,2)
val p2 = Point(3,4)
val p3 = p1 + p2
print (p3)
```

## lambdas
* show how lambdas can align with an interface:
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

## functional transformations

* `.map `: `val x = listOf(1, 2, 3, 4, 5).map { it * 2 }`

* `.let` : kind of like if u could map a single variable; transforms the value to another value
```
    fun print(range: IntRange) = println(range)
    val result  = (1..2).let {
        print(it)
        it.sum()
    }
```

* `.apply`: lets u work in the context of the receiver
```
    val result = (1..2)
        .apply { print(this.first) }
        .let {
            it.sum()
        }
```



