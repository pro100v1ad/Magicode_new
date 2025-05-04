package main.java.com.magicode.ui.gamestate.tablet;

import main.java.com.magicode.ui.gamestate.Tablet;

import java.io.*;

public class TextRedactor {

    private EditArea[] editAreas;
    private int countEditAreas;
    private int startRowsForSpell;

    private Tablet tablet;

    public TextRedactor(Tablet tablet) {

        this.tablet = tablet;

        editAreas = new EditArea[10];
        countEditAreas = 0;
        String[] text = new String[4];
        text[0] = "public Spells {";
        text[1] = " ";
        text[2] = " ";
        text[3] = "} ";
        tablet.setText(text);

        startRowsForSpell = 2;



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

            System.out.println("Функция-заклинание загружено из файла: " + filePath);
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке заклинания: " + e.getMessage());
        }
    }

    public EditArea[] getEditAreas() {
        return editAreas;
    }
}
