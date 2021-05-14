package ee.taltech.ee.taltech.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*


@Serializable
data class PlayerScore(val playerId: Long, val score: Long, @Serializable(with = DateSerializer::class) val timestamp: Date)

object DateSerializer : KSerializer<Date> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.LONG)
    override fun serialize(encoder: Encoder, value: Date) = encoder.encodeString(value.time.toString())
    override fun deserialize(decoder: Decoder): Date = Date(decoder.decodeString().toLong())
}