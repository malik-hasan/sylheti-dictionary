package models.dictionary

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class Domain(
    @PrimaryKey
    @ColumnInfo("domain_id")
    val domainId: String,
    val description: String
)

@Entity(
    primaryKeys = ["entry_id", "domain_id"],
    foreignKeys = [
        ForeignKey(
            entity = DictionaryEntry::class,
            parentColumns = ["entry_id"],
            childColumns = ["entry_id"]
        ),
        ForeignKey(
            entity = Domain::class,
            parentColumns = ["domain_id"],
            childColumns = ["domain_id"]
        )
    ]
)
data class EntryDomainCrossRef(
    @ColumnInfo("entry_id")
    val entryId: String,
    @ColumnInfo("domain_id")
    val domainId: String
)

data class DomainWithEntries(
    @Embedded val domain: Domain,

    @Relation(
        parentColumn = "domain_id",
        entityColumn = "entry_id",
        associateBy = Junction(EntryDomainCrossRef::class)
    )
    val entries: List<DictionaryEntry>
)
