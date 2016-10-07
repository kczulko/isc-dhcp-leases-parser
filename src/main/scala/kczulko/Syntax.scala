package kczulko

import scala.util.parsing.combinator._

class Syntax extends JavaTokenParsers {
  def lease : Parser[Any] = "lease"~"{"~rep(element)~"}"
  def element : Parser[Any] = (
        interface
      | fixed_address
      | filename
      | option
      | renew
      | rebind
      | expire
    )~";"

  def interface : Parser[Any] = "interface"~stringLiteral
  def fixed_address : Parser[Any] = "fixed-address"~"""[\d\.]+""".r
  def filename : Parser[Any] = "filename"~stringLiteral
  def option : Parser[Any] = "option"~(
        "subnet-mask"
      | "routers"
      | "dhcp-lease-time"
      | "dhcp-message-type"
      | "domain-name-servers"
      | "dhcp-server-identifier"
      | "ntp-servers"
      | "broadcast-address"
      | "host-name"
      | "netbios-name-servers"
      | "domain-name"
    )~(
        repsep("""["\d\w\.-]+""".r, ",")
    )
  def renew : Parser[Any] = "renew"~decimalNumber~dateLiteral~hourLiteral
  def rebind : Parser[Any] = "rebind"~decimalNumber~dateLiteral~hourLiteral
  def expire : Parser[Any] = "expire"~decimalNumber~dateLiteral~hourLiteral

  def dateLiteral = """\d{4}/\d{2}/\d{2}""".r
  def hourLiteral = """\d{2}:\d{2}:\d{2}""".r
}
