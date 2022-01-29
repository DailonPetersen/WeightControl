package com.example.weigthcontrol.view.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView


abstract class GenericAdapter<T, F>(contextFragment: Context, list: List<T>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mContext: Context = contextFragment
    private var mArrayList: List<T>

    abstract fun getLayoutResId(): Int
    abstract fun onBindData(model: T, position: Int, dataBinding: F)
    abstract fun onItemClick(model: T, position: Int)

    init {
        mArrayList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val dataBinding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            getLayoutResId(),
            parent,
            false
        )
        return ItemViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindData(mArrayList[position], position, ((holder as ItemViewHolder).mDataBinding as F))

        holder.mDataBinding.root.setOnClickListener {
            onItemClick(mArrayList[position], position)
        }
    }

    override fun getItemCount(): Int {
        return mArrayList.size
    }

    fun addItems(arrayList: List<T>) {
        mArrayList = arrayList
        this.notifyDataSetChanged()
    }

    fun getItem(position: Int): T {
        return mArrayList[position]
    }

    class ItemViewHolder(val mDataBinding : ViewDataBinding): RecyclerView.ViewHolder(mDataBinding.root)
}