help:
    just --list

docs:
    mvn dependency:copy-dependencies
    java -p target/dependency --add-modules ALL-MODULE-PATH scripts/CheckForDocs.java

verify:
    rm -rf target
    mvn dependency:copy-dependencies
    java -p target/dependency --add-modules ALL-MODULE-PATH scripts/GetModules.java
    jlink \
            --output target/verify2 \
            --module-path target/dependency \
            --add-modules ALL-MODULE-PATH \
            --bind-services   # --launcher junit=org.junit.platform.console/org.junit.platform.console.ConsoleLauncher

    jlink \
        --output target/verify \
        --module-path target/dependency \
        --add-modules $(java -p target/dependency --add-modules ALL-MODULE-PATH scripts/GetModules.java) \
        --bind-services  #  --launcher junit=org.junit.platform.console/org.junit.platform.console.ConsoleLauncher
