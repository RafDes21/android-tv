package com.rafdev.practicestv

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Outline
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.leanback.widget.BaseCardView
import androidx.leanback.widget.ImageCardView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.rafdev.practicestv.databinding.ActivityMainBinding
import com.rafdev.practicestv.player.PlaybackActivity

class MainActivity : FragmentActivity() {

    private lateinit var binding: ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    private fun initUI() {
        binding.playButton.apply {
            requestFocus()
            setOnClickListener {
                val intent = Intent(this@MainActivity, PlaybackActivity::class.java)
                startActivity(intent)
            }
        }

        val cardView: ImageCardView = binding.cardView
        cardView.titleText = "Título de la tarjeta"
        cardView.contentText = "Descripción de la tarjeta"
//        cardView.findViewById<TextView>(R.id.title_text).text = "New Title"
        cardView.infoVisibility = BaseCardView.CARD_REGION_VISIBLE_ALWAYS
        cardView.setBackgroundColor(getColor(R.color.transparent))
        cardView.setInfoAreaBackgroundColor(getColor(R.color.transparent))
        cardView.setMainImageDimensions(313, 176)
        val drawable = ContextCompat.getDrawable(this, R.drawable.card)
        Glide.with(this)
            .load(drawable)
            .apply(RequestOptions().transform(RoundedCorners(16)))
            .into(cardView.mainImageView)

//        val backgroundDrawable = GradientDrawable().apply {
//            setColor(Color.WHITE) // Set the background color
//            cornerRadius = 10f // Set the corner radius
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                setPadding(2, 2, 2, 2)
//            } // Set padding if needed
//        }
        val backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.rounded_background)

        // Set the background drawable to the main image view
        cardView.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                cardView.mainImageView.background = backgroundDrawable
            } else {
                cardView.mainImageView.background = null
            }
        }

        // Clip the image view to its outline to follow the drawable's corners
//        cardView.mainImageView.clipToOutline = true
//        cardView.mainImageView.outlineProvider = object : ViewOutlineProvider() {
//            override fun getOutline(view: View, outline: Outline) {
//                outline.setRoundRect(0, 0, view.width, view.height, 16f)
//            }
//        }
//        val cardBackground = GradientDrawable()
//        cardBackground.setColor(Color.WHITE) // Background color
//        cardBackground.cornerRadius = 16f // Corner radius
//        cardView.background = cardBackground

//        val drawable = getDrawable(R.drawable.card)
//        cardView.mainImage = drawable

//        cardView.setOnFocusChangeListener { v, hasFocus ->
//            if (hasFocus){
//                v.setPadding(50, 50, 50, 50)
//            }else {
//                v.setPadding(0, 0, 0, 0)
//            }
//        }

//        cardView.setTitleText("New Card")
//        cardView.setContentText("Description for the new card")
    }
}