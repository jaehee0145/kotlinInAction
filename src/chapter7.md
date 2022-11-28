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









## 7.2 비교 연산자 오버로딩
### 동등성 연산자: equals
### 순서 연산자: compareTo

## 7.3 컬렉션과 범위에 대해 쓸 수 있는 관례
### 인덱스로 원소에 접근: get과 set
### in 관례
### rangeTo 관례
### for 루프를 위한 iterator 관례 

## 7.4 구조 분해 선언과 component 함수
## 7.5 프로퍼티 접근자 로직 재활용: 위임 프로퍼티 