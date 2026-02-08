# Flashcards4u
[www.flashcards4u.com](http://www.flashcards4u.com) is a test-driven, Java Spring Boot webapp. 
It loads content into digital flashcards for on-the-go studying. Click the card to toggle between front and back.
Click the 'next' button to view the subsequent card.

As of 2026, this app is using content for learning French, but the app is content-agnostic. 
It can be used for any subject.

<p align="center">
  <img src="./docs/images/flashcards4u-two-screens-compressed.jpg" height="300" style="margin-right: 10px;" />
</p>

## Architecture
```
flashcards4u/                            # project root
├── pom.xml                              # configuration and dependencies
├── mvnw                                 # Maven wrapper
│   
├── src/main/java/.../flashcards4u/      # java package
│   │
│   ├── controller/                      # HTTP concerns                         
│   │   ├── DeckController.java          # REST endpoints
│   │   └── RootController.java          # serves index.html to frontend
│   │
│   ├── model/                           # domain objects (internal representation of data)
│   ├── response/                        # data transfer objects (DTOs) for JSON serialization
│   └── service/                         # business logic
│
├── src/main/resources/static/           # static resources
│   ├── csv/                             # flashcard content; analogous to a database
│   ├── index.html                       # frontend entry point
│   └── source.js                        # frontend logic (AJAX, card rendering, animations)
│
└── src/test/                            # test suite
```

## Future Development
* Integration with Generative AI service to allow the user to gain additional context about the topic of a flashcard without leaving the app.

## Development

### Dependencies
* Java 21
* Spring Boot 3.4.0
* Maven 3

### Run
* From command line: `./mvnw spring-boot:run`
* Navigate to `http://localhost:8080/` in browser

### Testing
* Run test suite: 
    ```
    ./mvnw test
  
    # print out logs
    ./mvnw test -X 
  
    # build from scratch and execute tests
    ./mvnw clean test
    ```

### Troubleshooting
* See [springboot4 repo](https://github.com/chrisbrickey/springboot4) for sample code and notes

## Content Sources

### CSV Files

#### Why use CSV files when I could use a database? 

I'm a lifelong learner, taking on a new subject every few years. Flashcards (physical and digital) are a powerful tool for internalizing concepts and accelerating learning.
I built this app to learn Spring Boot framework and also to use it for studying numerous subjects including French and new programming languages. 

For me, the creation of the content of a flashcard is just as impactful as the review. It forces me to write out the logic, which crystallizes the learning. 
And, as I learn more about a domain, the flashcards must evolve to capture new understanding of connections and contexts that were not apparent at first.

So the requirement of easy editing across hundreds or thousands of human-readable items is paramount for the primary use case of this app. CSV meets this need well while also providing sufficient structure to the data. 

Traditional databases are more scalable, but - for me - the ease of add and edit to a CSV file outweighs the advantages of a separate database for this app. 

#### Parsing Particularities

This app parses CSV files on the server and displays the content in a browser as HTML. Both CSV and HTML have different character string constraints. 

This app performs the following mutations (from CSV to HTML) to handle the gap in constraints:
* backtick -> comma