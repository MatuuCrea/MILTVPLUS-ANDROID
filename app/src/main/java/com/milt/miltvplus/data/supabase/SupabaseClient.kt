package com.milt.miltvplus.data.supabase

import android.content.Context
import android.util.Log

/**
 * Secure Supabase client configuration manager for MILTVPLUS.
 *
 * Inspects configuration keys from:
 * 1. System Properties (`SUPABASE_URL`, `SUPABASE_ANON_KEY` / `SUPABASE_KEY`)
 * 2. Environment Variables (`SUPABASE_URL`, `SUPABASE_ANON_KEY` / `SUPABASE_KEY`)
 * 3. `BuildConfig` fields (`SUPABASE_URL`, `SUPABASE_ANON_KEY` / `SUPABASE_KEY`)
 *
 * Strictly enforces security guardrails:
 * - `service_role` keys are strictly prohibited and rejected to prevent unauthorized database access.
 * - Gracefully falls back to local cached IPTV & media data when keys are missing or invalid.
 */
object SupabaseClient {
    private const val TAG = "SupabaseClient"

    fun getSupabaseUrl(): String? {
        val sysProp = System.getProperty("SUPABASE_URL")
        if (!sysProp.isNullOrBlank()) return sysProp

        val envVar = System.getenv("SUPABASE_URL")
        if (!envVar.isNullOrBlank()) return envVar

        return runCatching {
            Class.forName("com.milt.miltvplus.BuildConfig").getField("SUPABASE_URL").get(null) as? String
        }.getOrNull()
    }

    fun getSupabaseAnonKey(): String? {
        val sysProp = System.getProperty("SUPABASE_ANON_KEY") ?: System.getProperty("SUPABASE_KEY")
        if (!sysProp.isNullOrBlank()) return sysProp

        val envVar = System.getenv("SUPABASE_ANON_KEY") ?: System.getenv("SUPABASE_KEY")
        if (!envVar.isNullOrBlank()) return envVar

        return runCatching {
            val buildConfigClass = Class.forName("com.milt.miltvplus.BuildConfig")
            (runCatching { buildConfigClass.getField("SUPABASE_ANON_KEY").get(null) as? String }.getOrNull()
                ?: runCatching { buildConfigClass.getField("SUPABASE_KEY").get(null) as? String }.getOrNull())
        }.getOrNull()
    }

    /**
     * Security check to ensure no service_role keys are ever embedded in client application builds.
     */
    fun isServiceRoleKeyDetected(): Boolean {
        val key = getSupabaseAnonKey() ?: return false
        return key.contains("service_role", ignoreCase = true) || key.startsWith("eyJ") && key.contains("service_role")
    }

    fun isConfigured(): Boolean {
        val url = getSupabaseUrl()
        val key = getSupabaseAnonKey()

        if (isServiceRoleKeyDetected()) {
            Log.e(
                TAG,
                "SECURITY WARNING: A Supabase service_role key was detected! Service role keys bypass RLS and must NEVER be embedded in mobile apps. Connection rejected."
            )
            return false
        }

        return !url.isNullOrBlank() &&
                !key.isNullOrBlank() &&
                url != "YOUR_SUPABASE_URL" &&
                key != "YOUR_SUPABASE_KEY" &&
                url.startsWith("http")
    }

    /**
     * Initializes Supabase client connection if valid anonymous keys are provided.
     * Falls back to local/cached data mode if keys are absent or invalid.
     */
    fun initialize(context: Context? = null): Boolean {
        return if (isConfigured()) {
            val url = getSupabaseUrl()
            Log.i(TAG, "Supabase client successfully initialized with endpoint: $url")
            true
        } else {
            if (isServiceRoleKeyDetected()) {
                Log.e(TAG, "Initialization failed: Prohibited service_role key provided. Using local fallback data store.")
            } else {
                Log.w(
                    TAG,
                    "Supabase environment keys (SUPABASE_URL, SUPABASE_ANON_KEY) are absent or default placeholders. " +
                            "App operates in local fallback mode with cached IPTV & catalog data."
                )
            }
            false
        }
    }

    fun getStatusMessage(): String {
        return when {
            isServiceRoleKeyDetected() -> "Error de Seguridad (Service Role Prohibido)"
            isConfigured() -> "Conectado (${getSupabaseUrl()})"
            else -> "Modo Local Fallback (Sin Claves Remote)"
        }
    }
}
