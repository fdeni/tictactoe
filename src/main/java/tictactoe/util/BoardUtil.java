package tictactoe.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BoardUtil {
    public static List<List<String>> createEmptyBoard(int arraySize) {
        List<List<String>> rows = new ArrayList<>();

        for (int rowIndex = 0; rowIndex < arraySize; rowIndex++) {
            List<String> row = new ArrayList<>();
            for (int columnIndex = 0; columnIndex < arraySize; columnIndex++) {
                row.add(BoardTileEnum.EMPTY.toString());

            }
            rows.add(row);
        }

        return rows;
    }

    public static String getRandomAvailableTile(List<List<String>> rows) {
        List<String> available = new ArrayList<>();

        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
            List<String> row = rows.get(rowIndex);
            for (int columnIndex = 0; columnIndex < row.size(); columnIndex++) {
                String tileValue = row.get(columnIndex);
                if (tileValue.isEmpty()) {
                    available.add(rowIndex + "-" + columnIndex);
                }
            }
        }

        if (available.isEmpty()) {
            return null;
        }
        int randomNum = new Random().nextInt(available.size());
        return available.get(randomNum);

    }

    public static List<List<String>> getAllLines(List<List<String>> rows, int arraySize) {
        List<List<String>> lines = new ArrayList<>();

        for (int rowIndex = 0; rowIndex < arraySize; rowIndex++) {
            lines.add(rows.get(rowIndex));
        }

        for (int columnIndex = 0; columnIndex < arraySize; columnIndex++) {
            List<String> columnLine = new ArrayList<>();
            for (List<String> row : rows) {
                columnLine.add(row.get(columnIndex));
            }
            lines.add(columnLine);
        }

        List<String> diagonal1 = new ArrayList<>();
        for (int i = 0; i < arraySize; i++) {
            diagonal1.add(rows.get(i).get(i));
        }
        lines.add(diagonal1);

        List<String> diagonal2 = new ArrayList<>();
        for (int i = 0; i < arraySize; i++) {
            diagonal2.add(rows.get(i).get(arraySize - 1 - i));
        }
        lines.add(diagonal2);

        return lines;
    }
}
