package com.tjoeun.a201911_kotlinfinaltest.utils

import android.content.Context
import android.util.Log
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class ServerUtil {

    interface JsonResponseHandler {
        fun onResponse(json:JSONObject)
    }

    companion object {
        var BASE_URL = "http://192.168.0.26:5000"

        fun postRequestLogin(
            context: Context,
            userId:String,
            userPw:String,
            handler: JsonResponseHandler?
        ) {

            var client = OkHttpClient()
            var url = "${BASE_URL}/auth"

//            POST/PUT/PATCH 메쏘드에서 요구하는 파라미터를 FormBody에 담아줌
            var formBody = FormBody.Builder()
                .add("login_id", userId)
                .add("password", userPw)
                .build()

//            실제로 날아갈 요청(request)을 생성.

            var request = Request.Builder()
                .url(url)
                .post(formBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                    Log.e("서버통신에러", e.localizedMessage)
                }

                override fun onResponse(call: Call, response: Response) {

                    var body = response.body()!!.string()
                    var json = JSONObject(body)
                    handler?.onResponse(json)
                }

            })
        }

        fun postRequestBlackList(
            context: Context,
            title:String,
            content:String,
            handler: JsonResponseHandler?
        ) {

            var client = OkHttpClient()
            var url = "${BASE_URL}/black_list"

//            POST/PUT/PATCH 메쏘드에서 요구하는 파라미터를 FormBody에 담아줌
            var formBody = FormBody.Builder()
                .add("title", title)
                .add("content", content)
                .build()

//            실제로 날아갈 요청(request)을 생성.

            var request = Request.Builder()
                .url(url)
                .header("X-Http-Token", ContextUtil.getUserToken(context))
                .post(formBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                    Log.e("서버통신에러", e.localizedMessage)
                }

                override fun onResponse(call: Call, response: Response) {

                    var body = response.body()!!.string()
                    var json = JSONObject(body)
                    handler?.onResponse(json)
                }

            })
        }

        fun getRequestUserList(context:Context, needActive:String, handler:JsonResponseHandler?) {

            val client = OkHttpClient()

            var urlBuilder = HttpUrl.parse("${BASE_URL}/admin/user")!!.newBuilder()
            urlBuilder.addEncodedQueryParameter("active", needActive)

            var requestUrl = urlBuilder.build().toString()

            Log.d("가공된GETURL", requestUrl)

            val request = Request.Builder()
                .url(requestUrl)
//                    만약 헤더가 필요하면 header() 함수 사용
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {

                    val body = response.body()!!.string()
                    val jsonObject = JSONObject(body)
                    handler?.onResponse(jsonObject)

                }

            })




        }

        fun getRequestCategoryList(context:Context, handler:JsonResponseHandler?) {

            val client = OkHttpClient()

            var urlBuilder = HttpUrl.parse("${BASE_URL}/system/user_category")!!.newBuilder()
//            urlBuilder.addEncodedQueryParameter("active", needActive)

            var requestUrl = urlBuilder.build().toString()

            Log.d("가공된GETURL", requestUrl)

            val request = Request.Builder()
                .url(requestUrl)
//                    만약 헤더가 필요하면 header() 함수 사용
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {

                    val body = response.body()!!.string()
                    val jsonObject = JSONObject(body)
                    handler?.onResponse(jsonObject)

                }

            })




        }

        fun getRequestNotice(context:Context, handler:JsonResponseHandler?) {

            val client = OkHttpClient()

            var urlBuilder = HttpUrl.parse("${BASE_URL}/notice")!!.newBuilder()
//            urlBuilder.addEncodedQueryParameter("active", needActive)

            var requestUrl = urlBuilder.build().toString()

            Log.d("가공된GETURL", requestUrl)

            val request = Request.Builder()
                .url(requestUrl)
//                    만약 헤더가 필요하면 header() 함수 사용
                .header("X-Http-Token", ContextUtil.getUserToken(context))
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {

                    val body = response.body()!!.string()
                    val jsonObject = JSONObject(body)
                    handler?.onResponse(jsonObject)

                }

            })
        }

        fun getRequestBlackList(context:Context, handler:JsonResponseHandler?) {

            val client = OkHttpClient()

            var urlBuilder = HttpUrl.parse("${BASE_URL}/black_list")!!.newBuilder()
//            urlBuilder.addEncodedQueryParameter("active", needActive)

            var requestUrl = urlBuilder.build().toString()

            Log.d("가공된GETURL", requestUrl)

            val request = Request.Builder()
                .url(requestUrl)
//                    만약 헤더가 필요하면 header() 함수 사용
                .header("X-Http-Token", ContextUtil.getUserToken(context))
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {

                    val body = response.body()!!.string()
                    val jsonObject = JSONObject(body)
                    handler?.onResponse(jsonObject)

                }

            })
        }

    }


}