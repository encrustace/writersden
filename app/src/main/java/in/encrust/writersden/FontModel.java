package in.encrust.writersden;

import android.graphics.Typeface;

public class FontModel {
    private Typeface customfont;
    private String fontname;

    public FontModel() {
    }

    public FontModel(Typeface customfont, String fontname) {
        this.customfont = customfont;
        this.fontname = fontname;
    }

    public Typeface getCustomfont() {
        return customfont;
    }

    public void setCustomfont(Typeface customfont) {
        this.customfont = customfont;
    }

    public String getFontname() {
        return fontname;
    }

    public void setFontname(String fontname) {
        this.fontname = fontname;
    }
}
