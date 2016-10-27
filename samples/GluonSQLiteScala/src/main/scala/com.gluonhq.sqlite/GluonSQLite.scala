package com.gluonhq.sqlite

import com.gluonhq.charm.down.common.JavaFXPlatform
import com.gluonhq.charm.glisten.application.MobileApplication
import com.gluonhq.charm.glisten.visual.Swatch
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import com.gluonhq.sqlite._

class GluonSQLite extends MobileApplication {

    import MobileApplication._

    override def init(): Unit = {
        addViewFactory(HOME_VIEW, new BasicView(HOME_VIEW))
    }

    override def postInit(scene: Scene) {
        Swatch.RED.assignTo(scene)
        val stage = scene.getWindow.asInstanceOf[Stage]
        stage.getIcons.add(new Image(classOf[GluonSQLite].getResourceAsStream("/icon.png")))
        if (System.getProperty("os.arch").toUpperCase.contains("ARM") && !JavaFXPlatform.isIOS && !JavaFXPlatform.isAndroid) {
            stage.setFullScreen(true)
            stage.setFullScreenExitHint("")
        }
    }
}
