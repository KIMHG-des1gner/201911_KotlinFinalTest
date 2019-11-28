package com.tjoeun.a201911_kotlinfinaltest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.tjoeun.a201911_kotlinfinaltest.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_edit_black_list.*
import org.json.JSONObject

class EditBlackListActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_black_list)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        postBtn.setOnClickListener {
            if (titleEdt.text.length < 5) {
                Toast.makeText(mContext, "제목은 최소 5글자 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (contentEdt.text.length < 10) {
                Toast.makeText(mContext, "내용은 최소 10글자 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            ServerUtil.postRequestBlackList(mContext, titleEdt.text.toString(),
                contentEdt.text.toString(), object : ServerUtil.JsonResponseHandler {
                    override fun onResponse(json: JSONObject) {
                        Log.d("글쓰기서버응답", json.toString())
                        val code = json.getInt("code")

                        runOnUiThread {
                            if (code == 200) {
                                Toast.makeText(mContext, "게시글 등록에 성공했습니다.", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                            else {
                                Toast.makeText(mContext, "게시글 등록에 실패했습니다. 계속 실패하면 관리자에게 문의해주세요.", Toast.LENGTH_SHORT).show()
                            }
                        }

                    }
                })

        }

    }

    override fun setValues() {

    }

}
