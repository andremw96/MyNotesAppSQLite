package andreamw.com.mynoteapp.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Note (
    var id : Int? = -1,
    var title : String? = null,
    var description : String? = null,
    var date : String? = null
) : Parcelable