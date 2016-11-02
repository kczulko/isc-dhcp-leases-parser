package com.kczulko.isc.dhcp.grammars

import com.kczulko.isc.dhcp.parsers.RegexMatchedParser

import scala.util.matching.Regex
import scala.util.parsing.combinator.Parsers

private[grammars] trait IscDhcpLeasesTokens
    extends Parsers
    with RegexMatchedParser {
  def DATE_LITERAL_REGEX: Parser[String] = """\d{4}/\d{2}/\d{2}""".r
  def HOUR_LITERAL_REGEX: Parser[String] = """\d{2}:\d{2}:\d{2}""".r
  def MAC_ADDRESS_REGEX: Parser[String] =
    """([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})""".r
  def IP_ADDRESS_REGEX: Parser[String] = """(?:[0-9]{1,3}\.){3}[0-9]{1,3}""".r
  def WHATEVER_REGEX: Parser[String] =
    regexMatchedParser(new Regex(s"""(.*)?(;)""", "content", "semicolon")) ^^
      (_.group("content"))
}
