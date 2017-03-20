package basics

object thread1 extends App {
  var counter = 0L

  def getUniqueId: Long = {
    counter += 1
    counter
  }

  def startThread(): Unit = {
    val t = new Thread() {
      override def run(): Unit =
        println(Seq.fill(10)(getUniqueId))
    }
    t.start()
  }

  startThread()
  startThread()
  startThread()
  startThread()
}
