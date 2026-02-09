# flashcards4u
[www.flashcards4u.com](http://www.flashcards4u.com) is a test-driven, Java Spring Boot app. 
It can load content on any subject into digital flashcards for studying convenience. 

I built flashcards4u to learn the Spring Boot framework, but it was also instrumental in achieving lifetime
certification in French at B2 level. As of 2026, the hosted app serves my english-to-french content.

Click the card to toggle between front and back. Click the 'next' button to view the subsequent card.
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
Content is served from CSV files that anyone can type from scratch or generate using LLM agents.
The structure is extremely constrained: three required fields, no conversion of values to types.

Adding the following CSV file to `src/main/resources/static/csv/` 
and updating the filepath on `source.js` would serve three flashcards to the frontend.

```csv
question,answer,category
a cat,un chat,nouns
demanding,exigeant(e),adjectives
thirty,trente,numbers
```

#### Why use CSV files instead of a database? 

TLDR: eases CRUD operations across hundreds or thousands of human-readable items

For me, the creation of a flashcard is just as impactful as the review. 
It forces me to write out the logic, which crystallizes the learning.
And the flashcards should evolve to capture new understanding as more is learned about a domain.

So the desire for a digital solution with easy add/edit capability drove the development of this app.
CSV meets this need well while providing sufficient structure to the data. 
It's easy for any human user to update, regardless of technical background. 
The use of a separate database would inhibit all CRUD operations by forcing the user to make changes through a controlled interface.

### Character restrictions and transformations

CSV and HTML have different character constraints. So some things that could be stored in a CSV file cannot be served well via HTML and vice-versa.

These characters are not allowed in the CSV files to ensure readability in the browser.
* `<`
* `>`
* `&`

This app performs the following mutation to permit the display of commas in the browser, which are reserved as separators in CSV files.
* backtick -> comma 