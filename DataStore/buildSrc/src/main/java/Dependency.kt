import Versions.dataStore
import Versions.lifeCycle
import Versions.protoBuf

object Versions {
    const val dataStore = "1.0.0"

    const val protoBufPlugin = "0.9.1"
    const val protoBuf = "3.25.1"

    const val lifeCycle = "2.6.2"
}

object Lib {
    object DataStore{
        const val preferencesStore = "androidx.datastore:datastore-preferences:$dataStore"
        const val protoStore = "androidx.datastore:datastore:$dataStore"

        //Android 종속성 없이 사용 (Multi 모듈용)
        const val preferencesCore = "androidx.datastore:datastore-preferences-core:$dataStore"
        const val protoCore = "androidx.datastore:datastore-core:1.0.0:$dataStore"
    }

    object Activity{
        const val ktx = "androidx.activity:activity-ktx:1.8.1"
    }

    object Google{

        object ProtoBuf{
            const val kotlin = "com.google.protobuf:protobuf-kotlin-lite:$protoBuf"
            const val java = "com.google.protobuf:protobuf-javalite:$protoBuf"
            const val protoCompiler = "com.google.protobuf:protoc:$protoBuf"
        }
    }
}