package com.goldze.mvvmhabit.aioui.kepu.content

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.goldze.mvvmhabit.BR
import com.goldze.mvvmhabit.R
import com.goldze.mvvmhabit.aioui.kepu.KepuBeanRecord
import com.goldze.mvvmhabit.aioui.kepu.content.KepuContentModel
import com.goldze.mvvmhabit.databinding.ActivityKepuContentBinding
import com.goldze.mvvmhabit.databinding.ActivityKnowsContentBinding
import me.goldze.mvvmhabit.base.BaseActivity

class KepuContentActivity : BaseActivity<ActivityKepuContentBinding, KepuContentModel>() {

    var bean: KepuBeanRecord? = null
    lateinit var list: List<KepuItemBeanRecord>
    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_kepu_content
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
        binding.brRootView.setPageTitle("情绪扫描")
        bean = intent.getSerializableExtra("bean") as KepuBeanRecord
//        list = bean?.
        viewModel.setKnowBean(bean!!)
        viewModel.loadData()
        viewModel.dataLiveData.observe(this) {
            list = it.records
            loadView()
        }

    }

    private fun loadView() {
        var adapter = InnerAdapter(this, list)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    class InnerAdapter(var context: Context, var list: List<KepuItemBeanRecord>) :
        RecyclerView.Adapter<InnerVH>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerVH {
            var view = LayoutInflater.from(context).inflate(R.layout.item_kepu, parent, false)
            return InnerVH(view)
        }

        override fun onBindViewHolder(holder: InnerVH, position: Int) {
            holder.bind(list[position])
        }

        override fun getItemCount(): Int {
            return list.size
        }

    }

    class InnerVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_title = itemView.findViewById<TextView>(R.id.tv_title)
        var tv_jieshao = itemView.findViewById<TextView>(R.id.tv_jieshao)

        fun bind(record: KepuItemBeanRecord) {
            tv_title.text = record.name
            tv_jieshao.text = record.brief
            record.contentList.forEach {
                addSonItem(it)

            }
        }

        private fun addSonItem(content: KepuItemBeanContent) {
            var context = itemView.context
            var view = LayoutInflater.from(context)
                .inflate(R.layout.item_kepu_content_item, itemView as LinearLayout, false)
            view.findViewById<TextView>(R.id.tv_content).text = content.resourcesName
            view.setOnClickListener {
                Log.i("fengao_xiaomi", "addSonItem: ")
            }
            view.findViewById<ImageView>(R.id.iv_icon).setImageResource(R.drawable.word_icon)
            var line = ImageView(context)
            line.setBackgroundColor(context.resources.getColor(R.color.background_gray_2))
            var param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1)
            (itemView as LinearLayout).addView(view, )
            (itemView as LinearLayout).addView(line,  param)
        }
    }
}