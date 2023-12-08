package hu.ait.formed.screens.PlaceDancers

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.ait.formed.data.FormedDAO
import hu.ait.formed.data.Dance
import hu.ait.formed.data.Form
import hu.ait.formed.data.Dancer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FormedPlaceDancersViewModel @Inject constructor(
    private val formedDAO: FormedDAO
) : ViewModel() {

    private var clickedDancer: Int? = null

    suspend fun getDance(id: Int): Dance {
        return formedDAO.getDance(id)
    }

    fun updateDance(danceItem: Dance?) {
        viewModelScope.launch {
            if (danceItem != null) {
                formedDAO.updateDance(danceItem)
            }
        }
    }

    fun setClickedDancer(number: Int) {
        viewModelScope.launch {
            clickedDancer = number
        }
    }

    fun getClickedDancer(): Int? {
        return clickedDancer
    }

    fun dancerPoints(form: Form): List<Offset>  {
        val list = emptyList<Offset>().toMutableList()
        form.dancers.forEach {dancer: Dancer ->
            list += dancer.offset
        }
        return list
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