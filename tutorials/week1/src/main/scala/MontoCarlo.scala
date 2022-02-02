object MontoCarlo {

  import scala.util.Random

  def mcCount(iter: Int): Int = {
    val randomX = new Random
    val randomY = new Random
    var hits = 0
    for(i <- 0 until iter) {
      val x = randomX.nextDouble
      val y = randomY.nextDouble
      if(x*x + y*y < 1) hits = hits + 1
    }
    hits
  }

  def monteCarloPiSeq(iter: Int) = 4.0 * mcCount(iter) / iter

  def monteCarloPiPar(iter: Int): Double = {
    val ((pi1, pi2), (pi3, pi4)) = parallel(
      parallel(mcCount(iter/4), mcCount(iter/4)),
      parallel(mcCount(iter/4), mcCount(iter - 3*iter/4))
    )
    4.0 * (pi1 + pi2 + pi3 + pi4) / iter
  }
}

object testMontoCarlo extends App:
  
  import MontoCarlo._
  val beforeSeq = System.nanoTime
  val piSeq = monteCarloPiSeq(100000000)
  val afterSeq = System.nanoTime
  println(piSeq)
  println(s"Time elapsed when calculate sequences: ${(afterSeq - beforeSeq)/Math.pow(10, 9)} s")

  val beforePar = System.nanoTime
  val piPar = monteCarloPiPar(100000000)
  val afterPar = System.nanoTime
  println(piPar)
  println(s"Time elapsed when calculate parallel: ${(afterPar - beforePar)/Math.pow(10, 9)} s")
