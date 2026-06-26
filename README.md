# leet-source

Reconstructed source project from `leet-1.0-SNAPSHOT.jar`.

What is included:
- Decompiled Java sources under `src/main/java`
- Original resources under `src/main/resources`
- Nested jars copied to `src/main/resources/META-INF/jars`
- Additional local jars copied to `libs`
- JNA dependencies declared in Gradle for Windows native wrappers

Important limitations:
- This code was recovered from a compiled JAR, not the original repository.
- Decompiled code can differ from the real source formatting and may contain synthetic artifacts.
- The sources were remapped to named Minecraft classes where possible, but many member names still remain in intermediary form (`method_*`, `field_*`).
- A successful build is not guaranteed without further fixing imports, annotations, or mapping/version mismatches.

Suggested next steps:
1. Open the project in IntelliJ IDEA.
2. Let Gradle import dependencies.
3. Continue migrating intermediary member names to Yarn/named mappings.
4. Fix compile issues class-by-class, starting from mixins and Minecraft imports.
