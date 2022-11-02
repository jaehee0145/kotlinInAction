# 5장 람다로 프로그래밍
- 람다 식과 멤버 참조
- 함수형 스타일로 컬렉션 다루기
- 시퀀스: 지연 컬렉션 연산
- 자바 함수형 인터페이스를 코틀린데서 사용
- 수신 객체 지정 람다 사용

- 람다 식 또는 람다는 기본적으로 다른 함수에 넘길 수 있는 작음 코드 조각을 뜻한다. 람다를 사용하면 쉽게 공통 코드 구조를 라이브러리 함수로 뽑아낼 수 있다.

## 5.1 람다 식과 멤버 참조

### 람다 소개: 코드 블록을 함수 인자로 넘기기
- 일련의 동작을 변수에 저장하거나 다른 함수에 넘겨야 하는 경우
    - 예전 자바에서는 무명 내부 클래스를 이용 - 번거로움
    - 함수형 프로그래밍에서는 함수를 값처럼 다른 함수에 전달

```java
/* Java - 무명 내부 클래스로 리스너 구현*/
button.setOnClickListener(new OnClickListener() {
    @Override
    public vlid onClick(View view) {
        /* 클릭시 수행할 동작 */
    }
});

/* 람다로 리스너 구현하기 */
button.setOnClickListener { /* 클릭시 수행할 동작 */ }
```

### 람다와 컬렉션
- 컬렉션을 다룰 때 수행하는 대부분의 작업은 몇 가지 일반적인 패턴에 속한다.
- 람다가 없으면 이러한 패턴을 라이브러리로 제공하기 힘들다.
- 이러한 이유로 자바8 이전에는 필요한 컬렉션 기능을 직접 작성해야 했다.

```kotlin
// 컬렉션을 직접 검색하기 
fun findTheOldest(people: List<Person>) {
    var maxAge = 0;
    var theOldest: Person? = null
    for (person in people) {
        if (person.age > maxAge) {
            maxAge = person.age
            theOldest = person
        }
    }
    println(theOldest)
}

// 람다를 사용해 컬렉션 검색하기
>>> val people = listOf(Person("Alice", 29), Person("Tom", 31))
>>> println(people.maxBy { it.age })

// 멤버 참조를 사용해 컬렉션 검색하기
people.maxBy(Person::age)
```
- 모든 컬렉션에 대해 maxBy함수를 호출할 수 있다.

### 람다 식의 문법
- 람다는 값처럼 전달할 수 있는 동작의 모음
- 람다를 다로 선언해서 변수에 저장할 수도 있지만 람다를 정의하면서 함수에 인자로 넘기는 경우가 대부분

```kotlin
{ x: Int, y: Int -> x + y }
```
- 코틀린 람다 식은 항상 중괄호
- 화살표가 파라미터와 본문을 구분

```kotlin
// 정식으로 작성한 람다
people.maxBy({ p: Person -> p.age })

// 함수 호출 시 맨 뒤에 있는 인자가 람다 식이라면 그 람다를 괄호 밖으로 빼낼 수 있다. 
people.maxBy() { p: Person -> p.age }

// 람다가 어떤 함수의 유일한 인자이고 괄호 뒤에 람다를 썼다면 빈 괄호를 없앨 수 있다. 
people.maxBy { p: Person -> p.age }

// 컴파일러가 타입 추론이 가능하므로 생략
people.maxBy { p -> p.age }

// 람다의 파라미터 디폴트 이름 it으로 변경하고 바로 사용 가능
people.maxBy { it.age }
```

### 현재 영역에 있는 변수에 접근
- 자바 메서드 안에서 무명 내부 클래스를 정의할 때 메서드의 로컬 변수를 무명 내부 클래스에서 사용할 수 있다.
  람다를 함수 안에서 정의하면 함수의 파라미터 뿐 아니라 람다 정의 앞에 선언된 로컬 변수까지 람다에서 모두 사용할 수 있다.

```kotlin
fun printMessagesWithPrefix(messages: Collection<String>, prefix: String) {
    messages.forEach {
        println("$prefix : $it")
    }
}
```

- 자바와 다른 점: 코틀린 람다 안에서는 파이널 변수가 아닌 변수에 접근할 수 있고, **변수를 변경할 수 있다.**
```kotlin
fun printProblemCounts(responses: Collection<String>) {
    var clientErrors = 0
    var serverErrors = 0
    responses.forEach {
        if (it.startsWith("4")) {
            clientErrors++
        } else {
            serverErrors++
        }
    }
    
}
```
- prefix, clientErrors, serverErrors와 같이 람다 안에서 사용하는 외부 변수를 '람다가 포획한 변수(capture)'

5. 멤버 참조
- 코틀린에서는 자바8과 마찬가지로 함수를 값으로 바꿀 수 있다. 이때 이중 콜론을 사용한다.
  ::을 사용하는 식을 멤버 참조라고 부른다. 멤버 참조는 프로퍼티나 메서드를 단 하나만 호출하는 함수 값을 만들어준다.
```kotlin
val getAge = Person::age
```

- 최상위에 선언된 함수나 프로퍼티를 참조할 수도 있다.
```kotlin
fun salute() = println("salute")
>>> run(::salute)
// run은 인자로 받은 람다를 호출 
```

### 5.2 컬렉션 함수형 API
1. 필수적인 함수 : filter와 map

```kotlin
data class Person(val name: String, val age: Int) 

val people = listOf(Person("Alice", 89), Person("Tom", 112))
println(people.filter { it.age > 90 })
```
- filter: 컬렉션에서 원치 않는 원소를 제거한다. 원소를 변환할 수는 없다.

```kotlin
val people = listOf(Person("Alice", 89), Person("Tom", 112))
println(people.map { it.name })
>>> [Alice, Tom]

// 멤버 참조 사용 
println(people.map(Person::name))

// 연쇄
people.filter { it.age > 90 }.map(Person::name)

// 나이 최댓값과 같은 모든 사람 반환 
people.filter { it.age == people.maxBy(Person::age)!!.age }

// 반복 연산 제거
val maxAge = people.maxBy(Person::age)!!.age
people.filter { it.age == maxAge }
```

2. all, any, count, find: 컬렉션에 술어 적용
- 컬렉션에 대해 자주 수행하는 연산
- all, any: 모든 원소 또는 특정 원소가 어떤 조건을 만족하는지 판단하는 연산

```kotlin
val canBeInClub27 = { p: Person -> p.age <= 27 }
>>> val people = listOf(Person("Alice", 24), Person("Tom", 112))
>>> println(people.all(canBeInClub27))
false

>>> println(people.any(canBeInClub27))
false

// count 사용
>>> println(people.count(canBeInClub27))
1

// 단순히 count만 필요한 경우에는 조건에 해당하는 컬렉션을 생성해서 size를 구하는 것보다 효율적
>>> println(people.filter(canBeInClub27).size)
1

// find 는 firstOrNull과 같다. 
>>> println(people.find(canBeInClub27))
Person(name=Alice, age=24)
```

3. groupBy: 리스트를 여러 그룹으로 이뤄진 맵으로 변경
```kotlin
>>> val people = listOf(Person("Alice", 24), Person("Tom", 112), Person("A", 112))
>>>println(people.groupBy { it.age })
// Map<Int, List<Person>>
{24 = [Person("Alice", 24)], 112 = [Person("Tom", 112), Person("A", 112)]}


>>> val list = listOf("ab", "aa", "bb")
>>> println(list.groupBy(String::first))
{a = [ab, aa], b = [bb]}
```

4. flatMap과 flatten: 중첩된 컬렉션 안의 원소 처리
```kotlin
>>> val strings = listOf("abc", "def")
>>> println(strings.flatMap { it.toList() })
[a, b, c, d, e, f]

// 중첩된 리스트 
>>> val strings = listOf(listOf("abc", "def"), listOf("ggg", "hhh", "iii"))
>>> println(strings.flatten())
[abc, def, ggg, hhh, iii]
```

### 5.3 지연 계산 lazy 컬렉션 연산
- map이나 filter 같은 함수는 결과 컬렉션을 즉시 생성한다.
- sequence를 사용하면 중간 임시 컬렉션을 사용하지 않고 컬렉션 연산을 연쇄할 수 있다.

```kotlin
people.asSequence() // 원본 컬렉션을 시퀀스로 변환
    .map(Person::name) // 시퀀스도 컬렉션과 동일한 API를 제공
    .filter { it.startsWith("A") }
    .toList() // 결과 시퀀스를 다시 리스트로 변환
```

- Sequence 인터페이스 안에는 iterator 메서드만 있음
- 시퀀스의 원소는 필요할 때 계산되기 때문에 중간 처리 결과를 저장하지 않고도 계산을 수행할 수 있다.

1. 시퀀스 연산 실행: 중간 연산과 최종 연산
```kotlin
// 최종 연산이 없어서 아무것도 출력되지 않는다. 
listOf(1, 2, 3, 4).asSequence()
    .map { println("map($it"); it * it }
    .filter { println("filter($it"); it % 2 == 0}
```
- 지연 계산은 원소를 하나씩 처리한다.
  <img width="700" alt="스크린샷 2022-10-24 오후 11 48 05" src="https://user-images.githubusercontent.com/45681372/197555542-35999312-e1e8-4d8e-9ce7-79e772df63fd.png">


### 5.4 자바 함수형 인터페이스 활용
- 어떻게 코틀린 람다를 자바 API에서 활용할 수 있는지 살펴보자

1. 자바 메서드에 람다를 인자로 전달
- 함수형 인터페이스를 원하는 자바 메서드에 코틀린 람다를 전달할 수 있다.

```kotlin
// 자바 - 함수형 인터페이스 Runnable을 인자로 받는 메서드 
void postponeComputation(int delay, Runnable computation)

// 코틀린 
postponeComputation(1000) { println(42) }
```
- 컴파일러가 람다를 Runnable 인스턴스로 변환해준다.
    - Runnable 인스턴스 : Runnable을 구현한 무명 클래스의 인스턴스
    - 컴파일러가 자동으로 무명 클래스와 인스턴스를 만들어준다.
    - 이때 그 무명 클래스에 있는 유일한 추상 메서드를 구현할 때 람다 본문을 메서드 본문으로 사용한다.

```kotlin
postponeComputation(1000, object: Runnable { // 객체 식을 함수형 인터페이스 구현으로 넘긴다.
    override fun run() {
        println(42)
    }
})
```
- 람다와 무명 객체는 차이
    - 무명 객체: 객체를 명시적으로 선언하는 경우 메서드를 호출할 때마다 새로운 객체가 생성
    - 람다: 정의가 들어있는 함수의 변수에 접근하지 않는 람다에 대응하는 무명 객체를 메서드를 호출할 때마다 반복 사용한다.??
        - 그러나 람다가 주변 영역의 변수를 포획한다면 새로운 인스턴스를 생성한다.
```kotlin
fun handleComputation(id: String) { // 람다 안에서 id 변수를 포획 
    postponeComputation(1000) { println(id) }   // 호출마다 새로운 Runnable 인스턴스를 생성 
}
```

2. SAM 생성자: 람다를 함수형 인터페이스로 명시적으로 변경

### 5.5 수신 객체 지정 람다: with 와 apply
- 수신 객체 지정 람다 : 수신 객체를 명시하지 않고 람다의 본문 안에서 다른 객체의 메서드를 호출할 수 있게 하는 것

1. with 함수
```kotlin
fun alphabet(): String {
    val result = StringBuilder() 
    for (letter in 'A'..'Z') {
        result.append(letter)
    }
    result.append("\nNow I know the alphabet")
    return result.toString()
}

// with 활용
fun alphabet(): String {
    val stringBuilder = StringBuilder
    return with(stringBuilder) {    //메서드를 호출하려는 수신 객체 지정
        for (letter in 'A'..'Z') {
            this.append(letter) //this를 명시해 수신 객체의 메서드를 호출
        }
        append("\nNow I know the alphabet") //this 생략하고 메서드 호출 
        this.toString()
    }
}
```
- with는 파라미터가 2개 있는 함수
- 인자1로 받은 객체를 인자2로 받은 람다의 수신 객체로 만든다.
- this를 사용해 수신 객체에 접근할 수 있다.
- 일반적인 this와 마찬가지로 this와 점.을 사용하지 않고 프로퍼티나 메서드 이름만 사용해도 수신 객체의 멤버에 접근 가능

```kotlin
// 리팩토링
fun alphabet() = with(StringBuilder()) {
    for (letter in 'A'..'Z') {
        append(letter)
    }
    append("\nNow I know the alphabet")
    toString()
}
```

2. apply 함수
- with와 동일한데 자신에게 전달된 객체(수신 객체)를 반환한다는 것이 차이점

```kotlin
fun alphabet() = StringBuilder().apply {
    for (letter in 'A'..'Z') {
        append(letter)
    }
    append("\nNow I know the alphabet")
}.toString()
```

- 객체의 인스턴스를 만들면서 즉시 프로퍼티 중 일부를 초기화해야 하는 경우 유용
    - 자바에서 보통 Builder 객체가 하던 역할

### 5.6 요약
- 람다를 사용하면 코드 조각을 다른 함수에게 인자로 넘길 수 있다.
- 코틀린에서는 람다가 함수 인자인 경우 괄호 밖으로 람다를 빼낼 수 있고, 람다의 인자가 단 하나뿐인 경우 인자 이름을 지정하지 않고 it이라는 디폴트 이름으로 부를 수 있다.
- 람다 안에 있는 코드는 그 람다가 들어있는 바깥 함수의 변수를 읽거나 쓸 수 있다.
- 메서드, 생성자, 프로퍼티의 이름 앞에 ::을 붙이면 각각에 대한 참조를 만들 수 있다. 그런 참조를 람다 대신 다른 함수에게 넘길 수 있다.
- filter, map, all, any 등의 함수를 활용하면 컬렉션에 대한 대부분의 연산을 직접 원소를 이터레이션하지 않고 수행할 수 있다.
- 시퀀스를 사용하면 중간 결과를 담는 컬렉션을 생성하지 않고도 컬렉션에 대한 여러 연산을 조합할 수 있다.
- 함수형 인터페이스(추상 메서드가 단 하나뿐인 SAM인터페이스)를 인자로 받는 자바 함수를 호출할 경우 람다를 함수형 인터페이스 인자 대신 넘길 수 있다.
- 수신 객체 지정 람다를 사용하면 람다 안에서 미리 정해둔 수신 객체의 메서드를 직접 호출할 수 있다.
- 표준 라이브러리의 with 함수를 사용하면 어떤 객체에 대한 참조를 반복해서 언급하지 않으면서 그 객체의 메서드를 호출할 수 있다. apply를 사용하면 어떤 객체라도 빌더 스타일의 API를 사용해 생성하고 초기화할 수 있다. 