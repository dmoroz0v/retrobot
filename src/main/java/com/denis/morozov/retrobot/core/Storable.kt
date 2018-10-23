package com.denis.morozov.retrobot.core

interface Storable {

    class Container {
        private var strings: MutableMap<String, String?> = mutableMapOf()
        private var ints: MutableMap<String, Int?> = mutableMapOf()
        private var containers: MutableMap<String, Container> = mutableMapOf()

        fun setString(value: String?, key: String) {
            strings[key] = value
        }

        fun stringValue(key: String): String? {
            return strings[key]
        }

        fun setInt(value: Int?, key: String) {
            ints[key] = value
        }

        fun intValue(key: String): Int? {
            return ints[key]
        }

        fun setContainer(container: Container, key: String) {
            containers[key] = container
        }

        fun container(key: String): Container? {
            return containers[key]
        }
    }

    fun store(): Container?
    fun restore(container: Container)
}
