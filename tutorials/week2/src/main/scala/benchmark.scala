/**
  * test benchmark
  * multiple repititions
  * statistical treatment - computing mean and variance
  * eliminating outliers
  * ensuring steady state(warm-up)
  * preventing anomalies(GC, JIT compilation, aggressive optimize)
  *
  *
  * ScalaMeter: using to test benchmark
  *
  * */

import org.scalameter.*
object Benchmark extends App:
//  println('a')
  /**
   * test
   * */
  val time = measure {
    (0 until 1000000000).toArray
  }
  println(s"Array initialization time : $time ms")

  /**
   *
   * scalameter
   * */
  val standardConfig = config(
    Key.exec.minWarmupRuns := 5,
    Key.exec.maxWarmupRuns := 10,
    Key.exec.benchRuns := 10,
    Key.verbose := false
  ) withWarmer(Warmer.Default())

  val partime = standardConfig measure {
    (0 until 100000000).toArray
  }
  println(s"Elapsed time: $partime")
