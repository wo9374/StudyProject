import Versions.dataStore

object Versions {
    const val dataStore = "1.0.0"
}

object Libs {
    object DataStore{
        const val preferencesStore = "androidx.datastore:datastore-preferences:$dataStore"
        const val protoStore = "androidx.datastore:datastore:$dataStore"

        //Android 종속성 없이 사용 (Multi 모듈용)
        const val preferencesCore = "androidx.datastore:datastore-preferences-core:$dataStore"
        const val protoCore = "androidx.datastore:datastore-core:1.0.0:$dataStore"
    }
}