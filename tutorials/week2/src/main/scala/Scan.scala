class Scan {

  def scanLeftSeq[A](inp: Array[A], z: A, f: (A, A) => A, out: Array[A]): Unit = {
    out(0) = z
    var a = z
    var i = 0
    while(i < inp.length) {
      a = f(a, inp(i))
      i = i + 1
      out(i) = a
    }
  }

  def scanLeftPar[A](inp: Array[A], z: A, f: (A, A) => A, out: Array[A]): Unit = {
    var fi = {(i: Int, a: A) => reduceSeg1(inp, 0, i, z, f)}
    mapSeg(inp, out, 0, inp.length, fi)
    val last = inp.length - 1
    out(last + 1) = f(out(last), inp(last))
  }

  def reduceSeg1[A, B](inp: Array[A], from: Int, until: Int, z: A, f: (A, A) => A): A = {
    ???
  }

  def mapSeg[A, B](inp: Array[A], out: Array[B], from: Int, until: Int, f: (Int, A) => B): Unit = {
    ???
  }


}

sealed abstract class Tree[A]
object Tree{
  case class Leaf[A](a: A) extends Tree[A]

  case class Node[A](l: Tree[A], r: Tree[A]) extends Tree[A]

  def prepend[A](x: A, t: Tree[A]): Tree[A] = t match {
    case Leaf(a) => Node(Leaf(x), Leaf(a))
    case Node(l, r) => Node(prepend(x, l), r)
  }

  def showTree[A](t: Tree[A]): String = t match {
    case Leaf(a) => s"${Leaf(a)}"
    case Node(l, r) => s"Node(${showTree(l)}, ${showTree(r)})"
  }
}

sealed abstract class TreeRes[A] {val res: A}
object TreeRes {
  import Tree._

  case class LeafRes[A](override val res: A) extends TreeRes[A]

  case class NodeRes[A](l: TreeRes[A], override val res: A, r: TreeRes[A]) extends TreeRes[A]

  def reduceRes[A](t: Tree[A], f: (A, A) => A): TreeRes[A] = t match {
    case Leaf(a) => LeafRes(a)
    case Node(l, r) => {
      val (resLeft, resRight) = (reduceRes(l, f), reduceRes(r, f))
      NodeRes(resLeft, f(resLeft.res, resRight.res), resRight)
    }
  }

  def upsweep[A](t: Tree[A], f: (A, A) => A): TreeRes[A] = t match {
    case Leaf(a) => LeafRes(a)
    case Node(l, r) => {
      val (tl, tr) = parallel(
        upsweep(l, f),
        upsweep(r, f)
      )
      NodeRes(tl, f(tl.res, tr.res), tr)
    }
  }

  def downsweep[A](t: TreeRes[A], z: A, f: (A, A) => A): Tree[A] = t match {
    case LeafRes(a) => Leaf(f(z, a))
    case NodeRes(l, _, r) => {
      val (tl, tr) = parallel(
        downsweep(l, z, f),
        downsweep(r, f(z, l.res), f)
      )
      Node(tl, tr)
    }
  }

  def scanLeft[A](t: Tree[A], z: A, f: (A, A) => A): Tree[A] = {
    val tRes = upsweep(t, f)
    val scanRes = downsweep(tRes, z, f)
    prepend(z, scanRes)
  }

  def showTreeRes[A](tr: TreeRes[A]): String = tr match {
    case LeafRes(a) => s"LeafRes($a)"
    case NodeRes(tl, res, tr) => s"NodeRes(${showTreeRes(tl)}, ${res}, ${showTreeRes(tr)})"
  }
}

object Test extends App:
  import TreeRes._
  import Tree._
  val t1 = Node(Node(Leaf(1), Leaf(3)), Node(Leaf(8), Leaf(50)))
  val plus = (x: Int, y: Int) => x + y
  val upTree = reduceRes(t1, plus)
  println(showTreeRes(upTree))

  val resTree = downsweep(upTree, 100, plus)
  println(showTree(resTree))

  val scanTree = scanLeft(t1, 100, plus)
  println(showTree(scanTree))


