import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.nio.file.Files;
import java.nio.file.Path;

public class RenderHtml {
    public static void main(String[] args) throws Exception {
        try (var list = Files.walk(Path.of("./docs"))) {
            for (var file : list.toList()) {
                if (Files.exists(file) && !Files.isDirectory(file) && file.toString().endsWith(".md")) {
                    Parser parser = Parser.builder().build();
                    Node document = parser.parse(Files.readString(file));
                    HtmlRenderer renderer = HtmlRenderer.builder().build();

                    Files.createDirectories(Path.of("target/html"));
                    String htmlPathStr = file.toString().replaceFirst("docs", "target/html");
                    htmlPathStr = new StringBuilder(new StringBuilder(htmlPathStr).reverse()
                            .toString()
                            .replaceFirst("dm\\.", "lmth.")
                    ).reverse().toString();

                    Path htmlPath = Path.of(htmlPathStr);
                    Files.createDirectories(htmlPath.getParent());
                    Files.writeString(htmlPath, renderer.render(document));
                }

            }
        }

    }
}