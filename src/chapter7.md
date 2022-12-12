# 7장 연산자 오버로딩과 기타 관례
- 연산자 오버로딩 
- 관례 : 여러 연산을 지원하기 위해 특별한 이름이 붙은 메서드
- 위임 프로퍼티

## 7.1 산술 연산자 오버로딩
- 자바에서는 원시 타입에 대해서만 산술 연산자를 사용할 수 있고, String에 대해 + 연산자 사용 가능
- 코틀린에서는 BigInteger 등에서도 산술 연산자 사용 가능 

### 이항 산술 연산 오버로딩
```kotlin
data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point): Point {    // plus 라는 이름의 연산자 함수 정의 
        return Point(x + other.x, y + other.y)
    }
}
>>> val p1 = Point(10, 20)
>>> val p2 = Point(30, 40)
>>> println(p1 + p2)    // + 로 계산하면 plus가 호출
Point(x=40, y=60)
```

- 연산자 오버로딩하는 함수 앞에는 `operator` 키워드 필수
  - operator 키워드를 붙임으로써 어떤 함수가 관례를 따르는 함수임을 명확히 할 수 있다. 
  - operator 키워드 없이 관례에서 사용하는 함수 이름을 쓰면 에러 "operator modifier is required on 'plus' in 'Point'" 
  - a + b ---컴파일----> a.plus(b)

```kotlin
operator fun Point.plus(other: Point) : Point {
    return Point(x + other.x, y + other.y)
}
```
- 연산자를 멤버 함수로 만드는 대신 확장 함수로 정의할 수도 있다.
- 외부 함수의 클래스에 대한 연산자를 정의할 때는 관례를 따르는 이름의 확장 함수로 구현하는게 일반적인 패턴이다.??

- 다른 언어와 비교할 때 코틀린에서 오버로딩한 연산자를 정의하고 사용하기가 더 쉽다.
  - 코틀린에서는 직접 연산자를 만들어 사용할 수 없고 언어에서 미리 정해둔 연산자만 오버로딩할 수 있다.

- 오버로딩 가능한 이항 산술 연산자  

  | 식   | 함수 이름 |  
  |---|-------|  
  | a*b | times |  
  | a/b | div |  
  | a%b | mod(1.1부터 rem) |  
  | a+b | plus |  
  | a-b | minus |  
- 연산자 우선 순위는 언제나 표준 숫자 타입에 대한 연산자 우선 순위와 같다. 

- 연산자 함수의 반환 타입이 꼭 두 피연산자 중 하나와 일치해야 하는 것도 아니다.
```kotlin
operator fun Char.times(count: Int): String {
    return toString().repeat(count)
}
>>> println('a'*3)
aaa
```
- 일반 함수와 마찬가지로 operator 함수도 오버로딩할 수 있다. 

### 복합 대입 연산자 오버로딩 
- plus와 같은 연산자를 오버로딩하면 그와 관련 있는 연산자인 +=도 자동으로 함께 지원한다. 
- +=, -+ 등의 연산자는 복합 대입 연산자(compound assignment)라 불린다. 
```kotlin
// 동일한 식
point += Point(3, 4)
point = point + Point(3, 4)
```

- 경우에 따라 복합 대인 연산이 객체에 대한 참조를 바꾸기보다 원래 객체의 내부 상태를 변경하게 만들고 싶을때가 있다. 
  - 변경 가능한 컬렉션에 원소를 추가하는 경우가 대표적인 예
```kotlin
val numbers = ArrayList<Int>
numbers += 42
println(numbers[0])
42
```

- 반환 타입이 Unit인 plusAssign 함수를 정의하면 코틀린은 += 연산자에 그 함수를 사용한다. 
  - 다른 복합 대입 연산자 함수도 비슷하게 minusAssign, timeAssign 등의 이름을 사용

```kotlin
operator fun <T> MutableCollection<T>.plusAssign(element: T) {
    this.add(element)
}
```
- 코틀린 표준 라이브러리는 변경 가능한 컬렉션에 대해 plusAssign을 정의
  - 앞의 예제는 그 plusAssign을 사용

- 이론적으로는 코드에 있는 +=를 plus와 plusAssign 양쪽으로 컴파일 할 수 있다.
  - plus와 plusAssign 연산을 동시에 정의하지 않는 것이 좋다. 

- +와 -는 항상 새로운 컬렉션을 반환하며, +=와 -= 연산자는 항상 변경 가능한 컬렉션에 작용해 메모리에 있는 객체 상태를 변화시킨다.
- 또한 읽기 전용 컬렉션에서 +=와 -+는 변경을 적용한 복사본을 반환한다.(따라서 var로 선언한 변수가 가리키는 읽기 전용 컬렉션에만 +=와 -=를 적용할 수 있다.)
- 이런 연산자의 피 연산자로으는 개별 원소를 사용하거나 우너소 타입이 일치하는 다른 컬렉션을 사용할 수 있다.

```kotlin
val list = arryaListOf(1, 2)
list += 3
val newList = list + listOf(4, 5)
println(list)
>>> [1, 2, 3]
println(newList)
>>> [1, 2, 3, 4, 5]
```

### 단항 연산자 오버로딩
- 이항 연산자와 마찬가지로 미리 정해진 이름의 함수를 멤버나 확장 함수로 선언하면서 operator로 표시하면 된다. 

```kotlin
operator fun Point.unaryMinus(): Point {
    return Point(-x, -y)
}
val p = Point(10, 20)
println(-p)
>>> Point(x=-10, y=-20)
```
- 단항 연산자를 오버로딩하기 위해 사용하는 함수는 인자를 취하지 않는다. 
- 오버로딩할 수 있는 단항 산술 연산자
  
  | 식   | 함수 이름 |   
    |---|---|    
    | +a | unaryPlus |  
    | -a | unaryMinus |  
    | !a | not |  
    | ++a, a++ | inc |  
    | --a, a-- | dec |  

- inc, dec 함수를 정의해 증가/감소 연산자를 오버로딩하는 경우 컴파일러는 일반적인 값에 대한 전위나 후위 증가/감소 연산자와 같은 의미를 제공한다.

```kotlin
operator fun BigDecimal.inc() = this + BigDecimal.ONE
var bd = BigDecimal.ZERO
println(bd++)   // 후위 증가 연산자
>>> 0 
println(++bd)   // 전위 증가 연산자
>>> 2
```


## 7.2 비교 연산자 오버로딩
- 코틀린에서는 원시 타입 값뿐 아니라 모든 객체에 대해 비교 연산을 수행할 수 있다. 
  - equals나 compareTo를 호출해야 하는 자바와 달리 == 비교 연산자를 직접 사용할 수 있다. 

### 동등성 연산자: equals
- 코틀린은 == 연산자 호출을 equals 메서드 호출로 컴파일한다. 
  - != 연산자를 사용하는 식도 equals 호출로 컴파일 된다.
- ==와 !=는 내부에서 인자가 널인지 검사하므로 다른 연산과 달리 널이 될 수 있는 값에도 적용할 수 있다.
  - a == b --------컴파일-------> a?.equals(b) ?: (b == null)
    - a가 널인지 판단해서 널이 아닌 경우에만 equals를 호출한다. a가 널이면 b도 널인 경우에만 true

- data 클래스는 컴파일러가 자동으로 equals를 생성해준다.
- Point에 대한 equals를 직접 구현
```kotlin
class Point(val x: Int, val y: Int) {
    // Any에 정의된 메서드를 오버라이딩
    override fun equsls(obj: Any?): Boolean {
        // 최적화: 파라미터가 this와 같은 객체인지 확인
        if (obj === this) return true
        // 파라미터 타입을 검사
        if (obj !is Point) return false
        // Point로 스마트 캐스트해서 x와 y 프로퍼티에 접근 
        return obj.x == x && obj.y == y
    }
}
```
- 식별자 비교(identity equals) 연산자(===)를 사용해 equals의 파라미터가 수신 객체와 같은지 살펴본다.
  - 식별자 비교 연산자는 자바 == 연산자와 같다. 
  - 따라서 ===는 자신의 두 피연산자가 서로 같은 객체를 가리키는지(원시 타입인 경우 두 값이 같은지) 비교한다.
- equals를 구현할 때는 ===를 사용해 자기 자신과의 비교를 최적화하는 경우가 많다. 
- ===를 오버로딩할 수는 없다. 

- equals는 Any에 정의된 메서드이므로 override가 필요하다.
  - Any에 정의된 equals에는 operator가 붙었지만 그 메서드를 오버라이드하는 하위 클래스 메서드에는 붙이지 않아도 된다.
- Any에서 상속 받은 equals가 확장 함수보다 우선순위가 높기 때문에 equals를 확장 함수로 정의할 수 없다. 


### 순서 연산자: compareTo
- 자바에서 정렬이나 최댓값, 최솟값 등 값을 비교해야 하는 알고리즘에 사용할 클래스는 Comparable 인터페이스를 구현해야 한다.
  - Comparable에 들어있는 compareTo 메서드는 한 객체와 다른 객체의 크기를 비교해 정수로 나타내준다. 
  - <, > 연산자로는 원시 타입의 값만 비교할 수 있다. 
  - 다른 모든 타입의 값에는 element1.compareTo(element2)를 명시적으로 사용해야 한다.
- 코틀린도 똑같은 Comparable 인터페이스를 지원한다.
  - compareTo 메서드를 호출하는 관례를 제공해서 비교 연산자 (<, >, <=, >=)는 compareTo로 컴파일된다.
  - compareTo가 반환하는 값은 Int
  - p1 < p2 는 p1.compareTo(p2) < 0 과 같다. 

```kotlin
class Person(
  val firstName: String, val lastName: String
) : Comparable<Person> {
    override fun compareTo(other: Person): Int {
        return compareValuesBy(this, other, Person::lastName, Person::firstName)
    }
}
val p1 = Person("Alice", "Smith")
val p2 = PErson("Bob", "Johnson")
println(p1 < p2)
>>> false
```
- 코틀린 표준 라이브러리의 compareValuesBy 함수를 사용해 compareTo를 쉽고 간결하게 정한다.
  - compareValuesBy 두 객체와 여러 비교 함수를 인자로 받는다.
    - 첫번째 비교 함수에 두 객체를 넘겨서 두 객체가 같지 않다는 결과가 나오면 그 결과 값을 즉시 반환
      - 두 객체가 같다는 결과가 나오면 두 번째 비교 함수를 통해 두 객체를 비교



## 7.3 컬렉션과 범위에 대해 쓸 수 있는 관례
- 컬렉션을 다룰 때 인덱스를 사용해 원소를 읽거나 쓰는 연산과 어떤 값이 컬렉션에 속해있는지 검사하느 ㄴ연산을 많이 사용
- 이 모든 연산을 연산자 구문으로 사용할 수 있다. 

### 인덱스로 원소에 접근: get과 set
- 코틀린은 맵의 원소에 접근할때 각괄호[]를 사용한다.

```kotlin
operator fun Point.get(index: Int): Int {
    return when(index) {
        0 -> x
        1 -> y
        else -> 
          throw java.lang.IndexOutOfBoundsException("Invalid coordinate $index")
    }
}
val p = Point(10, 20)
println(p[1])
>>> 20
```
- operator 변경자를 붙인 get 메서드를 정의하면 된다. 
- get 메서드의 파라미터로 Int가 아닌 타입도 사용할 수 있다. 

- 인덱스에 해당하는 컬렉션 원소를 쓰고 싶을 때는 set 함수를 정의하면 된다. 
```kotlin
data class MutablePoint(var x: Int, var y: Int)

operator fun MutablePoint.set(index: Int, value: Int) {
	when(index) {
		0 -> x = value
		1 -> y = value
		else -> throw IndexOutOfBoundsException("Invalid coordinate ${index}")
	}
}

val p = MutablePoint(10, 20)
p[1] = 42
println(p) 
>>> MutablePoint(x=10, y=42)
```


### in 관례
- in은 객체가 컬렉션에 들어있는지 검사한다. 
- in 연산자와 대응하는 함수는 contains

```kotlin
data class Rectangle(val upperLeft: Point, val lowerRight: Point)

operator fun Rectangle.contains(p: Point): Boolean {
	return p.x in upperLeft.x until lowerRight.x &&
            p.y in upperLeft.y until lowerRight.y
}

val rect = Rectangle(Point(10, 20), Point(50, 50))
println(Point(20, 30) in rect)
>>> true
println(Point(5, 5) in rect)
>>> false
```

### rangeTo 관례
- 범위를 만들려면 .. 구문을 사용해야 한다. 
- rangeTo 함수는 범위를 반환한다. 
  - 하지만 어떤 클래스가 Comparable 인터페이스를 구현하면 rangeTo를 정의할 필요가 없다.
```kotlin
val now = LocalDate.now()
val vacation = now..now.plusDays(10)
println(now.plusWeeks(1) in vacation)
>>> true
```
- rangeTo 연산자는 다른 산술 연산자보다 우선순위가 같다. 
  - 혼동을 피하기 위해 괄호



### for 루프를 위한 iterator 관례 
- for 루프는 범위 검사와 똑같이 in 연산자를 사용한다. 
- 코틀린 표준 라이브러리는 String의 상위 클래스인 CharSequence에 대한 iterator 확장 함수를 제공한다.
```kotlin
operator fun CharSequence.iterator(): CharIterator
>>> for (c in "abc") {}
```

## 7.4 구조 분해 선언(destructuring declaration)과 component 함수
- 구조 분해를 사용하면 복합적인 값을 분해해서 여러 다른 변수를 한꺼번에 초기화할 수 있다. 

```kotlin
val p = Point(10, 20)
val (x, y) = p
println(x)
>>> 10
println(y)
>>> 20
```
- 구조 분해 선언은 일반 변수 선언과 비슷해 보인다. 다만 = 의 좌변에 여러 변수를 괄호로 묶었다는 점이 다르다. 
- 내부에서 구조 분해 선언은 다시 관례를 사용한다. 구조 분해 선언의 각 변수를 초기화하기 위해 componentN이라는 함수를 호출한다. 
  - N은 구조 분해 선언에 있는 변수 위치에 따라 붙은 번호

> val (a, b) = p   ------컴파일----> val a = p.component1(), val b = p.component2()
- data 클래스의 주 생성자에 들어있는 프로퍼티에 대해서는 컴파일러가 자동으로 componentN 함수를 만들어준다. 
- 데이터 타입이 아닌 클래스에서는 아래와 같이 구현한다. 
```kotlin
class Point(val x: Int, val y: Int) {
    operator fun component1() = x
    operator fun component2( ) = y
}
```
- 구조 분해 선언은 여러 값을 반환할 때 유용하다. 
- 여러 값을 한꺼번에 반환해야 하는 함수가 있다면 반환해야 하는 모든 값이 들어갈 데이터 클래스를 정의하고 함수의 반환 타입을 그 데이터 클래스로 바꾼다. 

```kotlin
data class NameComponents(val name: String, val extension: String)  //값을 저장하기 위한 데이터 클래스 선언
fun splitFilename(fullName: String): NameComponents {
    val result = fullName.split('.', limit = 2)
    return NameComponents(result[0], result[1]) // 함수에서 데이터 클래스의 인스턴스를 반환
}

val(name, ext) = splitFilename("example.kt")    // 구조 분해 선언 구문을 사용해 데이터 클래스를 푼다. 
println(name)
>>> example
prtinln(ext)
>>> kt
```

- 배열이나 컬렉션에도 componentN 함수가 있어서 개선 가능
```kotlin
data class NameComponents(val name: String, val extension: String)
fun splitFilename(fullName: String): NameComponents {
    val (name, ext) = fullName.split('.', limit = 2)
    return NameComponents(name, ext)
}
```
- 코틀린 표준 라이브러리에서는 component5까지 제공한다. 
- 표준 라이브러리의 Pair나 Triple 클래스를 사용하면 함수에서 여러 값을 더 간단한게 반환할 수 있다. 
  - 장점: 직접 클래스를 작성할 필요가 없어 코드가 단순해 진다. 
  - 단점: Pair나 Triple 안에 담겨있는 원소는 의미를 알 수 없어 가독성이 떨어진다. 

### 구조 분해 선언과 루프 
- 함수 본문 내의 선언문 뿐 아니라 변수 선언이 들어갈 수 있는 장소라면 어디든 구조 분해 선언을 사용할 수 있다. 
- 특히 맵의 원소에 대해 이터레이션 할 때 구조 분해 선언이 유용하다.

```kotlin
fun printEntries(map: Map<String, String>) {
  for ((key, value) in map) {   // 루프 변수에 구조 분해 선언을 사용한다. 
    println("$key -> $value")
  }
}

val map = mapOf("Oracle" to "Java", "Jet" to "kotlin")
printEntries(map)
>>> Oracle -> Java
>>> Jet -> kotlin
```
- 두가지 코틀린 관례 사용
  - 객체를 이터레이션하는 관례 
  - 구조 분해 선언

## 7.5 프로퍼티 접근자 로직 재활용: 위임 프로퍼티(delegated property)
- 코틀린이 제공하는 관례에 의존하는 특성 중에 독특하고 강력한 기능 
- 장점
  - 값을 뒷받침하는 필드에 단순히 저장하는 것보다 더 복잡한 방식으로 작동하는 프로퍼티를 쉽게 구현
  - 그 과정에서 접근자 로직을 매번 재구현할 필요 없음
- 디자인 패턴 중 **위임**에 기반한 특성
  - 위임: 객체가 직접 작업을 수행하지 않고 다른 도우미 객체가 그 작업을 처리하게 맡기는 디자인 패턴
  - 작업을 처리하느 도우미 객체를 위임 객체(delegate)라고 부른다.
- 여기서는 그 패턴을 프로퍼티에 적용해서 접근자 기능을 도우미 객체가 수행하게 위임
  - 도우미 객체를 직접 작성할 수도 있지만 코틀린이 제공하는 기능을 활용하는 것

### 위임 프로퍼티 소개
```kotlin
class Foo {
    var p: Type by Delegate()
}
// p 프로퍼티는 접근자 로직을 Delegate 클래스의 인스턴스에 위임한다. 
```
- 위임 프로퍼티의 일반적인 문법
- by 뒤에 있는 식을 계산해서 위임에 쓰일 객체를 얻는다. 
- 프로퍼티 위임 객체가 따라야 하는 관례를 따르는 모든 객체를 위임에 사용할 수 있다. 

```kotlin
class Foo {
  private val delegate = Delegate()   // 컴파일러가 생성한 도우미 프로퍼티
  var p: Type   // p 프로퍼티를 위해 컴파일러가 생성한 접근자는 "delegate"의 getValue와 setValue를 호출한다.
  set(value: type) = delegate.setValue(..., value)
  get() = delegate.getValue(...)
}
```
- 컴파일러는 숨겨진 도우미 프로퍼티를 만들고 그 프로퍼티를 위임 객체의 인스턴스로 초기화 한다.
- 프로퍼티 위임 관례를 따르는 Delegate 클래스는 getValue와 setValue 메서드를 제공해야 한다.
- 관례를 사용하는 다른 경우와 마찬가지로 getValue와 setValue는 멤버 메서드이거나 확장 함수일 수 있다. 

```kotlin
class Delegate {
    operator fun getValue(...) {...}
    operator fun setValue(..., value:Type) {...}
}
class Foo {
    var p: Type by Delegate() // by 키워드는 프로퍼티와 위임 객체를 연결한다. 
}
>>> val foo = Foo()
>>> val oldValue = foo.p    // 내부적으로 delegate.getValue가 호출된다.
>>> foo.p = newVale         // 내부적으로 delegate.setValue  
```
- Delegate 클래스를 단순화하면 위와 같다. 
- foo.p는 일반 프로퍼티처럼 쓸 수 있고 일반 프로퍼티 같아 보인다. 
  - 하지만 실제로 p의 게터나 세터는 Delegate 타입의 위임 프로퍼티 객체에 있는 메서드를 호출한다. 

### 위임 프로퍼티 사용: by lazy()를 사용한 프로퍼티 초기화 지연 
- 지연 초기화(lazy initialization) 객체의 일부분을 초기화하지 않고 남겨뒀다가 실제로 그 부분의 값이 필요할 경우 초기화할 때 쓰는 패턴
  - 초기화 과정에 자원을 많이 사용하거나 객체를 사용할 때마다 초기화하지 않아도 되는 프로퍼티에 대해 적용

```kotlin
class Person(val name: String) {
  private var _emails:List<Email>? = null     // 데이터를 저장하고 emails의 위임 객체 역할을 하는 _emails 프로퍼티
  val emails: List<Email>
    get() {
        if (_emails == null) {
            _emails = loadEmails(this)  // 최초 접근시 이메일 가져오기
        }
        return _emails!!    // 저장한 데이터가 있으면 반환
    }
}
>>> val p = Person("Alice")
>>> p.emails    // 최초로 emails를 읽을때 이메일을 가져온다.
Load emails for Alice
```
- 여기서는 뒷받침하는 프로퍼티(backing property)라는 기법을 사용한다.
  - _emails라는 프로퍼티는 값을 저장하고 emails는 _emails라는 프로퍼티에 대한 읽기 연산을 제공
  - 각각 널이 될 수있는 타입과 널이 될 수 없는 타입 두 개를 사용
- 단점
  - 성가시다. 지연 초기화해야 하는 프로퍼티가 많아지면??
  - 스레드 안전하지 않다. 
- 위임 프로퍼티를 사용해서 개선 가능 

```kotlin
class Person(val name: String) {
    val emails by lazy { loadEmails(this) }
}
```
- lazy 위임 객체를 반환하는 표준 라이브러리 함수 
- 위임 프로퍼티는 데이터를 저장할 때 쓰이는 뒷받침하는 프로퍼티와 값이 오직 한 번만 초기화됨을 보장하는 게터 로직을 함께 캡슐화해준다.
- lazy 함수는 코틀린 관례에 맞는 시그니처의 getValue 메서드가 들어있는 객체를 반환한다. 따라서 lazy를 by 키워드와 함게 사용해 위임 프로퍼티를 만들 수 있다.
- lazy 함수의 인자는 값을 초기화할 때 호출할 람다다.
- lazy 함수는 기본적으로 스레드 안전하다.
  - 하지만 필요에 따라 동기화에 사용할 락을 lazy 함수에 전달할 수 있고, 다중 스레드 환경에서 사용하지 않을 프로퍼티를 위해 lazy 함수가 동기화를 하지 못하게 막을 수도 있다. 

### 위임 프로퍼티 구현
- 어떤 객체의 프로퍼티가 바뀔 때마다 리스너에게 변경 통지를 보내는 기능
  - 예를 들어 어떤 객체를 UI에 표시하는 경우 객체가 바뀌면 자동으로 UI도 바뀌어야 한다.
  - 자바에서는 PropertyChangeSupport와 PropertyChangeEvent 클래스를 사용해 이런 통지를 처리하는 경우가 있다.

- PropertyChangeSupport는 리스너의 목록을 관리하고 PropertyChangeEvent는 이벤트가 들어오면 목록의 모든 리스너에게 이벤트를 통지한다.
  - 자바 빈 클래스의 필드에 PropertyChangeSupport 인스턴스를 저장하고 프로퍼티 변경 시 그 인스턴스에게 처리를 위임하는 방식으로 이런 통지 기능을 주로 구현 

- 필드를 모든 클래스에 추가하고 싶지 않으므로 PropertyChangeSupport 인스턴스를 changeSupport라는 필드에 저장하고 프로퍼티 변경 리스너를 추적해주는 작은 도우미 클래스를 만들자

```kotlin
// PropertyChangeSupport를 사용하기 위한 도우미 클래스 
open class PropertyChangeAware {
  protected val changeSupport = PropertyChangeSupport(this)
  fun addPropertyChangeListener(listener: PropertyChangeListener) {
      changeSupport.addPropertyChangeListener(listener)
  }
  
  fun removePropertyChangeListener(listner: PropertyChagListener) {
    removeSupport.addPropertyChangeListener(listener)
  }
}
```

```kotlin
class Person(val name: String, age: Int, salary: Int) : PropertyChageAware() {
  var age: Int = age
    set(newValue) {
      val oldValue = field    // 뒷받침하는 필드에 접근할 때 field 식별자를 사용한다.
      field = newValue
      changeSupport.firePropertyChange("age", oldValue, newValue)   // 프로퍼티 변경을 리스너에게 통지
    }
  var salary: Int = salary
    set(newValue) {
      val oldValue = field
      field = newValue
      changeSupport.firePropertyChange("salary", oldValue, newValue)
    }
}
>>> val p = Person("Dmitry", 34, 200)
>>> p.addProperthChangeListener(
  PropertyChangeListener { event ->
    println("Property ${event.propertyName} changed" + 
    "from ${event.oldValue} to ${event.newValue}")
      
  }
)
>>> p.age = 35
Property age change frtm 34 to 35
```

- 위 코드는 field 키워드를 사용해 age와 salary 프로퍼티를 뒷받침하는 필드에 접근하는 방법을 보여준다.
- setter 코드를 보면 중복이 많이 보인다. 프로퍼티의 값을 저장하고 필요에 따라 통지를 보내주는 클래스를 추출해보자 

```kotlin
class ObservableProperty(
  val propName: String, var propValue: Int,
  val changeSupport: PropertyChangeSupport
) {
  fun getValue(): Int = propValue
  fun setValue(newValue: Int) {
    val oldValue = propValue
    propValue = newValue
    changeSupport.firePropertyChange(propName, oldValue, newValue)
  }
}

class Person(val name: String, age: Int, salary: Int) : PropertyChageAware() {
  val _age = ObservableProperty("age", age, changeSupport)
  var age: Int
    get() = _age.getValue()
    set(value) {
      _age.setValue(value)
    }
  val _salary = ObservableProperty("salary", salary, changeSupport)
  var salary: Int
    get() = _salary.getValue()
    set(value) {
      _salary.setValue(value)
    }
}
```
- 프로퍼티 값을 저장하고 그 값이 바뀌면 자동으로 변경 통지를 전달해주는 클래스를 만들었고, 로직 중복이 상당 부분 제거됐다.
- 하지만 아직도 각각 프로퍼티마다 ObservableProperty를 만들고 게터와 세터에서 작업을 위임하는 준비 코드가 상당 부분 필요하다.
- 코틀린의 위임 프로퍼티 기능을 활용하면 이런 준비 코드를 없앨 수 있다. 

















