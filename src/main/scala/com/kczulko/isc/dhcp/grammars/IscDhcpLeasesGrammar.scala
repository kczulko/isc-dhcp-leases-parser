package com.kczulko.isc.dhcp.grammars

import com.kczulko.isc.dhcp.model._
import scala.util.parsing.combinator._

class IscDhcpLeasesGrammar extends RegexParsers with IscDhcpLeasesTokens with JavaTokenParsers {

  def leases: Parser[Result] = rep(
        lease
      | server_duid
      | unknown
  ) ^^
    {
      case list =>
        list.foldRight(Result()){(item, result) =>
          item match {
            case l: Lease => result.copy(leases = l :: result.leases)
            case s: ServerDuid => result.copy(serverDuid = Some(s))
            case _ => result
          }
        }
    }

  private def unknown: Parser[Item] = """(.*)?\n""".r ^^ { case _ => Unknown}

  private def server_duid: Parser[ServerDuid] = "server-duid"~WHATEVER_REGEX ^^
    { case _~duid => ServerDuid(duid) }

  private def lease: Parser[Lease] = "lease"~IP_ADDRESS_REGEX~"{"~rep(element)~"}" ^^
    { case _~ip~_~elems~_ =>
      elems.foldRight(Lease(Ip(ip)))((token, l) => token match {
        case uid @ Uid(_) => l.copy(uid = Some(uid))
        case bs @ BindingState(_) => l.copy(bindingState = Some(bs))
        case oe @ OnEvent(_,_) => l.copy(onEvent = oe :: l.onEvent)
        case v @ Variable(_) => l.copy(variables = l.variables + v)
        case ch @ ClientHostname(_) => l.copy(clientHostname = Some(ch))
        case he @ HardwareEthernet(_) => l.copy(hardwareEthernet = Some(he))
        case n @ Notification(_,_) => l.copy(notifications = n :: l.notifications)
        case ebs @ ExtendedBindingState(_, _) => l.copy(extendedBindingStates = ebs :: l.extendedBindingStates)
        case _ => l
      })
    }

  private def element: Parser[Item] = (
        notification
      | bindingState
      | extendedBindingState
      | hardwareEthernet
      | clientHostname
      | set
      | uid
      | onEvent
      | other
    )<~";?".r

  private def other: Parser[Item] = WHATEVER_REGEX ^^ { case _ => Unknown }

  private def onEvent: Parser[OnEvent] = "on"~(
        "expiry"
      | "release"
      | "commit"
    )~"{"~rep(WHATEVER_REGEX)~"}" ^^ { case _~event~_~cmd~_ => OnEvent(event, cmd) }

  private def notification: Parser[Notification] = (
        "starts"
      | "ends"
      | "cltt"
      | "tstp"
    )~decimalNumber~DATE_LITERAL_REGEX~HOUR_LITERAL_REGEX ^^
    { case name~id~date~time => Notification(name, Map("id" -> id, "date" -> date, "time" -> time)) }

  private def bindingState: Parser[BindingState] = "binding"~"state"~WHATEVER_REGEX ^^
    { case _~_~option => BindingState(option) }
  private def extendedBindingState: Parser[ExtendedBindingState] = ("next" | "rewind")~bindingState ^^
    { case exOption~option => ExtendedBindingState(`type` = exOption, option.state) }
  private def hardwareEthernet: Parser[HardwareEthernet] = "hardware"~"ethernet"~MAC_ADDRESS_REGEX ^^
    { case _~mac => HardwareEthernet(mac) }
  private def clientHostname: Parser[ClientHostname] = "client-hostname"~WHATEVER_REGEX ^^
    { case _~name => ClientHostname(name) }
  private def set: Parser[Variable] = "set"~WHATEVER_REGEX ^^
    { case _~item => Variable(item) }
  private def uid: Parser[Uid] = "uid"~WHATEVER_REGEX ^^
    { case _~uid => Uid(uid) }

}
