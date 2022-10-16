class Chap4 {
}

interface Animal {
    // 인터페이스에 디폴트 메서드 구현 가능
    fun eat() = println("eat every day")
    fun sleep()
}

interface Animal2 {
    fun eat() = println("eat too much2")
    fun run() = println("default method : run ")
}

// interface는 여러개 구현 가능
class Dog : Animal, Animal2 {

    // override 키워드 필수
    override fun sleep() = println("sleep at night")

    // 두개의 인터페이스에 메서드 시그니처가 동일한 디폴트 메서드가 있는 경우 override 필수
    override fun eat() = super<Animal2>.eat()

}

fun main() {
    val dog = Dog()
    dog.sleep()
    dog.eat()
    dog.run()
}