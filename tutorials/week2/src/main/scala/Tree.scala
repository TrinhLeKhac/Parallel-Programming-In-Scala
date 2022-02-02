//sealed abstract class Tree[A] {
//  val size: Int
//}
//
//object Tree {
//
////  case class Leaf[A](a: Array[A]) extends Tree[A] {
////    override val size = a.size
////  }
//  case class Leaf[A](a: A) extends Tree[A] {
//  override val size = 1
//}
//
//  case class Node[A](left: Tree[A], right: Tree[A]) extends Tree[A] {
//   override val size = left.size + right.size
//  }
//
//  def mapTreePar[A: Manifest, B: Manifest](t: Tree[A], f: A => B): Tree[B] = t match {
////    case Leaf(a) => Leaf(a.map(f))
////    case Leaf(a) => {
////      val b = new Array[B](a.length)
////      var i = 0
////      while(i < a.length) {
////        b(i) = f(a(i))
////        i = i + 1
////      }
////      Leaf(b)
////    }
//    case Leaf(a) => Leaf(f(a))
//    case Node(left, right) => {
//      val (mpl, mpr) = parallel(mapTreePar(left, f), mapTreePar(right, f))
//      Node(mpl, mpr)
//    }
//  }
//
//  def toList[A](t: Tree[A]): List[A] = t match {
//    case Leaf(a) => List(a)
//    case Node(left, right) => toList(left) ++ toList(right)
//  }
//
//  def toList2[A](t: Tree[A]) = reduce(map(t, List(_)), _ ++ _)
//
//  def reduce[A](t: Tree[A], f: (A, A) => A): A = t match {
//    case Leaf(a) => a
//    case Node(l, r) => f(reduce(l, f), reduce(r, f))
//  }
//
//  def map[A, B](t: Tree[A], f: A => B): Tree[B] = t match {
//    case Leaf(a) => Leaf(f(a))
//    case Node(left, right) => Node(map(left, f), map(right, f))
//  }
//
//  def reduceSeg[A](inp: Array[A], from: Int, until: Int, threshold: Int, f: (A, A) => A): A = {
//    if(until - from < threshold) {
//      var res = inp(from)
//      var i = from + 1
//      while(i < until) {
//        res = f(res, inp(i))
//        i = i + 1
//      }
//      res
//    } else {
//      val mid = (from + until) / 2
//      val (res1, res2) = parallel(
//        reduceSeg(inp, from, mid, threshold, f),
//        reduceSeg(inp, mid, until, threshold, f)
//      )
//      f(res1, res2)
//    }
//  }
//
//  def reduce[A](inp: Array[A], threshold: Int, f: (A, A) => A): A = reduceSeg(inp, 0, inp.length, threshold, f)
//
//
//
//  /**
//   * Comparison of arrays and immutable trees
//   * Arrays:
//   * (+) random access to elements
//   * (+) good memory locality
//   * (-) compared to imperative: => must ensure parallel tasks write to disjoint parts
//   * (-) expensive concatenate
//   *
//   * Immutable trees
//   * (+) purely functional, produces new trees, keep old trees
//   * (+) no need to worry to disjointness of writes by parallel tasks
//   * (+) efficient to combine two trees
//   * (-) high memory allocation overhead
//   * (-) bad locality
//   * */
//}
//
///**
// * we need operate associative to run parallel
// * foldLeft[A, B](z: A)(f: (A, B) => A): B
// *
// * val xs = List(1, 2, 3)
// * xs.foldLeft(100)((acc, x) => acc - x)
// * (((100 - 1) - 2) -3) = 94
// * xs.foldRight(100)((x, acc) => x - acc)
// * (1 - (2 - (3 - 100))) = - 98
// *
// * operate f have associative if:
// * f(f(a, b), c) = f(a, f(b, c))
// *
// * in run parallel we don't have foldLeft, foldRight
// * we only have fold
// * */
