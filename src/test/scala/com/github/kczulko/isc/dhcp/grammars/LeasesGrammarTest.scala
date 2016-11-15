package com.github.kczulko.isc.dhcp.grammars

import TestData.{multipleLeases, singleLeaseWithServerDuid, singleLeaseWithoutSomeData}
import com.github.kczulko.isc.dhcp.model.Result
import org.scalatest.{FlatSpec, Inside, Matchers}

class LeasesGrammarTest extends FlatSpec with Matchers with Inside {

  val grammar = new LeasesGrammar

  "leases parser" should "parse valid single entry without some data" in {
    val result =
      grammar.parseAll(grammar.leases, singleLeaseWithoutSomeData._1)

    result.successful shouldBe true
    inside(result.get) {
      case Result(leases, serverDuid) =>
        serverDuid shouldEqual None
        leases should have length 1
        leases should contain only
          singleLeaseWithoutSomeData._2
    }
  }

  it should "parse multiple entries" in {
    val result = grammar.parseAll(grammar.leases, multipleLeases._1)

    result.successful shouldBe true
    inside(result.get) {
      case Result(leases, serverDuid) =>
        serverDuid shouldEqual None
        leases.length shouldEqual multipleLeases._2.length
        leases should contain theSameElementsAs multipleLeases._2
    }
  }

  it should "parse entry with server-duid" in {
    val result = grammar.parseAll(grammar.leases, singleLeaseWithServerDuid._1)

    result.successful shouldBe true
    inside(result.get) {
      case result @ Result(leases, serverDuid) =>
        result shouldEqual singleLeaseWithServerDuid._2
    }
  }

  it should "not fail when unknow entry appears in file stream" in {
    val entry = (
      singleLeaseWithServerDuid._1 + "\n whatever can be here\n",
      singleLeaseWithServerDuid._2
    )

    val result = grammar.parseAll(grammar.leases, entry._1)
    result.successful shouldBe true
  }
}