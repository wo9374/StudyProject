package com.ljb.ktor

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.TypedValue
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ljb.ktor.news.NewsActivity
import com.ljb.ktor.databinding.ActivityMainBinding
import com.ljb.ktor.holiday.HolidayActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.apply {
            btnNews.setOnClickListener {
                val intent = Intent(this@MainActivity, NewsActivity::class.java)
                startActivity(intent)
            }

            btnHoliday.setOnClickListener {
                val intent = Intent(this@MainActivity, HolidayActivity::class.java)
                startActivity(intent)
            }
        }
    }
}

fun Context.showToast(str:String?) =
    Toast.makeText(this, str, Toast.LENGTH_SHORT).show()

fun String.htmlToString() : String =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
    else
        Html.fromHtml(this).toString()

val Int.dp: Int
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics).toInt()