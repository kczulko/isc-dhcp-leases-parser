package com.kczulko.isc.dhcp.grammars

import com.kczulko.isc.dhcp.model.{ClientHostname, _}


object TestData {
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
                                  ))

  val multipleLeases = ("""
                         |lease 110.31.40.13 {
                         |  starts 2 2016/10/18 10:16:46;
                         |  ends 2 2016/10/18 10:21:46;
                         |  cltt 2 2016/10/18 10:16:46;
                         |  binding state active;
                         |  next binding state free;
                         |  rewind binding state free;
                         |  hardware ethernet 54:ab:aa:36:b4:e1;
                         |  client-hostname "other";
                         |}
                         |lease 103.32.10.93 {
                         |  starts 2 2016/10/18 10:17:05;
                         |  ends 2 2016/10/18 10:22:05;
                         |  cltt 2 2016/10/18 10:17:05;
                         |  binding state active;
                         |  next binding state free;
                         |  rewind binding state free;
                         |  hardware ethernet c0:aa:d5:65:cc:f4;
                         |  set mac_addr = "c0:aa:d5:65:cc:f4";
                         |  set ip_addr = "102.31.50.97";
                         |  set lease_hostname = "abc";
                         |  client-hostname "abc";
                         |  on expiry {
                         |    execute ("/usr/bin/python", "/home/com.kczulko/script.py", "--param", ip_addr, "--otherParam", "expiry", "--hostname", lease_hostname, "--mac-address", "00:00:00:00:00:00");
                         |  }
                         |  on release {
                         |    execute ("/usr/bin/python", "/home/com.kczulko/script.py", "--param", ip_addr, "--otherParam", "release", "--hostname", lease_hostname, "--mac-address", mac_addr);
                         |  }
                         |}
                       """.stripMargin,
                        Seq(
                          Lease(
                            ip = Ip("110.31.40.13"),
                            notifications = List(
                              Notification("starts", Map(
                                "id" -> "2",
                                "date" -> "2016/10/18",
                                "time" -> "10:16:46"
                              )),
                              Notification("ends", Map(
                                "id" -> "2",
                                "date" -> "2016/10/18",
                                "time" -> "10:21:46"
                              )),
                              Notification("cltt", Map(
                                "id" -> "2",
                                "date" -> "2016/10/18",
                                "time" -> "10:16:46"
                              ))
                            ),
                            bindingState = Some(BindingState("active")),
                            extendedBindingStates = List(
                              ExtendedBindingState("next", "free"),
                              ExtendedBindingState("rewind", "free")
                            ),
                            hardwareEthernet = Some(HardwareEthernet("54:ab:aa:36:b4:e1")),
                            clientHostname = Some(ClientHostname("\"other\""))
                          ),
                          Lease(
                            ip = Ip("103.32.10.93"),
                            notifications = List(
                              Notification("starts", Map(
                                "id" -> "2",
                                "date" -> "2016/10/18",
                                "time" -> "10:17:05"
                              )),
                              Notification("ends", Map(
                                "id" -> "2",
                                "date" -> "2016/10/18",
                                "time" -> "10:22:05"
                              )),
                              Notification("cltt", Map(
                                "id" -> "2",
                                "date" -> "2016/10/18",
                                "time" -> "10:17:05"
                              ))
                            ),
                            bindingState = Some(BindingState("active")),
                            extendedBindingStates = List(
                              ExtendedBindingState("next", "free"),
                              ExtendedBindingState("rewind", "free")
                            ),
                            hardwareEthernet = Some(HardwareEthernet("c0:aa:d5:65:cc:f4")),
                            variables = Set(
                              Variable("""mac_addr = "c0:aa:d5:65:cc:f4""""),
                              Variable("""ip_addr = "102.31.50.97""""),
                              Variable("""lease_hostname = "abc"""")
                            ),
                            clientHostname = Some(ClientHostname(""""abc"""")),
                            onEvent = List(
                              OnEvent("expiry", List(
                                """execute ("/usr/bin/python", "/home/com.kczulko/script.py", "--param", ip_addr, "--otherParam", "expiry", "--hostname", lease_hostname, "--mac-address", "00:00:00:00:00:00")"""
                              )),
                              OnEvent("release", List(
                                """execute ("/usr/bin/python", "/home/com.kczulko/script.py", "--param", ip_addr, "--otherParam", "release", "--hostname", lease_hostname, "--mac-address", mac_addr)"""
                              ))
                            )
                          )
                        )
                      )

  val singleLeaseWithServerDuid = (
      """server-duid "can be whatever";""" + singleLeaseWithoutSomeData._1,
      Result(List(singleLeaseWithoutSomeData._2), Some(ServerDuid(""""can be whatever"""")))
    )
}
