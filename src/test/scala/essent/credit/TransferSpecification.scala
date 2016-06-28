package essent.credit

import org.scalacheck.Gen
import org.scalatest.PropSpec
import org.scalatest.prop.PropertyChecks
import org.scalatest.Matchers._
import org.scalatest.prop.Tables.Table

class TransferSpecification extends PropSpec with PropertyChecks {

  import TransferSpecification.Assumptions._

  property("transfer should be constructable with valid parameters") {
    forAll (validValues, validIBANs, validIBANs) {
      (value: Amount, source: IBAN, target: IBAN) => {
        whenever(source != target) {
          val transfer = Transfer(value, source, target, "date", "reference")
          transfer.value should be > zeroValue
          transfer.source should not be transfer.target
        }
      }
    }
  }

  property("value should be non-zero, positive") {
    forAll (invalidValues) { (value: Amount) =>
      an[IllegalArgumentException] should be thrownBy {
        Transfer(value, "source", "target", "date", "reference")
      }
    }
  }

  property("source and target should not be equal") {
    forAll (validIBANs) { (iban: IBAN) =>
      an[IllegalArgumentException] should be thrownBy {
        Transfer(unitValue, iban, iban, "date", "reference")
      }
    }
  }
}

object TransferSpecification {

  object Assumptions {

    /** assume zero value as an amount of 0.00 euro */
    val zeroValue: Amount = 0

    /** assume unit value as an amount of 0.01 euro */
    val unitValue: Amount = 1

    /** assume transfer values in the range of 0,01 to 1.000.000,00 euro */
    val validValues: Gen[Amount] = Gen.choose[Amount](unitValue, 100000000)

    /** assume typically known invalid values to be: */
    val invalidValues = Table[Amount]("value", 0, -1, Long.MinValue)

    /** assume transfers between bank accounts located in either the BE, NL or DE */
    val bankAccountCountries: Seq[IBAN] = Array("BE", "NL", "DE")

    // TODO generate valid IBANs
    val validIBANs: Gen[IBAN] = Gen.oneOf(bankAccountCountries)
  }
}
