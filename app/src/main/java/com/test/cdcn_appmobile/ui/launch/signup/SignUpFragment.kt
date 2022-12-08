package com.test.cdcn_appmobile.ui.launch.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.test.cdcn_appmobile.data.models.User
import com.test.cdcn_appmobile.databinding.FragmentSignUpBinding
import com.test.cdcn_appmobile.extension.addTextChangedListener
import com.test.cdcn_appmobile.extension.backToPreFragment
import com.test.cdcn_appmobile.extension.setVisibility
import com.test.cdcn_appmobile.ui.launch.LaunchViewModel
import com.test.cdcn_appmobile.utils.Constant
import com.test.cdcn_appmobile.utils.InjectorUtil

class SignUpFragment() : Fragment() {

    private var binding: FragmentSignUpBinding? = null
    private var launchViewModel: LaunchViewModel? = null
    private var firstTimeClick = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() {

        launchViewModel =
            ViewModelProvider(
                requireActivity(),
                InjectorUtil.provideLaunchViewModelFactory()
            )[LaunchViewModel::class.java]

    }

    private fun initListener() {

        binding?.run {

            tvLoginOpen.setOnClickListener {
                activity?.backToPreFragment()
            }

            edtSignupEmail.addTextChangedListener {
                if (!firstTimeClick)
                    tvWarningEmail.text = launchViewModel?.validEmail(it)
            }

            edtSignupPw1.addTextChangedListener {
                if (!firstTimeClick) {
                    tvWarningPass.text = launchViewModel?.validPassWord(it)
                    tvWarningConfirm.text =
                        if (edtSignupPw2.text.toString() == edtSignupPw1.text.toString()
                        ) "" else "Mật khẩu không trùng khớp"
                }
            }

            edtSignupPw2.addTextChangedListener {
                if (!firstTimeClick)
                    tvWarningConfirm.text =
                        if (edtSignupPw2.text.toString() == edtSignupPw1.text.toString()
                        ) "" else "Mật khẩu không trùng khớp"
            }

            btnSignup.setOnClickListener {
                firstTimeClick = false

                tvWarningEmail.text =
                    launchViewModel?.validEmail(edtSignupEmail.text.toString())
                tvWarningPass.text =
                    launchViewModel?.validPassWord(edtSignupPw1.text.toString())
                tvWarningConfirm.text =
                    if (edtSignupPw2.text.toString() == edtSignupPw1.text.toString()
                    ) "" else "Mật khẩu không trùng khớp"

                if (tvWarningEmail.text.toString() == "" && tvWarningPass.text.toString() == "" && tvWarningConfirm.text.toString() == "") {
                    viewBg.setVisibility(true)
                    progressBar.setVisibility(true)
                    launchViewModel?.register(
                        User(
                            id = "",
                            userName = "",
                            email = edtSignupEmail.text.toString().trim(),
                            password = edtSignupPw1.text.toString().trim(),
                            name = edtSignupName.text.toString().trim(),
                            tokenJWT = ""
                        ), onResult = { done, message ->
                            viewBg.setVisibility(false)
                            progressBar.setVisibility(false)
                            if (done) {
                                Constant.USER.email = edtSignupEmail.text.toString().trim()
                                Constant.USER.password = edtSignupPw1.text.toString().trim()
                                activity?.backToPreFragment()
                            } else {
                                context?.let {
                                    Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )
                }
            }

        }

    }
}
