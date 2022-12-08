package com.test.cdcn_appmobile.ui.main.settings.changepass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.test.cdcn_appmobile.R
import com.test.cdcn_appmobile.databinding.FragmentChangePasswordBinding
import com.test.cdcn_appmobile.extension.PASSWORD
import com.test.cdcn_appmobile.extension.addTextChangedListener
import com.test.cdcn_appmobile.extension.backToPreFragment
import com.test.cdcn_appmobile.extension.setVisibility
import com.test.cdcn_appmobile.ui.main.settings.UserViewModel
import com.test.cdcn_appmobile.utils.Constant
import com.test.cdcn_appmobile.utils.InjectorUtil

class ChangePasswordFragment : Fragment() {

    private var binding: FragmentChangePasswordBinding? = null
    private var userViewModel: UserViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() {
        userViewModel =
            ViewModelProvider(
                requireActivity(),
                InjectorUtil.provideUserViewModelFactory()
            )[UserViewModel::class.java]

        binding?.run {

        }
    }

    private fun initListener() {
        binding?.run {
            btnBack.setOnClickListener {
                requireActivity().backToPreFragment()
            }

            edtOldPass.addTextChangedListener {
                tvWarningOld.setVisibility(it != Constant.USER.password)
            }

            edtNewPassword.addTextChangedListener {
                tvWarningPass.setVisibility(!PASSWORD.matches(it))
                tvWarningConfirm.setVisibility(it != edtConfirmPass.text.toString())
            }

            edtConfirmPass.addTextChangedListener {
                tvWarningConfirm.setVisibility(it != edtNewPassword.text.toString())
            }

            btnSubmit.setOnClickListener {
                if (tvWarningOld.visibility == View.GONE
                    && tvWarningPass.visibility == View.GONE
                    && tvWarningConfirm.visibility == View.GONE
                ) {
                    viewBg.setVisibility(true)
                    progressBar.setVisibility(true)
                    userViewModel?.updatePass(
                        resources.getString(
                            R.string.tokenJWT,
                            Constant.USER.tokenJWT
                        ),
                        Constant.USER.id,
                        edtNewPassword.text.toString()
                    ) { message ->
                        viewBg.setVisibility(false)
                        progressBar.setVisibility(false)
                        message?.let {
                            context?.let {
                                Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    context?.let {
                        Toast.makeText(
                            it,
                            "Vui lòng kiểm tra lại thông tin !!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

}