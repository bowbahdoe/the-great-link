help:
    just --list

docs:
    mvn dependency:copy-dependencies
    java -p target/dependency --add-modules ALL-MODULE-PATH scripts/CheckForDocs.java

html:
    rm -rf target/html
    java -p target/dependency --add-modules ALL-MODULE-PATH scripts/RenderHtml.java

verify:
    rm -rf target
    mvn dependency:copy-dependencies
    java -p target/dependency --add-modules ALL-MODULE-PATH scripts/GetModules.java
    jlink \
            --output target/verify2 \
            --module-path target/dependency \
            --add-modules ALL-MODULE-PATH \
            --bind-services   # --launcher junit=org.junit.platform.console/org.junit.platform.console.ConsoleLauncher

    #  --launcher junit=org.junit.platform.console/org.junit.platform.console.ConsoleLauncher
    jlink \
        --output target/verify \
        --module-path target/dependency \
        --add-modules $(java -p target/dependency --add-modules ALL-MODULE-PATH scripts/GetModules.java) \
        --bind-services \
        --launcher update4j=org.update4j/org.update4j.Bootstrap


gen_licenses_report:
    mvn license:aggregate-third-party-report