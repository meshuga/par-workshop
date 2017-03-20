package basics

import common._
import org.scalameter._

object parallel2 extends App {

  def mapSeqSeq(inp: Array[Int], left: Int, right: Int, f: Int => Int): Unit = {
    var i = left
    while (i < right) {
      inp(i) = f(inp(i))
      i += 1
    }
  }

  def mapSeqPar(inp: Array[Int], threshold: Int, left: Int, right: Int, f: Int => Int): Unit = {
    if (right - left < threshold) {
      mapSeqSeq(inp, left, right, f)
    } else {
      val n = left + (right - left) / 2
      parallel(mapSeqPar(inp, threshold, left, n, f),
        mapSeqPar(inp, threshold, left, n, f))
    }
  }

  def squareModuloSeq(inp: Array[Int], p: Int, left: Int, right: Int): Unit = {
    var i = left
    while (i < right) {
      inp(i) = math.pow(inp(i), p).toInt % 15
      i += 1
    }
  }

    def squareModuloPar(inp: Array[Int], p: Int, threshold: Int, left: Int, right: Int): Unit = {
    if (right - left < threshold) {
      var i = left
      while (i < right) {
        inp(i) = math.pow(inp(i), 2).toInt % 15
        i += 1
      }
    } else {
      val n = left + (right - left) / 2
      parallel(squareModuloPar(inp, p, threshold, left, n),
        squareModuloPar(inp, p, threshold, left, n))
    }
  }

  val r = scala.util.Random
  r.setSeed(0)
  val in = Array.fill(4000000){ r.nextInt(15)}
  val f: (Int) => Int = math.pow(_, 2).toInt % 15
  val threshold = 1000000

  val timeMapSeqSeq = withWarmer(new Warmer.Default) measure {
    mapSeqSeq(in, 0, in.length, f)
  }

  val timeMapSeqPar = withWarmer(new Warmer.Default) measure {
    mapSeqPar(in, threshold, 0, in.length, f)
  }

  val timeSquareModuloSeq = withWarmer(new Warmer.Default) measure {
    squareModuloSeq(in, 2, 0, in.length)
  }

  val timeSquareModuloPar = withWarmer(new Warmer.Default) measure {
    squareModuloPar(in, 2, threshold, 0, in.length)
  }

  println(s"mapSeqSeq $timeMapSeqSeq")
  println(s"mapSeqPar $timeMapSeqPar")
  println(s"squareModuloSeq $timeSquareModuloSeq")
  println(s"squareModuloPar $timeSquareModuloPar")
}
