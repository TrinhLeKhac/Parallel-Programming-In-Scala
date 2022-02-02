object Seq

  val maxDepth: Int = 10
  /**
   * FP programming
   * */
  def mapSeq[A, B](ls: List[A], f: A => B): List[B] = ls match {
    case Nil => Nil
    case head::tail => f(head)::mapSeq(tail, f)
  }

  /**
   * imperative programming
   * */
  def mapSeqSeq[A, B](inp: Array[A], out: Array[B], f: A => B, from: Int, until: Int): Unit = {
    var i = from
    while(i < until) {
      out(i) = f(inp(i)) // List not update value => use Array for this imperative programming
      i = i + 1
    }
  }

  def mapSeqPar[A, B](inp: Array[A], out: Array[B], f: A => B, from: Int, until: Int, depth: Int): Unit = {
    /**
     * 1) => Using threshold for until - from
     * 2) => increase depth through recursive and set condition: depth == maxDepth
     * */
    if(depth == maxDepth) {
      mapSeqSeq(inp, out, f, from, until)
    } else {
      val mid = (from + until) / 2
      parallel(
        mapSeqPar(inp, out, f, from, mid, depth + 1),
        mapSeqPar(inp, out, f, mid, until, depth + 1)
      )
    }
  }

object SeqTest extends App:
  /**
   * test 1
   * */
//  val xs = List(1, 2, 3).map(_*2)
//  xs foreach println
//  val ys = List(1, 2, 3).fold(100)((acc: Int, x: Int) => acc + x)
//  println(ys)
//  val zs = List(1, 2, 3).scan(100)((acc: Int, x: Int) => acc + x)
//  zs foreach println

  import Seq._
//  val ts = Array(1, 2, 3)
//  val rs = new Array[Int](ts.length)
//  // val rs = Array.ofDim[Int](ts.length)
//  mapSeqSeq(ts, rs, (x: Int) => x * 2, 0, 3)
//  ts foreach println

  val as = (1 to 100000000).toArray
  val rs = Array.ofDim[Int](as.length)

  val startTimeSeq = System.nanoTime
  mapSeqSeq(as, rs, (x: Int) => x * 2, 0, as.length)
  println(rs.length)
  val stopTimeSeq = System.nanoTime
  println(s"Time elapsed: ${(stopTimeSeq - startTimeSeq)/Math.pow(10, 9)} s")

  val startTimePar = System.nanoTime
  mapSeqPar(as, rs, (x: Int) => x * 2, 0, as.length, 0)
  println(rs.length)
  val stopTimePar = System.nanoTime
  println(s"Time elapsed: ${(stopTimePar - startTimePar)/Math.pow(10, 9)} s")

