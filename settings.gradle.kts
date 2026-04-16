plugins {
    id("org.gradle.toolchains.foojay-resolver-convention").version("0.8.0")
}
rootProject.name = "AetherStream"

include("aether-common")
include("aether-pulse")
include("aether-catalyst")
include("aether-ledger")
include("aether-beacon")
include("aether-gateway")