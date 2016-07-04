package io.getquill.context.cassandra

import io.getquill.Spec

class FreezeSpec extends Spec {
  
  import testSyncDB._
  
  case class Person(id: Int, name: String, age: Int)
  "Contains" - {
    "freeze" in {
      val entries = List(
        Person(1, "Bob", 30),
        Person(2, "Gus", 40),
        Person(3, "Pet", 20),
        Person(4, "Don", 50),
        Person(5, "Dre", 60))

      testSyncDB.run(query[Person].delete)
      testSyncDB.run(query[Person].insert)(entries)
      val q = quote {
        (ids: Set[Int]) => query[Person].filter(p => ids.contains(p.id))
      }
      testSyncDB.run(q)(Set(3, 4)) mustEqual List(Person(3, "Pet", 20), Person(4, "Don", 50))
    }
  }
}
