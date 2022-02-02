sealed trait Tree[+T]
object Tree {
  case class Node[T](left: Tree[T], right: Tree[T]) extends Tree[T]
  case class Leaf[T](elem: T) extends Tree[T]
  case object Empty extends Tree[Nothing]
  }

def filter[T](t: Tree[T])(p: T => Boolean): Tree[T] = t match {
  case Node(left, right) => Node(parallel(filter(left)(p), filter(right)(p)))
  case Leaf(elem) => if p(elem) then Leaf(elem) else Tree.Empty
  case Tree.Empty => Tree.Empty
}

/**
 * Tree is not good for parallelism unless they are balanced
 * */

sealed trait Conc[+T] {
  def level: Int
  def size: Int
  def left: Conc[T]
  def right: Conc[T]
}

case object Empty extends Conc[Nothing] {
  def level = 0
  def size = 0
}

case class Single[T](val x: T) extends Conc[T] {
  def level = 0
  def size = 1
}

case class <>[T](left: Conc[T], right: Conc[T]) extends Conc[T] {
  val level = 1 + Math.max(left.level, right.level)
  val size = left.size + right.size
}

  def concat[T](xs: Conc[T], ys: Conc[T]): Conc[T] = {
    val diff = ys.level - xs.level
    if (diff >= -1 && diff <= 1) {
      new <>(ys, xs)
    }
    else if (diff < -1) {
      if (xs.left.level >= xs.right.level) {
        val nr = concat(xs.right, ys)
        new <>(xs.left, nr)
      }
      else {
        val nrr = concat(xs.right.right, ys)
        if (nrr.level == xs.level - 3) {
          val nl = xs.left
          val nr = new <>(xs.right.left, nrr)
        } else {
          val nl = new <>(xs.left, xs.right.left)
          val nr = nrr
          new <>(nl, nr)
        }
      }
    }
    else {

    }
  }



