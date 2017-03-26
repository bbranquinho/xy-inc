package br.com.xy.inc.utils.template

enum class TypeFieldBean(val type: String, val defaultValue: String) {
    DATETIME("Date", "Date()"),

    DECIMAL("BigDecimal", "BigDecimal(0)"),

    DOUBLE("Double", "0.0"),

    FLOAT("Float", "0.0f"),

    INTEGER("Integer", "0"),

    LONG("Long", "0L"),

    STRING("String", "")
}
