package com.example.roomdemo

import android.content.Context
import android.media.effect.EffectUpdateListener
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdemo.databinding.ActivityMainBinding
import com.example.roomdemo.databinding.ItemsRowBinding

class ItemAdapter(private val items : ArrayList<EmployeeEntity>,

                  private val updateListener: (id:Int)->Unit,
                  private val deleteListener: (id:Int)->Unit

): RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    class ViewHolder(binding: ItemsRowBinding):RecyclerView.ViewHolder(binding.root){

      val llMain = binding.llMain
        val tvname = binding.tvName
        val tvEmail = binding.tvEmail
        val ivEdit = binding.ivEdit
        val ivDelete = binding.ivDelete


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemsRowBinding.inflate(
            LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val items = items[position]

        holder.tvname.text = items.name
        holder.tvEmail.text = items.email

        if (position % 2 == 0){
            holder.llMain.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,
                R.color.colorLightGray))
        }else{
            holder.llMain.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,
                R.color.white))
        }
        holder.ivEdit.setOnClickListener {
            updateListener.invoke(items.id)
        }
        holder.ivDelete.setOnClickListener {
            deleteListener.invoke(items.id)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }


}