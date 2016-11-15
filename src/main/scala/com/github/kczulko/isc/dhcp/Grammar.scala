package com.github.kczulko.isc.dhcp

import com.github.kczulko.isc.dhcp.model.Lease.toLease
import com.github.kczulko.isc.dhcp.model.Result.toResult
import com.github.kczulko.isc.dhcp.model.ServerDuid.toServerDuid
import com.github.kczulko.isc.dhcp.model.Unknown.toUnknown
import com.github.kczulko.isc.dhcp.model._

import scala.util.parsing.combinator._

class Grammar
    extends RegexParsers
    with Tokens
    with JavaTokenParsers {

  def leases: Parser[Result] =
    rep(
      lease
        | server_duid
        | unknown
    ) ^^ toResult

  private def unknown: Parser[Item] = """(.*)?\n""".r ^^ toUnknown

  private def server_duid: Parser[ServerDuid] =
    "server-duid" ~ WHATEVER_REGEX ^^ toServerDuid

  private def lease: Parser[Lease] =
    "lease" ~ IP_ADDRESS_REGEX ~ "{" ~ rep(element) ~ "}" ^^ {
      case _ ~ ip ~ _ ~ elems ~ _ => toLease(Ip(ip), elems)
    }

  private def element: Parser[Item] =
    (
      notification
        | bindingState
        | extendedBindingState
        | hardwareEthernet
        | clientHostname
        | set
        | uid
        | onEvent
        | other
    ) <~ ";?".r

  private def other: Parser[Item] = WHATEVER_REGEX ^^ toUnknown

  private def onEvent: Parser[OnEvent] =
    "on" ~ (
      "expiry"
        | "release"
        | "commit"
    ) ~ "{" ~ rep(WHATEVER_REGEX) ~ "}" ^^ {
      case _ ~ event ~ _ ~ cmd ~ _ => OnEvent(event, cmd)
    }

  private def notification: Parser[Notification] =
    (
      "starts"
        | "ends"
        | "cltt"
        | "tstp"
    ) ~ decimalNumber ~ DATE_LITERAL_REGEX ~ HOUR_LITERAL_REGEX ^^ {
      case name ~ id ~ date ~ time =>
        Notification(name, Map("id" -> id, "date" -> date, "time" -> time))
    }

  private def bindingState: Parser[BindingState] =
    "binding" ~ "state" ~ WHATEVER_REGEX ^^ {
      case _ ~ _ ~ option => BindingState(option)
    }
  private def extendedBindingState: Parser[ExtendedBindingState] =
    ("next" | "rewind") ~ bindingState ^^ {
      case exOption ~ option =>
        ExtendedBindingState(`type` = exOption, option.state)
    }
  private def hardwareEthernet: Parser[HardwareEthernet] =
    "hardware" ~ "ethernet" ~ MAC_ADDRESS_REGEX ^^ {
      case _ ~ mac => HardwareEthernet(mac)
    }
  private def clientHostname: Parser[ClientHostname] =
    "client-hostname" ~ WHATEVER_REGEX ^^ {
      case _ ~ name => ClientHostname(name)
    }
  private def set: Parser[Variable] = "set" ~ WHATEVER_REGEX ^^ {
    case _ ~ item => Variable(item)
  }
  private def uid: Parser[Uid] = "uid" ~ WHATEVER_REGEX ^^ {
    case _ ~ uid => Uid(uid)
  }

}
