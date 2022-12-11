                              Observability using Open Telemetry Manual Implementation (v2.0)

Observability is the ability to see what’s happening in a distributed system and conclude what is its current state. What we see, we can measure and improve. Observability takes into account mainly three different things:

- Metrics: Data about the resources involved inside the distributed system, for instance memory consumption, database connections, CPU, requests by minute, etc. Metrics are constrained by time, those change when time passes and can mean different things at different times.

- Logs: Information at different levels of details about what operations are happening in the system. They have levels like INFO, DEBUG and so on, plus, information relevant to that moment in time and operation.

- Traces: How the information is flowing through the system. Connecting dots through the whole system, understanding where an operation starts and finishes, and how much time it took.

Pre-requisite for the Development
- Java 1.8 or Higher
- Maven
- Docker
- Postman
- Any IDE – Eclipse/STS/IntelliJ
