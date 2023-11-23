package view

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
import java.awt.Color

/**
 * Represents the graphical user interface (GUI) for the card game table.
 *
 * @param rootService The root service providing access to game-related services.
 */
class GameTableScene(private val rootService: RootService) : BoardGameScene(1920, 1080),
    Refreshable {

    // Properties for tracking clicked cards and counter
    private var firstClickedCard: Card? = null
    private var secondClickedCard: Card? = null
    private var clickCounter: Int = 0  // Add this line

    // UI components
    private val pyramidStack = LabeledStackView(posX = 600, posY = 50)
    private val drawStack = LabeledStackView(posX = 100, posY = 500).apply {
        // Event handling for draw stack click
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
        // Event handling for pass label click
        font = Font(size = 16)
        visual = ColorVisual(Color.LIGHT_GRAY)

        onMouseClicked = {
            rootService.currentGame?.let { pyramide ->
                rootService.playerActionService.pass()
            }
        }
    }
    private val reserveStack = LabeledStackView(posX = 300, posY = 500)

    private val cardMap: BidirectionalMap<Card, CardView> = BidirectionalMap()

    // Player labels and score displays
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

    // Initialization block
    init {
        // Set fonts and visuals for player labels and score display
        player1Label.font = Font(size = 20)
        player1ScoreDisplay.font = Font(size = 18)
        player2Label.font = Font(size = 20)
        player2ScoreDisplay.font = Font(size = 18)

        player1Label.visual = ColorVisual(Color.WHITE)
        player1ScoreDisplay.visual = ColorVisual(Color.WHITE)
        player2Label.visual = ColorVisual(Color.WHITE)
        player2ScoreDisplay.visual = ColorVisual(Color.WHITE)

        // Set background color
        background = ColorVisual(Color(0, 102, 0)) // Dark Green background

        // Add components to the scene
        addComponents(
            pyramidStack, drawStack, reserveStack,
            player1Label, player1ScoreDisplay,
            player2Label, player2ScoreDisplay, passLabel
        )
    }
    /**
     * Function to refresh the UI after starting a new game.
     */

    override fun refreshAfterStartGame() {

        // Implementation of the refresh logic after starting the game
        val pyramide = rootService.currentGame
        checkNotNull(pyramide) { "No started game found." }

        // Reset scores to 0 für all players.
        pyramide.playerList.forEach { it.score = 0 }

        // Clear card map and UI components
        cardMap.clear()
        clearComponents()
        // Add UI components for game elements
        addComponents(
            pyramidStack, drawStack, reserveStack,
            player1Label, player1ScoreDisplay,
            player2Label, player2ScoreDisplay, passLabel
        )

        // Initialize the draw stack and pyramid stack views with card images
        val cardImageLoader = CardImageLoader()

        initializeStackView(pyramide.drawStack.cards,drawStack,cardImageLoader)
        initializeStackView1(pyramide.pyramid, pyramidStack, cardImageLoader)

        // Uncomment the following line if there is a reserve stack in the game
        //initializeStackView(pyramide.reserveStack, reserveStack, cardImageLoader)

        // Update player labels and score displays
        player1Label.text = "Player 1: ${pyramide.indexPlayer == 0}"
        player1ScoreDisplay.text = "Score: ${pyramide.playerList[0].score}"

        player2Label.text = "Player 2: ${pyramide.indexPlayer == 1}"
        player2ScoreDisplay.text = "Score: ${pyramide.playerList[1].score}"
    }

    /**
     * Initializes the view for a card stack with the given list of cards.
     *
     * @param stack The list of cards to be displayed in the stack.
     * @param stackView The view representing the card stack.
     * @param cardImageLoader The loader for card images.
     */
    private fun initializeStackView(
        stack: MutableList<Card>,
        stackView: LabeledStackView,
        cardImageLoader: CardImageLoader
    ) {
        // Clear the existing content in the stack view
        stackView.clear()

        // Iterate through each card in the stack and create a corresponding CardView
        stack.forEachIndexed { index, card ->
            // Create a CardView with specified dimensions and front/back images
            val cardView = CardView(
                height = 100,
                width = 80,
                front = ImageVisual(cardImageLoader.frontImageFor(card!!.suit, card.value)),
                back = ImageVisual(cardImageLoader.backImage)
            )

            // Set the position of the card view within the stack view
            cardView.posX = stackView.posX + (index * 1)
            cardView.posY = stackView.posY + (index * 1)

            // Map the card to its corresponding view and add it to the stack view
            cardMap.add(card to cardView)
            stackView.add(cardView)
        }
    }

    /**
     * Initializes the view for a card stack representing a pyramid structure.
     *
     * @param pyramid The pyramid object containing the card layout information.
     * @param stackView The view representing the card stack.
     * @param cardImageLoader The loader for card images.
     */
    private fun initializeStackView1(pyramide: Pyramid, stackView: LabeledStackView, cardImageLoader: CardImageLoader) {

        // Clear the existing content in the stack view
        stackView.clear()

        // Define dimensions for the cards and the row height
        val cardWidth = 80
        val cardHeight = 100
        val rowHeight = 120

        // Iterate through each row in the pyramid
        for (row in pyramide.cards.indices) {
            // Calculate the width of the row based on the card width
            val rowWidth = row * cardWidth
            // Iterate through each column in the current row
            for (col in pyramide.cards[row].indices) {
                // Obtain the card at the current row and column
                val card = pyramide.cards[row][col]
                // Create a CardView for the current card
                val cardView = createCardView(card, cardWidth, cardHeight, cardImageLoader)

                // Add the card to the bidirectional map
                cardMap.add(card to cardView)

                // Set the position of the card view within the stack view and add it
                setPositionAndAddComponents(stackView, row, col, rowWidth, rowHeight, cardView)
                // Add click handling for the first and last cards in each row
                if (col == 0 || col == pyramide.cards[row].size - 1) {
                    cardView.apply {
                        showFront()
                        onMouseClicked = {
                            if (clickCounter == 0) {
                                handleFirstCardClick(card)
                            } else {
                                handleSecondCardClick(card)
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Handles the click event for the first card in a pair.
     *
     * @param clickedCard The card that was clicked.
     */
    private fun handleFirstCardClick(clickedCard: Card) {
        // Check if it is the first click in a pair
        if (clickCounter == 0) {
            // Set the clicked card as the first clicked card and increment the click counter
            firstClickedCard = clickedCard
                clickCounter++
        }

    }

    /**
     * Handles the click event for the second card in a pair.
     *
     * @param clickedCard The card that was clicked.
     */
    private fun handleSecondCardClick(clickedCard: Card) {
        // Output information about the second clicked card to the console
        println("Second card clicked: $clickedCard")
        // Reset the click counter
        clickCounter = 0

        // Set the clicked card as the second clicked card
        secondClickedCard = clickedCard
        // Remove the pair from the pyramid through the player action service
        rootService.playerActionService.removePair(firstClickedCard!!, secondClickedCard!!)
        // Output information about the removed pair to the console
        println("Removed pair from pyramid: $firstClickedCard, $secondClickedCard")
    }

    /**
     * Creates a CardView based on the given card, dimensions, and card image loader.
     *
     * @param card The card for which to create the view.
     * @param width The width of the card view.
     * @param height The height of the card view.
     * @param cardImageLoader The loader for card images.
     * @return The created CardView.
     */
    private fun createCardView(
        card: Card,
        width: Int,
        height: Int,
        cardImageLoader: CardImageLoader
    ): CardView {
        return CardView(
            height = height,
            width = width,
            front = ImageVisual(cardImageLoader.frontImageFor(card.suit, card.value)),
            back = ImageVisual(cardImageLoader.backImage)

        )
    }
    /**
     * Sets the position for a CardView within a stack view and adds it as a component.
     *
     * @param stackView The stack view to which the card view belongs.
     * @param row The row index of the card in the stack.
     * @param col The column index of the card in the stack.
     * @param rowWidth The total width occupied by the row.
     * @param rowHeight The height of each row in the stack.
     * @param cardView The CardView to position and add to the stack view.
     */
    private fun setPositionAndAddComponents(
        stackView: LabeledStackView,
        row: Int,
        col: Int,
        rowWidth: Int,
        rowHeight: Int,
        cardView: CardView
    ) {
        // Calculate the X and Y positions for the card within the stack view
        val posX = stackView.posX + (stackView.width - rowWidth) / 2 + (col * cardView.width)
        val posY = stackView.posY + (row * rowHeight)

        // Set the calculated positions for the card view
        cardView.posX = posX
        cardView.posY = posY

        // Add the card view as a component to the stack view
        addComponents(cardView)
    }

    /**
     * Updates the UI after a player has passed their turn.
     */
    override fun refreshAfterPass() {
        // Retrieve the current game from the root service
        val game = rootService.currentGame
        // Check if a game is currently in progress
        checkNotNull(game) { "No started game found." }
        // Print a message indicating that the player has passed
        print("player has passed ")
    }

    /**
     * Updates the UI after a pair of cards has been successfully removed.
     *
     * @param isValid Indicates whether the removal of the pair is valid.
     */
    override fun refreshAfterRemovePair(isValid: Boolean) {
        // Retrieve the current game from the root service
        val game = rootService.currentGame
        // Check if a game is currently in progress
        checkNotNull(game) { "No started game found." }
        // Obtain the pyramid layout from the current game
        val pyramid = game.pyramid.cards
        // Check if the removal of the pair is valid
        if(isValid){
            // Retrieve the CardView instances corresponding to the removed cards
            val card1 = cardMap.forward(firstClickedCard!!)
        val card2 = cardMap.forward(secondClickedCard!!)
        // Remove the CardViews from the parent view
        card1.removeFromParent()
        card2.removeFromParent()
        // Iterate through each row in the pyramid
            for (i in pyramid.indices) {
                // Retrieve the CardView instances corresponding to the first and last cards in the current row
                val firstCard = cardMap.forward(pyramid[i].first())
                val lastCard = cardMap.forward(pyramid[i].last())
                // Show the front of the first and last cards in the row and set click handling
                firstCard.apply { showFront()
                    val card = cardMap.backward(this)
                    onMouseClicked = {
                    if (clickCounter == 0) {
                        handleFirstCardClick(card)
                    } else {
                        handleSecondCardClick(card)
                    }
                }}
            lastCard.apply {
                showFront()
                val card = cardMap.backward(this)
                onMouseClicked = {
                    if (clickCounter == 0) {
                        handleFirstCardClick(card)
                    } else {
                        handleSecondCardClick(card)
                    }
                }
            }
        }
      }

        // Set the clicked cards to null after removal
        firstClickedCard = null
        secondClickedCard = null
        /*} else {
                // Handle the case where one or both clicked cards are null
                println("One or both clicked cards are null. Check the card selection logic.")
            }
    */
    }

    /**
     * Updates the UI after revealing a card, moving it to the reserve stack.
    *
    * @param card The card to be revealed.
    */
    override fun refreshAfterRevealCard(card: Card) {
        // Retrieve the current game from the root service
        val pyramide = rootService.currentGame
        // Check if a game is currently in progress
        checkNotNull(pyramide) { "No started game found." }

        // Check if a card has been drawn before attempting to use it
        if (card != null) {
            // Retrieve the CardView instance corresponding to the revealed card
            val cardView = cardMap.forward(card)

            // Move the CardView to the reserve stack and reveal it
            moveCardView(cardView, reserveStack, true)
        }
    }

    /**
     * Moves a CardView from its current position to the specified stack view.
     *
     * @param cardView The CardView to be moved.
     * @param toStack The target stack view to which the card will be moved.
     * @param flip Specifies whether to flip the card before moving it (default is false).
     */
    private fun moveCardView(cardView: CardView, toStack: LabeledStackView, flip: Boolean = false) {
        // If flip is true, toggle between showing the front and back of the card
        if (flip) {
            when (cardView.currentSide) {
                CardView.CardSide.BACK -> cardView.showFront()
                CardView.CardSide.FRONT -> cardView.showBack()
            }
        }
        // Set click handling for the moved card
        cardView.apply {
            val card = cardMap.backward(this)
            onMouseClicked = {
                println("clicked")
                if (clickCounter == 0) {
                    handleFirstCardClick(card)
                } else {
                    handleSecondCardClick(card)
                }
            }
        }
        // Remove the card from its current parent view
        cardView.removeFromParent()
        // Add the card to the target stack view
        toStack.add(cardView)
    }
    /**
     * Updates the UI after changing the active player in the game.
     */
    override fun refreshAfterChangePlayer() {
        // Retrieve the current game from the root service
        val pyramide = rootService.currentGame
        // Check if a game is currently in progress
        checkNotNull(pyramide) { "No started game found." }

        // Update player labels and score displays based on the current active player
        player1Label.text = "Player 1: ${pyramide.currentPlayer == pyramide.playerList[0]}"
        player1ScoreDisplay.text = "Score: ${pyramide.playerList[0].score}"

        player2Label.text = "Player 2: ${pyramide.currentPlayer == pyramide.playerList[1]}"
        player2ScoreDisplay.text = "Score: ${pyramide.playerList[1].score}"
    }
    /**
     * Updates the UI after flipping revealed cards in the pyramid.
     */
    override fun refreshAfterFlip() {
        // Retrieve the current game from the root service
        val game = rootService.currentGame
        // Check if a game is currently in progress
        checkNotNull(game) { "No started game found." }
        // Retrieve the pyramid structure from the current game
        val pyramid = game.pyramid
        val rows = pyramid.cards.size

        // Iterate through each row in the pyramid
        for (row in 0 until rows) {
            // Find the first and last revealed cards in the current row
            val firstCard = pyramid.cards[row].firstOrNull { it != null && it.isRevealed }
            val lastCard = pyramid.cards[row].lastOrNull { it != null && it.isRevealed }

            // If the first revealed card is found, show its front side
            if (firstCard != null) {
                try {
                    val cardView = cardMap.forward(firstCard)
                    cardView.showFront()
                } catch (e: NoSuchElementException) {
                    println("Error: Key $firstCard not found in the bidirectional map.")

                }
            }
            // If the last revealed card is found, show its front side
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
