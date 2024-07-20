Feature: login of new student
  after registration, student receives auto generated username and password
  when logging in with password, user receives access token
  when logging in with incorrect password, user is denied access
  user can access their user information if appropriate access token is provided
  if token is not provided, the information is not returned
  if token is invalid, information is not returned

  Scenario:
    Given new student "someStudent" has completed registration
    When logging in with incorrect password
    Then access is denied as "FORBIDDEN"

  Scenario:
    Given new student "someStudent" has completed registration
    When logging in with correct password
    Then access token is returned
    When request user information without access token
    Then access is denied as "UNAUTHORIZED"

  Scenario:
    Given new student "someStudent" has completed registration
    When logging in with correct password
    Then access token is returned
    When request user information with invalid access token
    Then access is denied as "UNAUTHORIZED"

  Scenario:
    Given new student "someStudent" has completed registration
    When logging in with correct password
    Then access token is returned
    When request user information with valid access token
    Then user information is returned