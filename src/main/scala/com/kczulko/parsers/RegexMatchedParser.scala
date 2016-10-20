package com.kczulko.parsers

import scala.util.matching.Regex
import scala.util.matching.Regex.Match
import scala.util.parsing.combinator.RegexParsers

trait RegexMatchedParser extends RegexParsers {
  def regexMatchedParser(r: Regex): Parser[Match] = new Parser[Match] {
    override def apply(in: Input): ParseResult[Match] = {
      val source = in.source
      val offset = in.offset
      val start = handleWhiteSpace(source, offset)
      r findPrefixMatchOf source.subSequence(start, source.length) match {
        case Some(matched) => Success(matched, in.drop(start + matched.end - offset))
        case None => Failure(
          s"string matching regex `$r' expected but `${in.first}' found", in.drop(start - offset)
        )
      }
    }
  }
}
