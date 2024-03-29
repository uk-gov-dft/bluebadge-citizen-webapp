@actuator-endpoints
Feature: Verify spring actuator endpoints

  Scenario: Verify info endpoint with no auth
    Given Call actuator '/info'
    Then Actuator data present 'build.version'

  Scenario: Verify health endpoint with no auth
    Given Call actuator '/health'
    Then Actuator data 'status' equals 'UP'
    Then Actuator data present 'details'

  Scenario: Verify loggers endpoint with no auth
    Given Call actuator '/loggers'

  Scenario: Verify prometheus endpoint with no auth
    Given Call actuator '/prometheus'