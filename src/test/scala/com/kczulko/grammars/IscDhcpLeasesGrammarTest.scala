package com.kczulko.grammars

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
//
//    result.get should contain theSameElementsAs List(
//      "110.31.40.13" -> List(
//        "starts" -> Map(
//          "id" -> 2,
//          "date" -> "2016/10/18",
//          "time" -> "10:16:46"
//        ),
//        "ends" -> Map(
//          "id" -> 2,
//          "date" -> "2016/10/19",
//          "time" -> "10:16:47"
//        ),
//        "cltt" -> Map(
//          "id" -> 2,
//          "date" -> "2016/10/20",
//          "time" -> "10:16:48"
//        ),
//        "next-binding-state" -> "free",
//        "binding-state" -> "active",
//        "rewind-binding-state" -> "free",
//        "hardware-ethernet" -> "54:ab:aa:36:b4:e1",
//        "client-hostname" -> "\"other\""
//      )
//    )
  }
}
