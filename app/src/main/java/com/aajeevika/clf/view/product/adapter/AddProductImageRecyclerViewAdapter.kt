package com.aajeevika.clf.view.product.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.core.net.toUri
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.clf.baseclasses.BaseViewHolder
import com.aajeevika.clf.databinding.ListItemAddProductImageBinding
import com.aajeevika.clf.utility.UtilityActions
import com.aajeevika.clf.utility.app_enum.ProductMediaType
import com.aajeevika.clf.view.dialog.MediaDialog
import java.io.File
import java.lang.StringBuilder

class AddProductImageRecyclerViewAdapter(
    private val filename: StringBuilder,
    private val fileRequestLocation: StringBuilder,
    private val galleryCallback: ActivityResultLauncher<String>,
    private val cameraCallback: ActivityResultLauncher<Uri>
) : BaseRecyclerViewAdapter() {
    private val mediaData = ArrayList<Pair<ProductMediaType, Any>>()

    fun addData(data: ArrayList<Pair<ProductMediaType, Any>>) {
        mediaData.clear()
        mediaData.addAll(data)
        notifyDataSetChanged()
    }

    fun addImage(file: File) {
        mediaData.add(Pair(ProductMediaType.IMAGE, file))
        notifyDataSetChanged()
    }

    fun mediaSelected() = mediaData

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder = run {
        AddProductImageViewHolder(ListItemAddProductImageBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = 5

    private inner class AddProductImageViewHolder(private val viewDataBinding: ListItemAddProductImageBinding) : BaseViewHolder(viewDataBinding) {
        override fun bindData(context: Context) {
            viewDataBinding.run {
                if(adapterPosition < mediaData.size) {
                    val media = mediaData[adapterPosition]

                    when (media.first) {
                        ProductMediaType.IMAGE -> {
                            youtubeImg.visibility = View.GONE

                            if(media.second is File)
                                productImg.setImageURI((media.second as File).toUri())
                            else if(media.second is String)
                                product = BaseUrls.baseUrl + (media.second as String)
                        }
                        ProductMediaType.VIDEO -> {
                            youtubeImg.visibility = View.VISIBLE
                            product = UtilityActions.getVideoThumbnail(media.second as String)
                        }
                    }

                    removeBtn.visibility = View.VISIBLE
                } else {
                    removeBtn.visibility = View.GONE
                    youtubeImg.visibility = View.GONE
                    productImg.setImageResource(R.drawable.ic_baseline_add)
                }

                root.setOnClickListener {
                    if(adapterPosition >= mediaData.size) {
                        fileRequestLocation.clear().append(AddProductImageRecyclerViewAdapter::class.simpleName)
                        filename.clear().append("${System.currentTimeMillis()}.jpg")

                        MediaDialog(
                            context = context,
                            cameraFileName = filename.toString(),
                            galleryCallback = galleryCallback,
                            cameraCallback = cameraCallback,
                            youtubeCallback = {
                                mediaData.add(Pair(ProductMediaType.VIDEO, it))
                                notifyDataSetChanged()
                            },
                            youtubeInputEnabled = mediaData.none { it.first == ProductMediaType.VIDEO }
                        ).show()
                    }
                }

                removeBtn.setOnClickListener {
                    mediaData.removeAt(adapterPosition)
                    notifyDataSetChanged()
                }
            }
        }
    }
}