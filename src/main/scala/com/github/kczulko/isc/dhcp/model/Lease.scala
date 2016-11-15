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


object Unknown extends Item {
  def toUnknown(any: Any): Item = Unknown
}


object Lease {
  def toLease(ip: Ip, items: List[Item]) =
    items.foldRight(Lease(ip))((item, l) =>
      item match {
        case uid @ Uid(_) => l.copy(uid = Some(uid))
        case bs @ BindingState(_) => l.copy(bindingState = Some(bs))
        case oe @ OnEvent(_, _) => l.copy(onEvent = oe :: l.onEvent)
        case v @ Variable(_) => l.copy(variables = l.variables + v)
        case ch @ ClientHostname(_) => l.copy(clientHostname = Some(ch))
        case he @ HardwareEthernet(_) =>
          l.copy(hardwareEthernet = Some(he))
        case n @ Notification(_, _) =>
          l.copy(notifications = n :: l.notifications)
        case ebs @ ExtendedBindingState(_, _) =>
          l.copy(extendedBindingStates = ebs :: l.extendedBindingStates)
        case _ => l
      })
}
