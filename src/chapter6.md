# 6장 코틀린 타입 시스템
- 널이 될 수 있는 타입과 널을 처리하는 구문의 문법
- 코틀린 원시 타입 소개와 자바 타입과 코틀린 원시 타입의 관계
- 코틀린 컬렉션 소개와 자바 컬렉션과 코틀린 컬렉션의 관계

## 6.1 널 가능성
- 널 가능성(Nullability)은 NPE를 피할 수 있게 돕기 위한 코틀린 타입 시스템의 특성
- 코틀린을 비롯한 최신 언어 경향: 컴파일 시점에서 NPE 감지하도록 

### 널이 될 수 있는 타입
- 코틀린과 자바의 가장 중요한 차이점: 코틀린 타입 시스템이 널이 될 수 있는 타입을 명시적으로 지원한다는 점

```java
/* Java */
int strLen(String s) {
    return s.length();
}
// 인자로 null을 넘기면 NPE 발생
```

- 코틀린에서는 "이 함수가 null을 인자로 받을 수 있는가?"
```kotlin
fun strLen(s: String) = s.length
// 인자로 null이나 null이 될 수 있는 값이 오면 컴파일 에러 

fun strLen(s: String?) = ...
// null 허용하려면 타입 이름 뒤에 물음표를 붙이기

fun strLen(s: String?) = s.length()
// null이 될 수 있는 변수가 있다면 컴파일 에러로 연산 제한
```   


- null이 될 수 있는 타입의 값으로 할 수 있는 것???
```kotlin
fun strLensafe(s: String?): Int = 
    if (s != null) s.length else 0  // null 검사를 추가해야 컴파일된다.
```
- 널 가능성을 다루기 위해 사용할 수 있는 도구가 if 뿐이라면 불필요한 코드가 많아졌을 것
  - 해당 값을 다룰 수 있는 다양한 도구 제공

### 타입의 의미
- 자바의 타입 시스템은 널을 제대로 다루지 못한다!
  - 변수에 선언된 타입이 있지만 널 여부를 추가로 검사하기 전에는 그 변수에 대해 어떤 연산을 수행할 수 있을지 알 수 없다.
  - ex. String : null이 될 수 있지만 String이 실행할 수 있는 연산과 완전히 다름
- 코틀린에서 종합적인 해법을 제공

### 안전한 호출 연산자: ?.
- 코틀린에서 제공하는 가장 유용한 도구 중 하나 
- null 검사와 메서드 호출을 한번의 연산으로 수행

```kotlin
// java style
if (s != null) {
    s.toUpperCase()
} else {
    null
}

// 동일한 로직의 코드 
s?.toUpperCase()

// 안전한 호출 연산자를 연쇄 사용
fun Person.countryName(): String {
    val country = this.company?.address?.country    
    return if (country != null) country else "UNKNOWN"
}
```

### 엘비스 연산자: ?:
- 엘비스 elvis 연산자 : null 대신 사용할 디폴트 값을 지정할 때 사용 
- 이항 연산자로 좌항이 널이 아니면 좌항 값을, 좌항이 널이면 우항 값을 결과로 

```kotlin
fun foo(s: String?) {
    val t: String = s ?: "" // s가 null 이면 결과는 ""
}
```

- 객체가 널인 경우 널을 반환하는 안전한 호출 연산자(?.)와 함께 사용하는 패턴 
```kotlin
// 앞에서 나온 예제
fun strLensafe(s: String?): Int = 
    if (s != null) s.length else 0  

// 패턴을 사용해서 개선
fun strLenSafe(s: String?): Int = s?.length ?: 0
```

- 코틀린에서는 return이나 throw 등의 연산도 식이라서 엘비스 연산자 우항에 넣을 수 있음
```kotlin
class Address (val streetAddress: String, val zipCode: Int, val city: String, val country: String)
class Company (val name: String, val address: Address?)
class Person (val name: String, val company: Cpmpany?)
fun printShippingLabel (person: Person) {
  val address = person.company?.address ?: throw new IllegalArgumentException ("No address")
  with(address) {
    println(streetAddress)
    println("$zipCode $city, $country")
  }
}
```

### 안전한 캐스트: as?
- 어떤 값을 지정한 타입으로 캐스트 하는데 변환할 수 없으면 null 반환
- 캐스트를 수행한 뒤에 엘비스 연산자를 사용하는 것이 일반적인 패턴

```kotlin
class Person (val firstName: String, val lastName: String) {
    override fun equals(o: Any?): Boolean {
        val otherPerson = o as? Person ?: return false // 타입이 서로 일치하지 않으면 false
        return otherPerson.firstName == firstName && otherPerson.lastName == lastName
    }
}
```

### 널 아님 단언: !!
- 어떤 값이든 널이 될 수 없는 타입으로 바꿀 수 있다. 
- 널에 대해 !!을 적용하면 NPE 발생

```kotlin
fun ignoreNulls(s: String?) { // null이 가능한 타입인데
  val sNotNull: String = s!!    // null이 아니라고 단언 
  println(sNotNull.length)      // s가 null이면 s!!에서 에러 발생
}

person.company!!.address!!.country  // 안티 패턴; 어떤 값이 널인지 확인 어려움
```

### let 함수
- 널이 될 수 있는 값을 널이 아닌 값만 인자로 받는 함수에 넘기는 경우에 사용

```kotlin
fun sendEmailTo(email: String) {
    println("sending email to $email")
}

>>> var email: String? = "test@email.com"
>>> email?.let { sendEmailTo(it) }
sending email to test@email.com

>>> email = null
>>> email?.let { sendEmailTo(it) }
```

- 긴 식이 있고 그 결과가 널이 아닐 때 수행해야 하는 로직이 있을때 유용
```kotlin
val person: Person? = getTheBestPersonIntheWorld()
if (person != null) sendEmailTo(person.email)

// 개선
getTheBestPersonIntheWorld()?.let { sendEmailTo(it.email) }
```

### 나중에 초기화할 프로퍼티 lateinit
- 코틀린에서 일반적으로 생성자에서 모든 프로퍼티를 초기화해야 한다. 
  - 널이 될 수 없는 타입의 프로퍼티를 초기화 할 수 없으면 널이 될 수 있는 타입으로 사용해야 함
- lateinit 변경자를 붙이면 프로퍼티를 나중에 초기화할 수 있다.

```kotlin
class MyService {
  fun performAction(): String = "foo"
}

class MyTest {
  private lateinit var myService: MyService   // 초기화하지 않고 널이 될수 없는 프로퍼티 선언

  @Before fun setUp() {
    myService = MyService()
  }
  
  @Test fun testAction() {
      Assert.assertEquals("foo", myService.performAction())
  }
}
```

### 널이 될 수 있는 타입 확장
- 널이 될 수 있는 타입에 대한 확장 함수를 정의하면 null 값을 다루는 강력한 도구로 활용할 수 있다.
- 어떤 메서드를 호출하기 전에 수신 객체 역할을 하는 변수가 널이 될 수 없다고 보장하는 대신, 직접 변수에 대해 메서드를 호출해도 확장 함수인 메서드가 알아서 널을 처리 
  - 이런 처리는 확장 함수에서만 가능
  - 일반 멤버 호출은 객체 인스턴스를 통해 디스패치(dispatch) 되므로 그 인스턴스가 널인지 여부를 검사하지 않는다.
  > 객체 지향 언어에서 객체의 동적 타입에 따라 적절한 메서드를 호출해주는 방식을 동적 디스패치라 부른다. 반대로 컴파일러가 컴파일 시점에 어떤 메서드가 호출될지 결정해서 코드를 생성하는 방식을 직접 디스패치라고 한다.

### 타입 파라미터의 널 가능성
- 코틀린에서 함수나 클래스의 모든 타입 파라미터는 기본적으로 널이 될 수 있다. 

```kotlin
fun <T> printHashCode(t: T) {
    println(t?.hashCode())  // t가 null이 될 수 있으므로 안전한 호출    
}
>>> printHashCode(null) // T의 타입은 Any?로 추론된다. 
```

- 타입 파라미터가 널이 아님을 확실히 하려면 널이 될 수 없는 타입 상한(upper bound)을 지정해야 한다.

```kotlin
fun <T: Any> printHashCode(t: T) {  // t는 null이 될 수 없는 타입
    println(t?.hashCode())      
}
>>> printHashCode(null) // 컴파일 에러 
// Error: Type parameter bound for T is not satisfied
```

### 널 가능성과 자바
- 자바와 코틀린은 상호운영성이 높다. 
- 자바와 코틀린을 조합해서 사용할 때 안전하게 null을 사용하는 방법
  - 자바 애노테이션 
    - @Nullable + Type = Type? 
    - @NotNull + Type = Type
  - 코틀린은 여러 널 가능성 애노테이션을 알아본다. 
    - JSR-305 표준 (javax.annotation package)
    - Android (android.support.annotation package)
    - JetBrains (org.jetbrains.annotations)
  - null 가능성 애노테이션이 소스코드에 없는 경우 자바 타입은 코틀린의 플랫폼 타입이 된다. 

**플랫폼 타입**
- 플랫폼 타입은 코틀린이 널 관련 정보를 알 수 없는 타입을 말한다.
- 그 타입을 널이 될 수 있는 타입으로 처리해도 되고, 널이 될 수 없는 타입으로 처리해도 된다.
- 컴파일러는 모든 연산을 허용한다.
- 널이 될 수 없는 타입 && 널 안전성을 검사하는 연산 : 경고
  - 플랫폼 타입 && 널 안정성 검사 : 경고 없음 

```java
public class Person {
    private final String name;
    public Person(String name) {
        this.name = name;
    }
    public String getName() {   // 코드상으로는 null을 리턴할 가능성이 있음 
        return name;
    }
}
```
```kotlin
fun yellAt(person: Person) {
    println(person.name.toUpperCase() + "!!!")
}
>>> yellAt(Person(null))
// IllegalArgumentException : toUpperCase() 수신 객체로 널을 받을 수 없다.

// 개선
fun yellAtSafe(person: Person) {
    println((person.name ?: "Anyone").toUpperCase() + "!!!")
}
>>> yellAt(Person(null))
// ANYONE!!!
```

> 코틀린이 왜 플랫폼 타입을 도입했는가?  
> 모든 자바 타입을 널이 될 수 있는 타입으로 다루면 결코 널이 될 수 없는 값에 대해서도 불필요한 널 검사가 들어간다.
> 특히 제네릭을 다룰 때 비용이 커진다. 
> 프로그래머에게 타입을 제대로 처리할 책임을 부여^^   
> - ArrayList<String> 을 코틀린에서 ArrayList<String?>? 으로 처리하면 배열의 원소에 접근할 때마다 널 검사 or 안전한 캐스트   

- 코틀린에서 플랫폼 타입을 선언할 수는 없다. 
  - 자바에서 가져온 타입만 플랫폼 타입이 된다. 

**상속**
- 코틀린에서 자바 메서드를 오버라이드할 때 그 메서드의 파라미터와 반환 타입을 결정해야 한다.

```java
interface StringProcessor {
    void process(String value);
}
```
```kotlin
class StringPrinter : StringProcessor {
  override fun process(value: String) {
    println(value)
  }
}
class NullableStringPrinter : StringProcessor {
  override fun process(value: String?) {
    if (value != null) println(value)
  }
}
```


## 6.2 코틀린의 원시 타입 
- 코틀린은 원시 타입과 래퍼 타입을 구분하지 않는다. 

### 원시 타입: Int, Boolean 등
- 자바 
  - 원시 타입(primitive type)의 변수에는 값이 직접 들어간다.
    - 원시 타입의 값을 효율적으로 저장하고 전달할 수 있다. 
    - 메서드를 호출하거나 컬렉션에 원시 타입 값을 담을 수는 없다. 
    - 참조 타입이 필요한 경우 래퍼 타입을 사용한다.
  - 참조 타입(reference type)의 변수에는 메모리상 객체의 위치가 들어간다.
- 코틀린
  - 원시 타입과 래퍼 타입을 구분하지 않는다. 
  - 숫자 타입 등 원시 타입의 값에 대해 메서드르 호출할 수 있다. 
  - 실행 시점에 숫자 타입은 가능한 효율적인 방식으로 표현된다. 
    - 대부분의 경우(변수, 프로퍼티, 파라미터, 반환 타입 등) 코틀린의 Int 타입은 자바 int 타입으로 컴파일 된다.
    - 제네릭 클래스를 사용하는 경우는 제외 
  - 자바 원시 타입에 해당하는 타입
    - 정수 타입 Byte, Short, Int, Long
    - 부동소수점 수 타입 Float, Double
    - 문자 타입 Char
    - 불리언 타입 Boolean
  - 자바 원시 타입을 코틀린에서 사용할 때에는 널이 될 수 없는 타입으로 취급한다.

### 널이 될 수 있는 원시 타입: Int?. Boolean?


### 숫자 변환
### Any, Any?: 최상위 타입
### Unit 타입: 코틀린의 void
### Nothing 타입: 이 함수는 결코 정상적으로 끝나지 않는다











