package com.example.randomzooanimals

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.randomzooanimals.databinding.ActivityMainBinding
import com.example.randomzooanimals.main.MainViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressBar.visibility = View.INVISIBLE

        binding.btnGetAnimal.setOnClickListener {
            viewModel.getZooAnimal()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.animal.collect{event ->
                when(event){
                    is MainViewModel.ZooAnimalEvent.Success ->{
                        binding.ivAnimalPic.scaleType = ImageView.ScaleType.CENTER_INSIDE
                        binding.tvAnimalDesc.text = event.resultText1
                        Picasso.with(this@MainActivity).load(event.resultText2).into(binding.ivAnimalPic,
                            object: Callback {
                                override fun onSuccess() {
                                    binding.progressBar.visibility = View.INVISIBLE
                                    binding.ivAnimalPic.visibility = View.VISIBLE
                                }

                                override fun onError() {
                                    Toast.makeText(this@MainActivity, "Something went wrong.", Toast.LENGTH_SHORT)
                                        .show()
                                    binding.progressBar.visibility = View.INVISIBLE
                                    binding.ivAnimalPic.visibility = View.VISIBLE
                                }
                            })
                    }

                    is MainViewModel.ZooAnimalEvent.Failure ->{
                        Toast.makeText(this@MainActivity, event.errorText, Toast.LENGTH_LONG).show()
                    }

                    is MainViewModel.ZooAnimalEvent.Loading ->{
                        binding.progressBar.visibility = View.VISIBLE
                        binding.ivAnimalPic.visibility = View.INVISIBLE
                    }
                    else -> Unit
                }
            }
        }

    }
}