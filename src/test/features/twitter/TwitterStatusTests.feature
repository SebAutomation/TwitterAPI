Feature: Twitter api tests - full framework

  Scenario: Twitter end to end framework workflow
    Given user is authenticated by Twitter API
    And twitter baseURL and basePath is set for statuses
    When user sends a following tweet "End to end twitter workflow" to the server
    Then user will receive status code 200
    And tweet id is saved
    When user gets the tweet by Id
    Then response contains elements with expected values
      | name        | "Mark Web"                    |
      | screen_name | "sebstone8"                   |
      | text        | "End to end twitter workflow" |
    And user deletes already created tweet

  Scenario Outline: User sends tweets
    Given user is authenticated by Twitter API
    And twitter baseURL and basePath is set for statuses
    When user sends a following tweet "<tweetMessage>" to the server
    Then user will receive status code 200

    Examples:
      | tweetMessage   |
      | I post tweet 1 |
      | I post tweet 2 |
      | I post tweet 3 |
      | I post tweet 4 |
      | I post tweet 5 |
      | I post tweet 6 |

  Scenario: User deletes tweets
    Given user is authenticated by Twitter API
    And twitter baseURL and basePath is set for statuses
    When user gets a collection of the most recent tweets
    Then tweet IDs are saved
    And user deletes collection of already created tweets by tweet IDs



