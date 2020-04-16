/**
 * Created by magiliang on 2015/4/20.
 */


class Counter {
  private var value = 0

  def increment(): Unit = {
    value += 1
  }

  def current() = value
}
