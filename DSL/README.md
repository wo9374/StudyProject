# Kotlin DSL

1. 루트 폴더에 buildSrc 폴더 생성  

2. buildSrc 폴더 안에 build.gradle.kts 파일을 생성한 뒤, 아래의 코드와 같이 kotlin-dsl를 enable  
```
plugins {
  `kotlin-dsl`
}

repositories {
  jcenter()
}
```   

3. gradle sync를 하여 이 plugin을 활성화  

4. src > main > java 디렉토리 생성 (폴더 생성)  

5. java 폴더안에 Kotlin 파일을 생성하고, 원하는 이름을 지정  

6. 생성한 Kotlin 파일에 dependency를 지정  
```
import Versions.liveData
import Versions.room

object Versions {
    const val ksp = "1.9.21-1.0.15"

    const val room = "2.6.1"

    const val liveData = "2.6.2"
}

object Lib{

    object Room{
        const val core = "androidx.room:room-runtime:$room"
        const val compiler = "androidx.room:room-compiler:$room"
        const val ktx = "androidx.room:room-ktx:$room"
    }

    object LiveData{
        const val ktx = "androidx.lifecycle:lifecycle-livedata-ktx:$liveData"
    }

}
```  
7. 이후 아래와 같이 build.gradle 파일에서 사용 가능  
```
dependencies {
    implementation(Lib.LiveData.ktx)

    implementation(Lib.Room.core)
    ksp(Lib.Room.compiler)
    implementation(Lib.Room.ktx)
}
```