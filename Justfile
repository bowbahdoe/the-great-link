help:
    just --list

docs:
    mvn dependency:copy-dependencies
    java -p target/dependency --add-modules ALL-MODULE-PATH scripts/CheckForDocs.java

html:
    rm -rf target/html
    java -p target/dependency --add-modules ALL-MODULE-PATH scripts/RenderHtml.java

clean:
   ./mvnw clean

copy_deps: clean
   ./mvnw dependency:copy-dependencies

verify: copy_deps
    java -p target/dependency --add-modules ALL-MODULE-PATH scripts/GetModules.java
    jlink \
            --output target/verify2 \
            --module-path target/dependency \
            --add-modules ALL-MODULE-PATH \
            --bind-services   # --launcher junit=org.junit.platform.console/org.junit.platform.console.ConsoleLauncher
    @just generate

generate: copy_deps
    #  --launcher junit=org.junit.platform.console/org.junit.platform.console.ConsoleLauncher
    jlink \
        --output target/generate \
        --module-path target/dependency \
        --add-modules $(java -p target/dependency --add-modules ALL-MODULE-PATH scripts/GetModules.java) \
        --bind-services \
        --launcher update4j=org.update4j/org.update4j.Bootstrap


gen_licenses_report:
    ./mvnw license:aggregate-third-party-report