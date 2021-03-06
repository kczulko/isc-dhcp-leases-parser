Table of contents
=================

* [Introduction](#isc-dhcp-leases-parser)
* [Mapping example](#mapping-example)
* [Usage example](#usage-example)
* [Installation](#installation)

isc-dhcp-leases-parser
======================

[![Build Status](https://travis-ci.org/kczulko/isc-dhcp-leases-parser.svg?branch=master)](https://travis-ci.org/kczulko/isc-dhcp-leases-parser)

Simple isc-dhcp-leases file parser written in scala. Utilizes combinator-parsing module from `scala.util`. It allows to map content of `/var/lib/dhcp/dhcpd.leases` file into simple scala's case classes structure.

Mapping example
---------------

Library allows to map `dhcpd.leases` content like:

```
lease 110.31.40.13 {
  starts 2 2016/10/18 10:16:46;
  ends 2 2016/10/18 10:21:46;
  cltt 2 2016/10/18 10:16:46;
  binding state active;
  next binding state free;
  rewind binding state free;
  hardware ethernet 54:ab:aa:36:b4:e1;
  client-hostname "other";
}
lease 103.32.10.93 {
  starts 2 2016/10/18 10:17:05;
  ends 2 2016/10/18 10:22:05;
  cltt 2 2016/10/18 10:17:05;
  binding state active;
  next binding state free;
  rewind binding state free;
  hardware ethernet c0:aa:d5:65:cc:f4;
  set mac_addr = "c0:aa:d5:65:cc:f4";
  set ip_addr = "102.31.50.97";
  set lease_hostname = "abc";
  client-hostname "abc";
  on expiry {
    execute ("/usr/bin/python", "/home/kczulko/script.py", "--param", ip_addr, "--otherParam", "expiry", "--hostname", lease_hostname, "--mac-address", mac_addr);
  }
  on release {
    execute ("/usr/bin/python", "/home/kczulko/script.py", "--param", ip_addr, "--otherParam", "release", "--hostname", lease_hostname, "--mac-address", mac_addr);
  }
}
```

into following scala's case classes hierarchy:

```scala
Result(
  leases = Seq(
    Lease(
      ip = Ip("110.31.40.13"),
      notifications = List(
        Notification("starts", Map(
          "id" -> "2",
          "date" -> "2016/10/18",
          "time" -> "10:16:46"
        )),
        Notification("ends", Map(
          "id" -> "2",
          "date" -> "2016/10/18",
          "time" -> "10:21:46"
        )),
        Notification("cltt", Map(
          "id" -> "2",
          "date" -> "2016/10/18",
          "time" -> "10:16:46"
        ))
      ),
      bindingState = Some(BindingState("active")),
      extendedBindingStates = List(
        ExtendedBindingState("next", "free"),
        ExtendedBindingState("rewind", "free")
      ),
      hardwareEthernet = Some(HardwareEthernet("54:ab:aa:36:b4:e1")),
      clientHostname = Some(ClientHostname("\"other\""))
    ),
    Lease(
      ip = Ip("103.32.10.93"),
      notifications = List(
        Notification("starts", Map(
          "id" -> "2",
          "date" -> "2016/10/18",
          "time" -> "10:17:05"
        ))
      ),
      bindingState = Some(BindingState("active")),
      extendedBindingStates = List(
        ExtendedBindingState("next", "free"),
        ExtendedBindingState("rewind", "free")
      ),
      hardwareEthernet = Some(HardwareEthernet("c0:aa:d5:65:cc:f4")),
      variables = Set(
        Variable("""mac_addr = "c0:aa:d5:65:cc:f4""""),
        Variable("""ip_addr = "102.31.50.97""""),
        Variable("""lease_hostname = "abc"""")
      ),
      clientHostname = Some(ClientHostname("\"abc\"")),
      onEvent = List(
        OnEvent("expiry", List(
          """execute ("/usr/bin/python", "/home/kczulko/script.py", "--param", ip_addr, "--otherParam", "expiry", "--hostname", lease_hostname, "--mac-address", mac_addr)"""
        )),
        OnEvent("release", List(
          """execute ("/usr/bin/python", "/home/kczulko/script.py", "--param", ip_addr, "--otherParam", "release", "--hostname", lease_hostname, "--mac-address", mac_addr)"""
        ))
      )
    )
  ),
  serverDuid = None
)
```

Parser is aware of following tokens within simple lease entry:

1. `starts`, `ends`, `cltt`, `tstp` mapped to `Notification` class
1. `binding state` mapped to `BindingState` class 
1. `(next | rewind) binding state` mapped to `ExtendedBindingState` class 
1. `hardware ethernet` mapped to `HardwareEthernet` class 
1. `set` mapped to `Set[String]` collection
1. `client-hostname` mapped to `ClientHostname` class
1. `on (expiry | release | commit)` mapped to `OnEvent` class

**Tokens other than mentioned above will be ignored**.

On the other hand, parser expects only `lease` or `server-duid` tokens on the root level of `dhcpd.leases` file content.

Usage example
-------------

With a little help of [scala-arm](https://github.com/jsuereth/scala-arm).

```scala

import com.github.kczulko.isc.dhcp.Grammar
import resource.managed
import scala.io.Source

object Main {
  def main(args: Array[String]) =
    for { file <- managed(Source.fromFile("/var/lib/dhcp/dhcpd.leases")) }
      Grammar(file getLines() mkString "\n") match {
        case Right(result) => // launch your rocket here
        case _ => // oops!!! open pull request ;)
      }
}

```

Installation
------------

Currently available only snapshot version for scala 2.12.

```scala
resolvers ++= Seq(
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
)

libraryDependencies ++= Seq(
  "com.github.kczulko" %% "isc-dhcp-leases-parser" % "1.0-SNAPSHOT"
)
```
