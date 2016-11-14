package com.github.kczulko.isc.dhcp.model

case class Lease(ip: Ip,
                 uid: Option[Uid] = None,
                 bindingState: Option[BindingState] = None,
                 clientHostname: Option[ClientHostname] = None,
                 hardwareEthernet: Option[HardwareEthernet] = None,
                 onEvent: List[OnEvent] = List(),
                 variables: Set[Variable] = Set(),
                 notifications: List[Notification] = List(),
                 extendedBindingStates: List[ExtendedBindingState] = List())
    extends Item

case class Ip(ip: String) extends Item
case class Uid(uid: String) extends Item
case class Variable(item: String) extends Item
case class BindingState(state: String) extends Item
case class ClientHostname(hostname: String) extends Item
case class HardwareEthernet(hardwareEthernet: String) extends Item
case class OnEvent(eventName: String, commands: List[String]) extends Item
case class ExtendedBindingState(`type`: String, state: String) extends Item
case class Notification(name: String, info: Map[String, String]) extends Item
