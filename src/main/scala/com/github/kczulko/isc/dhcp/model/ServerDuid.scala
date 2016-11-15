package com.github.kczulko.isc.dhcp.model

import com.github.kczulko.isc.dhcp.Grammar

case class ServerDuid(duid: String) extends Item

object ServerDuid {
  type `~`[A,B] = Grammar#`~`[A,B]
  def toServerDuid(sequence: `~`[Any, String]): ServerDuid = ServerDuid(sequence._2)
}
