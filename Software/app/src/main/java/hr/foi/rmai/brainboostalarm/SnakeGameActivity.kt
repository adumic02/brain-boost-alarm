package hr.foi.rmai.brainboostalarm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.foi.rmai.brainboostalarm.view.SnakeGameView

class SnakeGameActivity : AppCompatActivity() {

    private lateinit var snakeGameView: SnakeGameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        snakeGameView = SnakeGameView(this)
        setContentView(snakeGameView)
    }

    override fun onPause() {
        super.onPause()
        snakeGameView.pause()
    }

    override fun onResume() {
        super.onResume()
        snakeGameView.resume()
    }
}
