package org.jetbrains.plugins.scala.failed.typeInference

import org.jetbrains.plugins.scala.PerfCycleTests
import org.jetbrains.plugins.scala.lang.typeInference.TypeInferenceTestBase
import org.junit.experimental.categories.Category

/**
  * Created by kate on 3/23/16.
  */
@Category(Array(classOf[PerfCycleTests]))
class SelfTypeTest extends TypeInferenceTestBase {
  override def folderPath: String = super.folderPath + "bugs5/"

  def testSCL5571(): Unit = doTest()

  def testSCL5947(): Unit = doTest()

  def testSCL8661(): Unit = doTest()

  def testSCL10173a(): Unit = doTest(
    s"""
       |trait T
       |class Root
       |class A extends Root with T
       |class B extends A {
       |  def foo(node: Root with T): Unit = {}
       |}
       |
       |object Example extends App {
       |
       |  def bug1(b: B): Unit = {
       |    val a: A = new A()
       |    b.foo(${START}a$END)
       |  }
       |}
       |//b.type
      """.stripMargin)

  def testSCL10173b(): Unit = doTest(
    s"""
       |trait T
       |class Root
       |class A extends Root with T
       |class B extends A {
       |  def foo(node: Root with T): Unit = {}
       |}
       |
       |object Example extends App {
       |  def bug2(b: B): Unit = {
       |    val b2: B = new B()
       |    b.foo(${START}b2$END)
       |  }
       |}
       |//b.type
      """.stripMargin)
}