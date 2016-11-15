package com.github.kczulko.isc.dhcp.model

import com.github.kczulko.isc.dhcp.grammars.LeasesGrammar

case class ServerDuid(duid: String) extends Item

object ServerDuid {
  type `~`[A,B] = LeasesGrammar#`~`[A,B]
  def toServerDuid(sequence: `~`[Any, String]): ServerDuid = ServerDuid(sequence._2)
}
