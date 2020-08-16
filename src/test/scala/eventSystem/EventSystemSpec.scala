package eventSystem

import org.scalatest.flatspec._
import org.scalatest.matchers._
import org.scalamock.scalatest.MockFactory

class EventSystemSpec extends AnyFlatSpec with should.Matchers with MockFactory {
  "Listener" should "handle subscribed event" in {
    var count = 1
    val listener = EventSystem.subscribe()

    listener.on(Events.simple, _ => { count += 1 })

    count should be (1)

    EventSystem ! SimpleEvent()

    count should be (2)

    EventSystem ! SimpleEvent()

    count should be (3)

    listener.unsubscribe()
  }

  it should "ignore events after unsubscribing" in {
    var count = 0
    val listener = EventSystem.subscribe()

    listener.on(Events.simple, _ => { count += 1 })
    listener.unsubscribe()

    EventSystem ! SimpleEvent()

    count should be (0)

    listener.unsubscribe()
  }

  it should "only be called with subscribed event data" in {
    var count = 0
    val listener = EventSystem.subscribe()

    listener.on(Events.simple, {
      case SimpleEvent(data) => count += data.asInstanceOf[Int]
      case _ => throw new Error("should never be thrown")
    })

    count should be (0)

    EventSystem ! SimpleEvent(5)

    count should be (5)

    EventSystem ! WindowResizeEvent(1,2)
    EventSystem ! ClickEvent(1,2,3)

    listener.unsubscribe()
  }

  it should "be called with all subscribed events" in {
    var count = 0
    var count2 = 0
    val listener = EventSystem.subscribe()

    listener.on(Events.simple, _ => { count = 3 })
    listener.on(Events.simple, _ => { count2 = 8 })

    EventSystem ! SimpleEvent()

    count should be (3)
    count2 should be (8)

    listener.unsubscribe()
  }

  it should "be chainable" in {
    var count = 0
    EventSystem.subscribe()
      .on(Events.simple, _ => {})
      .on(Events.simple, _ => {})
      .unsubscribe()

    count should be (0)
  }
}
