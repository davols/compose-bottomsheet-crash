package com.example.myapplication.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.LocalBackPressedDispatcher
import com.example.myapplication.theme.MyApplicationTheme
import dev.chrisbanes.accompanist.insets.LocalWindowInsets
import dev.chrisbanes.accompanist.insets.ViewWindowInsetObserver

@ExperimentalMaterialApi
class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val windowInsets = ViewWindowInsetObserver(this).start()
        setContent {
            CompositionLocalProvider(
                LocalWindowInsets provides windowInsets,
                ) {
                MyApplicationTheme {
                    val navController = rememberNavController()
                    Scaffold(
                        content = {
                            NavHost(navController, startDestination = "menu") {
                                composable("menu") {
                                    MainComposable(onClickEdit = { navController.navigate("edit") })
                                }
                                composable("edit") {
                                    EditMenuComposable(onSubmit = {
                                        navController.popBackStack()
                                    })

                                }
                            }
                        },
                    )
                }
            }
        }


    }

}