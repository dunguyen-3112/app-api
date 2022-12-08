package com.test.cdcn_appmobile.ui.launch.login

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.test.cdcn_appmobile.R
import com.test.cdcn_appmobile.databinding.FragmentLoginBinding
import com.test.cdcn_appmobile.extension.addTextChangedListener
import com.test.cdcn_appmobile.extension.replaceFragment
import com.test.cdcn_appmobile.extension.setVisibility
import com.test.cdcn_appmobile.ui.launch.LaunchViewModel
import com.test.cdcn_appmobile.ui.launch.signup.SignUpFragment
import com.test.cdcn_appmobile.ui.main.HomeFragment
import com.test.cdcn_appmobile.utils.Constant
import com.test.cdcn_appmobile.utils.InjectorUtil

class LoginFragment : Fragment() {

    private var binding: FragmentLoginBinding? = null
    private var launchViewModel: LaunchViewModel? = null
    private var firstTimeClick = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() {
        //init View
        launchViewModel =
            ViewModelProvider(
                requireActivity(),
                InjectorUtil.provideLaunchViewModelFactory()
            )[LaunchViewModel::class.java]

        binding?.run {
            if (Constant.USER.email != "") {
                edtLoginUser.text = SpannableStringBuilder(Constant.USER.email)
            }
            if (Constant.USER.password != "") {
                edtLoginPass.text = SpannableStringBuilder(Constant.USER.password)
            }
        }
    }

    private fun initListener() {
        //init Listener
        binding?.run {
            tvSignupOpen.setOnClickListener {
                activity?.replaceFragment(
                    R.id.ctReplaceFragment,
                    SignUpFragment(),
                    true,
                    for_signUp = true
                )
            }

            edtLoginUser.addTextChangedListener {
                if (!firstTimeClick)
                    tvWarningEmailLogin.text = launchViewModel?.validEmail(it)
            }

            edtLoginPass.addTextChangedListener {
                if (!firstTimeClick)
                    tvWarningPassLogin.text = launchViewModel?.validPassWord(it)
            }

            btnLogin.setOnClickListener {
                firstTimeClick = false
                tvWarningEmailLogin.text =
                    launchViewModel?.validEmail(edtLoginUser.text.toString())
                tvWarningPassLogin.text =
                    launchViewModel?.validPassWord(edtLoginPass.text.toString())

                if (tvWarningEmailLogin.text.toString() == "" && tvWarningPassLogin.text.toString() == "") {
                    launchViewModel?.loginUser(edtLoginUser.text.toString(),
                        edtLoginPass.text.toString(),
                        onStart = {
                            viewBg.setVisibility(true)
                            progressBar.setVisibility(true)
                            layoutUser.setVisibility(false)
                            layoutPass.setVisibility(false)
                            tvForgotpassOpen.setVisibility(false)
                        },
                        onResult = { done, message ->
                            viewBg.setVisibility(false)
                            progressBar.setVisibility(false)
                            layoutUser.setVisibility(true)
                            layoutPass.setVisibility(true)
                            tvForgotpassOpen.setVisibility(true)
                            if (done) {
                                requireActivity().replaceFragment(
                                    R.id.ctReplaceFragment,
                                    HomeFragment()
                                )
                            } else {
                                context?.let {
                                    Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        })
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        initView()
    }

}
