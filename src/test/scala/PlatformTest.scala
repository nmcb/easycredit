package easy

import akka.actor.ActorSystem
import org.scalatest._
import funspec._
import matchers.should.Matchers._

import org.scalatest.ScalaTestVersion
import org.scalactic.ScalacticVersion

class PlatformTest extends AnyFunSpec {

  describe("The runtime platform") {
    it("should have the correct Java version") {
      System.getProperty("java.version") should startWith("23.0.")
    }
    it("should have the correct Scala version") {
      scala.util.Properties.versionNumberString should startWith("2.13.")
    }
    it("should have the correct Akka version") {
      ActorSystem.Version should startWith("2.5.")
    }

    it("should have the correct ScalaTest version") {
      ScalaTestVersion should startWith ("3.2.")
    }
    it("should have the correct Scalactic version") {
      ScalacticVersion should startWith ("3.2.")
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
