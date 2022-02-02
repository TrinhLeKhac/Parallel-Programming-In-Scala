object sumSegment {

  /**
    * define power
    * */
  def power(a: Double, b: Double): Double = math.exp(b * math.log(a))

  /**
    * define sum in interval [s, t)
    * */
  def sumInterval(xs: List[Int], p: Double, s: Int, t: Int): Double = {
    var i = s
    var res: Double = 0
    while(i < t) {
      res = res + power(xs(i).toDouble, p)
      i = i + 1
    }
    res
  }

  /**
    * define pNorm
    * */
  def pNorm(xs: List[Int], p: Double, s: Int, t: Int): Double = power(sumInterval(xs, p, s, t), 1/p)

  /**
    * define sum in parallel
    * */
  def pNormTwoPart(xs: List[Int], p: Double, s: Int, t: Int): Double = {
    val h = xs.length/2
    val (res1, res2) = parallel(sumInterval(xs, p, 0, h+1), sumInterval(xs, p, h+1, xs.length))
    power(res1 + res2, 1/p)
  }

  /**
    * define segmentRec
    * */
  def segmentRec(xs: List[Int], p: Double, s: Int, t: Int, threshold: Int): Double = {
    if(t - s < threshold) sumInterval(xs, p, s, t)
    else {
      val m = s + (t - s)/2
      val (sum1, sum2) = parallel(segmentRec(xs, p, s, m+1, threshold), segmentRec(xs, p, m+1, t, threshold))
      sum1 + sum2
    }
  }

  /**
   * define pNormRec
   * */
  def pNormRec(xs: List[Int], p: Double, s: Int, t: Int, threshold: Int): Double =
    power(segmentRec(xs, p, 0, xs.length, threshold), 1/p)

  /**
    * Call function
    * (va, vb) = parallel(a, b) (1)
    * (va1, vb1) = parallel1(a, b)  (2)
    *
    * difference
    * (2) <=> (va1, va2) = (a, b)
    * parallel parameters must pass by name
    * */
}

/**
  * Application
  * */
object testSumSegment:
  import sumSegment._
  def main(args: Array[String]): Unit = {
//    val xs = List(1, 2, 3, 4)
//    val p = 2
//    val s = 1
//    val t = 3
//    val res = power(2, 3)
//    val sum = sumInterval(xs, p, s, t)
//    val norm = pNorm(xs, p, s, t)
//
//    println(res)
//    println(sum)
//    println(norm)

      val xs = Range(1, 100000).toList
      val start = 1
      val end = 90000
      val p = 2
      val threshold = 10
      val startTime = System.nanoTime
      val normSer = sumInterval(xs, p, start, end)
      val stopTime = System.nanoTime

      println(s"Time elapsed when calculating pnorm in sequences: ${(stopTime - startTime)/power(10, 9)} s")

      val startTimePar = System.nanoTime
      val normPar = segmentRec(xs, p, start, end, threshold)
      val stopTimePar = System.nanoTime
      println(s"TIme elapsed when calculating pnorm in parallel: ${(stopTimePar - startTimePar)/power(10, 9)} s")

  }