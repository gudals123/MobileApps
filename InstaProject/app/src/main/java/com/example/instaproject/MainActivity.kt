package com.example.instaproject

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.instaproject.R
import com.example.instaproject.databinding.ActivityNaviBinding // View Binding 추가
import com.example.instaproject.navigation.AddFragment
import com.example.instaproject.navigation.HomeFragment
import com.example.instaproject.navigation.MyPageFragment
import com.example.instaproject.navigation.NotificationsFragment
import com.example.instaproject.navigation.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import android.content.Intent

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNaviBinding // View Binding 변수 추가


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNaviBinding.inflate(layoutInflater) // View Binding 인스턴스 초기화
        setContentView(binding.root) // root 뷰를 화면에 표시

        val bottomNavigationView = binding.bottomNavigation // View Binding으로 요소 참조
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_home -> {
                    replaceFragment(HomeFragment())
                    setMenuItemCheckedIcon(menuItem)
                    return@setOnItemSelectedListener true
                }
                R.id.action_search -> {
                    replaceFragment(SearchFragment())
                    setMenuItemCheckedIcon(menuItem)
                    return@setOnItemSelectedListener true
                }
                R.id.action_add -> {
                    startActivity(Intent(this, UploadActivity::class.java))
                    setMenuItemCheckedIcon(menuItem)
                    return@setOnItemSelectedListener true
                }
                R.id.action_notifications -> {
                    replaceFragment(NotificationsFragment())
                    setMenuItemCheckedIcon(menuItem)
                    return@setOnItemSelectedListener true
                }
                R.id.action_myPage -> {
                    replaceFragment(MyPageFragment())
                    setMenuItemCheckedIcon(menuItem)
                    return@setOnItemSelectedListener true
                }
                else -> return@setOnItemSelectedListener false
            }
        }

        // 앱 시작시 홈 프래그먼트로 초기화
        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.action_home
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_content, fragment)
            .commit()
        
    }
    private fun setMenuItemCheckedIcon(menuItem: MenuItem) {
        val homeIcon = R.drawable.ic_home2
        val searchIcon = R.drawable.ic_search2
        val addIcon = R.drawable.ic_add
        val notificationsIcon = R.drawable.ic_notif2
        val myPageIcon = R.drawable.ic_mypage2

        when (menuItem.itemId) {
            R.id.action_home -> {
                menuItem.setIcon(homeIcon)
                setOtherMenuItemsOriginalIcons(menuItem.itemId)
            }
            R.id.action_search -> {
                menuItem.setIcon(searchIcon)
                setOtherMenuItemsOriginalIcons(menuItem.itemId)
            }
            R.id.action_add -> {
                menuItem.setIcon(addIcon)
                setOtherMenuItemsOriginalIcons(menuItem.itemId)
            }
            R.id.action_notifications -> {
                menuItem.setIcon(notificationsIcon)
                setOtherMenuItemsOriginalIcons(menuItem.itemId)
            }
            R.id.action_myPage -> {
                menuItem.setIcon(myPageIcon)
                setOtherMenuItemsOriginalIcons(menuItem.itemId)
            }
        }
    }

    private fun setOtherMenuItemsOriginalIcons(selectedItemId: Int) {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        for (i in 0 until bottomNavigationView.menu.size()) {
            val menuItem = bottomNavigationView.menu.getItem(i)
            if (menuItem.itemId != selectedItemId) {
                val originalIconId = getOriginalIconId(menuItem.itemId)
                menuItem.setIcon(originalIconId)
            }
        }
    }

    private fun getOriginalIconId(itemId: Int): Int {
        return when (itemId) {
            R.id.action_home -> R.drawable.ic_home
            R.id.action_search -> R.drawable.ic_search
            R.id.action_add -> R.drawable.ic_add
            R.id.action_notifications -> R.drawable.ic_notif
            R.id.action_myPage -> R.drawable.ic_mypage2
            else -> throw IllegalArgumentException("Invalid itemId: $itemId")
        }
    }
}


