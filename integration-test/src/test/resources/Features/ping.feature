Feature: microservices respond to pinging

  Scenario: main service responds to direct ping
    When main service receives Get on "/ping"
    Then response code is 200, content is "pong"

  Scenario: main service forwards ping to second microservice
    When main service receives Get on "/ping/micro"
    Then response code is 200, content is "secondary pong"