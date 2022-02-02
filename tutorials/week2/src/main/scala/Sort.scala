/**
 * recursively sort the two halves of the array in parallel
 * sequentially merge the two halves by copying into a temporary array
 * copy the temporary array back the original array
 * */

object Sort:
  /**
   * copy in parallel
   * */
  val maxDepth: Int = 10

  def copy(src: Array[Int], dst: Array[Int], from: Int, until: Int, depth: Int): Unit = {
    if(depth == maxDepth) {
      Array.copy(src, from, dst, from, until - from)
    }
    else {
      val mid = (from + until) / 2
      parallel(
        copy(src, dst, from, mid, depth + 1),
        copy(src, dst, mid, until, depth + 1)
      )
    }
  }

  /**
   * quickSort
   * */
  def quickSort(xs: Array[Int], from: Int, util: Int): Array[Int] = {
    def quickSortHelper(ys: Array[Int]): Array[Int] = {
      if(ys.length <= 1) ys
      else {
        val pivot = ys(ys.length / 2)
        Array.concat(
          quickSortHelper(ys.filter(_ < pivot)),
          ys.filter(_ == pivot),
          quickSortHelper(ys.filter(_ > pivot))
        )
      }
    }
    val ys = new Array[Int](util - from)
    Array.copy(xs, from, ys, 0, util - from)
    quickSortHelper(ys)
  }

  def mergeSort(xs: Array[Int], maxDepth: Int): Unit = {

    val ys = new Array[Int](xs.length)

    def merge(src: Array[Int], dst: Array[Int], from: Int, mid: Int, until: Int): Unit = ???

    def sort(from: Int, until: Int, depth: Int): Unit = {
      if(depth == maxDepth) {
        quickSort(xs, from, until - from)
      } else {
        val mid = (from + until)/2
        parallel(sort(from, mid, depth + 1), sort(mid, until, depth + 1))

        val flip = (maxDepth - depth) % 2 == 0
        val src = if(flip) ys else xs
        val dst = if(flip) xs else ys
        merge(src, dst, from, mid, until)
      }
    }
    sort(0, xs.length, 0)
  }
