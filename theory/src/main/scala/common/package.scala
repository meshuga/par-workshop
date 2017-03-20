package object common {

  // parallel definition

  def parallel[A >: Null](t1: => A, t2: => A): (A, A) = {
    var t1_result: A = null
    val t1_thread = new Thread() {
      override def run(): Unit = t1_result = t1
    }
    t1_thread.start()

    val t2_result: A = t2
    t1_thread.join()
    (t1_result, t2_result)
  }

  // Task definitions

  trait Task[+A] {
    def join: A
  }

  def task[A >: Null](t: => A): Task[A] = {
    var t_result: A = null
    val t_thread = new Thread() {
      override def run(): Unit = t_result = t
    }
    t_thread.start()
    new Task[A] {
      override def join: A = {
        t_thread.join()
        t_result // JMM ensures c_result will have the same data
      }
    }
  }
}
