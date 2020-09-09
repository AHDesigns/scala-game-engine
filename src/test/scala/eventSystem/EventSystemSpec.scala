package eventSystem

import org.scalatest.flatspec._
import org.scalatest.matchers._

class EventSystemSpec extends AnyFlatSpec with should.Matchers {
  "Listener" should "handle subscribed event" in {
    var count = 1
    val listener = EventSystem.subscribe()

    listener.on[SimpleEvent](_ => { count += 1 })

    count should be(1)

    EventSystem ! SimpleEvent()

    count should be(2)

    EventSystem ! SimpleEvent()

    count should be(3)

    listener.unsubscribe()
  }

  it should "ignore events after unsubscribing" in {
    var count = 0
    val listener = EventSystem.subscribe()

    listener.on[SimpleEvent](_ => { count += 1 })
    listener.unsubscribe()

    EventSystem ! SimpleEvent()

    count should be(0)

    listener.unsubscribe()
  }

  it should "only be called with subscribed event data" in {
    var count = 0
    val listener = EventSystem.subscribe()

    listener.on[SimpleEvent](count += _.data.asInstanceOf[Int])

    count should be(0)

    EventSystem ! SimpleEvent(5)

    count should be(5)

    EventSystem ! WindowResize(1, 2)
    EventSystem ! Click(1, 2, 3)

    listener.unsubscribe()
  }

  it should "be called with all subscribed events" in {
    var count = 0
    var count2 = 0
    val listener = EventSystem.subscribe()

    listener.on[SimpleEvent](_ => {
      count = 3
    })
    listener.on[SimpleEvent](_ => {
      count2 = 8
    })

    EventSystem ! SimpleEvent()

    count should be(3)
    count2 should be(8)

    listener.unsubscribe()
  }

  it should "be called with all subscribed events in order" in {
    var count = 0
    val listener = EventSystem.subscribe()

    listener.on[SimpleEvent](_ => { count = 5 })
    listener.on[SimpleEvent](_ => { count *= 3 })

    EventSystem ! SimpleEvent()

    count should be(15)

    listener.unsubscribe()
  }

  it should "be chainable" in {
    val count = 0
    EventSystem
      .subscribe()
      .on[SimpleEvent](_ => {})
      .on[SimpleEvent](_ => {})
      .unsubscribe()

    count should be(0)
  }

  "Events trait" should "subscribe automatically" in {
    object Foo extends EventListener
    var count = 0

    Foo.events.on[SimpleEvent](_ => { count = 3 })

    EventSystem ! SimpleEvent()

    count should be(3)

    Foo.events.unsubscribe()
  }
}
