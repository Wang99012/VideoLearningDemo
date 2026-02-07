package com.simplation.demo

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import java.io.Serializable

/**
 * 页面跳转的扩展数据
 *
 * 用法：
 * <br>     context.launch<NextActivity>(
 * <br>         "id" to 1001,
 * <br>         "name" to "张三",
 * <br>         "isAdmin" to true
 * <br>     )
 */
inline fun <reified T : Any> Context.launch(
    vararg pairs: Pair<String, Any?>
) {
    val intent = Intent(this, T::class.java)
    pairs.forEach { (key, value) ->
        when (value) {
            is Int -> intent.putExtra(key, value)
            is String -> intent.putExtra(key, value)
            is Boolean -> intent.putExtra(key, value)
            is Long -> intent.putExtra(key, value)
            is Float -> intent.putExtra(key, value)
            is Double -> intent.putExtra(key, value)
            is Parcelable -> intent.putExtra(key, value)
            is Serializable -> intent.putExtra(key, value)
        }
    }
    startActivity(intent)
}