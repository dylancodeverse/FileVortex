package tmplFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Tsy haiko hoe inona no soratana eto fa node eh
 * 
 */
public class Children {

    // node name
    String node;

    // Represents the value associated with this instance
    String value;

    // Represents an array of Children objects
    Children[] children;

    /**
     * Gets the value associated with this instance.
     *
     * @return The value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value associated with this instance. Throws an exception if there
     * are children.
     *
     * @param value The new value.
     * @throws Exception If there are children already.
     */
    public void setValue(String value) throws Exception {
        if (children != null) {
            throw new Exception("Can't set the value because there are children");
        }
        while (value.contains("/{")) {
            value = value.replace("/{", "{");
        }
        while (value.contains("/}")) {
            value = value.replace("/}", "}");
        }
        Iterator<String> iterator = value.lines().iterator();
        this.value = "";
        while (iterator.hasNext()) {
            String s = iterator.next();
            if (!s.isBlank()) {
                this.value += s.trim() + "\n";

            }
        }
        while (value.contains("  ")) {
            value = value.replaceAll("  ", " ");
        }
    }

    /**
     * Gets the array of Children objects associated with this instance.
     *
     * @return The array of children.
     */
    public Children[] getChildren() {
        return children;
    }

    /**
     * Sets the array of Children objects associated with this instance. Throws an
     * exception if there is a value.
     *
     * @param children The new array of children.
     * @throws Exception If there is a value already.
     */
    public void setChildren(Children[] children) throws Exception {
        if (value != null) {
            throw new Exception("Can't set the children because there is a value");
        }
        this.children = children;
    }

    /**
     * Appends a Children object to the array of children. Throws an exception if
     * there is a value.
     *
     * @param child The Children object to append.
     * @throws Exception If there is a value already.
     */
    public void append(Children child) throws Exception {
        if (value != null) {
            throw new Exception("Can't set the children because there is a value");
        }
        List<Children> tempChildren = getChildrenAsList();
        tempChildren.add(child);
        setChildren(tempChildren.toArray(new Children[tempChildren.size()]));
    }

    /**
     * Converts the array of Children to an ArrayList of Children.
     *
     * @return An ArrayList of Children.
     */
    private ArrayList<Children> getChildrenAsList() {
        ArrayList<Children> tempList = new ArrayList<>();
        Children[] children = getChildren();
        for (int i = 0; i < children.length; i++) {
            tempList.add(children[i]);
        }
        return tempList;
    }

    /**
     * Parses the provided text and sets the children of this instance based on the
     * template structure.
     * If the text contains nested sections (denoted by ":{" and not ":/{" ), it
     * creates new Children objects
     * for each nested section and recursively sets their children. If the text does
     * not contain nested
     * sections, it sets the value of this instance.
     *
     * @param text The text to be parsed and used to set children or value.
     * @throws Exception If there is an issue parsing or setting the children.
     */
    public void setChildren(String text) throws Exception {
        // Check if the text contains nested sections and does not contain closing tags
        // for root level.
        if (text.contains(":{") && !text.contains(":/{")) {
            Iterator<String> lines = text.lines().iterator();
            ArrayList<Children> newChildren = new ArrayList<>();

            while (lines.hasNext()) {
                String t = lines.next();

                // Check if the line contains an opening tag for a nested section.
                if (t.contains(":{") && !t.contains(":/{")) {
                    Children n = new Children();
                    n.setNode(t.replace(":{", "").trim());
                    int x = 1;
                    StringBuilder newTxt = new StringBuilder();

                    // Read lines until the corresponding closing tag is found.
                    while (lines.hasNext()) {
                        t = lines.next();

                        if (t.contains(":{") && !t.contains(":/{")) {
                            x++;
                        }

                        if (t.contains("}") && !t.contains("/}")) {
                            x--;
                        }

                        // Append lines as long as the closing tag is not found.
                        if (x != 0) {
                            newTxt.append("\n").append(t);
                        }

                        // If the closing tag is found, set the children and add the new Children
                        // object.
                        if (x == 0) {
                            n.setChildren(newTxt.toString());
                            newChildren.add(n);
                            break;
                        }
                    }
                }
            }

            // Set the array of children for this instance.
            setChildren(newChildren.toArray(new Children[newChildren.size()]));
        } else {
            // If there are no nested sections, set the value of this instance.
            setValue(text);
        }
    }

    /**
     * Performs Breadth-First Search (BFS) on the tree-like structure represented by
     * the Children class.
     *
     * @param target The target node to search for.
     * @return The Children object representing the target node if found.
     * @throws Exception If the target node is not found in the structure.
     */
    public Children BFS(String target) throws Exception {
        // Create a queue for BFS traversal.
        Queue<Children> fileAttente = new LinkedList<>();
        // Add the root node (this) to the queue.
        fileAttente.add(this);

        // Create a HashMap to keep track of visited nodes.
        HashMap<String, Children> marked = new HashMap<>();
        marked.put(getNode(), this);

        // Perform BFS.
        while (!fileAttente.isEmpty()) {
            // Dequeue the front node.
            Children reference = fileAttente.poll();

            // Check if the current node is the target node.
            if (reference.getNode().equalsIgnoreCase(target)) {
                return reference;
            }

            // Enqueue all children of the current node.
            try {
                for (int i = 0; i < reference.getChildren().length; i++) {
                    Children child = reference.getChildren()[i];
                    // Check if the child node is already visited.
                    if (!marked.containsKey(child.getNode())) {
                        fileAttente.add(child);
                        marked.put(child.getNode(), child);
                    }
                }
            } catch (NullPointerException e) {
            }

        }

        // Target node not found.
        throw new Exception("Target: " + target + " not found");
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public ArrayList<String> getAllChildValues() {
        ArrayList<String> allValues = new ArrayList<>();
        getAllChildValuesRecursive(this, allValues);
        return allValues;
    }

    // Recursive helper method
    private static void getAllChildValuesRecursive(Children parent, ArrayList<String> allValues) {
        if (parent != null) {
            // Add the value of the current node to the list
            if (parent.value != null) {
                allValues.add(parent.value);
            }

            // Recursively call the method for each child
            if (parent.children != null) {
                for (Children child : parent.children) {
                    getAllChildValuesRecursive(child, allValues);
                }
            }
        }
    }

}
