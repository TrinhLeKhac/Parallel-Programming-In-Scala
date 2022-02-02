//object DataParallel
//
///**
// * Mandelbrot case
// * w(i) = #iterations
// * data-parallel scheduler: efficiently balance the workload across processors without knowledge about the w(i)
// * */
//def initializeArray(xs: Array[Int])(z: Int): Array[Int] = {
//  var i = 0
//  for(i <- (0 until xs.length).par) {
//    xs(i) = v
//  }
//  xs
//}
//private def computePixel(xc: Double, yc: Double, maxIteration: Int): Int = {
//  var i = 0
//  var x, y = 0d
//  while((x * x + y * y < 4) && (i < maxIteration)) {
//    val xt = x * x - y * y + xc
//    val yt = 2 * x * y + yc
//    x = xt
//    y = yt
//    i = i + 1
//  }
//  color(i)
//}
//
//def parRender(): Unit = {
//  for (idx <- (0 until image.length).par) {
//    val (xc, yc) = coordinateFor(idx)
//    images(idx) = computePixel(xc, yc, maxIteration)
//  }
//}

//  def sum(xs: Array[Int]): Int = {
//    xs.foldLeft(0)(_ + _)
//  }

/**
 * Scala collections hierarchy
 * Traversable[T] foreach
 * Iterable[T] iterator
 * Seq[T]
 * Set[T]
 * Map[K, V]
 *
 * ParIterable[T]
 * ParSeq[T]
 * ParSet[T]
 * ParMap[K, V]
 *
 *
 * GenIterable[T]
 * GenSeq[T]
 * GenSet[T]
 * GenMap[T]
 * */

/**
 * Generic collections allow us to write code unaware of parallelism
 * */

/**
 * def aggregate[B](z: => B)(seqop: (B, A) => B, combop: (B, B) => B): B
 * */

//def largestPalindrome(xs: Seq[Int]): Int = {
//  xs.aggregate(Int.MinValue) {
//    (largest, n) => if((n > largest) && (n.toString == n.toString.reverse)) n else largest,
//    Math.max
//  }
//}

//trait Iterator[A]:
//  def next(): A
//  def hasNext(): Boolean
//end Iterator
//
//def foldLeft[A, B](z: B)(f: (B, A) => B): B = {
//  var s = z
//  while(hasNext) {
//    s = f(s, next())
//  }
//  s
//}

object DataParallelRun:

  def sum(xs: Array[Int]): Int = {
    xs.fold(0)(_ + _)
  }

  def max(xs: Array[Int]): Int = {
    xs.fold(Int.MinValue)((x: Int, y: Int) => if(x > y) x else y)
  }

  def play(a: String, b: String): String = List(a, b).sorted match {
    case List("paper", "rock") => "paper"
    case List("paper", "scissors") => "scissors"
    case List("rock", "scissors") => "rock"
    case List(a, b) if(a == b) => a
    case List("", a) => a
  }

  def main(args: Array[String]): Unit = {

    val res = (1 until 1000).filter(n => n % 3 == 0).filter(n => n.toString == n.toString.reverse)
    res foreach println
//    println(res)

    val s = sum(Array(1, 2, 3, 4, 5))
    println(s)

    val m = max(Array(1, 2, 3, 4, 5))
    println(m)

    val resTwo = Array("paper", "rock", "paper", "scissors", "rock").fold("")(play)
    println(resTwo)
  }



