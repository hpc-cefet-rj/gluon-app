package com.gluonhq.codevault.util;

import com.gluonhq.codevault.git.GitBranch;
import com.gluonhq.codevault.git.GitRef;
import com.gluonhq.codevault.git.GitTag;
import javafx.scene.Node;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFontRegistry;

public class UITools {
    public static Node getIcon(Octicons.Glyph iconType ) {
        Glyph icon = GlyphFontRegistry.font(Octicons.NAME).create(iconType).size(14);
        icon.getStyleClass().add("ref-icon");
        return icon;
    }

    public static Node getRefIcon(GitRef ref) {
        if (ref instanceof GitTag) {
            return getIcon(Octicons.Glyph.TAG);
        } else if (ref instanceof GitBranch) {
            return getIcon(Octicons.Glyph.BRANCH);
        } else {
            return null;
        }
    }
}
