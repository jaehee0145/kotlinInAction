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



---
### 엘비스 연산자: ?:
### 안전한 캐스트: as?
### 널 아님 단언: !!
### let 함수
### 나중에 초기화할 프로퍼티
### 널이 될 수 있는 타입 확장
### 타입 파라미터의 널 가능성
### 널 가능성과 자바
