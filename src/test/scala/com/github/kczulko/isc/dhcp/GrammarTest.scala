package com.github.kczulko.isc.dhcp

import com.github.kczulko.isc.dhcp.data.MultipleLeases._
import com.github.kczulko.isc.dhcp.data.SingleLeaseWithServerDuid._
import com.github.kczulko.isc.dhcp.data.SingleLeaseWithoutSomeData._
import com.github.kczulko.isc.dhcp.model.Result
import org.scalatest.{EitherValues, FlatSpec, Inside, Matchers}

class GrammarTest
    extends FlatSpec
    with Matchers
    with Inside
    with EitherValues {

  val grammar = new Grammar

  "leases parser" should "parse valid single entry without some data" in {
    inside(Grammar(singleLeaseWithoutSomeData._1).right.value) {
      case Result(leases, serverDuid) =>
        serverDuid shouldEqual None
        leases should have length 1
        leases should contain only
          singleLeaseWithoutSomeData._2
    }
  }

  it should "parse multiple entries" in {
    inside(Grammar(multipleLeases._1).right.value) {
      case Result(leases, serverDuid) =>
        serverDuid shouldEqual None
        leases.length shouldEqual multipleLeases._2.length
        leases should contain theSameElementsAs multipleLeases._2
    }
  }

  it should "parse entry with server-duid" in {
    inside(Grammar(singleLeaseWithServerDuid._1).right.value) {
      case result @ Result(leases, serverDuid) =>
        result shouldEqual singleLeaseWithServerDuid._2
    }
  }

  it should "not fail when unknow entry appears in file stream" in {
    Grammar(singleLeaseWithServerDuid._1 + "\n whatever can be here\n").isRight shouldBe true
  }

  it should "not fail when comment line appears in file stream" in {
    Grammar("# any comment here\n" + singleLeaseWithServerDuid._1).isRight shouldBe true
  }
}
