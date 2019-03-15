@All
Feature: Twitter api tests - full framework

  Scenario: Twitter end to end framework workflow
    Given user is authenticated by Twitter API
    And twitter baseURL and basePath is set for statuses
    When user sends a following tweet "End to end twitter workflow" to the server
    Then user will receive status code 200
    And tweet id is saved
    When user gets the tweet by Id
    Then response contains elements with expected values
      | name        | "Rest Automator"              |
      | screen_name | "Rautomator"                  |
      | text        | "End to end twitter workflow" |
    And user deletes already created tweet

  Scenario Outline: User sends tweets
    Given user is authenticated by Twitter API
    And twitter baseURL and basePath is set for statuses
    When user sends a following tweet "<tweetMessage>" to the server
    Then user will receive status code 200
    Then response contains elements with expected values
      | name        | <name>              |
      | text        | <text>              |
      | screen_name | <twitterScreenName> |

    Examples:
      | tweetMessage   | name             | text             | twitterScreenName |
      | I post tweet 1 | "Rest Automator" | "I post tweet 1" | "Rautomator"      |
      | I post tweet 2 | "Rest Automator" | "I post tweet 2" | "Rautomator"      |
      | I post tweet 3 | "Rest Automator" | "I post tweet 3" | "Rautomator"      |
      | I post tweet 4 | "Rest Automator" | "I post tweet 4" | "Rautomator"      |
      | I post tweet 5 | "Rest Automator" | "I post tweet 5" | "Rautomator"      |
      | I post tweet 6 | "Rest Automator" | "I post tweet 6" | "Rautomator"      |
      | I post tweet 7 | "Rest Automator" | "I post tweet 7" | "Rautomator"      |
      | I post tweet 8 | "Rest Automator" | "I post tweet 8" | "Rautomator"      |

  Scenario: User deletes tweets
    Given user is authenticated by Twitter API
    And twitter baseURL and basePath is set for statuses
    When user gets a collection of the most recent tweets
    Then tweet IDs are saved
    And user deletes collection of already created tweets by tweet IDs



