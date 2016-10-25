package com.kczulko.isc.dhcp.grammars

import com.kczulko.isc.dhcp.model._
import org.scalatest.{FlatSpec, Inside, Matchers}

class IscDhcpLeasesGrammarTest extends FlatSpec with Matchers with Inside {

  val grammar: IscDhcpLeasesGrammar = new IscDhcpLeasesGrammar

  it should "parse valid entry without some data" in {
    val entry =
      """
        |lease 110.31.40.13 {
        |  binding state active;
        |  next binding state free;
        |  rewind binding state free;
        |  hardware ethernet 54:ab:aa:36:b4:e1;
        |  client-hostname "other";
        |}
      """.stripMargin

    val result = grammar.parseAll(grammar.leases, entry)

    result.successful shouldBe true
    inside(result.get) { case Result(leases, serverDuid) =>
        serverDuid shouldEqual None
        leases should have length 1
        leases should contain only (
          Lease(
            ip = Ip("110.31.40.13"),
            bindingState = Some(BindingState("active")),
            extendedBindingStates = List(
              ExtendedBindingState("next", "free"),
              ExtendedBindingState("rewind", "free")
            ),
            hardwareEthernet = Some(HardwareEthernet("54:ab:aa:36:b4:e1")),
            clientHostname = Some(ClientHostname("\"other\""))
          )
        )
    }
  }
}
