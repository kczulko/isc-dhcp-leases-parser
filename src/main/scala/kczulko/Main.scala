package kczulko

object Main {
  val leaseEntry =
    """
        lease {
          interface "eno1";
          fixed-address 10.91.48.49;
          filename " ";
          option subnet-mask 255.255.254.0;
          option routers 10.91.48.1;
          option dhcp-lease-time 302400;
          option dhcp-message-type 5;
          option domain-name-servers 172.28.168.170,10.248.2.1,163.33.253.76;
          option dhcp-server-identifier 172.28.170.7;
          option ntp-servers 172.28.168.170,172.28.168.40,172.28.168.1;
          option broadcast-address 10.91.49.255;
          option host-name "somename-48-049";
          option netbios-name-servers 10.125.144.16,163.33.7.86;
          option domain-name "domain.kczulko.com";
          renew 0 2016/08/07 04:48:22;
          rebind 1 2016/08/08 13:35:06;
          expire 2 2016/08/09 00:05:06;
        }""".stripMargin

  def main(args: Array[String]): Unit = {
    val syntax: Syntax = new Syntax
    println(leaseEntry)
    println(syntax.parseAll(syntax.lease, leaseEntry))
  }

}
