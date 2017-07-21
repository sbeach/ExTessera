package ny.gelato.extessera.feature.character_sheet.view_model

import android.support.design.widget.BottomSheetDialog
import ny.gelato.extessera.base.BaseViewModel
import ny.gelato.extessera.data.model.character.Character
import java.text.NumberFormat

/**
 * Created by jord.goldberg on 7/12/17.
 *
 * @layout bottom_sheet_character_coin.xml
 * @layout item_character_equipment_coin.xml
 */

data class CoinModel(
        val type: Type = CoinModel.Type.COPPER,
        var amount: Int = 0,
        var change: Int = 0

) : BaseViewModel() {

    var isSpendingTooMuch = false

    constructor(type: Type, character: Character) : this(type, when (type) {
        Type.COPPER -> character.copper
        Type.SILVER -> character.silver
        Type.ELECTRUM -> character.electrum
        Type.GOLD -> character.gold
        Type.PLATINUM -> character.platinum
    })

    enum class Type {
        COPPER, SILVER, ELECTRUM, GOLD, PLATINUM
    }

    override fun isSameAs(model: BaseViewModel): Boolean =
            if (model is CoinModel) type == model.type
            else false

    fun setChange(coin: CharSequence) {
        if (coin.isEmpty()) change = 0
        else change = coin.toString().toInt()
        isSpendingTooMuch = false
        notifyChange()
    }

    fun notifyError() {
        isSpendingTooMuch = true
        notifyChange()
    }

    fun addCoin(sheet: BottomSheetDialog): CoinModel {
        sheet.dismiss()
        amount += change
        return this.copy().apply { action = Action.UPDATE }
    }

    fun spendCoin(sheet: BottomSheetDialog): CoinModel {
        sheet.dismiss()
        amount -= change
        return this.apply { action = Action.UPDATE }
    }

    fun showAmount(): String = NumberFormat.getInstance().format(amount)

    fun showName(): String = when (type) {
        Type.COPPER -> "Copper"
        Type.SILVER -> "Silver"
        Type.ELECTRUM -> "Electrum"
        Type.GOLD -> "Gold"
        Type.PLATINUM -> "Platinum"
    }

    fun showType(): String = "${showName().first()}P"

    fun showHint(): String = "${showName()} pieces"

    fun showAddCoin(): String = "+ ${showName()}"

    fun showError(): String = "Not enough ${showName()}"
}