package hr.foi.rmai.brainboostalarm.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Random

class SnakeGameView(context: Context, attrs: AttributeSet? = null) : SurfaceView(context, attrs), Runnable, SurfaceHolder.Callback {

    private var thread: Thread? = null
    private var isPlaying = false
    private val paint = Paint()
    private val surfaceHolder: SurfaceHolder = holder.apply { addCallback(this@SnakeGameView) }

    private val snake = ArrayList<Pair<Int, Int>>()
    private var food: Pair<Int, Int>? = null
    private var direction = 0
    private var gridSize = 60
    private var gridCountX = 0
    private var gridCountY = 0
    private var score = 0
    private var isGameOver = false
    private val handler = Handler()

    init {
        // Initialize the game
        snake.add(Pair(10, 10))
        direction = 0
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        gridCountX = width / gridSize
        gridCountY = height / gridSize
        spawnFood()
        resume()
    }

    override fun run() {
        while (isPlaying) {
            update()
            draw()
            control()
        }
    }

    private fun update() {
        if (snake.isEmpty()) return

        val head = snake[0]
        var newHeadX = head.first
        var newHeadY = head.second

        when (direction) {
            0 -> newHeadY -= 1
            1 -> newHeadX += 1
            2 -> newHeadY += 1
            3 -> newHeadX -= 1
        }

        if (newHeadX == food?.first && newHeadY == food?.second) {
            snake.add(0, Pair(newHeadX, newHeadY))
            spawnFood()
            score++
            if (score >= 3) {
                isPlaying = false
                (context as AppCompatActivity).setResult(AppCompatActivity.RESULT_OK)
                (context as AppCompatActivity).finish()
            }
        } else {
            snake.add(0, Pair(newHeadX, newHeadY))
            snake.removeAt(snake.size - 1)
        }

        if (newHeadX < 1 || newHeadY < 1 || newHeadX >= gridCountX - 1 || newHeadY >= gridCountY - 1) {
            isPlaying = false
            isGameOver = true
            showGameOverMessage()
            handler.postDelayed({ restartGame() }, 3000)
        }

        for (i in 1 until snake.size) {
            if (snake[i].first == newHeadX && snake[i].second == newHeadY) {
                isPlaying = false
                isGameOver = true
                showGameOverMessage()
                handler.postDelayed({ restartGame() }, 3000)
            }
        }
    }

    private fun draw() {
        if (surfaceHolder.surface.isValid) {
            val canvas: Canvas = surfaceHolder.lockCanvas()
            canvas.drawColor(Color.BLACK)

            paint.color = Color.WHITE
            paint.style = Paint.Style.FILL
            canvas.drawRect(0f, 0f, width.toFloat(), gridSize.toFloat(), paint)
            canvas.drawRect(0f, (height - gridSize).toFloat(), width.toFloat(), height.toFloat(), paint)
            canvas.drawRect(0f, 0f, gridSize.toFloat(), height.toFloat(), paint)
            canvas.drawRect((width - gridSize).toFloat(), 0f, width.toFloat(), height.toFloat(), paint)

            paint.color = Color.GREEN
            for (pos in snake) {
                canvas.drawRect((pos.first * gridSize).toFloat(), (pos.second * gridSize).toFloat(), ((pos.first + 1) * gridSize).toFloat(), ((pos.second + 1) * gridSize).toFloat(), paint)
            }

            paint.color = Color.RED
            food?.let {
                canvas.drawRect((it.first * gridSize).toFloat(), (it.second * gridSize).toFloat(), ((it.first + 1) * gridSize).toFloat(), ((it.second + 1) * gridSize).toFloat(), paint)
            }

            paint.color = Color.RED
            paint.textSize = 80f
            canvas.drawText("$score", 80f, gridSize + 80f, paint)

            if (isGameOver) {
                paint.color = Color.RED
                paint.textSize = 100f
                canvas.drawText("Game Over", (width / 4).toFloat(), (height / 2).toFloat(), paint)
            }
            surfaceHolder.unlockCanvasAndPost(canvas)
        }
    }

    private fun control() {
        try {
            Thread.sleep(200)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun spawnFood() {
        if (gridCountX > 2 && gridCountY > 2) {
            val random = Random()
            food = Pair(random.nextInt(gridCountX - 2) + 1, random.nextInt(gridCountY - 2) + 1)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                val x = event.x
                val y = event.y

                if (!isGameOver) {
                    direction = when {
                        x > width / 2 && direction != 3 -> 1
                        x < width / 2 && direction != 1 -> 3
                        y > height / 2 && direction != 0 -> 2
                        y < height / 2 && direction != 2 -> 0
                        else -> direction
                    }
                }
            }
        }
        return true
    }

    private fun restartGame() {
        snake.clear()
        snake.add(Pair(10, 10))
        direction = 0
        spawnFood()
        score = 0
        isGameOver = false
        resume()
    }

    private fun showGameOverMessage() {
        handler.post {
            Toast.makeText(context, "Game Over. Restarting in 3 seconds...", Toast.LENGTH_LONG).show()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // Not needed
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        pause()
    }

    fun pause() {
        isPlaying = false
        try {
            thread?.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun resume() {
        isPlaying = true
        thread = Thread(this)
        thread?.start()
    }
}
