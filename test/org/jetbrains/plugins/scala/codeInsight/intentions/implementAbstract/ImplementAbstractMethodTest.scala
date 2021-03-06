package org.jetbrains.plugins.scala
package codeInsight.intentions.implementAbstract

import com.intellij.codeInsight.intention.impl.ImplementAbstractMethodAction
import org.jetbrains.plugins.scala.base.ScalaLightCodeInsightFixtureTestAdapter
import org.jetbrains.plugins.scala.codeInsight.intentions.ScalaIntentionTestBase
import org.jetbrains.plugins.scala.lang.formatting.settings.ScalaCodeStyleSettings
import org.jetbrains.plugins.scala.util.TypeAnnotationSettings

/**
 * Nikolay.Tropin
 * 12/27/13
 */
class ImplementAbstractMethodTest extends ScalaIntentionTestBase {
  def familyName: String = new ImplementAbstractMethodAction().getFamilyName

  val START = ScalaLightCodeInsightFixtureTestAdapter.SELECTION_START
  val END = ScalaLightCodeInsightFixtureTestAdapter.SELECTION_END

  def testFromTrait() {
    val text =
      """
        |trait A {
        |  def <caret>f: Int
        |}
        |
        |class AA extends A
      """
    val result =
      s"""
        |trait A {
        |  def f: Int
        |}
        |
        |class AA extends A {
        |  override def f = $START???$END
        |}"""
    
    TypeAnnotationSettings.set(getProject,
      TypeAnnotationSettings.noTypeAnnotationForPublic(TypeAnnotationSettings.alwaysAddType(ScalaCodeStyleSettings.getInstance(getProject))))
    
    doTest(text, result)
  }

  def testFromAbstractClass() {
    val text =
      """
        |abstract class A {
        |  def <caret>f: Int
        |}
        |
        |class AA extends A {}
      """
    val result =
      s"""
        |abstract class A {
        |  def f: Int
        |}
        |
        |class AA extends A {
        |  override def f: Int = $START???$END
        |}"""
  
    TypeAnnotationSettings.set(getProject, TypeAnnotationSettings.alwaysAddType(ScalaCodeStyleSettings.getInstance(getProject)))
    doTest(text, result)
  }

  def testParameterizedTrait() {
    val text =
      """
        |trait A[T] {
        |  def <caret>f: T
        |}
        |
        |class AA extends A[Int] {}
      """
    val result =
      s"""
        |trait A[T] {
        |  def f: T
        |}
        |
        |class AA extends A[Int] {
        |  override def f: Int = $START???$END
        |}"""
  
    TypeAnnotationSettings.set(getProject, TypeAnnotationSettings.alwaysAddType(ScalaCodeStyleSettings.getInstance(getProject)))
    doTest(text, result)
  }

  def testFunDefInTrait() {
    val text =
      """
        |trait A {
        |  def <caret>f: Int = 0
        |}
        |
        |class AA extends A
      """
  
    TypeAnnotationSettings.set(getProject, TypeAnnotationSettings.alwaysAddType(ScalaCodeStyleSettings.getInstance(getProject)))
    checkIntentionIsNotAvailable(text)
  }

  def testUnitReturn() {
    val text =
      """
        |trait A {
        |  def <caret>f
        |}
        |
        |class AA extends A
      """
    val result =
      s"""
        |trait A {
        |  def f
        |}
        |
        |class AA extends A {
        |  override def f: Unit = $START???$END
        |}"""
  
    TypeAnnotationSettings.set(getProject, TypeAnnotationSettings.alwaysAddType(ScalaCodeStyleSettings.getInstance(getProject)))
    doTest(text, result)
  }

}
