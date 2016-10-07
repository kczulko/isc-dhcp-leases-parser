package kczulko

import scala.util.parsing.combinator._

object Element extends Enumeration {
  val interface = Value("interface")
  val fixed_address = Value("fixed_address")
  val filename = Value("filename")
  val option = Value("option")
  val renew = Value("renew")
  val rebind = Value("rebind")
  val expire = Value("expire")
}

class Grammar extends JavaTokenParsers {

  def leases : Parser[Any] = rep(lease)
  private def lease : Parser[Any] = "lease"~"{"~rep(element)~"}"
  private def element : Parser[Any] = (
        interface
      | fixed_address
      | filename
      | option
      | renew
      | rebind
      | expire
    )~";"

  private def interface : Parser[Any] = "interface"~stringLiteral
  private def fixed_address : Parser[Any] = "fixed-address"~"""[\d\.]+""".r
  private def filename : Parser[Any] = "filename"~stringLiteral

  private def option : Parser[Any] = "option"~optionElement~repsep("""["\d\w\.-]+""".r, ",")
  private def optionElement : Parser[Any] = (
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
  )

  private def renew : Parser[Any] = "renew"~decimalNumber~dateLiteral~hourLiteral
  private def rebind : Parser[Any] = "rebind"~decimalNumber~dateLiteral~hourLiteral
  private def expire : Parser[Any] = "expire"~decimalNumber~dateLiteral~hourLiteral

  private def dateLiteral = """\d{4}/\d{2}/\d{2}""".r
  private def hourLiteral = """\d{2}:\d{2}:\d{2}""".r
}
