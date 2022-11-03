## 4장 클래스, 객체, 인터페이스
- 클래스와 인터페이스
- 뻔하지 않은 생성자와 프로퍼티
- 데이터 클래스
- 클래스 위임
- object 키워드 사용

### 4.1 클래스 계층 정의
#### 1. 코틀린 인터페이스
- 코틀린 인터페이스는 자바 8 인터페이스와 비슷하다.
  코틀린 인터페이스 안에는 추상 메서드뿐 아니라 구현이 있는 메서드도 정의할 수 있다. (자바8 디폴트 메서드와 비슷)

```kotlin
interface Clickable {
    fun click()
}

class Button : Clickable {
    override fun click() {
        TODO("Not yet implemented")
    }
}
```
- 코틀린에서는 클래스 이름 뒤에 콜론을 붙이는 것으로 클래스 확장과 인터페이스 구현을 모두 처리한다.
  자바와 마찬가지로 인터페이스는 제한 없이 구현, 클래스는 하나만 확장할 수 있다.
- 자바와 달리 override 변경자 필수
- 인터페이스 메서드도 디폴트 구현을 제공할 수 있다. 자바8과 다르게 default 키워드가 필요 없다.

```kotlin
interface Clickable {
    fun click() // 일반 메서드 선언
    fun showOff() = println("디폴트 구현") // 디폴트 구현 메서드
}
```
- 여러 개의 인터페이스를 구현할때 메서드 시그니처가 동일한 디폴트 메서드가 중복으로 있으면 하위 클래스에 override 하게 컴파일러 오류가 발생한다.

```kotlin
class Button : Clickable, Focusable {
    override fun click() = println("I was clicked")
    override fun showOff() {
        super<Clickable>.showOff() // super 키워드 사용 (자바: Clickable.super.showOff()
        super<Focusable>.showOff()
    }
}
```

#### 2. open, final, abstract 변경자: 기본적으로 final
- 자바는 final로 상속을 금지하지 않으면 기본적으로 상속이 가능
    - 취약한 기반 클래스 (fragile base class) 문제 : 기반 클래스의 의도와 다른 방식으로 메서드가 오버라이드 될 위험
- 코틀린에서는 클래스와 메서드가 기본적으로 final
    - 클래스, 메서드, 프로퍼티에 상속을 허용하려면 open 변경자를 붙여야 한다.
```kotlin
open class RichButton : Clickable { // open : 다른 클래스가 이 클래스를 상속할 수 있다. 
    fun disable() {}  // 이 함수는 final. 하위 클래스가 이 메서드를 오버라이드 할 수 없다. 
    open fun animate() {}  // open : 하위 클래스에서 이 메서드를 오버라이드할 수 있다. 
    override fun click() {}  // 상위 클레스에 선언된 열려있는 메서드를 오버라이드한다.
    // final override fun click() {}  오버라이드 메서드는 기본적으로 열려있어서 final을 붙여야 오버라이드 할 수 없게 된다.
}
```

- 자바처럼 코틀린에서도 abstract 클래스를 선언할 수 있다. abstract로 선언한 추상 클래스는 인스턴스화할 수 없다.
  추상 클래스에는 구현이 없는 추상 멤버가 있기 때문에 하위 클래스에서 그 추상 멤버를 오버라이드 해야만 하는 게 보통이다. 따라서 추상 멤버는 항상 열려 있고
  추상 멤버 앞에 open 변경자를 명시할 필요가 없다.
```kotlin
abstract class Animated {  // 이 클래스는 추상 클래스. 이 클래스의 인스턴스를 만들 수 없다. 
    abstract fun animate()  // 구현이 없는 추상 함수. 하위 클래스에서 이 함수를 반드시 오버라이드 해야 한다. 
    fun stopAnimating() {} // 추상 클래스에 속했더라도 비추상 함수는 기본적으로 final이지만 
    open fun animateTwice() {} // open 으로 오버라이드 허용 가능
}
```

- 인터페이스 멤버
    - 인터페이스 멤버의 경우 final, open, abstract를 사용하지 않는다.
      인터페이스 멤버는 항상 열려 있으며 final로 변경할 수 없다. 인터페이스 멤버에게 본문이 없으면 자동으로 추상 멤버가 되지만,
      그렇더라도 멤버 선언 앞에 abstract 키워드를 덧붙일 필요가 없다.

- 클래스 멤버에 대한 상속 제어 변경자
    - final : 오버라이드 할 수 없음 - 클래스 멤버의 기본 변경자
    - open : 오버라이드 할 수 있음 - 반드시 open을 명시해야 오버라이드 가능
    - abstract : 오버라이드 필수 - 추상 클래스의 멤버에만 이 변경자를 붙일 수 있다. 추상 멤버에는 구현이 잇으면 안 된다.
    - override : 상위 클래스나 상위 인스턴스의 멤버를 오버라이드 하는 중 - 오버라이드하는 멤버는 기본적으로 열려있다. 하위 클래스의 오버라이드를 금지하려면 final 추가


#### 3. 가시성 변경자(visibility modifier): 기본적으로 공개
- 코틀린 가시성 변경자 : public(기본), internal, protected, private
    - 자바 default: package private
- 코틀린은 패키지를 네임스페이스를 관리하기 위한 용도로만 사용한다. 그래서 패키지를 가시성 제어에 사용하지 않는다.
- 패키지 전용 가시성에 대한 대안은 internal. 모듈 내부로 제한. 모듈은 한번에 컴파일 되는 코틀린 파일들 ?

```kotlin
internal open class TalkativeButton : Focusable {
		private fun yell() = println("Hey!")
		protected fun whisper() = println("Let's talk!") // protected는 하위에서만 접근 가능?
}

fun TalkativeButton.giveSpeech() {  // 오류: "public" 멤버가 자신의 "internal" 수신 타입인 "TalkativeButton"을 노출
		yell()  // 오류: "yell"은 private 이라 접근 불가
		whisper() // 오류: "whisper"에 접근할 수 없음: "whisper"는 "TalkativeButton"의 "protected" 멤버임
    
}
```

### 4. 내부 클래스와 중첩된 클래스: 기본적으로 중첩 클래스
- 코틀린의 중첩 클래스는 명시하지 않는 한 외부 클래스 인스턴스에 대한 접근 권한이 없음
- 자바 예제. ButtonState를 생성하려면 Button을 먼저 생성해야되는데 Button은 serializable을 구현하지 않아서 에러 발생
- 코틀린 중첩 클래스는 자바 static 중첩 클래스와 같다.
- 코틀린에서 내부 클래스로 사용하려면 inner 키워드를 붙여야 한다.
- 바깥쪽 인스턴스를 참조하려면 this@Outer


### 봉인된 클래스 Sealed class
- sealed class는 자동으로 open

##2. 뻔하지 않은 생성자와
- 클래스 초기화: 주 생성자와 초기화 블록

- 함수와 똑같이 생성자 파라미터에도 디폴트 값을 정의 가능
- 인스턴스 생성시 new 키워드 없음
- 클래스에 기반 클래스가 있다면 주 생성자에서 기반 클래스의 생성자를 호출해야 할 필요가 있다.

- 별도로 생성자를 정의하ㅣㅈ 않으면 자동으로 인자가 없는 디폴트 생성자를 만든다.
- 인터페이스는 생성자가 없기 때문에 이름 뒤에 아무 괄호가 없다.

- 부 생성자: 상위 클래스를 다른 방식으로 초기화
- 부 생성자 대신 파라미터 디폴트 값을 생성자 시그니처에 직접 명시하는 방법이 좋다.

- 인터페이스에 선언된 프로퍼티 구현


## 컴파일러가 생성한 메소드 : 데이터 클래스와 클래스 위임
- 클래스 앞에 data를 붙이면 equals, tostring, hashcode를 자동으로 생성 !!디컴파일
- 코틀린은 동등성 연산에 ==을 사용
- 참조 비교를 하기 위해서는 ==== 비교
- 데이터 클래스와 불변성: copy() 메서드
    - 불변 객체를 복사하면서 일부 프로퍼티를 변경
- 클래스의 위임: by 키워드
    - 간결한 코드를 위해서
    - 여러가지 인터페이스를 위임해서 사용하고 싶을때도 사용 가능
- object 키워드:

슬랙 ??
- 무명 클래스 > 람다 > run 
