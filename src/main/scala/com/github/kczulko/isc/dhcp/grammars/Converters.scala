package com.github.kczulko.isc.dhcp.grammars

import com.github.kczulko.isc.dhcp.model._

object Converters {
  def toResult(items: List[Item]): Result = items.foldRight(Result()) { (item, result) =>
    item match {
      case l: Lease => result.copy(leases = l :: result.leases)
      case s: ServerDuid => result.copy(serverDuid = Some(s))
      case _ => result
    }
  }

  def toUnknown(any: Any): Item = Unknown

  def toServerDuid(sequence: LeasesGrammar#`~`[String, String]): ServerDuid = ServerDuid(sequence._2)

  def toLease = ???
}
