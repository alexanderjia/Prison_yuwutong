package com.gkzxhn.prison.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.gkzxhn.prison.R
import com.gkzxhn.prison.entity.MeetingEntity
import com.starlight.mobile.android.lib.adapter.OnItemClickListener
import com.starlight.mobile.android.lib.adapter.ViewHolder

import java.util.ArrayList
import kotlinx.android.synthetic.main.main_item_layout.view.main_item_layout_tv_time
as tvTime
import kotlinx.android.synthetic.main.main_item_layout.view.main_item_layout_tv_name
as tvName
import kotlinx.android.synthetic.main.main_item_layout.view.main_item_layout_tv_prison_area
as tvArea
import kotlinx.android.synthetic.main.main_item_layout.view.main_item_layout_tv_cancel
as tvCancel
/**
 * Created by Raleigh.Luo on 17/4/11.
 */

class MainAdapter(private val mContext: Context) : RecyclerView.Adapter<ViewHolder>() {

    private var mDatas: MutableList<MeetingEntity> = ArrayList()
    private var onItemClickListener: OnItemClickListener? = null
    private var mCurrentIndex = -1


    fun getCurrentItem(): MeetingEntity{
        return mDatas[mCurrentIndex];
    }
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    fun removeCurrentItem() {
        this.mDatas.removeAt(mCurrentIndex)
        notifyItemRemoved(mCurrentIndex)
    }

    fun updateItems(mDatas: List<MeetingEntity>?) {
        this.mDatas.clear()
        if (mDatas != null && mDatas.size > 0) {
            this.mDatas.addAll(mDatas)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.main_item_layout, null)
        view.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView){
            val entity = mDatas[position]
            tvName.text = entity.name
            val meetingTime = entity.time?:""
            val array = meetingTime.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            //        2017-08-03 9:00 - 9:30
            var formateTime = "-"
            if (array != null) {
                formateTime = String.format("%s - %s", if (array.size > 1) array[1] else "", if (array.size > 3) array[3] else "")
            }
            tvTime.text = formateTime
            tvArea.text = entity.area
            this.setOnClickListener { v ->
                mCurrentIndex = position
               onItemClickListener?.onClickListener(v, position)
            }
            tvCancel.setOnClickListener { v ->
                mCurrentIndex = position
                onItemClickListener?.onClickListener(v, position)
            }


        }

    }

    override fun getItemCount(): Int {
        return mDatas.size
    }
}