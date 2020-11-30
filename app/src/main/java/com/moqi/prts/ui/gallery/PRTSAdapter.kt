package com.moqi.prts.ui.gallery

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moqi.prts.R
import com.moqi.prts.databinding.ItemPrtsActionsBinding

class PRTSAdapter: RecyclerView.Adapter<PRTSAdapter.PRTSViewHolder>() {

    var onItemClickEvent: ((Int) -> Unit)? = null

    class PRTSViewHolder(private val vb: ItemPrtsActionsBinding): RecyclerView.ViewHolder(vb.root){
        fun initData(text: String, imageId: Int){
            vb.itemPrtsActionTvName.text = text
            vb.itemPrtsActionIvIcon.setImageResource(imageId)
        }

        fun setItemClickListener(event: ((Int) -> Unit)){
            vb.root.setOnClickListener {
                event(adapterPosition)
            }
        }
    }

    private val actionList = arrayListOf(
        "代理作战说明",
        "计算操作区域",
        "开启代理作战"
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PRTSViewHolder {
        val vb = ItemPrtsActionsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PRTSViewHolder(vb)
    }

    override fun onBindViewHolder(holder: PRTSViewHolder, position: Int) {
//        vb.itemPrtsActionTvName.text = actionList[position]
//        vb.itemPrtsActionIvIcon.setImageResource(R.drawable.ic_circle_random)

//        vb.root.setOnClickListener {
//            onItemClickEvent?.invoke(position)
//        }
        holder.initData(actionList[position], R.drawable.ic_circle_random)
        holder.setItemClickListener {
            onItemClickEvent?.invoke(it)
        }
    }

    override fun getItemCount(): Int = actionList.size
}