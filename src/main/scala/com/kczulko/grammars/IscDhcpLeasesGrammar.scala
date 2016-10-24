package com.kczulko.grammars

import com.kczulko.model._
import scala.util.parsing.combinator._

class IscDhcpLeasesGrammar extends RegexParsers with IscDhcpLeasesTokens with JavaTokenParsers {

  private implicit def `~toTupleParser`[T, U](p: Parser[~[T, U]]): Parser[(T, U)] = p ^^ (v => (v._1, v._2))

  def leases: Parser[Any] = rep(
        comment
      | lease
      | service_duid
  )

  private def comment: Parser[String] = """#(.*)?""".r

  private def service_duid: Parser[(String, String)] = "service-duid"~WHATEVER_REGEX

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

  private def element: Parser[LeaseToken] = (
        notification
      | bindingState
      | extendedBindingState
      | hardwareEthernet
      | clientHostname
      | set
      | uid
      | onEvent
      | unknown
    )<~";?".r

  private def unknown: Parser[LeaseToken] = WHATEVER_REGEX ^^ { case _ => Unknown }

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
