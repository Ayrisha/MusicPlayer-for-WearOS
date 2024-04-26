package com.example.musicplayer.data

import android.content.Context
import android.content.SharedPreferences

object AppPreferences {
    private const val NAME = "Auth"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences
    private val IS_LOGIN = Pair("is_login", false)
    private val SHOW_DIALOG = Pair("is_show", false)

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var isLogin: Boolean
        get() = preferences.getBoolean(IS_LOGIN.first, IS_LOGIN.second)
        set(value) = preferences.edit {
            it.putBoolean(IS_LOGIN.first, value)
        }

    var isShow: Boolean
        get() = preferences.getBoolean(SHOW_DIALOG.first, SHOW_DIALOG.second)
        set(value) = preferences.edit {
            it.putBoolean(SHOW_DIALOG.first, value)
        }
}