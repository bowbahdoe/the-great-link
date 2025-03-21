import dev.mccue.tools.jar.Jar;
import dev.mccue.tools.java.Java;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashSet;

public class GetModules {
    public static Collection<String> getNames() throws Exception {
        var moduleNames = new LinkedHashSet<String>();
        try (var filesStream = Files.list(Path.of("target", "dependency"))) {
            var files = filesStream.toList();
            for (var file : files)
            {
                var baos = new ByteArrayOutputStream();
                var runner = Jar.runner(a ->{});
                runner.echoCommand(false);
                runner.redirectOutput(baos);
                runner.arguments()
                        .__file(file).__describe_module().__release(21);
                runner.run();

                var output = baos.toString();
                if (output.startsWith("releases:")) {
                    output = output.replaceAll("releases:.+\n", "").strip();
                }
                var moduleName = output.split("\n")[0].split("([@ ])")[0];
                moduleNames.add(moduleName);
                if (output.contains("releases")) {
                    System.out.println(output);
                }
            }
        }

        var baos = new ByteArrayOutputStream();
        var runner = Java.runner();
        runner.echoCommand(false);
        runner.redirectOutput(baos);
        runner.arguments().__list_modules();
        runner.run();

        for (var moduleNameAndVersion : baos.toString().split("\n")) {
            moduleNames.add(moduleNameAndVersion.split("@")[0]);
        }

        return moduleNames;
    }

    public static void main(String[] args) throws Exception {
        var moduleNames = getNames();

        var joinedNames = String.join(",", moduleNames);
        if (args.length > 0) {
            Files.writeString(Path.of(args[0]), joinedNames);
        }

        System.out.println(joinedNames);

    }
}
