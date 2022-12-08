package experimental.users.jlsenterfitt.aoc.a2022.a08;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** */
public final class Main {

  private static final String PATH = "experimental/users/jlsenterfitt/aoc/a2022/a08/";
  private static final String FILE_PATH = PATH + "input.txt";
  private static final String TEST_FILE_PATH = PATH + "test_input.txt";

  static class Forest {
    ImmutableList<ImmutableList<Integer>> trees;

    Forest(ImmutableList<ImmutableList<Integer>> trees) {
      this.trees = trees;
    }

    boolean hasTaller(int row, int col, int rowOffset, int colOffset) {
      int currRow = row + rowOffset;
      int currCol = col + colOffset;
      while (currRow < trees.size() && currRow >= 0 && currCol < trees.size() && currCol >= 0) {
        if (trees.get(currRow).get(currCol) >= trees.get(row).get(col)) {
          return true;
        }
        currRow += rowOffset;
        currCol += colOffset;
      }
      return false;
    }

    int countShorter(int row, int col, int rowOffset, int colOffset) {
      int count = 0;
      int currRow = row + rowOffset;
      int currCol = col + colOffset;
      while (currRow < trees.size() && currRow >= 0 && currCol < trees.size() && currCol >= 0) {
        count++;
        if (trees.get(currRow).get(currCol) >= trees.get(row).get(col)) {
          break;
        }
        currRow += rowOffset;
        currCol += colOffset;
      }
      return count;
    }

    int countShorter(int row, int col) {
      int down = countShorter(row, col, 1, 0);
      int up = countShorter(row, col, -1, 0);
      int right = countShorter(row, col, 0, 1);
      int left = countShorter(row, col, 0, -1);
      return down * up * right * left;
    }

    boolean isVisible(int row, int col) {
      boolean visDown = !hasTaller(row, col, 1, 0);
      boolean visUp = !hasTaller(row, col, -1, 0);
      boolean visRight = !hasTaller(row, col, 0, 1);
      boolean visLeft = !hasTaller(row, col, 0, -1);
      return visDown || visUp || visRight || visLeft;
    }

    int countVisible() {
      int count = 0;
      for (int i = 0; i < trees.size(); i++) {
        for (int j = 0; j < trees.get(i).size(); j++) {
          if (isVisible(i, j)) {
            count++;
          }
        }
      }
      return count;
    }

    int getBestView() {
      int best = -1;
      for (int i = 0; i < trees.size(); i++) {
        for (int j = 0; j < trees.get(i).size(); j++) {
          int curr = countShorter(i, j);
          if (curr > best) {
            best = curr;
          }
        }
      }
      return best;
    }
  }

  public static Forest digest(List<String> lines) {
    ImmutableList.Builder<ImmutableList<Integer>> result = ImmutableList.builder();
    for (int i = 0; i < lines.size(); i++) {
      String line = lines.get(i);
      ImmutableList.Builder<Integer> row = ImmutableList.builder();
      for (int j = 0; j < line.length(); j++) {
        row.add(Integer.parseInt(line.substring(j, j + 1)));
      }
      result.add(row.build());
    }
    return new Forest(result.build());
  }

  public static int firstQuestion(Forest data) {
    return data.countVisible();
  }

  public static int secondQuestion(Forest data) {
    return data.getBestView();
  }

  public static void main(String[] args) throws IOException {
    Forest testData = digest(Files.readAllLines(Path.of(TEST_FILE_PATH)));
    System.out.println(testData);
    int firstTestAnswer = firstQuestion(testData);
    Preconditions.checkArgument(firstTestAnswer == 21);
    int secondTestAnswer = secondQuestion(testData);
    Preconditions.checkArgument(secondTestAnswer == 8);

    Forest data = digest(Files.readAllLines(Path.of(FILE_PATH)));
    int firstAnswer = firstQuestion(data);
    System.out.println(firstAnswer);
    int secondAnswer = secondQuestion(data);
    System.out.println(secondAnswer);
  }

  private Main() {}
}
