package com.ljb.datastore.proto

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.ljb.datastore.Sample
import java.io.InputStream
import java.io.OutputStream

object SampleSerializer: Serializer<Sample> {
    override val defaultValue: Sample
        get() = Sample.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Sample {
        try {
            return Sample.parseFrom(input)
        } catch (e: InvalidProtocolBufferException){
            throw CorruptionException("Cannot read proto.", e)
        }
    }

    override suspend fun writeTo(t: Sample, output: OutputStream) {
        t.writeTo(output)
    }
}