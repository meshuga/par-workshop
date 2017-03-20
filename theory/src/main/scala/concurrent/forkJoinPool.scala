package concurrent

import java.util.concurrent.{ForkJoinPool, RecursiveTask}

import basics.parallel1
import org.scalameter.{Warmer, withWarmer}

object forkJoinPool extends App {

  class SumArrayTask(input: Array[Int], threshold: Int, left: Int, right: Int)
    extends RecursiveTask[Int] {

    override def compute(): Int = {
      if (right - left < threshold) {
        parallel1.sumSegment(input, left, right)
      } else {
        val middle = left + (right - left) / 2

        val subTasks = List(new SumArrayTask(input, threshold, left, middle),
          new SumArrayTask(input, threshold, middle, right))

        subTasks.foreach(_.fork())
        subTasks.foldRight(0)((a: SumArrayTask, sum: Int) => a.join() + sum)
      }
    }
  }

  val forkJoinPool = new ForkJoinPool(4)

  val r = new scala.util.Random()
  r.setSeed(0)
  val in = Array.fill(4000000){ r.nextInt(15)}
  val threshold = 1600000

  val task = new SumArrayTask(in, threshold, 0, in.length)
  val result = forkJoinPool.invoke(task)
  println(result)

  val timeForkJoinTask = withWarmer(new Warmer.Default) measure {
    val task = new SumArrayTask(in, threshold, 0, in.length)
    val result = forkJoinPool.invoke(task)
    result.equals(0)
  }

  println(timeForkJoinTask)

}
