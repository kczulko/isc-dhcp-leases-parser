# isc-dhcp-leases-parser

Simple isc-dhcp-leases parser written in scala. Utilizes combinator-parsing module from scala.util.

# TODO

1. Unit/Property-based tests.
1. Assure that unknow properties won't fail whole parsing process.
   - handle `service-duid` and comments on top level
1. Convert elements from tuples/Lists to case class Lease
1. Create examples and proper README.md.
1. Pin building system.
1. Provide ivy releases.

