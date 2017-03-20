package basics

import common._

object parallel1 extends App {

  def sumSegment(input: Array[Int], left: Int, right: Int): Integer = {
    var i = left
    var sum = 0
    while (i < right) {
      sum += input(i)
      i += 1
    }
    sum
  }

  def sumPartially(input: Array[Int], left: Int, right: Int): Int = {
    val middle = input.length / 2
    val (sum1, sum2) = (sumSegment(input, left, middle),
      sumSegment(input, middle, right))
    sum1 + sum2
  }

  /*
  * One additional thread will be used
  */
  def sumParallel(input: Array[Int], left: Int, right: Int): Int = {
    val middle = input.length / 2
    val (sum1, sum2) = parallel(sumSegment(input, left, middle),
      sumSegment(input, middle, right))
    sum1 + sum2
  }

  /*
  * Two additional threads will be used
  */
  def sumTask(input: Array[Int], left: Int, right: Int): Int = {
    val middle = input.length / 2
    val t1 = task {sumSegment(input, left, middle)}
    val t2 = task {sumSegment(input, middle, right)}
    t1.join + t2.join
  }

  val input = Array(1, 2, 3, 4)
  println(sumSegment(input, 0, input.length))
//  println(sumPartially(a, 0, a.length))
//  println(sumParallel(a, 0, a.length))
//  println(sumTask(a, 0, a.length))
}