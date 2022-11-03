package com.aajeevika.individual.view.suvidha.adapter

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import com.aajeevika.individual.R
import com.aajeevika.individual.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.individual.baseclasses.BaseViewHolder
import com.aajeevika.individual.databinding.ListItemSarvottamPrathayeBinding
import com.aajeevika.individual.databinding.ListItemSuvidhaBinding
import com.aajeevika.individual.model.data_model.SarvottamPrathayeData
import com.aajeevika.individual.model.data_model.SuvidhaData
import com.aajeevika.individual.utility.Constant
import com.aajeevika.individual.view.suvidha.ActivitySuvidhaDetails

class SarcottamPrathanyeRecyclerViewAdapter(private val launcher: ActivityResultLauncher<String>) : BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<SarvottamPrathayeData>()

    fun addData(data: ArrayList<SarvottamPrathayeData>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        SarvottamPrathayeListViewHolder(ListItemSarvottamPrathayeBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = dataList.size

    private inner class SarvottamPrathayeListViewHolder(val dataBinding: ListItemSarvottamPrathayeBinding) : BaseViewHolder(dataBinding) {
        override fun bindData(context: Context) {
            dataBinding.run {
                val data = dataList[adapterPosition]
                title = data.name

                downloadButton.setOnClickListener {
                    if((Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) || (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                        val downloadableFile = Uri.parse(BaseUrls.baseUrl + data.download_link)
                        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

                        downloadManager.enqueue(DownloadManager.Request(downloadableFile).also { request ->
                            request.setTitle(data.name)
                            request.setDescription(String.format(context.getString(R.string.downloading_s), data.name))
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, data.download_link.substringAfterLast("/"))
                        })
                    } else {
                        launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
                }
            }
        }
    }
}