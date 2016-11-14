package com.github.kczulko.isc.dhcp

import com.github.kczulko.isc.dhcp.grammars.LeasesGrammar

object Main {
  val leaseEntry =
    """
      |# first line comment
      |# second line comment
      |lease 110.31.40.13 {
      |  starts 2 2016/10/18 10:16:46;
      |  ends 2 2016/10/18 10:21:46;
      |  cltt 2 2016/10/18 10:16:46;
      |  binding state active;
      |  next binding state free;
      |  rewind binding state free;
      |  hardware ethernet 54:ab:aa:36:b4:e1;
      |  client-hostname "other";
      |}fdsaasdf
      |server-duid "doesnotmatter";
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
    """.stripMargin

  def main(args: Array[String]): Unit = {
    val grammar: LeasesGrammar = new LeasesGrammar
    println(leaseEntry)
    println(
      grammar.parseAll(grammar.leases, leaseEntry /*+ "whatever can be here"*/)
    )
  }
}
