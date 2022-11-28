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

> 연산자 함수와 자바
> 










## 7.2 비교 연산자 오버로딩
## 7.3 컬렉션과 범위에 대해 쓸 수 있는 관례
## 7.4 구조 분해 선언과 component 함수
## 7.5 프로퍼티 접근자 로직 재활용: 위임 프로퍼티 