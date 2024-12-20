package app.culturedev.cultureconnect.ui.auth.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import app.culturedev.cultureconnect.data.result.ResultCafe
import app.culturedev.cultureconnect.databinding.ActivityRegisterBinding
import app.culturedev.cultureconnect.helper.ColorUtils
import app.culturedev.cultureconnect.helper.NetworkUtil
import app.culturedev.cultureconnect.ui.recomendation.DescribeMoodActivity
import app.culturedev.cultureconnect.ui.viewmodel.RegisterViewModel
import app.culturedev.cultureconnect.ui.viewmodel.factory.FactoryViewModel
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val vm by viewModels<RegisterViewModel> {
        FactoryViewModel.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ColorUtils.changeStatusBarColor(window, "#CC444B")
        navigateUp()

        if (!NetworkUtil.isOnline(this)) {
            NetworkUtil.netToast(this)
        }
        handleRegistration()
        animation()
        binding.progressBarRegister.visibility = View.INVISIBLE
    }

    private fun handleRegistration() {
        binding.btnRegister.setOnClickListener {
            val username = binding.edtRegisterName.text.toString()
            val email = binding.edtRegisterEmail.text.toString()
            val password = binding.edtRegisterPassword.text.toString()
            val confPassword = binding.edtRegisterPassword.text.toString()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confPassword.isEmpty() || confPassword != password) {
                Toast.makeText(this, "Silahkan isi data anda !!", Toast.LENGTH_SHORT).show()
            }
            vm.handleRegister(username, email, password).observe(this) { result ->
                when (result) {
                    is ResultCafe.Loading -> {
                        binding.progressBarRegister.visibility = View.VISIBLE
                    }

                    is ResultCafe.Success -> {
                        binding.progressBarRegister.visibility = View.INVISIBLE
                        Toast.makeText(
                            this,
                            "Selamat Datang di Culture Connect ",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(
                            Intent(
                                this@RegisterActivity,
                                DescribeMoodActivity::class.java
                            )
                        )
                        finish()
                    }

                    is ResultCafe.Error -> {
                        binding.progressBarRegister.visibility = View.INVISIBLE
                        Toast.makeText(this, "Error : ${result.error}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun navigateUp() {
        binding.btnBackLogin.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun animation() {
        val title = ObjectAnimator.ofFloat(binding.registerTitle, View.ALPHA, 1f).setDuration(400)
        val nameLayout =
            ObjectAnimator.ofFloat(binding.registerNameLayout, View.ALPHA, 1f).setDuration(400)
        val nameEdit =
            ObjectAnimator.ofFloat(binding.edtRegisterName, View.ALPHA, 1f).setDuration(400)
        val emailLayout =
            ObjectAnimator.ofFloat(binding.registerEmailLayout, View.ALPHA, 1f).setDuration(400)
        val emailEdit =
            ObjectAnimator.ofFloat(binding.edtRegisterEmail, View.ALPHA, 1f).setDuration(400)
        val passwordLayout =
            ObjectAnimator.ofFloat(binding.registerPasswordLayout, View.ALPHA, 1f).setDuration(400)
        val passwordEdit =
            ObjectAnimator.ofFloat(binding.edtRegisterPassword, View.ALPHA, 1f).setDuration(400)
        val confirmPasswordLayout =
            ObjectAnimator.ofFloat(binding.registerPasswordConfirmLayout, View.ALPHA, 1f).setDuration(400)
        val confirmPasswordEdit =
            ObjectAnimator.ofFloat(binding.edtRegisPasswordConfirm, View.ALPHA, 1f).setDuration(400)
        val signup = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(400)

        val usernameAnimations = AnimatorSet().apply {
            playTogether(
                nameLayout,nameEdit
            )
        }
        val emailAnimations = AnimatorSet().apply {
            playTogether(
                emailLayout, emailEdit
            )
        }

        val passwordAnimations = AnimatorSet().apply {
            playTogether(
                passwordLayout, passwordEdit
            )
        }

        val confirmPasswordAnimations = AnimatorSet().apply {
            playTogether(
                confirmPasswordLayout, confirmPasswordEdit
            )
        }

        AnimatorSet().apply {
            playSequentially(
                title,
                usernameAnimations,
                emailAnimations,
                passwordAnimations,
                confirmPasswordAnimations,
                signup
            )
            startDelay = 100
        }.start()
    }

    companion object {
        private const val TAG = "CustomAuthActivity"
    }
}