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
    ),
    ForeignKey(
        entity = DictionaryEntry::class,
        parentColumns = ["entry_id"],
        childColumns = ["component_id"]
    )
])
data class ComponentLexeme(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    @ColumnInfo("entry_id")
    val entryId: String,
    
    @ColumnInfo("component_id")
    val componentId: String,

    @ColumnInfo("is_primary")
    val isPrimary: Boolean,

    @ColumnInfo("complex_form_type")
    val complexFormType: String?, // TODO: enum

    @ColumnInfo("variant_type")
    val variantType: String? // TODO: enum
)
