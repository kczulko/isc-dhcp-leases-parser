package com.kczulko.model

case class Lease(ip: Ip,
                 uid: Option[Uid] = None,
                 bindingState: Option[BindingState] = None,
                 clientHostname: Option[ClientHostname] = None,
                 hardwareEthernet: Option[HardwareEthernet] = None,
                 onEvent: List[OnEvent] = List(),
                 variables: Set[Variable] = Set(),
                 notifications: List[Notification] = List(),
                 extendedBindingStates: List[ExtendedBindingState] = List()
                )

sealed trait LeaseToken

object Unknown extends LeaseToken
case class Ip(ip: String) extends LeaseToken
case class Uid(uid: String) extends LeaseToken
case class Variable(item: String) extends LeaseToken
case class BindingState(state: String) extends LeaseToken
case class ClientHostname(hostname: String) extends LeaseToken
case class HardwareEthernet(hardwareEthernet: String) extends LeaseToken
case class OnEvent(eventName: String, commands: List[String]) extends LeaseToken
case class ExtendedBindingState(`type`: String, state: String) extends LeaseToken
case class Notification(name: String, info: Map[String, String]) extends LeaseToken
