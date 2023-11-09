package service
import AbstractRefreshingService
import entity.*
import view.Refreshable
import service.*
class PlayerActionService(val rootService:RootService):AbstractRefreshingService(){
    fun pass()
    {	//get current game from root service
        var game = rootService.gameService
        checkNotNull(game)
        //get opponentPassed Boolean

        var pyramide = rootService.currentGame
        checkNotNull(pyramide)
        var opponentPassed=pyramide.opponentPassed
        //end game if previous Player already chose Pass
        if (opponentPassed)
        { game.endGame()}
        //otherwise switch to the other player and set Boolean
        else
        { opponentPassed = true
            game.changePlayer() }
        //refresh view
        onAllRefreshables{refreshAfterPass()}
       }

    fun removePair(card1: Card, card2: Card) {
        var game = rootService.gameService
        checkNotNull(game)
        //check validity of cards choosen and save result
        var isValid: Boolean = game.checkCardChoice(card1, card2)
        //get current game from rootService
        var pyramide = rootService.currentGame
        checkNotNull(pyramide)
        //get current player
        var player = pyramide.playerList[pyramide.currentPlayer]

        //remove cards
        if (isValid) { //award points
            if (card1.wert.toString() == "ACE" || card2.wert.toString() == "ACE") {
                player.setScore(1) }
            else { player.setScore(2) }

            removePair(card1, card2)
            //update opponentPassed Boolean
            onAllRefreshables { refreshAfterRemovePair(isValid) }
            }
            //check if Pyramide.Pyramid entity has cards left and end game if not
         if (game.isEmpty())
         { game.endGame()
           onAllRefreshables { refreshAfterEndGame()}
         }

         else {
            //Reveal any new cards that need to be revealed
            game.flipCards(card1)
            onAllRefreshables { refreshAfterRevealCard() }
             //change to next player
            game.changePlayer()
            //refresh view
            onAllRefreshables {refreshAfterChangePlayer() }
        }
    }

     fun removeCards(card1: Card, card2: Card){
        var pyramide = rootService.currentGame
         checkNotNull(pyramide)
        var pyramideCards : Array<Array<Card?>> = pyramide.pyramidCards
        var game = rootService.gameService
        checkNotNull(game)
        if (game.checkCardChoice(card1,card2)){
        }
        //if erste karte in reserveStack gefunden
         if (card1  in game.reserveStack  )
            {game.reserveStack.removeAt(game.reserveStack.size -1)
              //if zweite karte in Pyramid gefunden
                for( i in 0.. 6){
                    if(card2== pyramideCards[i][0] )
                    {pyramideCards[i][0]= null}
                    else if (card2==pyramideCards[i][pyramideCards[i].size-1])
                    {pyramideCards[i][pyramideCards[i].size-1]=null }
                }}
          //if erste karte in reserveStack gefunden
         else if(card2 in game.reserveStack)
              {game.reserveStack.removeAt(game.reserveStack.size -1)
          //if erste karte in Pyramid gefunden
              for( i in 0.. 6)
                {if(card1 == pyramideCards[i][0] )
                  {pyramideCards[i][0]= null}
                else if (card1 == pyramideCards[i][pyramideCards[i].size-1])
                        {pyramideCards[i][pyramideCards[i].size-1]=null }}
              } }


    fun revealCard(){
        //get current game from root service
        var pyramide = rootService.currentGame
        checkNotNull(pyramide)
       //get current player from root service
        var currentPlayer = pyramide.currentPlayer
       //get DrawStack from root service
        var drawStack = pyramide.toDrawStack
        checkNotNull(drawStack)
        //check if DrawStack has at least one card
        var drawStackSize :Int = pyramide.toDrawStack.size

        fun isValid() : Boolean {return drawStackSize  !=0}

        if (isValid()){
            //player reveals the first card from the drawing stack
            var drawnCard :Card? = drawStack[0]
            checkNotNull(drawnCard)
            drawnCard.isRevealed  = true
            //player removes the first card from the stack
            drawStack.removeAt(0)
            //all cards in the reserve Stack get pushed to the right
            increase length of reserveStack List by 1
            for (i from length of reserveStack down to 1){
                reserveStack[i] = reserveStack[i - 1]
                //new card is put on top of reserveStack
                reserveStack[0] = drawnCard
                //reset Pass counter
                Pyramide.opponentPassed = false
                //change player
                gameService.changePlayer()}}
        onAllRefreshables{refreshAfterRevea}
    }
    //refresh view


}
