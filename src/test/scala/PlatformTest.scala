package easy

import akka.actor.ActorSystem
import org.scalatest.FunSpec
import org.scalatest.Matchers._

import org.scalatest.ScalaTestVersion
import org.scalactic.ScalacticVersion

class PlatformTest extends FunSpec {

  describe("The runtime platform") {
    it("should have the correct Java version") {
      System.getProperty("java.version") should startWith("1.8.0")
    }
    it("should have the correct Scala version") {
      scala.util.Properties.versionNumberString should be("2.12.20")
    }
    it("should have the correct Akka version") {
      ActorSystem.Version should be("2.5.0")
    }

    it("should have the correct ScalaTest version") {
      ScalaTestVersion should be ("3.0.9")
    }
    it("should have the correct Scalactic version") {
      ScalacticVersion should be ("3.0.1")
    }

    // it("should have the correct ScalaCheck version") {
    //   def urlses(cl: ClassLoader): Array[java.net.URL] = cl match {
    //     case null => Array()
    //     case u: java.net.URLClassLoader => u.getURLs() ++ urlses(cl.getParent)
    //     case _ => urlses(cl.getParent)
    //   }
    //   // scalacheck_2.12/jars/scalacheck_2.12-1.13.5
    //   val  urls = urlses(getClass.getClassLoader)
    //   println(urls.mkString("\n"))
    // }
  }
}
