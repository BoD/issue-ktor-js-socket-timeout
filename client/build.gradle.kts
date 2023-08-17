import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
  kotlin("multiplatform") version "1.9.0"
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
    withJava()
    testRuns["test"].executionTask.configure {
      useJUnitPlatform()
    }
  }
  val hostOs = System.getProperty("os.name")
  val isMingwX64 = hostOs.startsWith("Windows")
  val nativeTarget = when {
    hostOs == "Mac OS X" -> macosArm64("native")
    hostOs == "Linux" -> linuxX64("native")
    isMingwX64 -> mingwX64("native")
    else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
  }.apply {
    compilations.all {
      kotlinOptions.apiVersion = "1.9"
      kotlinOptions.languageVersion = "1.9"
    }
  }

  js {
    nodejs()
  }

  sourceSets {
    val commonMain by getting
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
        implementation(libs.kotlinx.coroutines.test)

        implementation(libs.ktor.client.core)
      }
    }

    val jvmMain by getting
    val jvmTest by getting {
      dependencies {
        implementation(libs.ktor.client.okhttp)
      }
    }

    val nativeMain by getting
    val nativeTest by getting {
      dependencies {
        implementation(libs.ktor.client.darwin)
      }
    }

    val jsMain by getting
    val jsTest by getting {
      dependencies {
        implementation(libs.ktor.client.js)
      }
    }
  }
}

tasks.withType(AbstractTestTask::class.java) {
  testLogging {
    exceptionFormat = TestExceptionFormat.FULL
    events.add(TestLogEvent.PASSED)
    events.add(TestLogEvent.FAILED)
    showStandardStreams = true
  }
}
