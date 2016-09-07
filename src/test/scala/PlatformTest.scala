package easy

import akka.actor.ActorSystem
import org.scalatest.FunSpec
import org.scalatest.Matchers._

class PlatformTest extends FunSpec {

  describe("The runtime platform") {
    it("should have the correct Java version") {
      System.getProperty("java.version") should startWith("1.8.0")
    }
    it("should have the correct Scala version") {
      scala.util.Properties.versionNumberString should be("2.11.8")
    }
    it("should have the correct Akka version") {
      ActorSystem.Version should be("2.4.0")
    }
  }
}
