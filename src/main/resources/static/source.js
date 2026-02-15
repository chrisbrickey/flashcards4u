$(document).ready(function () {

var colors=["#086375", "#0C91AC", "#546a76","#88a0a8","#679436","#A5BE00", "#000000", "#4C5B5C"];
var cardState;
var currentCard=0;
var store=new Array;

start();

function start(){
    $.ajax({
        type: "GET",
        url: "/v1/decks/english-to-french",
        cache: false,
        success: function(result) {
            playDeck(result)
        }
    });
}

function playDeck(deckData) {
  buildStore(deckData['cards']);
  showFlashCard();
}

function buildStore(cards) {
   for (var i=0; i<cards.length; i++) {
       var card = cards[i];
       store[i] = {
           question: card['question'],
           answer: card['answer'],
           category: card['category']
       };
   }
}

function showFlashCard(){
 cardState=0;
 var color1=colors[0];
 var color2=colors[1]
 $("#categoryArea").empty();
 $("#cardArea").empty();
 $("#categoryArea").append('<div id="category">' + 'category: ' + store[currentCard]['category'] + '</div>');
 $("#cardArea").append('<div id="cardTop" class="card">' + store[currentCard]['question'] + '</div>');
 $("#cardArea").append('<div id="cardBottom" class="card">' + store[currentCard]['answer'] + '</div>');
 $("#cardTop").css("background-color",color1);
 $("#cardBottom").css("background-color",color2);
 $("#cardBottom").css("top","420px");
 $("#cardArea").on("click",function(){
  if(cardState!=1){
   cardState=1;
   $("#cardTop").animate({top: "-=420"}, 150, function() {cardState=0;toggleTopToBottom();});
   $("#cardBottom").animate({top: "-=420"}, 150, function() {toggleBottomToTop();});
  }
 });
 currentCard++;
 $("#buttonArea").empty();
 $("#buttonArea").append('<div id="nextButton">next</div>');
 $("#nextButton").on("click",function(){
  if(currentCard<store.length){showFlashCard();}
  else{displayGameOver();}
 });
}

function toggleTopToBottom(){
 if($("#cardTop").position().top==-420){$("#cardTop").css("top","420px");};
}

function toggleBottomToTop(){
 if($("#cardBottom").position().top==-420){$("#cardBottom").css("top","420px");};
}

function displayGameOver(){
 $("#categoryArea").empty();
 $("#buttonArea").empty();
 $("#cardArea").empty();
 $("#cardArea").append('<div id="gameOver">game over</div>');
}
});