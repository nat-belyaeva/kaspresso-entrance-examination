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

    private fun checkAmountNotNegative(amount: Float) {
        require(amount >= 0) {
            "Количество не может быть отрицательным"
        }
    }

    private fun getUsedStorage(): Float {
        return storage.values.sum()
    }

    override fun addCereal(cereal: Cereal, amount: Float): Float {
        checkAmountNotNegative(amount)

        val currentAmount = getAmount(cereal)
        val remainingSpace = getSpace(cereal)

        if (getUsedStorage() + containerCapacity > storageCapacity) {
            throw IllegalStateException("Хранилище не позволяет разместить ещё один контейнер для новой крупы")
        }

        return if (amount <= remainingSpace) {
            storage[cereal] = currentAmount + amount
            0f
        } else {
            storage[cereal] = containerCapacity
            amount - remainingSpace
        }
    }


    override fun getCereal(cereal: Cereal, amount: Float): Float {
        checkAmountNotNegative(amount)

        val currentAmount = getAmount(cereal)

        if (currentAmount == 0f) {
            return 0f
        }

        return if (amount <= currentAmount) {
            storage[cereal] = currentAmount - amount
            amount
        } else {
            storage.remove(cereal)
            currentAmount
        }
    }

    override fun removeContainer(cereal: Cereal): Boolean {
        val currentAmount = getAmount(cereal)

        if (currentAmount > 0f) {
            return false
        }
        storage.remove(cereal)
        return true
    }

    override fun getAmount(cereal: Cereal): Float {
        return storage.getOrDefault(cereal,0f)
    }

    override fun getSpace(cereal: Cereal): Float {
         return containerCapacity - getAmount(cereal)
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