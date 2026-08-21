package com.strk.jarvislauncher.launcher

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView adapter for the app grid. Stub only — wire up once
 * activity_launcher.xml and item_app_icon.xml layouts exist.
 */
class AppDrawerAdapter(
    private val apps: List<InstalledApp>,
    private val onAppClick: (InstalledApp) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Inflate item_app_icon.xml, return a ViewHolder wrapping icon ImageView + label TextView")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Bind apps[position].label and icon; set click listener to launch via PackageManager")
    }

    override fun getItemCount(): Int = apps.size
}
