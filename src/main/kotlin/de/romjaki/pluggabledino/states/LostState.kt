package de.romjaki.pluggabledino.states

import de.romjaki.pluggabledino.*
import de.romjaki.pluggabledino.api.Button
import org.newdawn.slick.Color
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.state.BasicGameState
import org.newdawn.slick.state.StateBasedGame

class LostState : BasicGameState() {
    override fun init(container: GameContainer?, game: StateBasedGame?) {
        game!!
        playAgain.addClickHandler {

            if(lastscore > highscore){

                highscore = lastscore

            }
            game.enterState(GAME)
        }
        back.addClickHandler {

            if(lastscore > highscore){

                highscore = lastscore

            }
            game.enterState(MAINMENU)
        }
    }

    override fun update(container: GameContainer?, game: StateBasedGame?, delta: Int) {
        playAgain.update(container!!.input)
        back.update(container.input)
    }

    override fun getID(): Int =
            LOST

    val back = Button("Back to Main Menu", WIDTH / 2f, HEIGHT / 2f + 50)
    val playAgain = Button("PLAY AGAIN", WIDTH / 2f, HEIGHT / 2f + 100)

    override fun render(container: GameContainer?, game: StateBasedGame?, g: Graphics?) {
        g!!
        g.scale(WIDTH_RATIO, HEIGHT_RATIO)
        g.background = Color.lightGray
        g.drawStringCentered(lastscore.toString(), WIDTH / 2f, HEIGHT /2f )
        g.drawStringCentered("Score:", WIDTH / 2f , HEIGHT / 2f - 40f  )
        if(lastscore > highscore ) {
            g.drawStringCentered("NEUER HIGHSCORE:" + lastscore, WIDTH / 2f, HEIGHT / 2f - 70f)
        }
        else {

            g.drawStringCentered("Highscore:" + highscore, WIDTH / 2f, HEIGHT / 2f - 70f)

        }

        back.draw(g)
        playAgain.draw(g)
    }
}