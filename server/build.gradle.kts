plugins {
  kotlin("multiplatform") version "1.9.0"
  application
}

repositories {
  mavenCentral()
}

kotlin {
  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
  }

  jvm {
    compilations.all {
      kotlinOptions.apiVersion = "1.9"
      kotlinOptions.languageVersion = "1.9"
    }
    testRuns["test"].executionTask.configure {
      useJUnitPlatform()
    }
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(libs.ktor.server.core)
        implementation(libs.ktor.server.cio)
      }
    }
    val commonTest by getting
  }
}

application {
  mainClass.set("MainKt")
}
