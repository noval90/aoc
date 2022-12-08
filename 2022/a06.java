package experimental.users.jlsenterfitt.aoc.a2022.a06;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** */
public final class Main {

  private static final String PATH = "experimental/users/jlsenterfitt/aoc/a2022/a06/";
  private static final String FILE_PATH = PATH + "input.txt";
  private static final String TEST_FILE_PATH = PATH + "test_input.txt";

  public static ImmutableList<Character> digest(List<String> lines) {
    ImmutableList.Builder<Character> result = ImmutableList.builder();
    for (String line : lines) {
      for (char c : line.toCharArray()) {
        result.add(c);
      }
    }
    return result.build();
  }

  public static int firstQuestion(ImmutableList<Character> lines) {
    for (int i = 0; i < lines.size() - 4; i++) {
      if (ImmutableSet.copyOf(lines.subList(i, i + 4)).size() == 4) {
        return i + 4;
      }
    }
    return -1;
  }

  public static int secondQuestion(ImmutableList<Character> lines) {
    for (int i = 0; i < lines.size() - 14; i++) {
      if (ImmutableSet.copyOf(lines.subList(i, i + 14)).size() == 14) {
        return i + 14;
      }
    }
    return -1;
  }

  public static void main(String[] args) throws IOException {
    ImmutableList<Character> testData = digest(Files.readAllLines(Path.of(TEST_FILE_PATH)));
    int firstTestAnswer = firstQuestion(testData);
    Preconditions.checkArgument(firstTestAnswer == 5);
    int secondTestAnswer = secondQuestion(testData);
    Preconditions.checkArgument(secondTestAnswer == 23);

    ImmutableList<Character> data = digest(Files.readAllLines(Path.of(FILE_PATH)));
    int firstAnswer = firstQuestion(data);
    System.out.println(firstAnswer);
    int secondAnswer = secondQuestion(data);
    System.out.println(secondAnswer);
  }

  private Main() {}
}
