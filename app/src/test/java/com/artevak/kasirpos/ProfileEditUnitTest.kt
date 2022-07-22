package com.artevak.kasirpos

import android.util.Log
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ProfileEditUnitTest {

    @Test
    fun isUpdatePasswordChange(){
        val current_password_same_local_password = isUpdatePassword("123456", "")
        val current_password_different_and_confirm_password_empty = isUpdatePassword("12345", "")
        val password_different_confirm_password_not_empty = isUpdatePassword("12345", "1234")
        val current_password_and_confirm_password_same_as_local = isUpdatePassword("123456", "123456")
        val password_empty = isUpdatePassword("", "")

        //true -> need to update, false -> password not updated
        assertFalse(current_password_same_local_password)
        assertFalse(current_password_different_and_confirm_password_empty)
        assertFalse(current_password_and_confirm_password_same_as_local)
        assertFalse(password_empty)
        assertTrue(password_different_confirm_password_not_empty)
    }

    fun isUpdatePassword(password : String, confirmPassword : String) : Boolean{
        val isDifferentPasswordExisting = password != "123456"
        val isConfirmed = password == confirmPassword
        val isEmptyPassword = password.isEmpty() && confirmPassword.isEmpty()
        if (confirmPassword.isEmpty())
            return false

        if (isEmptyPassword){
            return false
        }

        if (!isDifferentPasswordExisting && isConfirmed){
            return false
        }

        return true
    }
}