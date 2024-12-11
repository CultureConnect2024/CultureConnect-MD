package app.culturedev.cultureconnect.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import app.culturedev.cultureconnect.R
import app.culturedev.cultureconnect.data.response.DataRes
import app.culturedev.cultureconnect.data.response.ListDataItem
import app.culturedev.cultureconnect.databinding.FragmentHomeBinding
import app.culturedev.cultureconnect.helper.ColorUtils
import app.culturedev.cultureconnect.helper.NetworkUtil
import app.culturedev.cultureconnect.ui.adapter.Adapter
import app.culturedev.cultureconnect.ui.auth.login.LoginActivity
import app.culturedev.cultureconnect.ui.history.HistoryActivity
import app.culturedev.cultureconnect.ui.notification.NotificationActivity
import app.culturedev.cultureconnect.ui.recomendation.DescribeMoodActivity
import app.culturedev.cultureconnect.ui.viewmodel.HomeViewModel
import app.culturedev.cultureconnect.ui.viewmodel.factory.FactoryViewModel
import java.util.Locale

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val vm by viewModels<HomeViewModel> { FactoryViewModel.getInstance(requireContext()) }
    private var listCafe: ArrayList<ListDataItem> = ArrayList()
    private lateinit var adapter: Adapter
    private lateinit var searchView: SearchView
    private lateinit var recommendationAdapter: Adapter
    private lateinit var allCafeAdapter: Adapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.actionBar?.hide()

        if (!NetworkUtil.isOnline(requireContext())) {
            NetworkUtil.netToast(requireContext())
        }

        recommendationAdapter = Adapter()
        allCafeAdapter = Adapter()

        binding.rvRecommendation.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = recommendationAdapter
        }

        binding.rvAllCafe.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = allCafeAdapter

        }

        vm.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.recommendationProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        vm.listCafeRecommendation.observe(viewLifecycleOwner) {
            if (it != null) {
                recommendationAdapter.submitList(it)
            } else {
                Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show()
            }
        }

        vm.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.allCafeProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        vm.listAllCafe.observe(viewLifecycleOwner) {
            if (it != null) {
                allCafeAdapter.submitList(it)
                listCafe = it as ArrayList<ListDataItem>
            } else {
                Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show()
            }
        }

        ColorUtils.changeStatusBarColor(requireActivity().window, "#1B3E3B")
        btnHistory()
        btnNotification()
        toMoodBased()
        getUsername()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                search(newText)
                return true
            }

        })
    }

    private fun btnHistory() {
        binding.btnHistory.setOnClickListener {
            vm.getSession().observe(viewLifecycleOwner) {
                if (it.sessionId.isEmpty()) {
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                }
            }
            startActivity(Intent(requireContext(), HistoryActivity::class.java))
        }
    }

    private fun btnNotification() {
        binding.btnNotification.setOnClickListener {
            vm.getSession().observe(viewLifecycleOwner) {
                if (it.sessionId.isEmpty()) {
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                }
            }
            startActivity(Intent(requireContext(), NotificationActivity::class.java))
        }
    }

    private fun toMoodBased() {
        binding.btnRecommendation.setOnClickListener {
            vm.getSession().observe(viewLifecycleOwner) {
                if (it.sessionId.isEmpty()) {
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                }
            }
            startActivity(Intent(requireContext(), DescribeMoodActivity::class.java))
        }
    }

    private fun getUsername() {
        vm.getSession().observe(viewLifecycleOwner) {
            if (it.sessionId.isEmpty()) {
                binding.username.text = getString(R.string.username)
            } else {
                binding.username.text = it.username
            }
        }
    }

    private fun search(query: String?) {
        if (query != null) {
            val search = ArrayList<ListDataItem>()
            for (i in listCafe) {
                if (i.name?.lowercase(Locale.ROOT)!!.contains(query)) {
                    search.add(i)
                }
            }

            if (search.isEmpty()) {
                Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show()
            } else {
                adapter.setSearchList(search)
            }
        }
    }
}