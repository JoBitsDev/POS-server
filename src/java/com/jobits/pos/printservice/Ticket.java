/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jobits.pos.printservice;

import com.jobits.pos.persistence.Configuracion;
import com.jobits.utils.R;

public class Ticket {

    String commandSet = "";

    /**
     * the paper lenght is 32 for 58mm and 48 for 80mm
     */
    final static int PAPER_LENGHT = R.em1.find(Configuracion.class, R.SettingID.IMPRESION_TICKET_TAMANO_PAPEL.getValue()).getValor();
    final static char LINE_CHAR = R.em1.find(Configuracion.class, R.SettingID.IMPRESION_TICKET_CARACTER_SEPARADOR.getValue()).getValorString().charAt(0);
    private AlignmentState posState = AlignmentState.LEFT;

    public Ticket() {
    }

    public String initialize() {
        final byte[] Init = {27, 64};
        commandSet += new String(Init);
        final byte[] setCodePageLatin = {27, 116, 54};
        commandSet += new String(setCodePageLatin);
        return commandSet;
    }

    public String chooseFont(int Options) {
        String s = "";
        final byte[] ChooseFontA = {27, 77, 0};
        final byte[] ChooseFontB = {27, 77, 1};
        final byte[] ChooseFontC = {27, 77, 48};
        final byte[] ChooseFontD = {27, 77, 49};

        switch (Options) {
            case 1:
                s = new String(ChooseFontA);
                break;

            case 2:
                s = new String(ChooseFontB);
                break;

            case 3:
                s = new String(ChooseFontC);
                break;

            case 4:
                s = new String(ChooseFontD);
                break;

            default:
                s = new String(ChooseFontB);
        }
        commandSet += s;
        return new String(s);
    }

    public String feedBack(byte lines) {
        final byte[] Feed = {27, 101, lines};
        String s = new String(Feed);
        commandSet += s;
        return s;
    }

    public String feed(byte lines) {
        final byte[] Feed = {27, 100, lines};
        String s = new String(Feed);
        commandSet += s;
        return s;
    }

    public String alignLeft() {
        posState = AlignmentState.LEFT;
        final byte[] AlignLeft = {27, 97, 48};
        String s = new String(AlignLeft);
        commandSet += s;
        return s;
    }

    public String alignCenter() {
        posState = AlignmentState.CENTER;
//        final byte[] AlignCenter = {27, 97, 49};
//        String s = new String(AlignCenter);
//        commandSet += s;
        return "";
    }

    public String alignRight() {
        posState = AlignmentState.RIGHT;
//        final byte[] AlignRight = {27, 97, 50};
//        String s = new String(AlignRight);
//        commandSet += s;
        return "";
    }

    public String newLine() {
        final byte[] LF = {10};
        String s = new String(LF);
        commandSet += s;
        return s;
    }

    public String reverseColorMode(boolean enabled) {
        final byte[] ReverseModeColorOn = {29, 66, 1};
        final byte[] ReverseModeColorOff = {29, 66, 0};

        String s = "";
        if (enabled) {
            s = new String(ReverseModeColorOn);
        } else {
            s = new String(ReverseModeColorOff);
        }

        commandSet += s;
        return s;
    }

    public String doubleStrik(boolean enabled) {
        final byte[] DoubleStrikeModeOn = {27, 71, 1};
        final byte[] DoubleStrikeModeOff = {27, 71, 0};

        String s = "";
        if (enabled) {
            s = new String(DoubleStrikeModeOn);
        } else {
            s = new String(DoubleStrikeModeOff);
        }

        commandSet += s;
        return s;
    }

    public String doubleHeight(boolean enabled) {
        final byte[] DoubleHeight = {27, 33, 17};
        final byte[] UnDoubleHeight = {27, 33, 0};

        String s = "";
        if (enabled) {
            s = new String(DoubleHeight);
        } else {
            s = new String(UnDoubleHeight);
        }

        commandSet += s;
        return s;
    }

    public String emphasized(boolean enabled) {
        final byte[] EmphasizedOff = {27, 0};
        final byte[] EmphasizedOn = {27, 1};

        String s = "";
        if (enabled) {
            s = new String(EmphasizedOn);
        } else {
            s = new String(EmphasizedOff);
        }

        commandSet += s;
        return s;
    }

    public String underLine(int Options) {
        final byte[] UnderLine2Dot = {27, 45, 50};
        final byte[] UnderLine1Dot = {27, 45, 49};
        final byte[] NoUnderLine = {27, 45, 48};

        String s = "";
        switch (Options) {
            case 0:
                s = new String(NoUnderLine);
                break;

            case 1:
                s = new String(UnderLine1Dot);
                break;

            default:
                s = new String(UnderLine2Dot);
        }
        commandSet += s;
        return new String(s);
    }

    public String color(int Options) {
        final byte[] ColorRed = {27, 114, 49};
        final byte[] ColorBlack = {27, 114, 48};

        String s = "";
        switch (Options) {
            case 0:
                s = new String(ColorBlack);
                break;

            case 1:
                s = new String(ColorRed);
                break;

            default:
                s = new String(ColorBlack);
        }
        commandSet += s;
        return s;
    }

    public String finit() {
        final byte[] FeedAndCut = {29, 'V', 66, 0};
        String s = new String(FeedAndCut);
        commandSet += s;
        return s;
    }

    public String drawerKick() {

        final byte[] DrawerKick = {27, 70, 0, 60, 120};
        String s = new String(DrawerKick);

        commandSet += s;
        return s;
    }

    public String soundBuzzer() {
        final byte[] buzzer = {27, (int) '(', (int) 'A', 4, 0, 48, 55, 3, 15};
        String s = new String(buzzer);
        commandSet += s;
        return s;

    }

    public String addLineSeperator() {
        alignLeft();
        String lineSpace = "";
        while (lineSpace.length() < PAPER_LENGHT) {
            lineSpace += LINE_CHAR;
        }
        commandSet += lineSpace;
        return lineSpace;
    }

    public void resetAll() {
        commandSet = "";
    }

    public String setCodePageTable(byte codePage) {
        final byte[] setuserDefinedCodePage = {27,37,1};
        commandSet += new String(setuserDefinedCodePage);
        final byte[] setCodePageLatin = {27, 116, codePage};
        commandSet += new String(setCodePageLatin);
        return commandSet;
    }

    public void setText(String s) {
        int sLenght;
        do {
            sLenght = s.length();
            if (sLenght > PAPER_LENGHT) {
                writeText(s.substring(0, PAPER_LENGHT));
                s = s.substring(PAPER_LENGHT);
                newLine();
            } else {
                writeText(s);
            }
        } while (sLenght > PAPER_LENGHT);

    }

    private void writeText(String s) {
        switch (posState) {
            case LEFT:
                commandSet += s;
                break;
            case CENTER:
                commandSet += (addBlankSpaces((PAPER_LENGHT - s.length()) / 2)
                        + s
                        + addBlankSpaces((PAPER_LENGHT - s.length()) / 2));
                break;
            case RIGHT:
                commandSet += (addBlankSpaces(PAPER_LENGHT - s.length()) + s);
                break;
        }
    }

    public String finalCommandSet() {
        return commandSet;
    }

    private String addBlankSpaces(int amount) {
        String ret = "";
        for (int i = 0; i < amount; i++) {
            ret += " ";
        }
        return ret;
    }

    private enum AlignmentState {
        LEFT, RIGHT, CENTER
    }
}
