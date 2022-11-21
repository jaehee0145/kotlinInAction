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

## 7.2 비교 연산자 오버로딩
## 7.3 컬렉션과 범위에 대해 쓸 수 있는 관례
## 7.4 구조 분해 선언과 component 함수
## 7.5 프로퍼티 접근자 로직 재활용: 위임 프로퍼티 