# Kotlin In Action

## 1장 코틀린이란 무엇이며, 왜 필요한가?
- 자바 플랫폼에서 돌아가는 새로운 언어
- 간결하고 실용적, 자바 코드와의 상호운용성(interoperability) 중시

### 1.2 코틀린의 주요 특 정확한 코드 완성 기능을 제공할 수 있음
1. 대상 플랫폼: 서버, 안드로이드 등 자바가 실행되는 모든 곳
    - 코틀린의 주 목적: 자바가 사용되고 있는 모든 용도에 적합하면서 더욱 간결, 생산적, 안전한 대체 언어 제공
2. 정적 타입 지정 언어: 자바와 마찬가지로 정적 타입(statically typed) 언어
   * 타입 추론(type inference) 자바와 달리 변수의 타입을 프로그래머가 직접 명시할 필요가 없다. 컴파일러가 문맥으로부터 변수 타입을 자동 유추한다.
   * ```var x = 1```

> 정적 타입 지정: 모든 프로그램 구성 요소의 타입을 컴파일 시점에 알 수 있고 프로그램 안에서 객체의 필드나 메서드를 사용할 때마다 컴파일러가 타입을 검증해준다는 뜻  
> - 정적 타입 지정의 장점
>   - 성능: 실행 시점에 어떤 메서드르 호출할지 알아내는 과정이 필요없으므로 메서드 호출이 더 빠르다.
>   - 신뢰성: 컴파일러가 프로그램의 정확성을 검증하기 때문
>   - 유지보수성: 코드에서 다루는 객체가 어떤 타입에 속하는지 알 수 있기 때문
>   - 도구 지원: 더 정확한 코드 완성 기능을 제공할 수 있음


3. 함수형 vs 객체지향 프로그래밍
> 함수형 프로그래밍 특징
> - 일급 시민인 함수(first class): 함수를 일반 값처럼 다룰 수 있다. 함수를 변수에 저장할 수 있고 함수를 인자로 다른 함수에 전달할 수 있으며, 함수에서 새로운 함수를 만들어서 반환할 수 있다. 
> - 불변성(immutability): 함수형 프로그래밍에서는 일단 만들어지고 나면 내부 상태가 절대로 바뀌지 않는 불변 객체를 사용
> - 부수 효과(side effect) 없음: 입력이 같으면 항상 같은 출력, 다른 객체의 상태를 변경하지 않으며, 함수 외부나 다른 바깥 환경과 상호작용하지 않는 순수함수(pure function)를 사용
>
> 장점
> - 간결성: 명령형 코드에 비해 간결
> - 다중 스레드를 사용해도 안전(safe multithreading)
> - 테스트 용이: 부수 효과가 있는 함수는 실행에 필요한 환경을 구성하는 준비코드가 따로 필요하지만, 순수 함수는 독립적으로 테스트 가능

- 자바 8 이전에는 함수형 프로그래밍 지원 기능이 거의 없었다.
- 코틀린은 함수형 프로그래밍을 지원한다.

### 1.4 코틀린의 철학 
1. 실용성
- 코틀린은 실제 문제를 해결하기 위해 만들어진 실용적인 언어
- 다른 프로그래밍 언어가 채택한 이미 성공적으로 검증된 해법과 기능에 의존
- 특정 프로그래밍 스타일이나 패러다임을 사용할 것을 강제로 요구하지 않음

2. 간결성
- 코드를 잘 파악할 수 있도록 간결성을 강조
- 게터, 세터, 생성자 로직 등 자바에 존재하는 여러 준비 코드를 묵시적으로 제공
- 컬렉션에서 원소를 찾는 함수 등 다양한 라이브러리 함수를 제공 
- but 소스코드를 가능한 짧게 만드는 것이 코틀린 설계 목표는 아님

3. 안전성
- 안전성과 생산성 사이의 트레이드 오프
  - 컴파일러가 안전성을 검증하기 위해서는 더 많은 정보를 제공해야 하기 때문에 생산성이 하락
- 코틀린을 JVM에서 실행한다는 사실이 이미 상당한 안전성을 보장할 수 있다는 뜻
  - 메모리 안정성을 보장 
  - 버퍼 오버플로 방지
  - 동적으로 할당한 메모리를 잘못 사용해서 발생하는 문제 등 예방
- 코틀린 컴파일 시점에서 제공하는 검증
  - NullPointerException
    ```kotlin
      val s: String? = null  // null이 될 수 있음
      val s2: String = ""  // null이 될 수 없음
    ```

  - ClassCastException
    - 타입 검사와 캐스트가 한 연산자에 의해 이뤄진다.
4. 상호운용성
- 자바 코드에서 코틀린 코드를 아무 장치 없이 호출 가능
- 코틀린은 자바 표준 라이브러리 클래스에 의존
- 자바와 코틀린 소스 파일이 섞여있어도 컴파일 가능 

---
## 2장 코틀린 기초

### 2.1 함수와 변수 
1. Hello, World
- 함수 선언 fun
- 파라미터 이름 뒤에 타입
- 함수를 클래스 안에 넣지 않아도 된다.

2. 함수
```kotlin
// 블록이 본문인 함수    
fun max(a: Int, b: Int): Int {
        return if (a > b) a else b
    }

// 식이 본문인 함수
fun max(a: Int, b: Int): Int  = return if (a > b) a else b

// 반환 타입 생략
fun max(a: Int, b: Int) = return if (a > b) a else b
```
- 식이 본문인 함수의 경우 컴파일러가 함수 본문 식을 통해 함수 반환 타입을 지정 : 타입 추론


3. 변수
- 코틀린에서는 타입 지정을 생략하는 경우가 흔해서 변수 이름을 먼저 쓰고 타입을 명시하거나 생략
    ```kotlin
    val question = "It's monday."
    val answer = 42
    
    val answer: Int = 42
    ```

- 초기화 식을 사용하지 않고 변수를 선언할 때는 타입 명시
    ```kotlin
    val answer: Int
    answer = 42
    ```
- 코틀린 변수 키워드 
  - val (value)
    - 변경 불가능한 참조를 저장하는 변수
    - 자바에서 final 변수에 해당 
  - var (variable)
    - 변경 가능한 참조를 저장하는 변수
- 기본적으로 모든 변수를 val로 사용하고 꼭 필요한 경우에만 var로 변경하기
- val 참조 자체는 불변이지만 참조 객체 내부의 값은 변경 가능
    ```kotlin
    val languages = arrayListOf("Java")
    languages.add("Kotlin")
    ```
4. 문자열 템플릿
- 문자열 안에서 $변수 형태로 사용 가능 
    ```kotlin
    fun main(args: Array<String>) {
        val name = if (args.size > 0) args[0] else "Kotlin"
        println("Hello, $name!")
        
        // {} 사용하면 연산 가능 
        println("Hello, ${args[0]}!")
        
        // 중괄호로 둘러싼 식 안에서 큰 따옴표 사용 가능 
        println("Hello, ${if (args.size > 0) args[0] else "someone"}!")
  
        // 중괄호로 둘러싼 식 안에서도 문자열 템플릿 사용 가능 
        println("Hello, ${if (s.length > 2) "too short" else "normal string ${s}"}!")
    }
    ```

### 2.2 클래스와 프로퍼티
```java
/* java */
public class Person {
    private final String name;
    
    public Person(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
```

```kotlin
/* kotlin */ 
class Person(val name: String)
```
- 값 객체(value object) :  코드 없이 데이터만 저장하는 클래스

1. 프로퍼티 
- 클래스라는 개념의 목적: 데이터를 캡슐화하고 캡슐화한 데이터를 다루는 코드를 한 주체 아래 두는 것 
- 자바에서 프로퍼티 : 필드와 접근자 
- 코틀린은 프로퍼티를 언어 기본 기능으로 제공
```kotlin
class Person(
  val name: String, // 읽기 전용 프로퍼티 : 비공개 필드, 공개 게터 
  var isMarried: Boolean // 쓰기 가능 프로퍼티 : 비공개 필드, 공개 게터, 공개 세터 
)
```

```java
/* java */
Person person = new Person("Tom", true);
System.out.println(person.getName());
System.out.println(person.isMarried());
```
```kotlin
/* kotlin */
val person = Person("Tom", true)
println(person.name)
println(person.isMarried)
```

2. 커스텀 접근자

```kotlin
class Rectangle(val width: Int, var height: Int) {
    val isSquare: Boolean
        get() { // 프로퍼티 게터 선언 
            return height = width
        }
        // 블록 생략 가능 
        // get() = height = width
} 
```

3. 코틀린 소스코드 구조: 디렉토리와 패키지 
- 여러 클래스를 한 파일에 넣을 수 있고, 파일 이름도 마음대로 

### 2.3 선택 표현과 처리: enum과 when
1. enum 클래스 정의 
```kotlin
enum class Color(
  val r: Int, val g: Int, val b: Int
) {
    Red(255, 0, 0), 
    Orange(255, 165, 0);    // 코틀린에서 유일하게 ; 필수
  
    fun rgb() = (r * 256 + g) * 256 + b
}
```

2. when으로 enum 클래스 다루기 
- when: 자바의 switch
```kotlin
fun getMnemonic(color: Color) = 
    when (color) {
        Color.Red -> "Richard"
        Color.Orange -> "Of"
    }
    // 한 분기 안에 여러 값 사용하기 
    when (color) {
        Color.Red, Color.Orange -> "first"
        Color.yellow -> "second"
    }
```

3. when과 임의의 객체를 함께 사용
- 분기 조건에 상수만을 사용할 수 있는 자바 switch와 달리 코틀린 when은 임의의 객체를 허용

```kotlin
fun mix (c1: Color, c2: Color) = 
    when (setOf(c1, c2)) {
        setOf(Red, Yellow) -> Orange
        setOf(Yellow, Blue) -> Green
        else -> throw Exception("Unknown color")
    }
```

4. 인자 없는 when 사용
- 불필요한 객체 생성을 막을 수 있다. 
```kotlin
fun mixOptimized(c1: Color, c2: Color) = 
    when {
      (c1 == Red && c2 == Yellow) ||
              (c2 == Red && c1 == Yellow) ->
        Orange
      (c1 == Yellow && c2 == Blue) ||
              (c2 == Yellow && c1 == Blue) ->
        Green
      else -> throw Exception("Unknown color")
    }
```

5. 스마트 캐스트: 타입 검사와 타입 캐스트를 조합
- kotlin is 는 java instanceOf 
```kotlin
fun eval (e: Expr): Int {
    // 자바 스타일 코드 
    if (e is Num) { // 타입 확인 후 
        val n = e as Number // 타입 변환 
        return n.value
    }
    if (e is Sum) {
        return eval(e.right) + eval(e.left)
    }
    throw IllegalArgumentException("Unknown expression")
}
```

6. 리팩토링: if를 when으로 변경
- 코틀린 if는 자바 3항 연산자처럼 값을 만들어 낸다.
```kotlin
fun eval (e: Expr): Int =
    if (e is Num) {
        e.value
    } else if (e is Sum) {
        eval(e.right) + eval(e.left)
    } else {
        throw IllegalArgumentException("Unknown expression")
    }
```
- when 사용
```kotlin
fun eval (e: Expr): Int =
    when (e) {
        is Num ->
          e.value
        is Sum ->
          eval(e.right) + eval(e.left)
        else ->
          throw IllegalArgumentException("Unknown expression")
    }
```

7. if와 when의 분기에서 블록 사용
```kotlin
fun evalWithLogging(e: Expr): Int = 
    when (e) {
        is Num -> {
            println("num: ${e.value}")
            e.value
        }
        is Sum -> {
          val left = evalWithLogging(e.left)
          val right = evalWithLogging(e.right)
          println("sum: $left + $right")
          left + right
        }
      else -> throw IllegalArgumentException("Unknown expression")

    }
```

### 2.4 대상을 이터레이션: while과 for 루프
- 2장에서 설명한 코틀린 특성 중 자바와 가장 비슷한 것이 이터레이션
- for는 자바의 for-each 루프에 해당하는 형태만 존재

1. while 루프
  - while, do-while 루프는 자바와 동일

2. 수에 대한 이터레이션: 범위와 수열
  - 자바 for 루프 대신 범위(range) : `val oneToTen = 1..10`
    ```kotlin
    for (i in 1..100) {
        print(fizzBuzz(i))
    }
    
    for (i in 100 downTo 1 step 2) {
        print(fizzBuzz(i))
    }
    ```
    
3. 맵에 대한 이터레이션
   - .. 연산자를 문자 타입의 값에도 적용 가능 
    ```kotlin
    for (c in 'A'..'F') {
        ~~
    }
    ```
   
4. in으로 컬렉션이나 범위의 원소 검사
```kotlin
fun isLetter(c: Char) c in 'a'..'z' || c in 'A'..'Z'
fun isNotDigit(c: Char) c !in '0'.. '9'
```

- when에서 사용하기 
```kotlin
fun recoginze(c: Char) = when (c) {
    in '0'..'9' -> "It's a digit"
    in 'a'..'z', in 'A'..'Z' -> "It's a letter"
    else -> "I don't know"
} 
```

### 2.5 코틀린의 예외 처리 
- 코틀린의 예외 처리는 자바와 비슷하다. 함수는 정상적으로 종료할 수 있지만 오류가 발생하면 예외를 던질 수 있다. 

1. try, catch, finally
   - 자바 코드와 가장 큰 차이는 throw 절이 코드에 없다는 점이다. 자바에서는 체크 예외를 명시적으로 처리해야 한다. 
   - 다른 최신 JVM 언어와 마찬가지로 코틀린도 체크 예외와 언체크 예외를 구별하지 않는다. 

2. try를 식으로 사용
   - 코틀린의 try 키워드는 if나 when과 마찬가지로 식이다. 따라서 try의 값을 변수에 대입할 수 있다. 
   ```kotlin
    val number = try {
      Integer.parseInt(reader.readLine()) 
   } catch (e: NumberFormatException) {
      return
   } 
    ```
   

### 요약
- 함수를 정의할 때 fun 키워드를 사용한다. val과 var는 각각 읽기 전용 변수와 변경 가능한 변수를 선언할 때 쓰인다.
- 문자열 템플릿을 사용하면 문자열을 연결하지 않아도 되므로 코드가 간결해진다. 변수 이름 앞에 $를 붙이거나 식을 ${식}처럼 ${}로 둘러싸면 변수나 식의 값을 문자열 안에 넣을 수 있다. 
- 코틀린에서는 값 객체 클래스를 간결하게 표현할 수 있다. 
- 다른 언어에도 있는 if는 코틀린에서 식이며, 값을 만들어낸다.
- 코틀린 when은 자바의 switch와 비슷하지만 더 강력하다.
- 어떤 변수의 타입을 검사하고 나면 그 변수를 캐스팅하지 않아도 검사한 타입의 변수처럼 사용할 수 있다. 그런 경우 컴파일러가 스마트 캐스트를 활용해 자동으로 타입을 바꿔준다.
- for, while, do-while 루프는 자바가 제공하는 같은 키워드의 기능과 비슷하다. 하지만 코틀린의 for는 자바의 for보다 편리하다. 특히 맵을 이터레이션하거나 이터레이션 하면서 컬렉션의 원소와 인덱스를 함께 사용해야 하는 경우 코틀린의 for가 더 편리하다.
- 1..5와 같은 식은 범위를 만들어낸다. 범위와 수열은 코틀린에서 같은 문법을 사용하며, for 루프에 대해 같은 추상화를 제공한다. 어떤 값이 범위 안에 들어있거나 들어있지 않은지 검사하기 위해서 in이나 !in을 사용한다.
- 코틀린 예외 처리는 자바와 비슷하다. 다만 코틀린에서는 함수가 던질 수 있는 예외를 선언하지 않아도 된다.

---
## 3장 함수 정의와 호출
- 컬렉션, 문자열, 정규식을 다루기 위한 함수
- 이름 붙인 인자, 디폴트 파라미터 값, 중위 호출 문법 사용
- 확장 함수와 확장 프로퍼티를 사용해 자바 라이브러리 적용
- 최상위 및 로컬 함수와 프로퍼티를 사용해 코드 구조화 

### 3.1 코틀린에서 컬렉션 만들기

```kotlin
val set = hashSetOf(1, 7, 77)
val list = arrayListOf(1, 3, 55)
val map = hashMapOf(1 to "one", 7 to "seven", 45 to "forty-five")
```
- 코틀린은 자체 컬렉션을 제공하지 않고 표준 자바 컬렉션을 활용한다.
  - 자바 코드와의 상호작용을 위해
  - 하지만 더 많은 기능을 쓸 수 있다.
```kotlin
val strings = listOf("first", "second", "third")
println(strings.last())
//third

val numbers = setOf(1, 12, 44)
println(numbers.max)
//14
```

### 3.2 함수를 호출하기 쉽게 만들기
```kotlin
// 함수 선언을 간단하게 만들 수 있게 코틀린이 지원하는 여러 기능을 사용하지 않고 직접 구현
fun <T> joinToString (
    collection: Collection<T>,
    separator: String,
    prefix: String,
    postfix: String
): String {
    val result = StringBuilder(prefix)
    for((index, element) in collection.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}

val list = listOf(1, 2, 3)
println(joinToString(list, "; ", "(", ")"))
```

1. 이름 붙인 인자
- 함수 호출 부분의 가독성을 개선해보자  
- 위의 예시에서 인자로 전달한 각 문자열이 어떤 역할을 하는지 구분이 어렵다.  
  `joinToString(list, "; ", "(", ")")`
- 코틀린에서는 함수에 전달하는 인자의 이름을 명시할 수 있다.  
  `joinToString(collection, separator = " ", prefix = " ", postfix = "."`

2. 디폴트 파라미터 값
- 함수 선언에서 파라미터의 디폴트 값을 지정할 수 있다. 
```kotlin
fun <T> joinToString (
    collection: Collection<T>,
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
): String

joinToString(list)
joinToString(list, "; ") // 일부만 생략하면 뒷부분 인자들이 생략됨
```

3. 정적인 유틸리티 클래스 없애기: 최상위 함수와 프로퍼티 
- 자바에서는 메서드가 클래스 내에 포함되어야 하므로 정적 메서드를 모아두는 역할만 담당하며, 특별한 상태나 인스턴스 메서드는 없는 클래스가 생겨난다. 
- 코틀린에서는 함수를 소스 파일 최상위 수준, 모든 다른 클래스의 밖에 위치시키면 된다. 
- JVM이 클래스 안에 들어있는 코드만을 실행할 수 있기 때문에 컴파일러가 파일명에 해당하는 클래스를 새로 정의해준다.

- join.kt 파일
    ```kotlin
    package strings
    fun joinToString(...): String {...}
    ```
  
    ```java
    /* join.kt를 컴파일한 결과를 자바 코드로 써보면 아래와 같다. */
    package strings;
    public class JoinKt {
        public static String joinToString(...) {...}
    }
    ```
- 최상위 프로퍼티: 함수와 마찬가지로 프로퍼티도 파일 최상위 수준에서 선언 가능 

### 3.3 메서드를 다른 클래스에 추가: 확장 함수와 확장 프로퍼티 
- 확장함수: 어떤 클래스의 멤버 메서드인 것처럼 호출할 수 있지만 그 클래스의 밖에 선언된 함수
  - 수신 객체 타입: 이 함수가 확장할 클래스의 이름
  - 수신 객체: 확장 함수가 호촐되는 대상이 되는 값(객체)
```kotlin
fun String.lastChar(): Char = this.get(this.length - 1)
    // 수신 객체 타입           // 수신 객체 

println("Kotlin".lastChar())
```
- String 클래스에 새로운 메서드를 추가하는 것과 같다. 심지어 String이 자바나 코틀린 등의 언어 중 어떤 것으로 작성됐는가도 중요하지 않다.
- 일반 메서드의 본문에서 this를 사용할 때와 마찬가지로 확장 함수 본문에도 this를 쓸 수 있다. (생략도 가능)

```kotlin
fun String.lastChar(): Char = get(length - 1)
```

- 확장함수 안에서는 클래스 내부에서만 사용할 수 있는 private 멤버나 protected 멤버를 사용할 수 없다.

1. 임포트와 확장 함수
- 확장 함수를 사용하기 위해서는 임포트해야 한다. 
```kotlin
import strings.lastChar
val c = "Kotlin".lastChar()

// *를 사용한 임포트
import strings.*
val c = "Kotlin".lastChar()

// as를 사용한 임포트
import string.lastChar as last
val c = "Kotlin".last()
```
- 한 파일 안에서 다른 여러 패키지에 속해있는 이름이 같은 함수를 가져와 사용해야 하는 경우 이름을 바꿔서 임포트하면 충돌을 막을 수 있다. 
- ?? `물론 일반적인 클래스나 함수라면 그 전체 이름을 써도 된다. 하지만 코틀린 문법상 확장 함수는 반드시 짧은 이름을 써야 한다.`

2. 자바에서 확장 함수 호출 
- 내부적으로 확장 함수는 수신 객체를 첫번째 인자로 받는 정적 메서드다.

```Java
// StringUtil.kt 파일에 정의된 확장 함수
import string.StringUtilKt
char c = StringUtilKt.lastChar("Java");
```


3. 확장 함수로 유틸리티 함수 정의 
