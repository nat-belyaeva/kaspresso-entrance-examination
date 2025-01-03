package ru.webrelab.kie.cerealstorage

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CerealStorageImplTest {

    private val storage = CerealStorageImpl(10f, 20f)
    private val delta = 0.01f

    @Test
    fun `should throw IllegalArgumentException if containerCapacity is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(-4f, 10f)
        }
    }

    @Test
    fun `should throw IllegalArgumentException if storageCapacity is less than containerCapacity`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(10f, 5f)
        }
    }

    @Test
    fun `addCereal should throw IllegalArgumentException if amount is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
           storage.addCereal(Cereal.BUCKWHEAT, -5f)
        }
    }

    @Test
    fun `addCereal should throw IllegalStateException if no space in storage for a new container`() {
        val fullStorage = CerealStorageImpl(10f, 10f) // Изменено тут
        fullStorage.addCereal(Cereal.BUCKWHEAT, 5f)

        assertThrows(IllegalStateException::class.java) {
            fullStorage.addCereal(Cereal.RICE, 5f)
        }
    }

    @Test
    fun `addCereal should add cereal and return 0 if space is enough`() {
        val remaining = storage.addCereal(Cereal.BUCKWHEAT, 5f)
        assertEquals(0f, remaining, delta)
        assertEquals(5f, storage.getAmount(Cereal.BUCKWHEAT), delta)
    }

    @Test
    fun `addCereal should fill container and return remaining amount`() {
        storage.addCereal(Cereal.BUCKWHEAT, 7f)
        val remaining = storage.addCereal(Cereal.BUCKWHEAT, 5f)
        assertEquals(2f, remaining, delta)
        assertEquals(10f, storage.getAmount(Cereal.BUCKWHEAT), delta)
    }

    @Test
    fun `getCereal should throw IllegalArgumentException if amount is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            storage.getCereal(Cereal.BUCKWHEAT, -5f)
        }
    }

    @Test
    fun `getCereal should return 0 if no cereal of this type`() {
        val amount = storage.getCereal(Cereal.RICE, 5f)
        assertEquals(0f, amount, delta)
    }

    @Test
    fun `getCereal should return requested amount if enough cereal available`() {
        storage.addCereal(Cereal.BUCKWHEAT, 7f)
        val amount = storage.getCereal(Cereal.BUCKWHEAT, 3f)
        assertEquals(3f, amount, delta)
        assertEquals(4f, storage.getAmount(Cereal.BUCKWHEAT), delta)
    }

    @Test
    fun `getCereal should return remaining amount if not enough cereal available`() {
        storage.addCereal(Cereal.BUCKWHEAT, 7f)
        val amount = storage.getCereal(Cereal.BUCKWHEAT, 10f)
        assertEquals(7f, amount, delta)
        assertEquals(0f, storage.getAmount(Cereal.BUCKWHEAT), delta)
    }

    @Test
    fun `removeContainer should return false if container is not empty`() {
        storage.addCereal(Cereal.BUCKWHEAT, 5f)
        val removed = storage.removeContainer(Cereal.BUCKWHEAT)
        assertFalse(removed)
        assertEquals(5f, storage.getAmount(Cereal.BUCKWHEAT), delta)
    }

    @Test
    fun `removeContainer should return true if container is empty and remove it`() {
        storage.addCereal(Cereal.BUCKWHEAT, 5f)
        storage.getCereal(Cereal.BUCKWHEAT, 5f)
        val removed = storage.removeContainer(Cereal.BUCKWHEAT)
        assertTrue(removed)
        assertEquals(0f, storage.getAmount(Cereal.BUCKWHEAT), delta)
    }

    @Test
    fun `getAmount should return correct amount of cereal`() {
        storage.addCereal(Cereal.BUCKWHEAT, 5f)
        val amount = storage.getAmount(Cereal.BUCKWHEAT)
        assertEquals(5f, amount, delta)
    }

    @Test
    fun `getAmount should return 0 if no such cereal`() {
        val amount = storage.getAmount(Cereal.PEAS)
        assertEquals(0f, amount, delta)
    }

    @Test
    fun `getSpace should return correct available space for cereal`() {
        storage.addCereal(Cereal.BUCKWHEAT, 3f)
        val space = storage.getSpace(Cereal.BUCKWHEAT)
        assertEquals(7f, space, delta)
    }

    @Test
    fun `getSpace should return container capacity if no cereal added yet`(){
        val space = storage.getSpace(Cereal.BUCKWHEAT)
        assertEquals(10f, space, delta)
    }

    @Test
    fun `toString should return correct string representation of storage`() {
        storage.addCereal(Cereal.BUCKWHEAT, 3f)
        storage.addCereal(Cereal.RICE, 5f)
        val expectedString =
            "CerealStorage (containerCapacity=10.0, storageCapacity=20.0)\n  Гречка: 3.0 кг\n  Рис: 5.0 кг\n"
        assertEquals(expectedString, storage.toString())
    }
}