package experimental.users.jlsenterfitt.aoc.a2022.a07;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.re2j.Matcher;
import com.google.re2j.Pattern;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** */
public final class Main {

  private static final String PATH = "experimental/users/jlsenterfitt/aoc/a2022/a07/";
  private static final String FILE_PATH = PATH + "input.txt";
  private static final String TEST_FILE_PATH = PATH + "test_input.txt";

  static final class File {
    private final int size;
    private final String name;

    File(int size, String name) {
      this.size = size;
      this.name = name;
    }

    int size() {
      return this.size;
    }
  }

  static final class Directory {
    private Optional<Integer> size = Optional.empty();
    Map<String, Directory> children = Maps.newLinkedHashMap();
    Map<String, File> files = Maps.newLinkedHashMap();
    private Optional<Directory> parent = Optional.empty();
    private final String name;

    Directory(String name) {
      this.name = name;
    }

    Directory getChild(String name) {
      return Preconditions.checkNotNull(children.get(name));
    }

    void addFile(File file) {
      if (!this.files.containsKey(file.name)) {
        this.files.put(file.name, file);
      }
    }

    void addDirectory(Directory directory) {
      if (!this.children.containsKey(directory.name)) {
        this.children.put(directory.name, directory);
        directory.setParent(this);
      }
    }

    void setParent(Directory parent) {
      this.parent = Optional.of(parent);
    }

    int size() {
      if (!size.isPresent()) {
        size =
            Optional.of(
                children.values().stream().mapToInt(Directory::size).sum()
                    + files.values().stream().mapToInt(File::size).sum());
      }
      return size.get();
    }

    Directory parent() {
      return parent.orElseThrow();
    }
  }

  public static Directory digest(List<String> lines) {
    Directory root = new Directory("/");
    Directory current = root;
    Pattern cdDirPattern = Pattern.compile("\\$ cd (.+)");
    Pattern dirPattern = Pattern.compile("dir (.+)");
    Pattern filePattern = Pattern.compile("([0-9]+) (.+)");

    for (String line : lines) {
      Matcher cdDirMatcher = cdDirPattern.matcher(line);
      Matcher dirMatcher = dirPattern.matcher(line);
      Matcher fileMatcher = filePattern.matcher(line);

      if (line.equals("$ cd ..")) {
        // Handle cd up one dir
        current = Preconditions.checkNotNull(current).parent();
      } else if (line.equals("$ cd /")) {
        // Handle cp top
        current = root;
      } else if (cdDirMatcher.matches()) {
        // Handle cd down one dir
        current = current.getChild(cdDirMatcher.group(1));
      } else if (dirMatcher.matches()) {
        // Handle dir
        String dName = dirMatcher.group(1);
        Directory d = new Directory(dName);
        Preconditions.checkNotNull(current).addDirectory(d);
      } else if (fileMatcher.matches()) {
        // Handle file
        String fName = fileMatcher.group(2);
        File f = new File(Integer.parseInt(fileMatcher.group(1)), fName);
        Preconditions.checkNotNull(current).addFile(f);
      } else if (line.equals("$ ls")) {
        // Handle ls
        continue;
      } else {
        throw new IllegalArgumentException(line);
      }
    }

    return root;
  }

  public static int firstQuestion(Directory root) {
    int curr = root.size() <= 100000 ? root.size() : 0;
    curr += root.children.values().stream().mapToInt(Main::firstQuestion).sum();
    return curr;
  }

  public static int secondQuestion(Directory root, int limit) {
    int curr = root.size() >= limit ? root.size() : Integer.MAX_VALUE;
    int childBest =
        root.children.values().stream()
            .mapToInt(d -> secondQuestion(d, limit))
            .filter(x -> x >= limit)
            .min()
            .orElse(Integer.MAX_VALUE);
    return curr < childBest ? curr : childBest;
  }

  public static void main(String[] args) throws IOException {
    Directory testData = digest(Files.readAllLines(Path.of(TEST_FILE_PATH)));
    int firstTestAnswer = firstQuestion(testData);
    Preconditions.checkArgument(firstTestAnswer == 95437);
    int secondTestAnswer = secondQuestion(testData, testData.size() - 40000000);
    Preconditions.checkArgument(secondTestAnswer == 24933642);

    Directory data = digest(Files.readAllLines(Path.of(FILE_PATH)));
    int firstAnswer = firstQuestion(data);
    System.out.println(firstAnswer);
    System.out.println("Size " + data.size());
    System.out.println("Need " + (data.size() - 40000000));
    int secondAnswer = secondQuestion(data, data.size() - 40000000);
    System.out.println(secondAnswer);

    // Too high:  5,267,342
    // Too high: 43,636,666
  }

  private Main() {}
}
