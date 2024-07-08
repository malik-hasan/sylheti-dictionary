package models.dictionary

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(indices = [Index("sense_id", unique = true)])
data class DictionaryEntry(
    @PrimaryKey
    @ColumnInfo("entry_id")
    val entryId: String,

    @ColumnInfo("lexeme_ipa")
    val lexemeIPA: String,
    @ColumnInfo("lexeme_bangla")
    val lexemeBangla: String?,
    @ColumnInfo("lexeme_nagri")
    val lexemeNagri: String?,

    @ColumnInfo("citation_ipa")
    val citationIPA: String?,
    @ColumnInfo("citation_bangla")
    val citationBangla: String?,
    @ColumnInfo("citation_nagri")
    val citationNagri: String?,

    @ColumnInfo("sense_id")
    val senseId: String?,
    
    @ColumnInfo("part_of_speech")
    val partOfSpeech: String?,
    
    val gloss: String?,

    @ColumnInfo("definition_en")
    val definitionEN: String?,
    @ColumnInfo("definition_bn")
    val definitionBN: String?,
    @ColumnInfo("definition_bnipa")
    val definitionBNIPA: String?,
    @ColumnInfo("definition_ipa")
    val definitionIPA: String?,
    @ColumnInfo("definition_nagri")
    val definitionNagri: String?
)

@Entity(
    primaryKeys = ["sense_id1", "sense_id2"],
    foreignKeys = [
        ForeignKey(
            entity = DictionaryEntry::class,
            parentColumns = ["sense_id"],
            childColumns = ["sense_id1"]
        ),
        ForeignKey(
            entity = DictionaryEntry::class,
            parentColumns = ["sense_id"],
            childColumns = ["sense_id2"]
        )
    ]
)
data class EntryCrossRef(
    @ColumnInfo("sense_id1")
    val senseId1: String,
    @ColumnInfo("sense_id2")
    val senseId2: String,

    @ColumnInfo("relation_type")
    val relationType: String // TODO: Enum
)

data class DictionaryData(
    @Embedded val entry: DictionaryEntry,

    @Relation(
        parentColumn = "entry_id",
        entityColumn = "entry_id"
    )
    val componentLexemes: List<ComponentLexeme>,

    @Relation(
        parentColumn = "entry_id",
        entityColumn = "entry_id"
    )
    val examples: List<Example>,

    @Relation(
        parentColumn = "entry_id",
        entityColumn = "entry_id"
    )
    val variants: List<Variant>,

    @Relation(
        parentColumn = "entry_id",
        entityColumn = "domain_id",
        associateBy = Junction(EntryDomainCrossRef::class)
    )
    val domains: List<Domain>,

    // Splitting in two uses a fraction of the space
    @Relation(
        parentColumn = "sense_id",
        entityColumn = "sense_id",
        associateBy = Junction(
            EntryCrossRef::class,
            parentColumn = "sense_id1",
            entityColumn = "sense_id2"
        )
    )
    private val relatedEntries1: List<DictionaryEntry>,
    @Relation(
        parentColumn = "sense_id",
        entityColumn = "sense_id",
        associateBy = Junction(
            EntryCrossRef::class,
            parentColumn = "sense_id2",
            entityColumn = "sense_id1"
        )
    )
    private val relatedEntries2: List<DictionaryEntry>
) {
    val relatedEntries: List<DictionaryEntry>
        get() = relatedEntries1 + relatedEntries2
}
