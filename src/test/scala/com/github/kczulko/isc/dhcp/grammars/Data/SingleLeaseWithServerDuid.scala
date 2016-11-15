package com.github.kczulko.isc.dhcp.grammars.Data

import com.github.kczulko.isc.dhcp.model.{Result, ServerDuid}
import com.github.kczulko.isc.dhcp.grammars.Data.SingleLeaseWithoutSomeData.singleLeaseWithoutSomeData

object SingleLeaseWithServerDuid {
  val singleLeaseWithServerDuid = (
    """server-duid "can be whatever";""" + singleLeaseWithoutSomeData._1,
    Result(List(singleLeaseWithoutSomeData._2), Some(ServerDuid(""""can be whatever"""")))
    )
}
