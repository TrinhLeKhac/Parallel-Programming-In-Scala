/**
  * task parallelism
  * data parallelism
  * */

/**
  * process have PID, not share same memory
  * process includes threads
  * have a main thread
  * we can create new thread*/

//class HelloThread extends Thread{
//  override def run(): Unit ={
//    println("hello, world")
//  }
//}

//val t = new HelloThread
//
//t.start()
//
//t.join()

//def r(): Unit = {
//  val t = new HelloThread
//  val s = new HelloThread
//  t.start()
//  s.start()
//  t.join()
//  s.join()
//}
//r()

//private var uidCount = 0L
//
//def getUniqueId(): Long = {
//  uidCount = uidCount + 1
//  uidCount
//}
//
//def startThread(): Unit = {
//  val t = new Thread {
//    override def run(): Unit = {
//      val uids = for(i <- 0 until 10) yield getUniqueId()
//      println(uids)
//    }
//  }
//  t.start()
//  t
//}
//
//startThread()

/**
  * code is excutived by 2 threads*
  *
  * make sure code is executed by only one thread
  * at a certain time
  * synchronized
  * */

//private val x = new AnyRef {}
//private var uidCounts = 0L
//def getUniqueIdSync(): Long = x.synchronized {
//  uidCounts = uidCounts + 1
//  uidCounts
//}

class Account(private var amount: Int = 0) {
  def get = amount
  def transfer(that: Account, n: Int) = {
    this.synchronized {
      that.synchronized {
        this.amount -= n
        that.amount += n
      }
    }
  }
}

val a = new Account(50)
val b = new Account(70)

/**
  * Deadlock
  * */
a.transfer(b, 10)
println(a.get)
println(b.get)
b.transfer(a, 10)
println(a.get)
println(b.get)
//b.transfer(a, 10)
//def startThreads(a: Account, b: Account, n: Int) = {
//  val t = new Thread {
//    override def run(): Unit = {
//      for(i <- 0 until n) {
//        a.transfer(b, 1)
//      }
//    }
//  }
//  t.start()
//  t
//}
//
//val a1 = new Account(20)
//val a2 = new Account(10)
//
//val t = startThreads(a1, a2, 5)
//val s = startThreads(a1, a2, 5)
//
//t.join()
//s.join()






