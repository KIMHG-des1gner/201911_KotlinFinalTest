package com.tjoeun.a201911_kotlinfinaltest.fragments

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tjoeun.a201911_kotlinfinaltest.EditBlackListActivity
import com.tjoeun.a201911_kotlinfinaltest.R
import com.tjoeun.a201911_kotlinfinaltest.adapters.BlackListAdapter
import com.tjoeun.a201911_kotlinfinaltest.datas.BlackList
import com.tjoeun.a201911_kotlinfinaltest.utils.ServerUtil
import kotlinx.android.synthetic.main.fragment_board_list.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BoardListFragment : BaseFragment() {

    var dateFilterStartDate:Calendar? = null

    var blackList = ArrayList<BlackList>()
    var filteredBlackList = ArrayList<BlackList>()
    var blackListAdapter:BlackListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_board_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mContext = activity
        setupEvents()
        setValues()
    }

    override fun onResume() {
        super.onResume()

        getBlackListFromServer()
    }

    override fun setupEvents() {

//        날짜 필터 선택을 누르면 => 며칠부터 필터를 하고싶은지 DatePicker로 선택.
//        선택 결과를 텍스트뷰에 반영.
//        dateFilterStateDate가 null이면 초기화. 년/월/일 세팅
//        날짜는 2019.09.08 ~ 양식으로 반영

        dateFilterBtn.setOnClickListener {

            var datePickerDialog = DatePickerDialog(mContext!!, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                if (dateFilterStartDate == null) {
                    dateFilterStartDate = Calendar.getInstance()
                }

                dateFilterStartDate?.set(year, month, dayOfMonth, 0, 0, 0)

                val sdf = SimpleDateFormat("yyyy.MM.dd ~")
                dateFilterTxt.text = sdf.format(dateFilterStartDate?.time)

                filterBlackLists()

            }, 2019, Calendar.NOVEMBER, 1)
            datePickerDialog.show()

        }

        writeBlackListBtn.setOnClickListener {

            val intent = Intent(mContext!!, EditBlackListActivity::class.java)
            startActivity(intent)

        }
    }

    override fun setValues() {

        blackListAdapter = BlackListAdapter(mContext!!, filteredBlackList)
        boardListView.adapter = blackListAdapter

    }

    fun filterBlackLists() {

        filteredBlackList.clear()

        for (bl in blackList) {
            if (dateFilterStartDate == null) {
//                날짜 필터가 설정되지 않았다면
//                무조건 화면에 보여지라고 필터된 목록에 추가.
                filteredBlackList.add(bl)
            }
            else {
//                날짜 필터가 설정되어 있다면
//                게시글의 작성일자 >= 날짜필터 면 보이도록 목록에 추가.
                if (bl.createdAt.timeInMillis >= dateFilterStartDate!!.timeInMillis) {
                    filteredBlackList.add(bl)
                }

            }
        }

        blackListAdapter?.notifyDataSetChanged()
    }

    fun getBlackListFromServer() {
        ServerUtil.getRequestBlackList(mContext!!, object : ServerUtil.JsonResponseHandler {
            override fun onResponse(json: JSONObject) {

                val code = json.getInt("code")

                if (code == 200) {
                    val data = json.getJSONObject("data")
                    val black_lists = data.getJSONArray("black_lists")

                    blackList.clear()

                    for (i in 0..black_lists.length()-1) {
                        blackList.add(BlackList.getBlackListDataFromJson(black_lists.getJSONObject(i)))
                    }

                    activity!!.runOnUiThread {
                        blackListAdapter?.notifyDataSetChanged()
                        filterBlackLists()
                    }

                }

            }

        })
    }



}