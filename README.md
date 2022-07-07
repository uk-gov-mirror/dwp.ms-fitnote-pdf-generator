## ms-fitnote-pdf-generator


## Table of contents

* Development
* Testing
* Service Endpoints
* Health Checks

## Development

Dev using Java 11, Dropwizard

Clone repository and run `mvn clean package`

Starting the service -
 
    cd target; java -jar ms-pdf-generator--<version>.jar server path/to/config.yml

## Testing

Tests using Java 11 using Junit 4, Cucumber for Java

Clone repository and run `mvn clean test`

Run integration Tests: Execute `docker-compose up --exit-code-from pdf-tests` from the directory
where docker-compose.yml exists

## Service Endpoints

RESTful Webservice taking JSON posted information to convert a jpg to a fully rendered and A/1A compliant PDF file that will also rotate the PDF page according to the image's dimensions.

#`/pdfGenerator/createPdfFromImage`

Input JSON

    {
        "ninoObject": {
        "ninoBody": "AA370773",
        "ninoSuffix": "A"
         },
        "image":"<base 64 string encoded jpg>",
        "mobileNumber": "07865432345",
        "claimantAddress": {
                "sessionId": "254",
                "houseNameOrNumber": "254",
                "street": "Bakers Street",
                "city": "London",
                "postcode": "NE12 9LG"
            }
    }

The 'mobileNumber' is optional (null or missing node is allowed)
The other parameters are **mandatory**
Street and City within 'address' are optional (null is permitted) all others are mandatory

Outputs

* Invalid json input data - `BAD_REQUEST (400)`
* Exceptions during processing - `INTERNAL_SERVER_ERROR (500)`
* Success = encoded string of the resultant pdf file byte array - `SC_OK (200) with payload`

## Healthcheck

Health check can be found at **`/healthcheck` *[GET]***

## Version-info (Enabled via the APPLICATION_INFO_ENABLED env var)

Version info can be found at **`/version-info` *[GET]***


