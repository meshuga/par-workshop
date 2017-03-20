package concurrent

import java.util.concurrent.{ExecutorService, Executors, Future}

import basics.parallel1
import org.scalameter.{Warmer, withWarmer}

object executor extends App {

  val executor: ExecutorService = Executors.newFixedThreadPool(4)

  def sumArray(input: Array[Int], threshold: Int, left: Int, right: Int): Int = {
    if (right - left < threshold) {
      parallel1.sumSegment(input, left, right)
    } else {
      val middle = left + (right - left) / 2

      val spawnedTask: Future[Int] = executor.submit(() => sumArray(input, threshold, left, middle))

      sumArray(input, threshold, left, middle) + spawnedTask.get()
    }
  }

  val r = scala.util.Random
  r.setSeed(0)
  val in = Array.fill(4000000){ r.nextInt(15)}
  val threshold = 1600000

  val result = sumArray(in, threshold, 0, in.length)
  println(result)

  val timeForkJoinTask = withWarmer(new Warmer.Default) measure {
    val result = sumArray(in, threshold, 0, in.length)
    result.equals(0)
  }

  println(timeForkJoinTask)

  executor.shutdown()
}
