package `in`.encrust.writersden

import android.graphics.Typeface

class FontModel {
    var customfont: Typeface? = null
    var fontname: String? = null

    constructor() {}

    constructor(customfont: Typeface, fontname: String) {
        this.customfont = customfont
        this.fontname = fontname
    }
}
