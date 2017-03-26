package br.com.xy.inc.builder.utils

import br.com.xy.inc.builder.properties.PairProperty

interface PropertyReplacer {

    fun replaceProperties(properties: List<PairProperty>, pathClasspath: String): String

    fun replaceFieldProperties(properties: List<PairProperty>, pathClasspath: String): String

}
