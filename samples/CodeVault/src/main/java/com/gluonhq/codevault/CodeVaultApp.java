package com.gluonhq.codevault;

import com.gluonhq.codevault.util.Octicons;
import com.gluonhq.particle.application.ParticleApplication;
import javafx.scene.Scene;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import static org.controlsfx.control.action.ActionMap.actions;

public class CodeVaultApp extends ParticleApplication {

    static {
        GlyphFontRegistry.register(new Octicons());
    }

    public CodeVaultApp() {
        super("CodeVault");
    }

    @Override
    public void postInit(Scene scene) {
        scene.getStylesheets().add(CodeVaultApp.class.getResource("style.css").toExternalForm());

        setTitle("CodeVault");

        getParticle().buildMenu("File -> [openRepo,---, exit]", "Help -> [about]");
        
        getParticle().getToolBarActions().addAll(actions("openRepo"));
    }
}