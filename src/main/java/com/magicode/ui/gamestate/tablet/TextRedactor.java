package main.java.com.magicode.ui.gamestate.tablet;

import main.java.com.magicode.spells.Spell;
import main.java.com.magicode.spells.spells.KeySpell;
import main.java.com.magicode.ui.gamestate.Tablet;

import java.io.*;

public class TextRedactor {

    private EditArea[] editAreas;
    private int countEditAreas;
    private int startRowsForSpell;
    private int countOpenSpells;
    private String[] spellsPath;

    private Tablet tablet;

    public TextRedactor(Tablet tablet, String filePath) {

        this.tablet = tablet;
        spellsPath = new String[10];
        editAreas = new EditArea[10]; // При увеличении к-ва заклинаний менять это поле!
        countEditAreas = 0;
        countOpenSpells = 0;
        String[] text = new String[8];
        text[0] = "public Spells {";
        text[1] = " ";
        text[2] = "    int first = 4;";
        text[3] = "    int second = 5;";
        text[4] = "    String third = \"key\";";
        text[5] = "    String fourth = false;";
        text[6] = " ";
        text[7] = "} ";
        tablet.setText(text);

        startRowsForSpell = 6;

        if(filePath != null) {
            loadSaveSpells(filePath);
        } else {
            addSpell("/resources/spells/key");
        }


    }

    public void addSpell(String filePath) {
        try (InputStream is = getClass().getResourceAsStream(filePath)) {
            if (is == null) {
                System.out.println("Ошибка: файл не найден! " + filePath);
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            if (line == null) {
                System.out.println("Файл пуст");
                return;// Если файл закончился раньше, чем ожидалось
            }
            int count = Integer.parseInt(line); // количество редактируемых зон
            for(int i = 0; i < count; i++) { // Загрузка всех редактируемых зон
                line = br.readLine();
                editAreas[countEditAreas++] = new EditArea(startRowsForSpell + Integer.parseInt(line.split("_")[0]),
                        Integer.parseInt(line.split("_")[1]),
                        Integer.parseInt(line.split("_")[2]),
                        line.split("_")[3]);

            }
            int countRows = Integer.parseInt(br.readLine());
            startRowsForSpell += countRows;
            if(tablet.getText() == null) {
                String[] text;
                text = new String[countRows];
                for(int i = 0; i < countRows; i++) {
                    text[i] = br.readLine();
                }
                tablet.setText(text);
            } else {
                int countRowsAgo = tablet.getText().length;
                String[] text;
                text = new String[countRowsAgo + countRows];
                countRowsAgo -= 2; // Это пустая строка и }
                for(int i = 0; i < countRowsAgo; i++) {
                    text[i] = tablet.getText()[i];
                }
                for(int i = countRowsAgo; i < countRowsAgo + countRows; i++) {
                    text[i] = br.readLine();
                }
                text[countRowsAgo + countRows] = "} ";
                tablet.setText(text);
            }

            tablet.addButton(filePath.split("/")[filePath.split("/").length-1] + "()");
            spellsPath[countOpenSpells++] = filePath;

//            System.out.println("Функция-заклинание загружено из файла: " + filePath);
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке заклинания: " + e.getMessage());
        }
    }

    public void addSaveSpell(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            line = reader.readLine();
            if (line == null) {
                System.out.println("Файл пуст");
                return;// Если файл закончился раньше, чем ожидалось
            }
            int count = Integer.parseInt(line); // количество редактируемых зон
            for(int i = 0; i < count; i++) { // Загрузка всех редактируемых зон
                line = reader.readLine();
                editAreas[countEditAreas++] = new EditArea(startRowsForSpell + Integer.parseInt(line.split("_")[0]),
                        Integer.parseInt(line.split("_")[1]),
                        Integer.parseInt(line.split("_")[2]),
                        line.split("_")[3]);

            }
            int countRows = Integer.parseInt(reader.readLine());
            startRowsForSpell += countRows;
            if(tablet.getText() == null) {
                String[] text;
                text = new String[countRows];
                for(int i = 0; i < countRows; i++) {
                    text[i] = reader.readLine();
                }
                tablet.setText(text);
            } else {
                int countRowsAgo = tablet.getText().length;
                String[] text;
                text = new String[countRowsAgo + countRows];
                countRowsAgo -= 2; // Это пустая строка и }
                for(int i = 0; i < countRowsAgo; i++) {
                    text[i] = tablet.getText()[i];
                }
                for(int i = countRowsAgo; i < countRowsAgo + countRows; i++) {
                    text[i] = reader.readLine();
                }
                text[countRowsAgo + countRows] = "} ";
                tablet.setText(text);
            }

            tablet.addButton(filePath.split("/")[filePath.split("/").length-1] + "()");
            spellsPath[countOpenSpells++] = filePath;

        } catch (IOException e) {
            System.err.println("Ошибка при загрузке заклинания: " + e.getMessage());
        }
    }

    public void loadSaveSpells(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            line = reader.readLine();
            if (line == null) {
                System.out.println("Файл saveSpells пуст textRedactor");
                return;// Если файл закончился раньше, чем ожидалось
            }
            countOpenSpells = 0;
            for(int i = 0; i < Integer.parseInt(line); i++) {
                addSpell(reader.readLine());
            }
        } catch (Exception e) {
            System.err.println("Критическая ошибка загрузки сохраненных открытых заклинаний: ");
        }
    }

    public EditArea[] getEditAreas() {
        return editAreas;
    }

    public int getCountOpenSpells() {
        return countOpenSpells;
    }

    public void setCountOpenSpells(int countOpenSpells) {
        this.countOpenSpells = countOpenSpells;
    }

    public String[] getSpellsPath() {
        return spellsPath;
    }
}
