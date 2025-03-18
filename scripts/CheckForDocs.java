import dev.mccue.tools.jar.Jar;
import dev.mccue.tools.java.Java;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

public class CheckForDocs {
    static String replaceLast(String s, String target, String replacement) {
        return new StringBuilder(new StringBuilder(s).reverse().toString().replaceFirst(
                new StringBuilder(target).reverse().toString(),
                new StringBuilder(replacement).reverse().toString()
        )).reverse().toString();
    }
    public static void main(String[] args)throws Exception {
        var libs = List.copyOf(GetModules.getNames());
        var docs = Files.list(Path.of("docs", "modules"))
                .filter(lib -> lib.getFileName().toString().endsWith(".md"))
                .map(lib -> replaceLast(lib.getFileName().toString(),".md", ""))
                .toList();

        var jsons = Files.list(Path.of("docs", "modules"))
                .filter(lib -> lib.getFileName().toString().endsWith(".json"))
                .map(lib -> replaceLast(lib.getFileName().toString(),".json", ""))
                .toList();

        var libsSet = new LinkedHashSet<>(libs);
        docs.forEach(libsSet::remove);

        var jsonsSet = new LinkedHashSet<>(libs);
        jsons.forEach(jsonsSet::remove);

        int exit = 0;

        if (!libsSet.isEmpty()) {
            System.err.println("Missing Docs For Modules");
            System.err.println("-".repeat(50));
            libsSet.forEach(System.err::println);
            libsSet.forEach(j -> {
                try {
                    Files.writeString(Path.of("docs/modules/" + j + ".md"), "\n");
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
            System.err.println();
            exit = 1;
        }

        if (!jsonsSet.isEmpty()) {
            System.err.println("Missing Json Metadata For Modules");
            System.err.println("-".repeat(50));
            jsonsSet.forEach(System.err::println);

            jsonsSet.forEach(j -> {
                try {
                    Files.writeString(Path.of("docs/modules/" + j + ".json"), "{}\n");
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
            System.err.println();
            exit = 1;
        }

        System.exit(exit);
    }
}