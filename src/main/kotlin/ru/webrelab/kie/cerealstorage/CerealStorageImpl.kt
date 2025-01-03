package ru.webrelab.kie.cerealstorage

class CerealStorageImpl(
    override val containerCapacity: Float,
    override val storageCapacity: Float
) : CerealStorage {

    init {
        require(containerCapacity >= 0) {
            "Ёмкость контейнера не может быть отрицательной"
        }
        require(storageCapacity >= containerCapacity) {
            "Ёмкость хранилища не должна быть меньше ёмкости одного контейнера"
        }
    }

    private val storage = mutableMapOf<Cereal, Float>()
    private var usedStorage = 0f

    private fun getCurrentAmount(cereal: Cereal): Float {
        return storage.getOrDefault(cereal, 0f)
    }

    private fun checkAmountNotNegative(amount: Float) {
        require(amount >= 0) {
            "Количество не может быть отрицательным"
        }
    }

    override fun addCereal(cereal: Cereal, amount: Float): Float {
        checkAmountNotNegative(amount)

        // Проверка на доступное место в хранилище
        if (usedStorage + containerCapacity > storageCapacity) {
            throw IllegalStateException("Хранилище не позволяет разместить ещё один контейнер для новой крупы")
        }

        val currentAmount = getCurrentAmount(cereal)
        val remainingSpace = containerCapacity - currentAmount

        return if (amount <= remainingSpace) {
            storage[cereal] = currentAmount + amount
            usedStorage += amount
            0f
        } else {
            storage[cereal] = containerCapacity
            usedStorage += remainingSpace
            amount - remainingSpace
        }
    }

    override fun getCereal(cereal: Cereal, amount: Float): Float {
        checkAmountNotNegative(amount)

        val currentAmount = getCurrentAmount(cereal)

        if (currentAmount == 0f) {
            return 0f
        }

        return if (amount <= currentAmount) {
            storage[cereal] = currentAmount - amount
            usedStorage -= amount
            amount
        } else {
            val amountToReturn = currentAmount
            storage[cereal] = 0f
            usedStorage -= currentAmount
            amountToReturn
        }
    }

    override fun removeContainer(cereal: Cereal): Boolean {
        val currentAmount = getCurrentAmount(cereal)

        if (currentAmount > 0f) {
            return false
        }

        storage.remove(cereal)
        return true
    }

    override fun getAmount(cereal: Cereal): Float {
        return getCurrentAmount(cereal)
    }

    override fun getSpace(cereal: Cereal): Float {
        val currentAmount = getCurrentAmount(cereal)
        return containerCapacity - currentAmount
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("CerealStorage (containerCapacity=$containerCapacity, storageCapacity=$storageCapacity)\n")
        storage.forEach { (cereal, amount) ->
            sb.append("  ${cereal.local}: $amount кг\n")
        }
        return sb.toString()
    }
}