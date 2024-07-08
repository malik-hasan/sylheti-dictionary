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
data class Example(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    @ColumnInfo("entry_id")
    val entryId: String,

    @ColumnInfo("example_ipa")
    val exampleIPA: String?,
    @ColumnInfo("example_bangla")
    val exampleBangla: String?,
    @ColumnInfo("example_nagri")
    val exampleNagri: String?,
    @ColumnInfo("example_en")
    val exampleEN: String?,
    @ColumnInfo("example_bn")
    val exampleBN: String?,
    @ColumnInfo("example_bnipa")
    val exampleBNIPA: String?
)
