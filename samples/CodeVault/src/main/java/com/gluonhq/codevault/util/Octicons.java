package com.gluonhq.codevault.util;


import com.gluonhq.codevault.CodeVaultApp;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.INamedCharacter;

import java.io.InputStream;
import java.util.Arrays;

public class Octicons extends GlyphFont {

    public static final String NAME = "github-octicons";

    public Octicons() {
        super(NAME, 16, CodeVaultApp.class.getResourceAsStream("octicons-local.ttf"));
        registerAll(Arrays.asList(Glyph.values()));
    }

    public static enum Glyph implements INamedCharacter {
        BRANCH('\uf020'),
        COMMIT('\uf01f'),
        CLONE('\uf04c'),
        TAG('\uf015'),
        FOLDER('\uf016'),
        REPO('\uf001'),
        SIGN_OUT('\uf032'),
        MARK_GITHUB('\uf00a');

        private final Character ch;

        Glyph( Character ch ) {
            this.ch = ch;
        }

        public char getChar() {
            return ch;
        }

    };
}
