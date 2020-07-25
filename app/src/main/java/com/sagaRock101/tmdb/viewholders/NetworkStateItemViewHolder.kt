
package com.sagaRock101.tmdb.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sagaRock101.tmdb.databinding.ComponentNetworkStateItemBinding
import com.sagaRock101.tmdb.model.NetworkState
import com.sagaRock101.tmdb.model.Status

/**
 * A View Holder that can display a loading or have click action.
 * It is used to show the network state of paging.
 */
class NetworkStateItemViewHolder(
    val binding: ComponentNetworkStateItemBinding,
    val retryCallback: () -> Unit
)
    : RecyclerView.ViewHolder(binding.root) {



    fun bindTo(networkState: NetworkState?) {
        binding.progressBar.visibility = toVisibility(networkState?.status == Status.RUNNING)
        binding.retryButton.visibility = toVisibility(networkState?.status == Status.FAILED)
        binding.errorMsg.visibility = toVisibility(networkState?.msg != null)
        binding.errorMsg.text = networkState?.msg
        binding.retryButton.setOnClickListener {
            retryCallback.invoke()
        }
    }

    companion object {
        fun create(parent: ViewGroup, retryCallback: () -> Unit): NetworkStateItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ComponentNetworkStateItemBinding.inflate(inflater, parent, false)
            return NetworkStateItemViewHolder(binding, retryCallback)
        }

        fun toVisibility(constraint : Boolean): Int {
            return if (constraint) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }
}
