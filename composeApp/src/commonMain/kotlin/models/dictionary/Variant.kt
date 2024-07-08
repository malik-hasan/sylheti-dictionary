package models.dictionary

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [
    ForeignKey(
        entity = DictionaryEntry::class,
        parentColumns = ["entry_id"],
        childColumns = ["entry_id"]
    )
])
data class Variant(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    @ColumnInfo("entry_id")
    val entryId: String,

    @ColumnInfo("variant_ipa")
    val variantIPA: String,
    @ColumnInfo("variant_bangla")
    val variantBangla: String?,
    @ColumnInfo("variant_nagri")
    val variantNagri: String?,
    
    val environment: String?
)
