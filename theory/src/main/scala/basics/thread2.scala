package basics

object thread2 extends App {
  val x = new AnyRef
  var counter = 0L

  def getUniqueId: Long = x.synchronized {
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
