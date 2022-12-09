package experimental.users.jlsenterfitt.aoc.a2022.a09;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.base.Pair;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

/** */
public final class Main {

  private static final String PATH = "experimental/users/jlsenterfitt/aoc/a2022/a09/";
  private static final String FILE_PATH = PATH + "input.txt";
  private static final String TEST_FILE_PATH = PATH + "test_input.txt";

  static class Knot {
    int xPos = 0;
    int yPos = 0;
    Set<Pair<Integer, Integer>> positions = Sets.newLinkedHashSet(Pair.of(xPos, yPos));

    void execute(Move move) {
      xPos += move.xVel;
      yPos += move.yVel;
      positions.add(Pair.of(xPos, yPos));
    }

    void follow(Knot knot) {
      int xDelta = knot.xPos - xPos;
      int yDelta = knot.yPos - yPos;
      if (Math.abs(xDelta) > 1 || Math.abs(yDelta) > 1) {
        xDelta = xDelta != 0 ? xDelta / Math.abs(xDelta) : 0;
        yDelta = yDelta != 0 ? yDelta / Math.abs(yDelta) : 0;
        execute(new Move(xDelta, yDelta, 1));
      }
    }
  }

  static class Move {
    int xVel = 0;
    int yVel = 0;
    final int moveCount;

    Move(int x, int y, int moveCount) {
      this.xVel = x;
      this.yVel = y;
      this.moveCount = moveCount;
    }

    Move(String line) {
      ImmutableList<String> pieces = ImmutableList.copyOf(Splitter.on(" ").split(line));
      switch (pieces.get(0)) {
        case "U":
          yVel = 1;
          break;
        case "D":
          yVel = -1;
          break;
        case "L":
          xVel = -1;
          break;
        case "R":
          xVel = 1;
          break;
        default:
          throw new IllegalArgumentException(line);
      }
      moveCount = Integer.parseInt(pieces.get(1));
    }

    void executeMoves(ImmutableList<Knot> knots) {
      for (int i = 0; i < moveCount; i++) {
        knots.get(0).execute(this);
        for (int j = 1; j < knots.size(); j++) {
          knots.get(j).follow(knots.get(j - 1));
        }
      }
    }
  }

  public static ImmutableList<Move> digest(List<String> lines) {
    return lines.stream().map(line -> new Move(line)).collect(toImmutableList());
  }

  public static int firstQuestion(ImmutableList<Move> moves) {
    ImmutableList<Knot> knots =
        IntStream.range(0, 2).mapToObj(i -> new Knot()).collect(toImmutableList());
    moves.forEach(move -> move.executeMoves(knots));
    return knots.get(knots.size() - 1).positions.size();
  }

  public static int secondQuestion(ImmutableList<Move> moves) {
    ImmutableList<Knot> knots =
        IntStream.range(0, 10).mapToObj(i -> new Knot()).collect(toImmutableList());
    moves.forEach(move -> move.executeMoves(knots));
    return knots.get(knots.size() - 1).positions.size();
  }

  public static void main(String[] args) throws IOException {
    ImmutableList<Move> testData = digest(Files.readAllLines(Path.of(TEST_FILE_PATH)));
    int firstTestAnswer = firstQuestion(testData);
    Preconditions.checkArgument(firstTestAnswer == 13);
    int secondTestAnswer = secondQuestion(testData);
    Preconditions.checkArgument(secondTestAnswer == 1);

    ImmutableList<Move> data = digest(Files.readAllLines(Path.of(FILE_PATH)));
    int firstAnswer = firstQuestion(data);
    System.out.println(firstAnswer);
    int secondAnswer = secondQuestion(data);
    System.out.println(secondAnswer);
  }

  private Main() {}
}
