package `in`.encrust.writersden

import android.net.Uri

class ImageModel {
    var image: Uri? = null

    constructor() {}

    constructor(image: Uri) {
        this.image = image
    }
}
