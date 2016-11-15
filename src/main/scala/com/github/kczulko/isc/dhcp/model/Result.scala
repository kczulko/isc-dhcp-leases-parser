package com.github.kczulko.isc.dhcp.model

case class Result(leases: List[Lease] = List(),
                  serverDuid: Option[ServerDuid] = None)

object Result {
  def toResult(items: List[Item]): Result = items.foldRight(Result()) { (item, result) =>
    item match {
      case l: Lease => result.copy(leases = l :: result.leases)
      case s: ServerDuid => result.copy(serverDuid = Some(s))
      case _ => result
    }
  }
}
