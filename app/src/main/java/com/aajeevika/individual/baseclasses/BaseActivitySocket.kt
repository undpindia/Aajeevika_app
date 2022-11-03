package com.aajeevika.individual.baseclasses

import BaseUrls
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import com.aajeevika.individual.model.data_model.ChatUserResponse
import com.aajeevika.individual.model.data_model.MessageModel
import com.aajeevika.individual.model.data_model.UsersResult
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.client.SocketIOException
import io.socket.emitter.Emitter
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import kotlin.reflect.KClass

abstract class BaseActivitySocket<D : ViewDataBinding, V : BaseViewModel>(
    resourceId: Int,
    viewModelClass: KClass<V>
) : BaseActivityVM<D, V>(resourceId, viewModelClass) {
    companion object {
        const val TAG_SOCKET = "SOCKET_CHAT"
        const val NO_NETWORK_ERROR =
            "No network available, please check your WiFi or Data connection"
    }

    var mSocket: Socket? = null
    var isConnected = true

    /*---  FOR SOCKET NEED DATA */
    private fun initSocket() {
        try {
            Log.d(TAG_SOCKET, "Current_user " + preferencesHelper.uid)
            val mOptions = IO.Options()
            mOptions.query = "userId=${preferencesHelper.uid}"
            mSocket = IO.socket(BaseUrls.baseUrlSocket, mOptions)
            initSocketIo()
        } catch (e: SocketIOException) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }

    override fun onStart() {
        super.onStart()
        initSocket()
        mSocket?.on("add-message-response", onNewMessage)
        Handler(Looper.getMainLooper()).postDelayed({
            mSocket?.connect()
        }, 100)
        Log.d(TAG_SOCKET, "onStart " + mSocket?.connected() + " " + mSocket?.id())
    }

    override fun onStop() {
        super.onStop()
        mSocket?.disconnect()
    }

    private fun initSocketIo() {

        mSocket?.on(Socket.EVENT_CONNECT, onConnect)
        mSocket?.on(Socket.EVENT_DISCONNECT, onDisconnect)
        mSocket?.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
        mSocket?.connect()

        Log.d(TAG_SOCKET, "initSocketIo $mSocket")
    }

    fun getUserList() {
        val data = JSONObject()
        data.put("userId", preferencesHelper.uid.toString())
        mSocket?.emit("chat-list", data)
        Log.d(TAG_SOCKET, " emit chat-list $data")
    }

    fun destroySocket() {
        mSocket?.disconnect()
        mSocket?.close()
        mSocket = null
    }

    open fun onHideKeyboard() {
        //hideKeyboard()
    }

    open fun onShowKeyboard() {
        //showKeyboard()
    }

    open fun onNewMessage(msg: MessageModel) {}
    open fun onUserChatList(list: ArrayList<UsersResult>) {}
    open fun onConnect() {}
    open fun onConnectionError(msg: String) {}
    private val onConnect = Emitter.Listener {
        this@BaseActivitySocket.runOnUiThread {
            Log.d(TAG_SOCKET, "connected")
            onConnect()
        }
    }

    private val onDisconnect = Emitter.Listener {
        this@BaseActivitySocket.runOnUiThread {
            isConnected = false
            Log.d(TAG_SOCKET, "disConnected")
        }
    }

    private val onConnectError = Emitter.Listener {
        this@BaseActivitySocket.runOnUiThread {
            if (!isConnected) {
                onConnectionError(NO_NETWORK_ERROR)
            }
            Log.d(TAG_SOCKET, "onConnectError")
        }
    }

    val onUserChatList = Emitter.Listener { args ->
        Log.d(TAG_SOCKET, "onUserChatList call onUserChatList")
        this@BaseActivitySocket.runOnUiThread {
            try {
                Log.d(TAG_SOCKET, "onUserChatList call onUserChatList ${args[0] as JSONObject}")
                val data = args[0] as JSONObject
                val response = Gson().fromJson(data.toString(), ChatUserResponse::class.java)
                Log.d("onUserChatList", " $data")
                if (!response.error) {
                    onUserChatList(response.userList ?: ArrayList())
                } else {
                    onUserChatList(ArrayList())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d(TAG_SOCKET, "onUserChatList call onUserChatList ${e.message}")
            }
        }
    }

    private val onNewMessage = object : Emitter.Listener {
        override fun call(vararg args: Any) {
            Log.d(TAG_SOCKET, "onNewMessage call onNewMessage")
            this@BaseActivitySocket.runOnUiThread(Runnable {
                val json = args[0] as JSONObject
                Log.d("dataResponse", "" + json)
                try {
                    val msg = Gson().fromJson(json.toString(), MessageModel::class.java)
                    Log.d("dataResponse", "${preferencesHelper.uid} ${msg.fromUserId} " + json)
                    msg.timestamp = System.currentTimeMillis().toString()
                    onNewMessage(msg)
                } catch (e: JSONException) {
                    return@Runnable
                }
            })
        }
    }


    private val onTyping = object : Emitter.Listener {
        override fun call(vararg args: Any) {
            this@BaseActivitySocket.runOnUiThread(Runnable {
                val data = args[0] as JSONObject
                try {
                    val username = data.getString("username")
                } catch (e: JSONException) {
                    return@Runnable
                }
            })
        }
    }

    private val onStopTyping = object : Emitter.Listener {
        override fun call(vararg args: Any) {
            this@BaseActivitySocket.runOnUiThread(Runnable {
                val data = args[0] as JSONObject
                val username: String
                try {
                    username = data.getString("username")
                } catch (e: JSONException) {
                    Log.e("error", "" + e.printStackTrace())
                    return@Runnable
                }
            })
        }
    }
    var mTyping = false
    private val onTypingTimeout = object : Runnable {
        override fun run() {
            if (!mTyping) return
            mTyping = false
            mSocket?.emit("stop typing")
        }
    }

}
