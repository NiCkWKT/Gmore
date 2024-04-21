package edu.cuhk.csci3310.gmore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import edu.cuhk.csci3310.gmore.databinding.ActivityMainBinding
import edu.cuhk.csci3310.gmore.page.Account
import edu.cuhk.csci3310.gmore.page.Bookmark
import edu.cuhk.csci3310.gmore.page.Camera
import edu.cuhk.csci3310.gmore.page.News
import edu.cuhk.csci3310.gmore.ui.theme.GmoreTheme

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(News())

        binding.bottomNavView.setOnItemSelectedListener {

            when(it.itemId){

                R.id.navigation_news -> replaceFragment(News())
                R.id.navigation_account -> replaceFragment(Account())
                R.id.navigation_bookmark -> replaceFragment(Bookmark())
                R.id.navigation_camera -> replaceFragment(Camera())

                else ->{

                }
            }

            true

        }
//        setContent {
//            GmoreTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    Greeting("Android")
//                }
//            }
//        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()


    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GmoreTheme {
        Greeting("Android")
    }
}