package com.github.kczulko.isc.dhcp.data

import com.github.kczulko.isc.dhcp.model.{ClientHostname, _}

object SingleLeaseWithoutSomeData {
  val singleLeaseWithoutSomeData = ("""
                                      |lease 110.31.40.13 {
                                      |  binding state active;
                                      |  next binding state free;
                                      |  rewind binding state free;
                                      |  hardware ethernet 54:ab:aa:36:b4:e1;
                                      |  client-hostname "other";
                                      |}
                                    """.stripMargin,
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
