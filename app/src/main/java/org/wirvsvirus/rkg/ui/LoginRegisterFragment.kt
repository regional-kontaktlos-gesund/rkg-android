package org.wirvsvirus.rkg.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_login_register.*
import org.wirvsvirus.rkg.MainActivity
import org.wirvsvirus.rkg.R
import org.wirvsvirus.rkg.api.RkgClient
import org.wirvsvirus.rkg.getPrefs
import org.wirvsvirus.rkg.model.Vendor
import org.wirvsvirus.rkg.model.VendorSignup
import org.wirvsvirus.rkg.putVendorId
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.hideBottomNav()

        loginRegisterbutton.setOnClickListener {
            if (validateData()) {
                registerVendor()
            }
        }
    }

    private fun registerVendor() {
        loginRegisterbutton.isEnabled = false
        RkgClient.service.vendorSignUp(
            VendorSignup(
                loginRegisterName.text.toString(),
                loginRegisterMail.text.toString(),
                loginRegisterPassword.text.toString()
            )
        ).enqueue(object : Callback<Vendor> {
            override fun onFailure(call: Call<Vendor>, t: Throwable) {
                loginRegisterbutton.isEnabled = true
                Snackbar.make(loginRegisterRoot, R.string.genericError, Snackbar.LENGTH_SHORT)
            }

            @SuppressLint("ApplySharedPref")
            override fun onResponse(call: Call<Vendor>, response: Response<Vendor>) {
                val vendorId = response.body()?._id
                if (vendorId == null) {
                    Snackbar.make(loginRegisterRoot, R.string.genericError, Snackbar.LENGTH_SHORT)
                    loginRegisterbutton.isEnabled = true
                    return
                }

                requireContext().getPrefs().putVendorId(vendorId)
                findNavController().navigate(R.id.storeEditFragment)
            }
        })
    }

    private fun validateData(): Boolean {
        var valid = true
        if (loginRegisterMail.text.isNullOrEmpty()) {
            loginRegisterMailContainer.error = requireContext().getString(R.string.errorMail)
            valid = false
        } else {
            loginRegisterMailContainer.error = null
        }
        if (loginRegisterName.text.isNullOrEmpty()) {
            loginRegisterNameContainer.error = requireContext().getString(R.string.errorName)
            valid = false
        } else {
            loginRegisterNameContainer.error = null
        }
        if (loginRegisterPassword.text.isNullOrEmpty()) {
            loginRegisterPasswordContainer.error = requireContext().getString(R.string.errorPassword)
            valid = false
        } else {
            loginRegisterPasswordContainer.error = null
        }
        return valid
    }
}