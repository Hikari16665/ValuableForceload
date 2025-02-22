package me.eventually.valuableforceload.utils.inventory;

import me.eventually.valuableforceload.utils.handlers.MenuClickHandler;
import org.bukkit.inventory.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ParametersAreNonnullByDefault
public class MatrixDrawer implements MenuDrawer{
    private final int size;
    private final Map<Character, ItemStack> characterMap = new HashMap<>();
    private final Map<Character, MenuClickHandler> clickHandlerMap = new HashMap<>();
    private final List<String> matrix = new ArrayList<>();

    public MatrixDrawer(int size) {
        this.size = size;
    }
    public MatrixDrawer addLine(String line) {
        matrix.add(line);
        return this;
    }
    public MatrixDrawer addExplain(char c, ItemStack item) {
        characterMap.put(c, new ItemStack(item));
        return this;
    }
    public MatrixDrawer addExplain(String c, ItemStack item) {
        characterMap.put(c.charAt(0), new ItemStack(item));
        return this;
    }
    public MatrixDrawer addExplain(char c, ItemStack item, MenuClickHandler clickHandler) {
        characterMap.put(c, new ItemStack(item));
        clickHandlerMap.put(c, clickHandler);
        return this;
    }
    public MatrixDrawer addExplain(String c, ItemStack item, MenuClickHandler clickHandler) {
        characterMap.put(c.charAt(0), new ItemStack(item));
        clickHandlerMap.put(c.charAt(0), clickHandler);
        return this;
    }
    public MatrixDrawer addClickHandler(char c, MenuClickHandler clickHandler) {
        clickHandlerMap.put(c, clickHandler);
        return this;
    }
    public MatrixDrawer addClickHandler(String c, MenuClickHandler clickHandler) {
        clickHandlerMap.put(c.charAt(0), clickHandler);
        return this;
    }
    public int[] getCharPositions(String s) {
        return getCharPositions(s.charAt(0));
    }
    public int[] getCharPositions(char c) {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < matrix.size(); i++) {
            String line = matrix.get(i);
            for (int j = 0; j < line.length(); j++) {
                if (line.charAt(j) == c) {
                    positions.add(i * size + j);
                }
            }
        }
        int[] result = new int[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            result[i] = positions.get(i);
        }
        return result;
    }
    @Override
    public MatrixDrawer clone() {
        MatrixDrawer clone = new MatrixDrawer(size);
        clone.matrix.addAll(matrix);
        clone.characterMap.putAll(characterMap);
        clone.clickHandlerMap.putAll(clickHandlerMap);
        return clone;
    }
    @Override
    public void draw(Menu menu) {
        for (int i = 0; i < matrix.size(); i++) {
            String line = matrix.get(i);
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                if (characterMap.containsKey(c)) {
                    menu.setItem(i * 9 + j, new MenuItem(characterMap.get(c), clickHandlerMap.get(c)));
                }
            }
        }
    }
}
