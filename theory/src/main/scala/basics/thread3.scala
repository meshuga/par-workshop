package basics

object thread3 extends App {

  class Account(var balance: Long) {
    def transfer(target: Account, transferAmount: Long): Unit = {
      this.synchronized {
        target.synchronized {
          this.balance -= transferAmount
          target.balance += transferAmount
        }
      }
    }
  }

  def startThread(a: Account, b: Account, transferTimes: Int): Thread = {
    val thread = new Thread() {
      override def run(): Unit =
        for (_ <- 0 until transferTimes) a.transfer(b, 1)
    }
    thread.start()
    thread
  }

  val a1 = new Account(100000)
  val a2 = new Account(200000)

  val t1 = startThread(a1, a2, 10000)
  val t2 = startThread(a2, a1, 10000)

  t1.join()
  t2.join()
}
