Feature: PDF Generator Service

  Scenario: Send empty json body to the controller
    Given the controller is up
    When I hit the service url "http://localhost:9044/pdfGenerator/createPdfFromImage" with an invalid body
    Then I get a http response of 400

  Scenario: Send valid json body to the right controller on the wrong path
    Given the controller is up
    When I hit the service url "http://localhost:9044/pdfGenerator/pageDoesNotExist" with valid JSON, no mobile
    Then I get a http response of 404

  Scenario: Send valid json body to the controller with no mobile phone
    Given the controller is up
    When I hit the service url "http://localhost:9044/pdfGenerator/createPdfFromImage" with valid JSON, no mobile
    Then I get a http response of 200
    And The PDF is written to "src/test/resources/output/output1.pdf"

  Scenario: Send valid json body to the controller with a mobile phone number
    Given the controller is up
    When I hit the service url "http://localhost:9044/pdfGenerator/createPdfFromImage" with valid JSON with mobile
    Then I get a http response of 200
    And The PDF is written to "src/test/resources/output/output2.pdf"

  Scenario: Send valid json body to the controller with a mobile phone number
    Given the controller is up
    When I hit the service url "http://localhost:9044/pdfGenerator/createPdfFromImage" with valid JSON and Landscape Image
    Then I get a http response of 200
    And The PDF is written to "src/test/resources/output/output3.pdf"

  Scenario: Send valid json body to the controller with new address details
    Given the controller is up
    When I hit the service url "http://localhost:9044/pdfGenerator/createPdfFromImage" with valid JSON with new address details
    Then I get a http response of 200
    And The PDF is written to "src/test/resources/output/output4.pdf"

  Scenario: Send valid json body to the controller with correct address flag as false and no new address provided
    Given the controller is up
    When I hit the service url "http://localhost:9044/pdfGenerator/createPdfFromImage" with valid JSON and missing new address details
    Then I get a http response of 400

  Scenario: Send valid json body to the controller with all address details and Portrait image
    Given the controller is up
    When I hit the service url "http://localhost:9044/pdfGenerator/createPdfFromImage" with valid JSON containing all details
    Then I get a http response of 200
    And The PDF is written to "src/test/resources/output/output5.pdf"

  Scenario: Send valid json body to the controller with partial address and landscape image
    Given the controller is up
    When I hit the service url "http://localhost:9044/pdfGenerator/createPdfFromImage" with valid JSON containing a partial address
    Then I get a http response of 200
    And The PDF is written to "src/test/resources/output/output6.pdf"
