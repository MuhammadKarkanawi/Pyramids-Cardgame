package service
import entity.Card
import view.Refreshable

/**
 * [Refreshable] implementation that refreshes nothing, but remembers
 * if a refresh method has been called (since last [reset])
 */
class RefreshableTest :Refreshable {


       var refreshAfterStartGameCalled: Boolean = false
            private set

       var refreshAfterRevealCardCalled: Boolean = false
            private set

       var refreshAfterRemovePairCalled: Boolean = false
            private set

       var refreshAfterFlipCalled: Boolean = false
            private set

       var refreshAfterChangePlayerCalled: Boolean = false
            private set

       var refreshAfterPassCalled : Boolean =false
            private set

       var refreshAfterEndGameCalled : Boolean = false
        private set

     /**
     * resets all *Called properties to false
     */
        fun reset() {
            refreshAfterStartGameCalled = false
            refreshAfterRevealCardCalled = false
            refreshAfterRemovePairCalled = false
            refreshAfterFlipCalled = false
            refreshAfterChangePlayerCalled = false
            refreshAfterPassCalled = false
            refreshAfterEndGameCalled = false
        }

        override fun refreshAfterStartGame() {
            refreshAfterStartGameCalled = true
        }

        override fun refreshAfterRevealCard( card: Card) {
            refreshAfterRevealCardCalled = true
        }

         override fun refreshAfterRemovePair(isValid: Boolean) {
            refreshAfterRemovePairCalled = true

        }

        override fun refreshAfterFlip() {
            refreshAfterFlipCalled = true
        }

        override fun refreshAfterChangePlayer() {

            refreshAfterChangePlayerCalled = true
        }

         override fun refreshAfterPass() {
        refreshAfterPassCalled = true
    }

        override fun refreshAfterEndGame( ) {
        refreshAfterEndGameCalled = true
    }
}

