package com.strk.jarvislauncher.launcher

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.strk.jarvislauncher.databinding.ItemAppIconBinding

/**
 * RecyclerView adapter for the app grid.
 */
class AppDrawerAdapter(
    private val apps: List<InstalledApp>,
    private val onAppClick: (InstalledApp) -> Unit
) : RecyclerView.Adapter<AppDrawerAdapter.AppViewHolder>() {

    class AppViewHolder(val binding: ItemAppIconBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val binding = ItemAppIconBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val app = apps[position]
        val pm = holder.itemView.context.packageManager
        holder.binding.appIcon.setImageDrawable(pm.getApplicationIcon(app.appInfo))
        holder.binding.appLabel.text = app.label
        holder.binding.root.setOnClickListener { onAppClick(app) }
    }

    override fun getItemCount(): Int = apps.size
}
