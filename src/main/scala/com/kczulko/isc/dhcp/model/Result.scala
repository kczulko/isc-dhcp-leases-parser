package com.kczulko.isc.dhcp.model

case class Result(leases: List[Lease] = List(),
                  serverDuid: Option[ServerDuid] = None)
