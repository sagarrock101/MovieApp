
package com.example.tmdb.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdb.databinding.ComponentNetworkStateItemBinding
import com.example.tmdb.model.NetworkState
import com.example.tmdb.model.Status

/**
 * A View Holder that can display a loading or have click action.
 * It is used to show the network state of paging.
 */
class NetworkStateItemViewHolder(val binding: ComponentNetworkStateItemBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bindTo(networkState: NetworkState?) {
        binding.progressBar.visibility = toVisibility(networkState?.status == Status.RUNNING)
    }

    companion object {
        fun create(parent: ViewGroup): NetworkStateItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ComponentNetworkStateItemBinding.inflate(inflater, parent, false)
            return NetworkStateItemViewHolder(binding)
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
