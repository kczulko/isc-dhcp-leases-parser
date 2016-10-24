package com.kczulko.model

class Lease(ip: String,
            starts: Map[String, String] = Map(),
            ends: Map[String, String] = Map(),
            cltt: Map[String, String] = Map(),
            bindingState: String = "",
            nextBindingState: String = "",
            set: Set[String] = Set(),
            clientHostname: String = "",
            onEvent: List[(String, String)] = List()
           );
