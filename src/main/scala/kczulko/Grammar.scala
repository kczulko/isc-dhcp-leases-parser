package kczulko

import kczulko.parsers.RegexMatchedParser

import scala.util.matching.Regex
import scala.util.parsing.combinator._

sealed trait LeasesTokens extends Parsers with RegexMatchedParser {
  def DATE_LITERAL_REGEX: Parser[String] = """\d{4}/\d{2}/\d{2}""".r
  def HOUR_LITERAL_REGEX: Parser[String] = """\d{2}:\d{2}:\d{2}""".r
  def MAC_ADDRESS_REGEX: Parser[String] = """([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})""".r
  def IP_ADDRESS_REGEX: Parser[String] = """(?:[0-9]{1,3}\.){3}[0-9]{1,3}""".r
  def WHATEVER_REGEX: Parser[String] = regexMatchedParser(new Regex(s"""(.*)?(;)""", "content", "semicolon")) ^^
    (_.group("content"))
}

class Grammar extends RegexParsers with LeasesTokens with JavaTokenParsers {

  implicit def `~toTupleParser`[T, U](p: Parser[~[T, U]]): Parser[(T, U)] = p ^^ (v => (v._1, v._2))

  def leases: Parser[List[Any]] = rep(lease)

  private def lease: Parser[(String, Map[String, Any])] = "lease"~IP_ADDRESS_REGEX~"{"~rep(element)~"}" ^^
    { case _~ip~_~elem~_ => (ip, Map() ++ elem)}

  private def element: Parser[(String, Any)] = (
        notifications
      | bindingState
      | extendedBindingState
      | hardwareEthernet
      | clientHostname
      | set
      | uid
      | onEvent
      | unknown
    )<~";?".r

  private def unknown: Parser[(String, String)] = WHATEVER_REGEX ^^ ((_, "unknown"))

  private def onEvent: Parser[(String, List[Any])] = "on"~(
        "expiry"
      | "release"
      | "commit"
    )~"{"~rep(WHATEVER_REGEX)~"}" ^^ { case on~event~_~cmd~_ => (List(on,event).mkString("-"), cmd) }

  private def notifications: Parser[(String, Map[String,Any])] = (
        "starts"
      | "ends"
      | "cltt"
      | "tstp"
    )~decimalNumber~DATE_LITERAL_REGEX~HOUR_LITERAL_REGEX ^^
    { case name~id~date~hour => (name, Map("id" -> id, "date" -> date, "hour" -> hour)) }

  private def bindingState: Parser[(String, String)] = "binding"~"state"~WHATEVER_REGEX ^^
    { case b~s~option => (List(b,s).mkString("-") , option) }
  private def extendedBindingState: Parser[(String, String)] = ("next" | "rewind")~bindingState ^^
    { case exOption~option => (List(exOption,option._1).mkString("-"), option._2) }
  private def hardwareEthernet: Parser[(String, String)] = "hardware"~"ethernet"~MAC_ADDRESS_REGEX ^^
    { case _~mac => ("hardwareEthernet", mac) }
  private def clientHostname: Parser[(String, Any)] = "client-hostname"~WHATEVER_REGEX
  private def set: Parser[(String, String)] = "set"~WHATEVER_REGEX
  private def uid: Parser[(String, String)] = "uid"~WHATEVER_REGEX

}
