package basics

object associativity extends App {

  val r = scala.util.Random
  r.setSeed(0)
  val in = Array.fill(4000000){ r.nextInt(15)}

  val redSeq1 = in.reduce(_ - _)
  val redSeq2 = in.reduce(_ - _)


  val redPar1 = in.par.reduce(_ - _)
  val redPar2 = in.par.reduce(_ - _)
  val redPar3 = in.par.reduce(_ - _)

  println(s"redSeq1: $redSeq1")
  println(s"redSeq2: $redSeq2\n")

  println(s"redPar1: $redPar1")
  println(s"redPar2: $redPar2")
  println(s"redPar3: $redPar3")
}
