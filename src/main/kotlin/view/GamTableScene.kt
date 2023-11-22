import entity.*
import entity.Pyramid
import service.CardImageLoader
import service.RootService
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import view.LabeledStackView
import view.Refreshable
import java.awt.Color

class GameTableScene(private val rootService: RootService) : BoardGameScene(1920, 1080),
    Refreshable {
    private var firstClickedCard: Card? = null
    private var secondClickedCard: Card? = null
    private var isReserveStackSelected: Boolean = false
    private var clickCounter: Int = 0  // Add this line
    private var reserveCard: Card? = null


    private val pyramidStack = LabeledStackView(posX = 600, posY = 50)
    private val drawStack = LabeledStackView(posX = 100, posY = 500).apply {
        onMouseClicked = {
            rootService.currentGame?.let { pyramide ->
                rootService.playerActionService.revealCard()
            }
        }
    }
    // Add a new label for passing
    private val passLabel = Label(
        width = 100, height = 40,
        posX = 1700, posY = 500,
        text = "Pass"
    ).apply {
        font = Font(size = 16)
        visual = ColorVisual(Color.LIGHT_GRAY)

        onMouseClicked = {
            rootService.currentGame?.let { pyramide ->
                rootService.playerActionService.pass()
            }
        }
    }
    private val reserveStack = LabeledStackView(posX = 300, posY = 500).apply {
        onMouseClicked = {
            isReserveStackSelected=true
            reserveCard = rootService.currentGame?.peek()
            println("$isReserveStackSelected")

        }
    }


    private val cardMap: BidirectionalMap<Card, CardView> = BidirectionalMap()

    private val player1Label = Label(
        width = 300, height = 35,
        posX = 1500, posY = 50,
        text = "Player 1:"
    )

    private val player1ScoreDisplay = Label(
        width = 300, height = 35,
        posX = 1500, posY = 85,
        text = "Score: 0"
    )

    private val player2Label = Label(
        width = 300, height = 35,
        posX = 1500, posY = 130,
        text = "Player 2:"
    )

    private val player2ScoreDisplay = Label(
        width = 300, height = 35,
        posX = 1500, posY = 165,
        text = "Score: 0"
    )

    init {
        player1Label.font = Font(size = 20)
        player1ScoreDisplay.font = Font(size = 18)
        player2Label.font = Font(size = 20)
        player2ScoreDisplay.font = Font(size = 18)

        player1Label.visual = ColorVisual(Color.WHITE)
        player1ScoreDisplay.visual = ColorVisual(Color.WHITE)
        player2Label.visual = ColorVisual(Color.WHITE)
        player2ScoreDisplay.visual = ColorVisual(Color.WHITE)

        background = ColorVisual(Color(0, 102, 0)) // Dark Green background

        addComponents(
            pyramidStack, drawStack, reserveStack,
            player1Label, player1ScoreDisplay,
            player2Label, player2ScoreDisplay,passLabel
        )
    }

    override fun refreshAfterStartGame() {
        val pyramide = rootService.currentGame
        checkNotNull(pyramide) { "No started game found." }

        // Reset scores to 0
        pyramide.playerList.forEach { it.score = 0 }

        cardMap.clear()
        pyramidStack.clear()
        drawStack.clear()
        reserveStack.clear()

        val cardImageLoader = CardImageLoader()

        //initializeStackView(rootService.currentGame.drawStack,drawStack)
        initializeStackView1(pyramide.pyramid , pyramidStack,cardImageLoader)

         //initializeStackView(pyramide.reserveStack, reserveStack, cardImageLoader)

        for (row in rootService.currentGame?.pyramid?.cards!!.indices) {
            for (col in rootService.currentGame!!.pyramid.cards[row].indices) {
                val card = rootService.currentGame!!.pyramid.cards[row][col]
                if (card != null) {
                    addComponents(cardMap.forward(card))
                }
            }
        }

        // Update player labels and score displays
        player1Label.text = "Player 1: ${pyramide.indexPlayer == 0}"
        player1ScoreDisplay.text = "Score: ${pyramide.playerList[0].score}"

        player2Label.text = "Player 2: ${pyramide.indexPlayer == 1}"
        player2ScoreDisplay.text = "Score: ${pyramide.playerList[1].score}"

    }


    private fun initializeStackView(stack: MutableList<Card?>, stackView: LabeledStackView, cardImageLoader: CardImageLoader) {
        stackView.clear()

        stack.reversed().forEachIndexed { index, card ->
            val cardView = CardView(
                height = 100,
                width = 80,
                front = ImageVisual(cardImageLoader.frontImageFor(card!!.suit, card.value)),
                back = ImageVisual(cardImageLoader.backImage)
            )

            cardView.posX = stackView.posX + (index * 1)
            cardView.posY = stackView.posY + (index * 1)


        }
    }


    private fun initializeStackView1(pyramide: Pyramid, stackView: LabeledStackView, cardImageLoader: CardImageLoader) {
        stackView.clear()

        val cardWidth = 80
        val cardHeight = 100
        val rowHeight = 120

        for (row in pyramide.cards.indices) {
            val rowWidth = row * cardWidth
            for (col in pyramide.cards[row].indices) {
                val card = pyramide.cards[row][col]
                if (card != null) {
                    val isOuterCard = isOuterCard(pyramide!!.cards, row, col)
                    val clickableLabel = createClickableLabel(card, cardWidth, cardHeight)
                    val cardView = createCardView(card, cardWidth, cardHeight, isOuterCard, cardImageLoader)

                    // Add the card to the bidirectional map
                    cardMap.add(card to cardView)

                    setPositionAndAddComponents(stackView, row, col, rowWidth, rowHeight, clickableLabel, cardView)
                }
            }
        }
    }


    private fun createClickableLabel(card: Card, width: Int, height: Int): Label {
        return Label(height = height, width = width).apply {
            onMouseClicked = {
                if (clickCounter == 0) {
                    handleFirstCardClick(card)
                } else {
                    handleSecondCardClick(card)
                }
            }
        }
    }

    private fun handleFirstCardClick(clickedCard: Card) {
        if (clickCounter == 0) {
            // First click
            if (!isReserveStackSelected) {
                // If reserve stack is not selected, set the first clicked card and increment click counter
                firstClickedCard = clickedCard
                println("First card clicked: $firstClickedCard")
                clickCounter++
            } else {
                // If reserve stack is selected, remove pair with the first card from the reserve stack
                rootService.currentGame?.reserveStack?.let {
                    val reserveCard = it.peek()
                    secondClickedCard=clickedCard
                    println("Card from the pyramid :$secondClickedCard")
                    println("Reserve stack card clicked: $reserveCard")
                    rootService.playerActionService.removePair(reserveCard!!, clickedCard)
                }
                isReserveStackSelected = false // Reset the flag
            }
        } else {
            // Second click
            handleSecondCardClick(clickedCard)
        }
    }

    private fun handleSecondCardClick(clickedCard: Card) {
        println("Second card clicked: $clickedCard")
        clickCounter = 0
        secondClickedCard = clickedCard
        // Remove the pair from the pyramid
        rootService.playerActionService.removePair(firstClickedCard!!, secondClickedCard!!)
        println("Removed pair from pyramid: $firstClickedCard, $secondClickedCard")
    }



    private fun createCardView(
        card: Card,
        width: Int,
        height: Int,
        isOuterCard: Boolean,
        cardImageLoader: CardImageLoader
    ): CardView {
        return CardView(
            height = height,
            width = width,
            front = ImageVisual(cardImageLoader.frontImageFor(card.suit, card.value)),
            back = ImageVisual(cardImageLoader.backImage)

        )
    }

    private fun setPositionAndAddComponents(
        stackView: LabeledStackView,
        row: Int,
        col: Int,
        rowWidth: Int,
        rowHeight: Int,
        clickableLabel: Label,
        cardView: CardView
    ) {
        val posX = stackView.posX + (stackView.width - rowWidth) / 2 + (col * cardView.width)
        val posY = stackView.posY + (row * rowHeight)

        clickableLabel.posX = posX
        clickableLabel.posY = posY

        cardView.posX = posX
        cardView.posY = posY

        addComponents(cardView, clickableLabel)
    }


    private fun isOuterCard(cards: MutableList<MutableList<Card?>>, row: Int, col: Int): Boolean {
        val leftAdjacent = if (col > 0) cards[row][col - 1] != null else false
        val rightAdjacent = if (col < cards[row].size - 1) cards[row][col + 1] != null else false

        return !leftAdjacent || !rightAdjacent
    }


    override fun refreshAfterPass() {
        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }
        print("player has passed ")
    }


    override fun refreshAfterRemovePair(isValid :Boolean  ) {
        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }

        if (isReserveStackSelected) {
            // Case: Pair removed involving the reserve stack
            if (reserveCard != null) {
                println("Removed pair involving reserve stack: $reserveCard, $secondClickedCard")
                moveCardView(cardMap.forward(reserveCard!!),pyramidStack,true)
                removeAndDispose(secondClickedCard!!)
                isReserveStackSelected = false
            } else {
                println("Error: Reserve stack card is null.")
            }
        } else {
            // Case: Pair removed from the pyramid
            // Check if both clicked cards are not null
            if (firstClickedCard != null && secondClickedCard != null) {
                // Remove and dispose the clicked cards if they are present in the map
                removeAndDispose(firstClickedCard!!)
                removeAndDispose(secondClickedCard!!)

                // Set the clicked cards to null after removal
                firstClickedCard = null
                secondClickedCard = null
            } else {
                // Handle the case where one or both clicked cards are null
                println("One or both clicked cards are null. Check the card selection logic.")
            }
        }
    }


    private fun removeAndDispose(card: Card) {
        if (cardMap.containsForward(card)) {
            val cardView = cardMap.forward(card)

            // Remove the card from the map
            cardMap.removeForward(card)

            // Remove and dispose the cardView
            removeComponents(cardView)
        } else {
            // Handle the case where the card is not found in the map
            println("Error: Card not found in the bidirectional map.")
        }
    }



    override fun refreshAfterRevealCard(card: Card) {
        val pyramide = rootService.currentGame
        checkNotNull(pyramide) { "No started game found." }

        // Check if a card is drawn before using it
        if (card != null) {
            moveCardView(cardMap.forward(card), reserveStack, true)
        }
    }


    private fun moveCardView(cardView: CardView, toStack: LabeledStackView, flip: Boolean = false) {
        if (flip) {
            when (cardView.currentSide) {
                CardView.CardSide.BACK -> cardView.showFront()
                CardView.CardSide.FRONT -> cardView.showBack()
            }
        }
        cardView.removeFromParent()
        toStack.add(cardView)
    }

    override fun refreshAfterChangePlayer() {
        val pyramide = rootService.currentGame
        checkNotNull(pyramide) { "No started game found." }

        // Update player labels and score displays
        player1Label.text = "Player 1: ${pyramide.currentPlayer == pyramide.playerList[0]}"
        player1ScoreDisplay.text = "Score: ${pyramide.playerList.get(0).score}"

        player2Label.text = "Player 2: ${pyramide.currentPlayer == pyramide.playerList[1]}"
        player2ScoreDisplay.text = "Score: ${pyramide.playerList.get(1).score}"
    }

    override fun refreshAfterFlip() {
        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }

        val pyramid = game.pyramid
        val rows = pyramid.cards.size

        for (row in 0 until rows) {
            val firstCard = pyramid.cards[row].firstOrNull { it != null && it.isRevealed }
            val lastCard = pyramid.cards[row].lastOrNull { it != null && it.isRevealed }

            if (firstCard != null) {
                try {
                    val cardView = cardMap.forward(firstCard)
                    cardView.showFront()
                } catch (e: NoSuchElementException) {
                    println("Error: Key $firstCard not found in the bidirectional map.")

                }
            }
            if (lastCard != null) {
                try {
                    val cardView = cardMap.forward(lastCard)
                    cardView.showFront()
                } catch (e: NoSuchElementException) {
                    println("Error: Key $lastCard not found in the bidirectional map.")

                }
            }
        }
    }


}