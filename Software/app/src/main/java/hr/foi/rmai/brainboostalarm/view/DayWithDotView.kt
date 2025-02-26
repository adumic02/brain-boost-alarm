package hr.foi.rmai.brainboostalarm.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import hr.foi.rmai.brainboostalarm.R

class DayWithDotView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var dayTextView: TextView
    private var selected: Boolean = false
    private lateinit var dot: View
    private val selectedColor = ContextCompat.getColor(context, R.color.accent_color)
    private val defaultColor = ContextCompat.getColor(context, R.color.grey)

    var isDaySelected: Boolean
        get() = selected
        set(value) {
            selected = value
            updateColors()
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.day_of_week_with_dot, this, true)
        dayTextView = findViewById(R.id.tv_day_of_week)
        dot = findViewById(R.id.day_of_week_dot)
        setOnClickListener {
            isDaySelected = !isDaySelected
            updateColors()
        }

        updateColors()
    }

    private fun updateColors() {
        val color = if (isDaySelected) selectedColor else defaultColor
        dayTextView.setTextColor(color)
        dot.setBackgroundColor(color)
    }

    fun setDayText(dayText: String) {
        dayTextView.text = dayText
    }
}