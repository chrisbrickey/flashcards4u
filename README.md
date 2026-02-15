# flashcards4u
[www.flashcards4u.com](http://www.flashcards4u.com) is a test-driven, Java Spring Boot app. 
It can load content on any subject into digital flashcards for studying convenience. 

I built flashcards4u to learn the Spring Boot framework, but it was also instrumental in achieving lifetime
certification in French at B2 level. As of 2026, the hosted app serves my english-to-french content.

Click the card to toggle between front and back. Click the 'next' button to view the subsequent card.
<p align="center">
  <img src="./docs/images/flashcards4u-two-screens-compressed.jpg" height="300" style="margin-right: 10px;" />
</p>

## Project Structure
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

## Development

### Dependencies
* Java 21
* Spring Boot 3.4.0
* Maven 3

Validate your environment:
```
java -version    # must be 21+

./mvnw -version  # verify Maven wrapper works
```

### Build
```
./mvnw clean package

java -jar target/flashcards4u-4.0.0-SNAPSHOT.jar
```

### Configuration
The app uses Spring Boot defaults. 
To customize settings, edit `src/main/resources/application.properties`. For example, add `server.port=9090`.

### Run
* From command line: `./mvnw spring-boot:run`
* Navigate to `http://localhost:8080/` in browser

### Testing
    ```
    ./mvnw test

    # run a single test class
    ./mvnw test -Dtest=DeckServiceTest

    # print out logs
    ./mvnw test -X

    # build from scratch and execute tests
    ./mvnw clean test
    ```

### Troubleshooting
* **`./mvnw: Permission denied`:** The Maven wrapper script needs execute permission. Run `chmod +x mvnw`.
* **CSV file not found at runtime:** The filepath passed to the `/v1/deck` endpoint is resolved from the classpath root. Ensure the CSV file is in `src/main/resources/static/csv/` and the filepath matches (e.g., `static/csv/flashcard_content.csv`).
* **CSV parsing errors:** Verify the CSV has the required header (`question,answer,category`) and that no field values contain `<`, `>`, or `&`.

## Content Source

Content is served from CSV files that anyone can type from scratch or generate using LLM agents.
The structure is extremely constrained: three required fields, no conversion of values to types.

### To change the content source
- Add a CSV file with the following categories to `src/main/resources/static/csv/`.
- Declare a new key-value pair in `DECK_PATHS` that routes to the csv file.
- Update the url on `source.js` to use the new key.

The below example would serve three flashcards to the frontend.
```csv
question,answer,category
a cat,un chat,nouns
demanding,exigeant(e),adjectives
thirty,trente,numbers
```

### API

The backend exposes an endpoint for retrieving a deck (the content source):
- **`GET /v1/decks/{deckName}`**

For example: GET /v1/decks/english-to-french.

It returns a shuffled deck of flashcards parsed from the CSV file associated with the `deckName`.

Example response:
```json
{
  "cards": [
    { "id": 1, "question": "a cat", "answer": "un chat", "category": "nouns" },
    { "id": 2, "question": "demanding", "answer": "exigeant(e)", "category": "adjectives" },
    { "id": 3, "question": "thirty", "answer": "trente", "category": "numbers" }]
   }
```

- Returns `400` error with informative message if the url does not route to a key in the `DECK_PATH`.

### Character restrictions and transformations

CSV and HTML have different character constraints. So some things that could be stored in a CSV file cannot be served well via HTML and vice-versa.

These characters are not allowed in the CSV files to ensure readability in the browser.
* `<`
* `>`
* `&`

This app performs the following mutation to permit the display of commas in the browser, which are reserved as separators in CSV files.
* backtick -> comma

### Why use CSV files instead of a database? 

TLDR: eases CRUD operations across hundreds or thousands of human-readable items

For me, the creation of a flashcard is just as impactful as the review. 
It forces me to write out the logic, which crystallizes the learning.
And the flashcards should evolve to capture new understanding as more is learned about a domain.

So the desire for a digital solution with easy add/edit capability drove the development of this app.
CSV meets this need well while providing sufficient structure to the data. 
It's easy for any human user to update, regardless of technical background. 
The use of a separate database would inhibit all CRUD operations by forcing the user to make changes through a controlled interface.

## Future Development
* Integration with Generative AI service to allow the user to gain additional context about the topic of a flashcard without leaving the app.
* Add UI filters for category and content source. 