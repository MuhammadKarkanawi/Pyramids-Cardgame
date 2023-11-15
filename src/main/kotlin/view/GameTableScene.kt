package view

import tools.aqua.bgw.animation.DelayAnimation
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.components.gamecomponentviews.CardView
import service.RootService
import entity.*
import service.CardImageLoader
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual


class GameTableScene(private val rootService: RootService) : BoardGameScene() , Refreshable{

}