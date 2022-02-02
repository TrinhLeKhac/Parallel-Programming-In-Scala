package reductions

import scala.annotation.*
import org.scalameter.*

object ParallelParenthesesBalancingRunner:

  @volatile var seqResult = false

  @volatile var parResult = false

  val standardConfig = config(
    Key.exec.minWarmupRuns := 40,
    Key.exec.maxWarmupRuns := 80,
    Key.exec.benchRuns := 120,
    Key.verbose := false
  ) withWarmer(Warmer.Default())

  def main(args: Array[String]): Unit =
    val length = 100000000
    val chars = new Array[Char](length)
    val threshold = 10000
    val seqtime = standardConfig measure {
      seqResult = ParallelParenthesesBalancing.balance(chars)
    }
    println(s"sequential result = $seqResult")
    println(s"sequential balancing time: $seqtime")

    val fjtime = standardConfig measure {
      parResult = ParallelParenthesesBalancing.parBalance(chars, threshold)
    }
    println(s"parallel result = $parResult")
    println(s"parallel balancing time: $fjtime")
    println(s"speedup: ${seqtime.value / fjtime.value}")

object ParallelParenthesesBalancing extends ParallelParenthesesBalancingInterface:

  /** Returns `true` iff the parentheses in the input `chars` are balanced.
   */
  def balance(chars: Array[Char]): Boolean = {
    def loop(idx: Int, acc: Int): Int = {
      if(acc == -1) -1
      else if(idx == chars.length) acc
      else if(chars(idx) == '(') loop(idx + 1, acc + 1)
      else if(chars(idx) == ')') loop(idx + 1, acc - 1)
      else loop(idx + 1, acc)
    }
    loop(0, 0) == 0
  }

  /** Returns `true` iff the parentheses in the input `chars` are balanced.
   */
  def parBalance(chars: Array[Char], threshold: Int): Boolean = {

    def traverse(idx: Int, until: Int, arg1: Int, arg2: Int): (Int, Int) = {
      var i = idx
      var minRange = 0
      var curRange = 0
      while (i < until) {
        if (chars(i) == '(') curRange = curRange + 1
        if (chars(i) == ')') curRange = curRange - 1
        if (minRange > curRange) minRange = curRange
        i = i + 1
      }
      (minRange, curRange)
    }
    def reduce(from: Int, until: Int): (Int, Int) = {
      if (until - from < threshold) {
        traverse(from, until, 0, 0)
      } else {
        val mid = (from + until) / 2
        val (resL, resR) = parallel(reduce(from, mid), reduce(mid, until))
        (Math.min(resL._1, resL._2 + resR._1), resL._2 + resR._2)
      }
    }
    reduce(0, chars.length) == (0, 0)
  }
  // For those who want more:
  // Prove that your reduction operator is associative!

