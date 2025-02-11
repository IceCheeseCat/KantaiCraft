package com.github.icecheesecat.kantaicraft.menu.screen;

import net.minecraft.util.FastColor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TextGridLayout {

    List<GridCell> grid = new ArrayList<>();
    int x, y, fontSize;
    int fontWidth;
    int color = FastColor.ARGB32.color(255, 255, 255, 255);

    public TextGridLayout(int x, int y, int fontSize, int fontWidth) {
        this.x = x;
        this.y = y;
        this.fontSize = fontSize;
    }

    public void addChild(Supplier<String> textSupplier, int row, int column) {

        boolean has = false;
        GridCell nCell = new GridCell(row, column, textSupplier);
        for (int i = 0; i < grid.size(); i++) {
            int j = i - 1;
            GridCell prevCell;
            if (j < 0) prevCell = null;
            else prevCell = grid.get(j);

            if (canPlaceBetween(prevCell, grid.get(i), nCell)) {
                grid.add(i, new GridCell(row, column, textSupplier));
                has = true;
                break;
            }
        }

        if (!has) {
            grid.add(new GridCell(row, column, textSupplier));
        }
    }

    private boolean canPlaceBetween(GridCell front, GridCell back, GridCell curr) {
        if (front == null) {
            if (smallerCellThan(curr, back)) {
                return true;
            }
        }
        else if (greaterCellThan(curr, front) && smallerCellThan(curr, back)) {
            return true;
        }
        else if (equalCell(curr, front)) {
            return true;
        }

        return false;
    }

    private boolean smallerCellThan(GridCell a, GridCell b) {
        if (a.row < b.row) {
            return true;
        }
        else if (a.row == b.row && a.column < b.column) {
            return true;
        }

        return false;
    }

    private boolean greaterCellThan(GridCell a, GridCell b) {
        if (a.row > b.row) return true;
        if (a.row == b.row && a.column > b.column) return true;

        return false;
    }

    private boolean equalCell(GridCell a, GridCell b) {
        if (a.row == b.row && a.column == b.column) return true;
        return false;
    }

    public List<GridCell> getGrid() {
        return grid;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void arrangeElements() {
        for (var cell: grid) {
            int row = cell.row;
            int column = cell.column;

            cell.setPos(new TextPos(row * fontWidth, column * fontSize, row * (fontWidth + 1), column * (fontSize + 1)));
        }

        switchToRelativePoses();
    }

    public void switchToRelativePoses() {
        grid.forEach(
                cell -> {
                    cell.pos.move(this.x, this.y);
                }
        );
    }

    public static class GridCell {
        int row, column;
        Supplier<String> textSupplier;
        TextPos pos;

        public GridCell(int row, int column, Supplier<String> textSupplier) {
            this.row = row;
            this.column = column;
            this.textSupplier = textSupplier;
        }

        public TextPos getPos() {
            return pos;
        }

        public void setPos(TextPos pos) {
            this.pos = pos;
        }

        public String getText() {
            return textSupplier.get();
        }
    }

    public static class TextPos {
        int minX, minY, maxX, maxY;
        boolean moved = false;

        public TextPos(int minX, int minY, int maxX, int maxY) {
            this.minX = minX;
            this.minY = minY;
            this.maxX = maxX;
            this.maxY = maxY;
        }

        public Pair<Integer, Integer> getCenter() {
            return Pair.of((minX + maxX) / 2, (minY + maxY) / 2);
        }

        public int getMinX() {
            return minX;
        }

        public void setMinX(int minX) {
            this.minX = minX;
            this.moved = true;
        }

        public int getMinY() {
            return minY;
        }

        public void setMinY(int minY) {
            this.minY = minY;
            this.moved = true;
        }

        public int getMaxX() {
            return maxX;
        }

        public void setMaxX(int maxX) {
            this.maxX = maxX;
            this.moved = true;
        }

        public int getMaxY() {
            return maxY;
        }

        public void setMaxY(int maxY) {
            this.maxY = maxY;
            this.moved = true;
        }

        public void move(int x, int y) {
            this.minX += x;
            this.minY += y;
            this.maxX += x;
            this.maxY += y;
        }
    }

}
