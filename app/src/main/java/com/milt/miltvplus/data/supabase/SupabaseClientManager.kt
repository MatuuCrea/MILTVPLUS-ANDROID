package com.milt.miltvplus.data.supabase

import android.content.Context

/**
 * Manager object delegating to [SupabaseClient] for client initialization and status checks.
 */
object SupabaseClientManager {

    fun getSupabaseUrl(): String? = SupabaseClient.getSupabaseUrl()

    fun getSupabaseKey(): String? = SupabaseClient.getSupabaseAnonKey()

    fun isSupabaseConfigured(): Boolean = SupabaseClient.isConfigured()

    fun initializeConnection(context: Context? = null): Boolean {
        return SupabaseClient.initialize(context)
    }

    fun getStatusSummary(): String = SupabaseClient.getStatusMessage()
}
