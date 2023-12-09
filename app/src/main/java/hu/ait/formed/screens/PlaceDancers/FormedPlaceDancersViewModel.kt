package hu.ait.formed.screens.PlaceDancers

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.ait.formed.data.Dance
import hu.ait.formed.data.DanceDAO
import hu.ait.formed.data.Form
import hu.ait.formed.data.Dancer
import hu.ait.formed.data.DancersDAO
import hu.ait.formed.data.FormDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FormedPlaceDancersViewModel @Inject constructor(
    private val formDAO: FormDAO,
    private val dancersDAO: DancersDAO
) : ViewModel() {

    private var clickedDancer: Int? = null

    fun getForm(id: Int): Flow<Form> {
        return formDAO.getFormByID(id)
    }

    fun updateForm(formItem: Form) {
        viewModelScope.launch {
            formDAO.updateForm(formItem)
        }
    }

    fun setClickedDancer(number: Int?) {
        viewModelScope.launch {
            clickedDancer = number
        }
    }

    fun getClickedDancer(): Int? {
        return clickedDancer
    }

//    fun dancerPoints(form: Form): List<Offset>  {
//        val list = emptyList<Offset>().toMutableList()
//        form.dancers.forEach {dancer: Dancer ->
//            list += dancer.offset
//        }
//        return list
//    }

//    fun dancerPoints(formID: Int): Flow<List<Dancer>> {
//        var list = emptyFlow<List<Dancer>>()
//        list = dancersDAO.getAllDancersByForm(formID)
//        return list
//    }

    fun getAllDancersByForm(id: Int): Flow<List<Dancer>> {
        return dancersDAO.getAllDancersByForm(id)
    }

    fun updateDancerPoints(coords: Pair<Offset, Offset>, clickedNum: Int) {
        var dancer =
        dancersDAO.getDancerByID(clickedNum)
    }


//    suspend fun getImportantShoppingListsNum(): Int {
//        return shoppingListDAO.getImportantShoppingListsNum()
//    }
//
//    fun addShoppingList(shoppinglistItem: ShoppingListItem) {
//        viewModelScope.launch {
//            shoppingListDAO.insert(shoppinglistItem)
//        }
//    }
//
//
//    fun removeShopListItem(shoppinglistItem: ShoppingListItem) {
//        viewModelScope.launch {
//            shoppingListDAO.delete(shoppinglistItem)
//        }
//    }
//
//    fun editShopListItem(editedShoppingListItem: ShoppingListItem) {
//        viewModelScope.launch {
//            shoppingListDAO.update(editedShoppingListItem)
//        }
//    }
//
//    fun changeListState(shoppinglistItem: ShoppingListItem, value: Boolean) {
//        val newShoppingListItem = shoppinglistItem.copy()
//        newShoppingListItem.isDone = value
//        viewModelScope.launch {
//            shoppingListDAO.update(newShoppingListItem)
//        }
//    }
//
//    fun clearAllShopList() {
//        viewModelScope.launch {
//            shoppingListDAO.deleteAllShoppingList()
//        }
//    }

}