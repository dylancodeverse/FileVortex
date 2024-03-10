package tmplFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * The Tmpl class represents a template and provides methods to read and validate
 * the content of a template file. The template is expected to follow a specific
 * structure, and the class ensures that the structure is valid.
 */
public class Tmpl {

    // Represents a root child
    Children child;

    /**
     * Constructor for the Tmpl class.
     *
     * @param filePath The file path associated with this instance.
     * @throws Exception If there is an issue reading or validating the template file.
     */
    public Tmpl(String filePath) throws Exception {
        // Read content from the file, trim it, and then check and set the children.
        String text = readFromFile(filePath).trim();
        checkText(text);
        setChildren(text);
    }

    /**
     * Performs a search operation in the tree-like structure to find a specific node or a sequence of nested nodes.
     *
     * @param nodes The names of the nodes to search for in sequence.
     * @return The Children object representing the last node in the sequence if found.
     * @throws Exception If any of the specified nodes is not found in the structure.
     */
    public Children search(String... nodes) throws Exception {
        // Start the search from the root node.
        Children result = this.child.BFS("root");

        // Iterate through the specified nodes and perform BFS for each node.
        for (int i = 0; i < nodes.length; i++) {
            // Perform BFS for the current node.
            result = result.BFS(nodes[i]);
        }

        // Return the Children object representing the last node in the sequence.
        return result;
    }



    /**
     * Sets the children of the root node based on the provided text.
     *
     * @param text The text representing the content of the template.
     * @throws Exception If there is an issue setting the children.
     */
    public void setChildren(String text) throws Exception {
        child = new Children();
        child.setNode("root");
        child.setChildren(text);
    }

    /**
     * Checks if the provided text has a valid structure with balanced curly braces.
     *
     * @param text The text to be validated.
     * @throws Exception If the text structure is not valid.
     */
    private void checkText(String text) throws Exception {
        char[] chars = text.toCharArray();
        int n = 0;

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '{')
                n++;
            else if (chars[i] == '}')
                n--;

            if (n < 0)
                throw new Exception("The tmpl is not valid: unexpected '}' at line " + i);
        }
    }

    /**
     * Reads the content of a file and returns it as a string.
     *
     * @param filePath The path of the file to be read.
     * @return The content of the file as a string.
     */
    public static String readFromFile(String filePath) {
        StringBuilder content = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();
    }

    // The main method is commented out, but it can be uncommented for testing purposes.
    public static void main(String[] args) throws Exception {
        Tmpl t = new Tmpl("C:\\Users\\ratia\\Desktop\\workspace\\S5\\MrNaina\\ProjectBuilder4.0\\ProjectBuilder\\src\\tmplFile\\example.tmpl");
        // t.child.BFS("example").BFS("employee");
         

        System.out.println(t.search(new String[]{"example" ,"employee"}));
    }

    public Children getChild() {
        return child;
    }

    
}
