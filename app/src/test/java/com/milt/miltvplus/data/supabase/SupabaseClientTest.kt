package com.milt.miltvplus.data.supabase

import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Test

class SupabaseClientTest {

    @Test
    fun testSupabaseClientFallbackWhenKeysMissing() {
        val statusMessage = SupabaseClient.getStatusMessage()
        assertNotNull(statusMessage)
        assertFalse(SupabaseClient.isServiceRoleKeyDetected())
    }

    @Test
    fun testSupabaseClientManagerDelegation() {
        assertNotNull(SupabaseClientManager.getStatusSummary())
        assertFalse(SupabaseClientManager.isSupabaseConfigured() && SupabaseClient.getSupabaseAnonKey() == null)
    }
}
