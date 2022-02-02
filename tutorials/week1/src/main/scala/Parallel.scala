import java.util.concurrent.*
import scala.util.DynamicVariable

import org.scalameter.*

  /**
    * val (v1, v2) = parallel(e1, e2)
    *
    * val t1 = task(e1)
    * val t2 = task(e2)
    * val v1 = t1.join
    * val v2 = t2.join
    *
    *
    * t = task(e) starts computation e "in the background"
    *
    *
    * t is a task, which performs computation of e
    * to obtain the result of e, use t.join
    * t.join will block and waits until the result is computed
    * */
  //trait Task[A] {
    /**
      * define join and task => task(e).join == e
      * */
    //def join: A // not implemented

    //def task(c: => A): Task[A] // not implemented

    /**
      * obmit .join by implicit def
      * */

    /**
     *     
     *     implicit def getJoin[A](x: Task[A]): A = x.join // not implemented

  def parallel[A, B](cA: => A, cB: => B): (A, B) = {
    val tB: Task[B] = task { cB }
    val tA: A = cA
    (tA, tB.join)
  }
}
     */


val forkJoinPool = ForkJoinPool()

abstract class TaskScheduler:
  def schedule[T](body: => T): ForkJoinTask[T]
  def parallel[A, B](taskA: => A, taskB: => B): (A, B) = {
    val right = task {
      taskB
    }
    val left = taskA
    (left, right.join())
  }


class DefaultTaskScheduler extends TaskScheduler:
  def schedule[T](body: => T): ForkJoinTask[T] = {
    val t = new RecursiveTask[T] {
      def compute = body
    }
    Thread.currentThread match
      case wt: ForkJoinWorkerThread =>
        t.fork()
      case _ =>
        forkJoinPool.execute(t)
    t
  }

val scheduler =
  DynamicVariable[TaskScheduler](DefaultTaskScheduler())

def task[T](body: => T): ForkJoinTask[T] =
  scheduler.value.schedule(body)

def parallel[A, B](taskA: => A, taskB: => B): (A, B) =
  scheduler.value.parallel(taskA, taskB)

def parallel[A, B, C, D](taskA: => A, taskB: => B, taskC: => C, taskD: => D): (A, B, C, D) = {
  val ta = task { taskA }
  val tb = task { taskB }
  val tc = task { taskC }
  val td = taskD
  (ta.join(), tb.join(), tc.join(), td)
}

