package com.moqi.prts.ui.gallery

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moqi.prts.R
import kotlinx.android.synthetic.main.item_prts_actions.view.*

class PRTSAdapter: RecyclerView.Adapter<PRTSAdapter.PRTSViewHolder>() {

    var onItemClickEvent: ((Int) -> Unit)? = null

    class PRTSViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    private val actionList = arrayListOf(
        "代理作战说明",
        "计算操作区域",
        "开启代理作战"
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PRTSViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_prts_actions, parent, false)
        return PRTSViewHolder(v)
    }

    override fun onBindViewHolder(holder: PRTSViewHolder, position: Int) {
        holder.itemView.item_prts_action_tv_name.text = actionList[position]
        holder.itemView.item_prts_action_iv_icon.setImageResource(R.drawable.ic_circle_random)

        holder.itemView.setOnClickListener {
            onItemClickEvent?.invoke(position)
        }
    }

    override fun getItemCount(): Int = actionList.size
}