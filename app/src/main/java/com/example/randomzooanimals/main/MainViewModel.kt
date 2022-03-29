package com.example.randomzooanimals.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomzooanimals.util.DispatcherProvider
import com.example.randomzooanimals.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val dispatchers: DispatcherProvider
): ViewModel() {

    sealed class ZooAnimalEvent{
        class Success(val resultText1: String, val resultText2: String): ZooAnimalEvent()
        class Failure(val errorText: String): ZooAnimalEvent()
        object Loading: ZooAnimalEvent()
        object Empty: ZooAnimalEvent()
    }

    private val _animal = MutableStateFlow<ZooAnimalEvent>(ZooAnimalEvent.Empty)
    val animal: StateFlow<ZooAnimalEvent> = _animal

    fun getZooAnimal(){
        viewModelScope.launch(dispatchers.io){
            _animal.value = ZooAnimalEvent.Loading
            when(val response = repository.getZooAnimal()){
                is Resource.Error -> {
                    _animal.value = ZooAnimalEvent.Failure(response.message!!)
                }
                is Resource.Success -> {
                    val minLen = feetToMeters(response.data!!.length_min.toDouble()).toBigDecimal()
                        .setScale(1, RoundingMode.HALF_UP).toDouble()
                    val maxLen = feetToMeters(response.data.length_max.toDouble()).toBigDecimal()
                        .setScale(1, RoundingMode.HALF_UP).toDouble()

                    val minMass = poundsToKg(response.data.weight_min.toDouble()).toBigDecimal()
                        .setScale(1, RoundingMode.HALF_UP).toDouble()
                    val maxMass = poundsToKg(response.data.weight_max.toDouble()).toBigDecimal()
                        .setScale(1, RoundingMode.HALF_UP).toDouble()

                    val animal = "This animal is called ${response.data.name}. In latin, it's called " +
                            "${response.data.latin_name}. It's a ${response.data.animal_type}. " +
                            "It's ${response.data.active_time}. The length of ${response.data.name} " +
                            "goes from $minLen to $maxLen meters. It weights between $minMass to $maxMass " +
                            "kilograms. It's lifespan is around ${response.data.lifespan} years. It " +
                            "inhabits ${response.data.habitat}. It's diet consists of " +
                            "${response.data.diet}. It is found in ${response.data.geo_range}."

                    val picture = response.data.image_link

                    _animal.value = ZooAnimalEvent.Success(
                        animal, picture
                    )

                }
            }
        }
    }

    private fun feetToMeters(x: Double): Double = x * 0.31
    private fun poundsToKg(x: Double): Double = x * 0.45
}