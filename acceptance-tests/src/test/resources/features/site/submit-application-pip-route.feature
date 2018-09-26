@SubmitApplicationPIPRoute
Feature: DFT Blue badge Citizen app new application - PIP
  As a citizen user I want to be able to create a new application via PIP route

  Scenario: Create a successful new application for myself - England - 8 or more points
    Given I navigate to the "applicant" page
    Then  I should see the page titled "Who are you applying for? - GOV.UK Apply for a Blue Badge"
    And   I click on element "applicantType.label.YOURSELF"
    And   I can click on "Continue"

    Then  I should see the page titled "Choose your local council - GOV.UK Apply for a Blue Badge"
    When  I type "Worcester" for "councilShortCode" field by id
    And   I select "Worcester city council" from autosuggest council list
    And   I can click on "Continue"

    Then  I should see the page titled "Your issuing authority - GOV.UK Apply for a Blue Badge"
    And   I should see "Worcestershire county council" text on the page
    And   I can click on "Continue"

    Then  I should see the page titled "Do you receive any of these benefits? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Do you receive any of these benefits?"
    And   I select option "benefitType.option.PIP"
    And   I can click on "Continue"

    Then  I should see the page titled "How many points did you score in the 'moving around' activity of your assessment? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "How many points did you score in the 'moving around' activity of your assessment?"
    And   I select option "movingAroundPoints.option.MOVING_POINTS_12"
    And   I can click on "Continue"

    Then  I should see the page titled "You're eligible for a Blue Badge - GOV.UK Apply for a Blue Badge"
    And   I should see the title "You're eligible for a Blue Badge"
    And   I can click on "Start application"

    Then  I should see the page titled "What's your name? - GOV.UK Apply for a Blue Badge"
    When  I type "Tom Richardson" for "fullName" field by id
    And   I select an option "hasBirthName.no"
    And   I can click on "Continue"

    Then  I should see the page titled "What's your date of birth - GOV.UK Apply for a Blue Badge"
  And   I should see the title "What's your date of birth?"

    When  I type day as "02" month as "08" and year as "1966" for applicant's date of birth
    And   I can click on "Continue"

    Then  I should see the page titled "Describe health conditions - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Describe any health conditions that affect your mobility"
    When  I type "Sample health condition" for "descriptionOfConditions" field by id
    And   I can click on "Continue"

    Then  I should see the page titled "Declaration - GOV.UK Apply for a Blue Badge"
    And   I should see the content "I agree to this declaration"
    And   I select option "declaration.option"
    And   I can click on "Continue"

    Then  I should see the page titled "Application submitted - GOV.UK Apply for a Blue Badge"

  ##################################################################################################


  Scenario: Create a successful new application for someone else - England - 8 or more points
    Given I navigate to the "applicant" page
    Then  I should see the page titled "Who are you applying for? - GOV.UK Apply for a Blue Badge"
    And   I click on element "applicantType.label.SOMEONE_ELSE"
    And   I can click on "Continue"

    Then  I should see the page titled "Choose their local council - GOV.UK Apply for a Blue Badge"
    When  I type "Worcester" for "councilShortCode" field by id
    And   I select "Worcester city council" from autosuggest council list
    And   I can click on "Continue"

    Then  I should see the page titled "Their issuing authority - GOV.UK Apply for a Blue Badge"
    And   I should see "Worcestershire county council" text on the page
    And   I can click on "Continue"

    Then  I should see the page titled "Do they receive any of these benefits? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Do they receive any of these benefits?"
    And   I select option "benefitType.option.PIP"
    And   I can click on "Continue"

    Then  I should see the page titled "How many points did they score in the 'moving around' activity of their assessment? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "How many points did they score in the 'moving around' activity of their assessment?"
    And   I select option "movingAroundPoints.option.MOVING_POINTS_12"
    And   I can click on "Continue"

    Then  I should see the page titled "They are eligible for a Blue Badge - GOV.UK Apply for a Blue Badge"
    And   I should see the title "They are eligible for a Blue Badge"
    And   I can click on "Start application"

    Then  I should see the page titled "What's their name? - GOV.UK Apply for a Blue Badge"
    When  I type "Tom Richardson" for "fullName" field by id
    And   I select an option "hasBirthName.no"
    And   I can click on "Continue"

    Then  I should see the page titled "What's their date of birth - GOV.UK Apply for a Blue Badge"
    When  I type day as "02" month as "08" and year as "1966" for applicant's date of birth
    And   I can click on "Continue"

    Then  I should see the page titled "Describe health conditions - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Describe any health conditions that affect their mobility"
    When  I type "Sample health condition" for "descriptionOfConditions" field by id
    And   I can click on "Continue"

    Then  I should see the page titled "Declaration - GOV.UK Apply for a Blue Badge"
    And   I should see the content "They agree to this declaration"
    And   I select option "declaration.option"
    And   I can click on "Continue"

    Then  I should see the page titled "Application submitted - GOV.UK Apply for a Blue Badge"

  ##################################################################################################


  Scenario: Create a successful new application for myself - England - less than 8 points
    Given I navigate to the "applicant" page
    Then  I should see the page titled "Who are you applying for? - GOV.UK Apply for a Blue Badge"
    And   I click on element "applicantType.label.YOURSELF"
    And   I can click on "Continue"

    Then  I should see the page titled "Choose your local council - GOV.UK Apply for a Blue Badge"
    When  I type "Worcester" for "councilShortCode" field by id
    And   I select "Worcester city council" from autosuggest council list
    And   I can click on "Continue"

    Then  I should see the page titled "Your issuing authority - GOV.UK Apply for a Blue Badge"
    And   I should see "Worcestershire county council" text on the page
    And   I can click on "Continue"

    Then  I should see the page titled "Do you receive any of these benefits? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Do you receive any of these benefits?"
    And   I select option "benefitType.option.PIP"
    And   I can click on "Continue"

    Then  I should see the page titled "How many points did you score in the 'moving around' activity of your assessment? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "How many points did you score in the 'moving around' activity of your assessment?"
    And   I select option "movingAroundPoints.option.MOVING_POINTS_4"
    And   I can click on "Continue"

    Then  I should see the page titled "You may be eligible for a Blue Badge - GOV.UK Apply for a Blue Badge"
    And   I should see the title "You may be eligible for a Blue Badge"
    And   I can click on "Start application"

    Then  I should see the page titled "What's your name? - GOV.UK Apply for a Blue Badge"
    When  I type "Tom Richardson" for "fullName" field by id
    And   I select an option "hasBirthName.no"
    And   I can click on "Continue"

    Then  I should see the page titled "What's your date of birth - GOV.UK Apply for a Blue Badge"
    When  I type day as "02" month as "08" and year as "1966" for applicant's date of birth
    And   I can click on "Continue"

    Then  I should see the page titled "Describe health conditions - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Describe any health conditions that affect your mobility"
    When  I type "Sample health condition" for "descriptionOfConditions" field by id
    And   I can click on "Continue"

    Then  I should see the page titled "Declaration - GOV.UK Apply for a Blue Badge"
    And   I should see the content "I agree to this declaration"
    And   I select option "declaration.option"
    And   I can click on "Continue"

    Then  I should see the page titled "Application submitted - GOV.UK Apply for a Blue Badge"

  ##################################################################################################


  Scenario: Create a successful new application for someone else - England - than 8 points
    Given I navigate to the "applicant" page
    Then  I should see the page titled "Who are you applying for? - GOV.UK Apply for a Blue Badge"
    And   I click on element "applicantType.label.SOMEONE_ELSE"
    And   I can click on "Continue"

    Then  I should see the page titled "Choose their local council - GOV.UK Apply for a Blue Badge"
    When  I type "Worcester" for "councilShortCode" field by id
    And   I select "Worcester city council" from autosuggest council list
    And   I can click on "Continue"

    Then  I should see the page titled "Their issuing authority - GOV.UK Apply for a Blue Badge"
    And   I should see "Worcestershire county council" text on the page
    And   I can click on "Continue"

    Then  I should see the page titled "Do they receive any of these benefits? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Do they receive any of these benefits?"
    And   I select option "benefitType.option.PIP"
    And   I can click on "Continue"

    Then  I should see the page titled "How many points did they score in the 'moving around' activity of their assessment? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "How many points did they score in the 'moving around' activity of their assessment?"
    And   I select option "movingAroundPoints.option.MOVING_POINTS_4"
    And   I can click on "Continue"

    Then  I should see the page titled "They may be eligible for a Blue Badge - GOV.UK Apply for a Blue Badge"
    And   I should see the title "They may be eligible for a Blue Badge"
    And   I can click on "Start application"

    Then  I should see the page titled "What's their name? - GOV.UK Apply for a Blue Badge"
    When  I type "Tom Richardson" for "fullName" field by id
    And   I select an option "hasBirthName.no"
    And   I can click on "Continue"

    Then  I should see the page titled "What's their date of birth - GOV.UK Apply for a Blue Badge"
    When  I type day as "02" month as "08" and year as "1966" for applicant's date of birth
    And   I can click on "Continue"

    Then  I should see the page titled "Describe health conditions - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Describe any health conditions that affect their mobility"
    When  I type "Sample health condition" for "descriptionOfConditions" field by id
    And   I can click on "Continue"

    Then  I should see the page titled "Declaration - GOV.UK Apply for a Blue Badge"
    And   I should see the content "They agree to this declaration"
    And   I select option "declaration.option"
    And   I can click on "Continue"

    Then  I should see the page titled "Application submitted - GOV.UK Apply for a Blue Badge"

  ##################################################################################################


  Scenario: Create a successful new application for myself - Scotland - less than 8 points (moving) - 12 points (planning)
    Given I navigate to the "applicant" page
    Then  I should see the page titled "Who are you applying for? - GOV.UK Apply for a Blue Badge"
    And   I click on element "applicantType.label.YOURSELF"
    And   I can click on "Continue"

    Then  I should see the page titled "Choose your local council - GOV.UK Apply for a Blue Badge"
    When  I type "Aberdeenshire" for "councilShortCode" field by id
    And   I select "Aberdeenshire council" from autosuggest council list
    And   I can click on "Continue"

    Then  I should see the page titled "Your issuing authority - GOV.UK Apply for a Blue Badge"
    And   I should see "Aberdeenshire council" text on the page
    And   I can click on "Continue"

    Then  I should see the page titled "Do you receive any of these benefits? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Do you receive any of these benefits?"
    And   I select option "benefitType.option.PIP"
    And   I can click on "Continue"

    Then  I should see the page titled "How many points did you score in the 'moving around' activity of your assessment? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "How many points did you score in the 'moving around' activity of your assessment?"
    And   I select option "movingAroundPoints.option.MOVING_POINTS_4"
    And   I can click on "Continue"

    Then  I should see the page titled "How many points did you score in the 'planning and following a journey' activity of your assessment? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "How many points did you score in the 'planning and following a journey' activity of your assessment?"
    And   I select option "planningJourneyOption.option.PLANNING_POINTS_12"
    And   I can click on "Continue"

    Then  I should see the page titled "You're eligible for a Blue Badge - GOV.UK Apply for a Blue Badge"
    And   I should see the title "You're eligible for a Blue Badge"
    And   I can click on "Start application"

    Then  I should see the page titled "What's your name? - GOV.UK Apply for a Blue Badge"
    When  I type "Tom Richardson" for "fullName" field by id
    And   I select an option "hasBirthName.no"
    And   I can click on "Continue"

    Then  I should see the page titled "What's your date of birth - GOV.UK Apply for a Blue Badge"
    When  I type day as "02" month as "08" and year as "1966" for applicant's date of birth
    And   I can click on "Continue"

    Then  I should see the page titled "Describe health conditions - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Describe any health conditions that affect your mobility"
    When  I type "Sample health condition" for "descriptionOfConditions" field by id
    And   I can click on "Continue"

    Then  I should see the page titled "Declaration - GOV.UK Apply for a Blue Badge"
    And   I should see the content "I agree to this declaration"
    And   I select option "declaration.option"
    And   I can click on "Continue"

    Then  I should see the page titled "Application submitted - GOV.UK Apply for a Blue Badge"

  ################################################################################################


  Scenario: Create a successful new application for someone else - Scotland - less than 8 points (moving) - 12 points (planning)
    Given I navigate to the "applicant" page
    Then  I should see the page titled "Who are you applying for? - GOV.UK Apply for a Blue Badge"
    And   I click on element "applicantType.label.SOMEONE_ELSE"
    And   I can click on "Continue"

    Then  I should see the page titled "Choose their local council - GOV.UK Apply for a Blue Badge"
    When  I type "Aberdeenshire" for "councilShortCode" field by id
    And   I select "Aberdeenshire council" from autosuggest council list
    And   I can click on "Continue"

    Then  I should see the page titled "Their issuing authority - GOV.UK Apply for a Blue Badge"
    And   I should see "Aberdeenshire council" text on the page
    And   I can click on "Continue"

    Then  I should see the page titled "Do they receive any of these benefits? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Do they receive any of these benefits?"
    And   I select option "benefitType.option.PIP"
    And   I can click on "Continue"

    Then  I should see the page titled "How many points did they score in the 'moving around' activity of their assessment? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "How many points did they score in the 'moving around' activity of their assessment?"
    And   I select option "movingAroundPoints.option.MOVING_POINTS_4"
    And   I can click on "Continue"

    Then  I should see the page titled "How many points did they score in the 'planning and following a journey' activity of their assessment? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "How many points did they score in the 'planning and following a journey' activity of their assessment?"
    And   I select option "planningJourneyOption.option.PLANNING_POINTS_12"
    And   I can click on "Continue"

    Then  I should see the page titled "They are eligible for a Blue Badge - GOV.UK Apply for a Blue Badge"
    And   I should see the title "They are eligible for a Blue Badge"
    And   I can click on "Start application"

    Then  I should see the page titled "What's their name? - GOV.UK Apply for a Blue Badge"
    When  I type "Tom Richardson" for "fullName" field by id
    And   I select an option "hasBirthName.no"
    And   I can click on "Continue"

    Then  I should see the page titled "What's their date of birth - GOV.UK Apply for a Blue Badge"
    When  I type day as "02" month as "08" and year as "1966" for applicant's date of birth
    And   I can click on "Continue"

    Then  I should see the page titled "Describe health conditions - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Describe any health conditions that affect their mobility"
    When  I type "Sample health condition" for "descriptionOfConditions" field by id
    And   I can click on "Continue"

    Then  I should see the page titled "Declaration - GOV.UK Apply for a Blue Badge"
    And   I should see the content "They agree to this declaration"
    And   I select option "declaration.option"
    And   I can click on "Continue"

    Then  I should see the page titled "Application submitted - GOV.UK Apply for a Blue Badge"

  #################################################################################################


  Scenario: Create a successful new application for myself - Scotland - less than 8 points (moving) - less than 12 points (planning) - DLA Yes
    Given I navigate to the "applicant" page
    Then  I should see the page titled "Who are you applying for? - GOV.UK Apply for a Blue Badge"
    And   I click on element "applicantType.label.YOURSELF"
    And   I can click on "Continue"

    Then  I should see the page titled "Choose your local council - GOV.UK Apply for a Blue Badge"
    When  I type "Aberdeenshire" for "councilShortCode" field by id
    And   I select "Aberdeenshire council" from autosuggest council list
    And   I can click on "Continue"

    Then  I should see the page titled "Your issuing authority - GOV.UK Apply for a Blue Badge"
    And   I should see "Aberdeenshire council" text on the page
    And   I can click on "Continue"

    Then  I should see the page titled "Do you receive any of these benefits? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Do you receive any of these benefits?"
    And   I select option "benefitType.option.PIP"
    And   I can click on "Continue"

    Then  I should see the page titled "How many points did you score in the 'moving around' activity of your assessment? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "How many points did you score in the 'moving around' activity of your assessment?"
    And   I select option "movingAroundPoints.option.MOVING_POINTS_4"
    And   I can click on "Continue"

    Then  I should see the page titled "How many points did you score in the 'planning and following a journey' activity of your assessment? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "How many points did you score in the 'planning and following a journey' activity of your assessment?"
    And   I select option "planningJourneyOption.option.PLANNING_POINTS_10"
    And   I can click on "Continue"

    Then  I should see the page titled "Have you received Disability Living Allowance (DLA) in the past? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Have you received Disability Living Allowance (DLA) in the past?"
    And   I select option "receivedDlaOption.option.HAS_RECEIVED_DLA"
    And   I can click on "Continue"

    Then  I should see the page titled "You're eligible for a Blue Badge - GOV.UK Apply for a Blue Badge"
    And   I should see the title "You're eligible for a Blue Badge"
    And   I can click on "Start application"

    Then  I should see the page titled "What's your name? - GOV.UK Apply for a Blue Badge"
    When  I type "Tom Richardson" for "fullName" field by id
    And   I select an option "hasBirthName.no"
    And   I can click on "Continue"

    Then  I should see the page titled "What's your date of birth - GOV.UK Apply for a Blue Badge"
    When  I type day as "02" month as "08" and year as "1966" for applicant's date of birth
    And   I can click on "Continue"

    Then  I should see the page titled "Describe health conditions - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Describe any health conditions that affect your mobility"
    When  I type "Sample health condition" for "descriptionOfConditions" field by id
    And   I can click on "Continue"

    Then  I should see the page titled "Declaration - GOV.UK Apply for a Blue Badge"
    And   I should see the content "I agree to this declaration"
    And   I select option "declaration.option"
    And   I can click on "Continue"

    Then  I should see the page titled "Application submitted - GOV.UK Apply for a Blue Badge"

  ################################################################################################


  Scenario: Create a successful new application for someone else - Scotland - less than 8 points (moving) - less 12 points (planning)b - DLA Yes - (with validation messages)
    Given I navigate to the "applicant" page
    Then  I should see the page titled "Who are you applying for? - GOV.UK Apply for a Blue Badge"
    And   I can click on "Continue"
    And   I should see error summary box
    And   I click on element "applicantType.label.SOMEONE_ELSE"
    And   I can click on "Continue"

    Then  I should see the page titled "Choose their local council - GOV.UK Apply for a Blue Badge"
    And   I can click on "Continue"
    And   I should see error summary box
    When  I type "Aberdeenshire" for "councilShortCode" field by id
    And   I select "Aberdeenshire council" from autosuggest council list
    And   I can click on "Continue"

    Then  I should see the page titled "Their issuing authority - GOV.UK Apply for a Blue Badge"
    And   I should see "Aberdeenshire council" text on the page
    And   I can click on "Continue"

    Then  I should see the page titled "Do they receive any of these benefits? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Do they receive any of these benefits?"
    And   I can click on "Continue"
    And   I should see error summary box
    And   I select option "benefitType.option.PIP"
    And   I can click on "Continue"

    Then  I should see the page titled "How many points did they score in the 'moving around' activity of their assessment? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "How many points did they score in the 'moving around' activity of their assessment?"
    And   I can click on "Continue"
    And   I should see error summary box
    And   I select option "movingAroundPoints.option.MOVING_POINTS_4"
    And   I can click on "Continue"

    Then  I should see the page titled "How many points did they score in the 'planning and following a journey' activity of their assessment? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "How many points did they score in the 'planning and following a journey' activity of their assessment?"
    And   I can click on "Continue"
    And   I should see error summary box
    And   I select option "planningJourneyOption.option.PLANNING_POINTS_10"
    And   I can click on "Continue"

    Then  I should see the page titled "Have they received Disability Living Allowance (DLA) in the past? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Have they received Disability Living Allowance (DLA) in the past?"
    And   I can click on "Continue"
    And   I should see error summary box
    And   I select option "receivedDlaOption.option.HAS_RECEIVED_DLA"
    And   I can click on "Continue"

    Then  I should see the page titled "They are eligible for a Blue Badge - GOV.UK Apply for a Blue Badge"
    And   I should see the title "They are eligible for a Blue Badge"
    And   I can click on "Start application"

    Then  I should see the page titled "What's their name? - GOV.UK Apply for a Blue Badge"
    When  I type "Tom Richardson" for "fullName" field by id
    And   I can click on "Continue"
    And   I should see error summary box
    And   I select an option "hasBirthName.no"
    And   I can click on "Continue"

    Then  I should see the page titled "What's their date of birth - GOV.UK Apply for a Blue Badge"
    And   I can click on "Continue"
    And   I should see error summary box
    When  I type day as "02" month as "08" and year as "1966" for applicant's date of birth
    And   I can click on "Continue"

    Then  I should see the page titled "Describe health conditions - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Describe any health conditions that affect their mobility"
    And   I can click on "Continue"
    And   I should see error summary box
    When  I type "Sample health condition" for "descriptionOfConditions" field by id
    And   I can click on "Continue"

    Then  I should see the page titled "Declaration - GOV.UK Apply for a Blue Badge"
    And   I should see the content "They agree to this declaration"
    And   I can click on "Continue"
    And   I should see error summary box
    And   I select option "declaration.option"
    And   I can click on "Continue"

    Then  I should see the page titled "Application submitted - GOV.UK Apply for a Blue Badge"

  #################################################################################################


  Scenario: Create a successful new application for myself - Scotland - less than 8 points (moving) - less than 12 points (planning) - DLA No
    Given I navigate to the "applicant" page
    Then  I should see the page titled "Who are you applying for? - GOV.UK Apply for a Blue Badge"
    And   I click on element "applicantType.label.YOURSELF"
    And   I can click on "Continue"

    Then  I should see the page titled "Choose your local council - GOV.UK Apply for a Blue Badge"
    When  I type "Aberdeenshire" for "councilShortCode" field by id
    And   I select "Aberdeenshire council" from autosuggest council list
    And   I can click on "Continue"

    Then  I should see the page titled "Your issuing authority - GOV.UK Apply for a Blue Badge"
    And   I should see "Aberdeenshire council" text on the page
    And   I can click on "Continue"

    Then  I should see the page titled "Do you receive any of these benefits? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Do you receive any of these benefits?"
    And   I select option "benefitType.option.PIP"
    And   I can click on "Continue"

    Then  I should see the page titled "How many points did you score in the 'moving around' activity of your assessment? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "How many points did you score in the 'moving around' activity of your assessment?"
    And   I select option "movingAroundPoints.option.MOVING_POINTS_4"
    And   I can click on "Continue"

    Then  I should see the page titled "How many points did you score in the 'planning and following a journey' activity of your assessment? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "How many points did you score in the 'planning and following a journey' activity of your assessment?"
    And   I select option "planningJourneyOption.option.PLANNING_POINTS_10"
    And   I can click on "Continue"

    Then  I should see the page titled "Have you received Disability Living Allowance (DLA) in the past? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Have you received Disability Living Allowance (DLA) in the past?"
    And   I select option "receivedDlaOption.option.NEVER_RECEIVED_DLA"
    And   I can click on "Continue"

    Then  I should see the page titled "You may be eligible for a Blue Badge - GOV.UK Apply for a Blue Badge"
    And   I should see the title "You may be eligible for a Blue Badge"
    And   I can click on "Start application"

    Then  I should see the page titled "What's your name? - GOV.UK Apply for a Blue Badge"
    When  I type "Tom Richardson" for "fullName" field by id
    And   I select an option "hasBirthName.no"
    And   I can click on "Continue"

    Then  I should see the page titled "What's your date of birth - GOV.UK Apply for a Blue Badge"
    When  I type day as "02" month as "08" and year as "1966" for applicant's date of birth
    And   I can click on "Continue"

    Then  I should see the page titled "Describe health conditions - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Describe any health conditions that affect your mobility"
    When  I type "Sample health condition" for "descriptionOfConditions" field by id
    And   I can click on "Continue"

    Then  I should see the page titled "Declaration - GOV.UK Apply for a Blue Badge"
    And   I should see the content "I agree to this declaration"
    And   I select option "declaration.option"
    And   I can click on "Continue"

    Then  I should see the page titled "Application submitted - GOV.UK Apply for a Blue Badge"

  ################################################################################################


  Scenario: Create a successful new application for someone else - Scotland - less than 8 points (moving) - less 12 points (planning) - DLA Yes
    Given I navigate to the "applicant" page
    Then  I should see the page titled "Who are you applying for? - GOV.UK Apply for a Blue Badge"
    And   I click on element "applicantType.label.SOMEONE_ELSE"
    And   I can click on "Continue"

    Then  I should see the page titled "Choose their local council - GOV.UK Apply for a Blue Badge"
    When  I type "Aberdeenshire" for "councilShortCode" field by id
    And   I select "Aberdeenshire council" from autosuggest council list
    And   I can click on "Continue"

    Then  I should see the page titled "Their issuing authority - GOV.UK Apply for a Blue Badge"
    And   I should see "Aberdeenshire council" text on the page
    And   I can click on "Continue"

    Then  I should see the page titled "Do they receive any of these benefits? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Do they receive any of these benefits?"
    And   I select option "benefitType.option.PIP"
    And   I can click on "Continue"

    Then  I should see the page titled "How many points did they score in the 'moving around' activity of their assessment? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "How many points did they score in the 'moving around' activity of their assessment?"
    And   I select option "movingAroundPoints.option.MOVING_POINTS_4"
    And   I can click on "Continue"

    Then  I should see the page titled "How many points did they score in the 'planning and following a journey' activity of their assessment? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "How many points did they score in the 'planning and following a journey' activity of their assessment?"
    And   I select option "planningJourneyOption.option.PLANNING_POINTS_10"
    And   I can click on "Continue"

    Then  I should see the page titled "Have they received Disability Living Allowance (DLA) in the past? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Have they received Disability Living Allowance (DLA) in the past?"
    And   I select option "receivedDlaOption.option.NEVER_RECEIVED_DLA"
    And   I can click on "Continue"

    Then  I should see the page titled "They may be eligible for a Blue Badge - GOV.UK Apply for a Blue Badge"
    And   I should see the title "They may be eligible for a Blue Badge"
    And   I can click on "Start application"

    Then  I should see the page titled "What's their name? - GOV.UK Apply for a Blue Badge"
    When  I type "Tom Richardson" for "fullName" field by id
    And   I select an option "hasBirthName.no"
    And   I can click on "Continue"

    Then  I should see the page titled "What's their date of birth - GOV.UK Apply for a Blue Badge"
    When  I type day as "02" month as "08" and year as "1966" for applicant's date of birth
    And   I can click on "Continue"

    Then  I should see the page titled "Describe health conditions - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Describe any health conditions that affect their mobility"
    When  I type "Sample health condition" for "descriptionOfConditions" field by id
    And   I can click on "Continue"

    Then  I should see the page titled "Declaration - GOV.UK Apply for a Blue Badge"
    And   I should see the content "They agree to this declaration"
    And   I select option "declaration.option"
    And   I can click on "Continue"

    Then  I should see the page titled "Application submitted - GOV.UK Apply for a Blue Badge"



################################################################################################

  Scenario: Create a successful new application for myself - Wales - less than 8 points (moving) - 12 points (planning)
    Given I navigate to the "applicant" page
    Then  I should see the page titled "Who are you applying for? - GOV.UK Apply for a Blue Badge"
    And   I click on element "applicantType.label.YOURSELF"
    And   I can click on "Continue"

    Then  I should see the page titled "Choose your local council - GOV.UK Apply for a Blue Badge"
    When  I type "Anglesey" for "councilShortCode" field by id
    And   I select "Isle of Anglesey county council" from autosuggest council list
    And   I can click on "Continue"

    Then  I should see the page titled "Your issuing authority - GOV.UK Apply for a Blue Badge"
    And   I should see "Isle of Anglesey county council" text on the page
    And   I can click on "Continue"

    Then  I should see the page titled "Do you receive any of these benefits? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Do you receive any of these benefits?"
    And   I select option "benefitType.option.PIP"
    And   I can click on "Continue"

    Then  I should see the page titled "How many points did you score in the 'moving around' activity of your assessment? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "How many points did you score in the 'moving around' activity of your assessment?"
    And   I select option "movingAroundPoints.option.MOVING_POINTS_4"
    And   I can click on "Continue"

    Then  I should see the page titled "How many points did you score in the 'planning and following a journey' activity of your assessment? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "How many points did you score in the 'planning and following a journey' activity of your assessment?"
    And   I select option "planningJourneyOption.option.PLANNING_POINTS_12"
    And   I can click on "Continue"

    Then  I should see the page titled "You're eligible for a Blue Badge - GOV.UK Apply for a Blue Badge"
    And   I should see the title "You're eligible for a Blue Badge"
    And   I can click on "Start application"

    Then  I should see the page titled "What's your name? - GOV.UK Apply for a Blue Badge"
    When  I type "Tom Richardson" for "fullName" field by id
    And   I select an option "hasBirthName.no"
    And   I can click on "Continue"

    Then  I should see the page titled "What's your date of birth - GOV.UK Apply for a Blue Badge"
    When  I type day as "02" month as "08" and year as "1966" for applicant's date of birth
    And   I can click on "Continue"

    Then  I should see the page titled "Describe health conditions - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Describe any health conditions that affect your mobility"
    When  I type "Sample health condition" for "descriptionOfConditions" field by id
    And   I can click on "Continue"

    Then  I should see the page titled "Declaration - GOV.UK Apply for a Blue Badge"
    And   I should see the content "I agree to this declaration"
    And   I select option "declaration.option"
    And   I can click on "Continue"

    Then  I should see the page titled "Application submitted - GOV.UK Apply for a Blue Badge"

  ################################################################################################


  Scenario: Create a successful new application for someone else - Wales - less than 8 points (moving) - 12 points (planning)
    Given I navigate to the "applicant" page
    Then  I should see the page titled "Who are you applying for? - GOV.UK Apply for a Blue Badge"
    And   I click on element "applicantType.label.SOMEONE_ELSE"
    And   I can click on "Continue"

    Then  I should see the page titled "Choose their local council - GOV.UK Apply for a Blue Badge"
    When  I type "Anglesey" for "councilShortCode" field by id
    And   I select "Isle of Anglesey county council" from autosuggest council list
    And   I can click on "Continue"

    Then  I should see the page titled "Their issuing authority - GOV.UK Apply for a Blue Badge"
    And   I should see "Isle of Anglesey county council" text on the page
    And   I can click on "Continue"

    Then  I should see the page titled "Do they receive any of these benefits? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Do they receive any of these benefits?"
    And   I select option "benefitType.option.PIP"
    And   I can click on "Continue"

    Then  I should see the page titled "How many points did they score in the 'moving around' activity of their assessment? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "How many points did they score in the 'moving around' activity of their assessment?"
    And   I select option "movingAroundPoints.option.MOVING_POINTS_4"
    And   I can click on "Continue"

    Then  I should see the page titled "How many points did they score in the 'planning and following a journey' activity of their assessment? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "How many points did they score in the 'planning and following a journey' activity of their assessment?"
    And   I select option "planningJourneyOption.option.PLANNING_POINTS_12"
    And   I can click on "Continue"

    Then  I should see the page titled "They are eligible for a Blue Badge - GOV.UK Apply for a Blue Badge"
    And   I should see the title "They are eligible for a Blue Badge"
    And   I can click on "Start application"

    Then  I should see the page titled "What's their name? - GOV.UK Apply for a Blue Badge"
    When  I type "Tom Richardson" for "fullName" field by id
    And   I select an option "hasBirthName.no"
    And   I can click on "Continue"

    Then  I should see the page titled "What's their date of birth - GOV.UK Apply for a Blue Badge"
    When  I type day as "02" month as "08" and year as "1966" for applicant's date of birth
    And   I can click on "Continue"

    Then  I should see the page titled "Describe health conditions - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Describe any health conditions that affect their mobility"
    When  I type "Sample health condition" for "descriptionOfConditions" field by id
    And   I can click on "Continue"

    Then  I should see the page titled "Declaration - GOV.UK Apply for a Blue Badge"
    And   I should see the content "They agree to this declaration"
    And   I select option "declaration.option"
    And   I can click on "Continue"

    Then  I should see the page titled "Application submitted - GOV.UK Apply for a Blue Badge"

  #################################################################################################

  Scenario: Create a successful new application for myself - Wales - less than 8 points (moving) - less than 12 points (planning)
    Given I navigate to the "applicant" page
    Then  I should see the page titled "Who are you applying for? - GOV.UK Apply for a Blue Badge"
    And   I click on element "applicantType.label.YOURSELF"
    And   I can click on "Continue"

    Then  I should see the page titled "Choose your local council - GOV.UK Apply for a Blue Badge"
    When  I type "Anglesey" for "councilShortCode" field by id
    And   I select "Isle of Anglesey county council" from autosuggest council list
    And   I can click on "Continue"

    Then  I should see the page titled "Your issuing authority - GOV.UK Apply for a Blue Badge"
    And   I should see "Isle of Anglesey county council" text on the page
    And   I can click on "Continue"

    Then  I should see the page titled "Do you receive any of these benefits? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Do you receive any of these benefits?"
    And   I select option "benefitType.option.PIP"
    And   I can click on "Continue"

    Then  I should see the page titled "How many points did you score in the 'moving around' activity of your assessment? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "How many points did you score in the 'moving around' activity of your assessment?"
    And   I select option "movingAroundPoints.option.MOVING_POINTS_4"
    And   I can click on "Continue"

    Then  I should see the page titled "How many points did you score in the 'planning and following a journey' activity of your assessment? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "How many points did you score in the 'planning and following a journey' activity of your assessment?"
    And   I select option "planningJourneyOption.option.PLANNING_POINTS_10"
    And   I can click on "Continue"

    Then  I should see the page titled "You may be eligible for a Blue Badge - GOV.UK Apply for a Blue Badge"
    And   I should see the title "You may be eligible for a Blue Badge"
    And   I can click on "Start application"

    Then  I should see the page titled "What's your name? - GOV.UK Apply for a Blue Badge"
    When  I type "Tom Richardson" for "fullName" field by id
    And   I select an option "hasBirthName.no"
    And   I can click on "Continue"

    Then  I should see the page titled "What's your date of birth - GOV.UK Apply for a Blue Badge"
    When  I type day as "02" month as "08" and year as "1966" for applicant's date of birth
    And   I can click on "Continue"

    Then  I should see the page titled "Describe health conditions - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Describe any health conditions that affect your mobility"
    When  I type "Sample health condition" for "descriptionOfConditions" field by id
    And   I can click on "Continue"

    Then  I should see the page titled "Declaration - GOV.UK Apply for a Blue Badge"
    And   I should see the content "I agree to this declaration"
    And   I select option "declaration.option"
    And   I can click on "Continue"

    Then  I should see the page titled "Application submitted - GOV.UK Apply for a Blue Badge"

  ################################################################################################


  Scenario: Create a successful new application for someone else - Wales - less than 8 points (moving) - less than 12 points (planning)
    Given I navigate to the "applicant" page
    Then  I should see the page titled "Who are you applying for? - GOV.UK Apply for a Blue Badge"
    And   I click on element "applicantType.label.SOMEONE_ELSE"
    And   I can click on "Continue"

    Then  I should see the page titled "Choose their local council - GOV.UK Apply for a Blue Badge"
    When  I type "Anglesey" for "councilShortCode" field by id
    And   I select "Isle of Anglesey county council" from autosuggest council list
    And   I can click on "Continue"

    Then  I should see the page titled "Their issuing authority - GOV.UK Apply for a Blue Badge"
    And   I should see "Isle of Anglesey county council" text on the page
    And   I can click on "Continue"

    Then  I should see the page titled "Do they receive any of these benefits? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Do they receive any of these benefits?"
    And   I select option "benefitType.option.PIP"
    And   I can click on "Continue"

    Then  I should see the page titled "How many points did they score in the 'moving around' activity of their assessment? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "How many points did they score in the 'moving around' activity of their assessment?"
    And   I select option "movingAroundPoints.option.MOVING_POINTS_4"
    And   I can click on "Continue"

    Then  I should see the page titled "How many points did they score in the 'planning and following a journey' activity of their assessment? - GOV.UK Apply for a Blue Badge"
    And   I should see the title "How many points did they score in the 'planning and following a journey' activity of their assessment?"
    And   I select option "planningJourneyOption.option.PLANNING_POINTS_10"
    And   I can click on "Continue"

    Then  I should see the page titled "They may be eligible for a Blue Badge - GOV.UK Apply for a Blue Badge"
    And   I should see the title "They may be eligible for a Blue Badge"
    And   I can click on "Start application"

    Then  I should see the page titled "What's their name? - GOV.UK Apply for a Blue Badge"
    When  I type "Tom Richardson" for "fullName" field by id
    And   I select an option "hasBirthName.no"
    And   I can click on "Continue"

    Then  I should see the page titled "What's their date of birth - GOV.UK Apply for a Blue Badge"
    When  I type day as "02" month as "08" and year as "1966" for applicant's date of birth
    And   I can click on "Continue"

    Then  I should see the page titled "Describe health conditions - GOV.UK Apply for a Blue Badge"
    And   I should see the title "Describe any health conditions that affect their mobility"
    When  I type "Sample health condition" for "descriptionOfConditions" field by id
    And   I can click on "Continue"

    Then  I should see the page titled "Declaration - GOV.UK Apply for a Blue Badge"
    And   I should see the content "They agree to this declaration"
    And   I select option "declaration.option"
    And   I can click on "Continue"

    Then  I should see the page titled "Application submitted - GOV.UK Apply for a Blue Badge"

  ################################################################################################

